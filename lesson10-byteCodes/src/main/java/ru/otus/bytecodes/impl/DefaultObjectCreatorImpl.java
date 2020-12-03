package ru.otus.bytecodes.impl;

import ru.otus.bytecodes.*;

import java.lang.reflect.*;
import java.util.*;

import static java.util.stream.Collectors.toMap;

/**
 * Реализация интерфейса по созданию объектов
 */
public class DefaultObjectCreatorImpl implements ObjectCreator {

    private final AdviceResolver adviceResolver;

    public DefaultObjectCreatorImpl() {
        this(new DefaultAdviceResolverImpl());
    }

    public DefaultObjectCreatorImpl(AdviceResolver adviceResolver) {
        this.adviceResolver = adviceResolver;
    }

    @Override
    public <E extends T, T> T createObject(Class<E> aClass, Class<T> interfaceClass) throws InstantiationException {
        try {
            final E instance = aClass.getConstructor().newInstance();
            final Map<Method, Set<Advice>> methodsWithAdvices =
                    Arrays.stream(aClass.getDeclaredMethods())
                            .filter(adviceResolver::hasAdvices)
                            .collect(toMap(method -> method, adviceResolver::getAdvices));
            if (!methodsWithAdvices.isEmpty()) {
                return (T) Proxy.newProxyInstance(ObjectCreator.class.getClassLoader(),
                        aClass.getInterfaces(),
                        new CustomInvocationHandler(instance, methodsWithAdvices));
            }
            return instance;
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new InstantiationException("Error during object instantiation");
        } catch (NoSuchMethodException e) {
            throw new InstantiationException("Object can be instantiated only with default constructor");
        }
    }

}
