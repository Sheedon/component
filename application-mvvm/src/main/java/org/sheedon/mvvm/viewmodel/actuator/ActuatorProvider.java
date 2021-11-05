package org.sheedon.mvvm.viewmodel.actuator;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 执行器提供者
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2021/11/1 4:36 下午
 */
public final class ActuatorProvider {

    private static final String DEFAULT_KEY =
            "org.sheedon.mvvm.viewmodel.actuator.ViewModelProvider.DefaultKey";

    private final ActuatorProvider.Factory mFactory;
    private final Map<String, Actuator> mMap = new HashMap<>();

    public interface Factory {
        @NonNull
        <T extends Actuator> T create(@NonNull Class<T> modelClass);
    }

    /**
     * 默认工厂
     */
    private static class DefaultActuatorFactory implements Factory {

        private static final DefaultActuatorFactory INSTANCE = new DefaultActuatorFactory();

        private DefaultActuatorFactory() {
        }

        @NonNull
        @Override
        public <T extends Actuator> T create(@NonNull Class<T> modelClass) {
            T actuator;
            try {
                actuator = modelClass.getDeclaredConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException("Cannot create an instance of " + modelClass, e);
            }
            return actuator;
        }
    }

    public ActuatorProvider() {
        this(DefaultActuatorFactory.INSTANCE);
    }

    public ActuatorProvider(@NonNull ActuatorProvider.Factory factory) {
        mFactory = factory;
    }

    /**
     * 返回现有的 Actuator 或在与此 ActuatorProvider 关联的范围内（通常是片段或活动）创建新的 Actuator。
     * 创建的 Actuator 与 创建的ViewModel关联，并且随ViewModel销毁而销毁。
     *
     * @param modelClass 类Class
     * @param <T>        Actuator
     * @return Actuator
     */
    @NonNull
    @MainThread
    public <T extends Actuator> T get(@NonNull Class<T> modelClass) {
        String canonicalName = modelClass.getCanonicalName();
        if (canonicalName == null) {
            throw new IllegalArgumentException("Local and anonymous classes can not be ViewModels");
        }
        return get(DEFAULT_KEY + ":" + canonicalName, modelClass);
    }

    /**
     * 返回现有的 Actuator 或在与此 ActuatorProvider 关联的范围内（通常是片段或活动）创建新的 Actuator。
     * 创建的 Actuator 与 创建的ViewModel关联，并且随ViewModel销毁而销毁。
     *
     * @param key        类名
     * @param modelClass 类Class
     * @param <T>        Actuator
     * @return Actuator
     */
    @SuppressWarnings("unchecked")
    @NonNull
    @MainThread
    public <T extends Actuator> T get(@NonNull String key, @NonNull Class<T> modelClass) {
        Actuator actuator = mMap.get(key);

        if (modelClass.isInstance(actuator)) {
            return (T) actuator;
        }
        actuator = mFactory.create(modelClass);
        mMap.put(key, actuator);
        return (T) actuator;
    }

    /**
     * 清除操作
     */
    public void clear() {
        Collection<Actuator> values = mMap.values();
        for (Actuator value : values) {
            if (value != null) {
                value.onCleared();
            }
        }
    }

}
