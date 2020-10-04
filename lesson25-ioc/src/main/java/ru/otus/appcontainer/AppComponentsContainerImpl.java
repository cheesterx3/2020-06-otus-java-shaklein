package ru.otus.appcontainer;

import lombok.val;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;
import ru.otus.helper.ReflectUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class AppComponentsContainerImpl implements AppComponentsContainer {
    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();


    public AppComponentsContainerImpl(String packageToScan) {
        this(new Reflections(packageToScan)
                .getTypesAnnotatedWith(AppComponentsContainerConfig.class).toArray(new Class<?>[0]));
    }

    public AppComponentsContainerImpl(Class<?>... initialConfigClasses) {
        val classList = Arrays.stream(initialConfigClasses)
                .filter(Objects::nonNull)
                .collect(toList());
        if (classList.isEmpty())
            throw new IllegalArgumentException("Container should contain at least 1 config");
        processConfigList(classList);
    }

    private void processConfigList(List<Class<?>> configClasses) {
        configClasses.forEach(this::checkConfigClass);
        val configMap = configClasses.stream()
                .collect(toMap(Function.identity(), ReflectUtils::create));
        configClasses.stream()
                .sorted(Comparator.comparingInt(aClass -> aClass.getAnnotation(AppComponentsContainerConfig.class).order()))
                .forEachOrdered(aClass -> processConfig(configMap.get(aClass), aClass));
    }

    @SuppressWarnings("unchecked")
    private void processConfig(Object config, Class<?> aClass) {
        ReflectionUtils
                .getMethods(aClass, method -> method.isAnnotationPresent(AppComponent.class)).stream()
                .sorted(Comparator.comparingInt(method -> method.getAnnotation(AppComponent.class).order()))
                .forEachOrdered(method -> createComponent(method, config));
    }

    private void createComponent(Method method, Object config) {
        final List<Object> params = Stream.of(method.getParameters())
                .map(Parameter::getType)
                .map(this::getAppComponent)
                .filter(Objects::nonNull)
                .collect(toList());
        if (params.size() != method.getParameters().length)
            throw new IllegalArgumentException(String.format("Unable to create component %s. Dependencies missing", method.getReturnType().getName()));
        val component = ReflectUtils.invoke(method, config, params);
        appComponents.add(component);
        appComponentsByName.put(method.getAnnotation(AppComponent.class).name(), component);
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class))
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C> C getAppComponent(Class<C> componentClass) {
        return (C) appComponents.stream()
                .filter(object -> componentClass.isAssignableFrom(object.getClass()))
                .findAny()
                .orElse(null);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C> C getAppComponent(String componentName) {
        return (C) appComponentsByName.get(componentName);
    }

}
