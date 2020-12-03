package ru.otus.bytecodes.impl;

import ru.otus.bytecodes.Advice;
import ru.otus.bytecodes.JoinPoint;
import ru.otus.bytecodes.JoinPointType;
import ru.otus.bytecodes.exceptions.MethodInvokeInProxyException;
import ru.otus.bytecodes.helper.Functions;
import ru.otus.bytecodes.helper.Memoizer;
import ru.otus.bytecodes.helper.ReflectionUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;
import static ru.otus.bytecodes.JoinPointType.*;

/**
 * Реализация обработчика {@link InvocationHandler} для прокси-объектов с целью использования cross-cutting функционалов
 * @implNote  Реализаци поддерживает четыре типа так называемых advice'ов:
 *  - Before - выполняются до выполнения метода объекта - не влияет на выполнение за исключением ситуаций с выбрасыванием исключений
 *  - AfterSuccess - выполняются после успешного выполнения метода объекта - не влияет на поведение
 *  - AfterFinally - выполняется после выполнения метода объекта внезавимости от его результата - не влияет на поведение
 *  - OnException - выполянется в случае выполнения метода объекта с ошибкой(или исключения в Before) - не влияет на поведение
 *  - Around - выполняется "вокруг" выполнения метода объекта, т.е. оборачивает выполнение метода своей реализацией -
 *  может подменять результат выполнения при необходимости.
 *
 *  Не учитывается порядок выполнения однотипных advice'ов.
 *  Потоконебезопасен, т.е. выполнение отдного и того же метода одного и того же объекта в разных потоках
 *  может привести к side-эффектам. Можно, конечно, использовать и потокобезопсаные конструкции, однако,
 *  это может повлиять на призводительность и выходит за рамки задания
 */
class CustomInvocationHandler implements InvocationHandler {
    private final Map<Method, Set<Advice>> originalAnnotatedMethods;
    private final Map<Method, Map<JoinPointType, Set<Advice>>> checkedMethods;
    private final Object object;

    CustomInvocationHandler(Object object, Map<Method, Set<Advice>> originalAnnotatedMethods) {
        this.checkedMethods = new HashMap<>(object.getClass().getDeclaredMethods().length);
        this.originalAnnotatedMethods = Collections.unmodifiableMap(originalAnnotatedMethods);
        this.object = object;
    }

    private static Function<JoinPointType, Set<Advice>> getAdvicesByPoint(Set<Advice> list) {
        return point -> list.stream().filter(advice -> advice.joinPoints().contains(point)).collect(toSet());
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        final Map<JoinPointType, Set<Advice>> actionAdvices = checkedMethods.computeIfAbsent(method, this::adviceList);
        if (!actionAdvices.isEmpty()) {
            final JoinPoint joinPoint = new JoinPoint(method, args, object);
            final Set<Advice> aroundAdvices = actionAdvices.getOrDefault(AROUND, emptySet());
            if (!aroundAdvices.isEmpty()) {
                final Supplier<Object> objectSupplier = () -> invokeWithoutCheckedException(method, actionAdvices, joinPoint);
                return invokeWrappedWithAroundAspects(joinPoint, aroundAdvices, Memoizer.from(objectSupplier));
            } else
                return invokeMethodOnObject(method, actionAdvices, joinPoint);
        }
        return method.invoke(object, args);
    }

    private Object invokeWrappedWithAroundAspects(JoinPoint joinPoint,
                                                  Collection<Advice> aroundAdvices,
                                                  Supplier<Object> objectSupplier) {
        return aroundAdvices.stream()
                .map(advice -> (Function<Supplier<Object>, Object>) supplier -> advice.invokeAround(joinPoint, Memoizer.from(supplier)))
                .reduce(Functions::composeFunctions)
                .orElse((s) -> objectSupplier.get())
                .apply(objectSupplier);
    }

    private Object invokeWithoutCheckedException(Method method,
                                                 Map<JoinPointType, Set<Advice>> actionAdvices,
                                                 JoinPoint joinPoint) {
        try {
            return invokeMethodOnObject(method, actionAdvices, joinPoint);
        } catch (Throwable e) {
            throw new MethodInvokeInProxyException(e);
        }
    }

    private Object invokeMethodOnObject(Method method,
                                        Map<JoinPointType, Set<Advice>> actionAdvices,
                                        JoinPoint joinPoint) throws Throwable {
        Object retVal = null;
        try {
            invokeBefore(actionAdvices.getOrDefault(BEFORE, emptySet()), joinPoint);
            retVal = method.invoke(joinPoint.getObject(), joinPoint.getArgs());
            invokeOnSuccess(actionAdvices.getOrDefault(AFTER_SUCCESS, emptySet()), retVal, joinPoint);
        } catch (Exception e) {
            invokeOnFail(actionAdvices.getOrDefault(ON_EXCEPTION, emptySet()), joinPoint, e);
            final Throwable cause = e.getCause();
            throw cause != null ? cause : e;
        } finally {
            invokeFinally(actionAdvices.getOrDefault(AFTER_FINALLY, emptySet()), retVal, joinPoint);
        }
        return retVal;
    }

    private void invokeFinally(Collection<Advice> actionAdvices, Object retVal, JoinPoint joinPoint) {
        try {
            actionAdvices.forEach(advice -> advice.invokeFinally(joinPoint, retVal));
        } catch (Exception e) {
            // ignoring. We should use logging here, but not now
        }
    }

    private void invokeOnFail(Collection<Advice> actionAdvices, JoinPoint joinPoint, Exception e) {
        try {
            actionAdvices.forEach(advice -> advice.invokeOnException(joinPoint, e.getCause()));
        } catch (Exception ex) {
            // ignoring. We should use logging here, but not now
        }
    }

    private void invokeOnSuccess(Collection<Advice> actionAdvices, Object retVal, JoinPoint joinPoint) {
        try {
            actionAdvices.forEach(advice -> advice.invokeAfterSuccess(joinPoint, retVal));
        } catch (Exception ex) {
            // ignoring. We should use logging here, but not now
        }
    }

    private void invokeBefore(Collection<Advice> actionAdvices, JoinPoint joinPoint) {
        actionAdvices.forEach(advice -> advice.invokeBefore(joinPoint));
    }

    /*
     * Здесь, безусловно, можно было сделать не в таком функциональном стиле.
     * Возможно, это было бы понятнее, но в данном случае важнее скорее результат выполнения метода
     */
    private Map<JoinPointType, Set<Advice>> adviceList(Method method) {
        return originalAnnotatedMethods.entrySet().stream()
                .filter(entry -> ReflectionUtils.isParamArrayEquals(method.getParameters(), entry.getKey().getParameters())
                        && method.getName().equals(entry.getKey().getName()))
                .map(Map.Entry::getValue)
                .map(adviceSet -> adviceSet.stream()
                        .flatMap(advice -> advice.joinPoints().stream())
                        .collect(
                                toMap(point -> point,
                                        getAdvicesByPoint(adviceSet),
                                        (list1, list2) -> Stream.concat(list1.stream(), list2.stream()).collect(toSet()),
                                        () -> new EnumMap<>(JoinPointType.class))))
                .findAny()
                .orElseGet(() -> new EnumMap<>(JoinPointType.class));
    }
}
