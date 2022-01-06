package org.sheedon.mvvm.viewmodel.actuator

/**
 * 执行器提供者
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/1/6 12:35 下午
 */
class ActuatorProvider(_factory: Factory = DefaultActuatorFactory.INSTANCE) {

    private val mFactory = _factory
    private val mMap = HashMap<String, Actuator>()


    interface Factory {
        fun <T : Actuator> create(modelClass: Class<T>): T
    }

    companion object {
        private const val DEFAULT_KEY =
            "org.sheedon.mvvm.viewmodel.actuator.ViewModelProvider.DefaultKey"
    }

    /**
     * 默认工厂
     */
    private class DefaultActuatorFactory private constructor() : Factory {

        companion object {
            val INSTANCE = DefaultActuatorFactory()
        }

        override fun <T : Actuator> create(modelClass: Class<T>): T {
            try {
                return modelClass.getDeclaredConstructor().newInstance()
            } catch (e: Exception) {
                throw  RuntimeException("Cannot create an instance of $modelClass", e)
            }
        }
    }

    /**
     * 返回现有的 Actuator 或在与此 ActuatorProvider 关联的范围内（通常是片段或活动）创建新的 Actuator。
     * 创建的 Actuator 与 创建的ViewModel关联，并且随ViewModel销毁而销毁。
     *
     * @param modelClass 类Class
     * @param <T>        Actuator
     * @return Actuator
     */
    fun <T : Actuator> get(modelClass: Class<T>): T {
        val canonicalName = modelClass.canonicalName
            ?: throw IllegalArgumentException("Local and anonymous classes can not be ViewModels")
        return get("$DEFAULT_KEY:$canonicalName", modelClass)
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
    fun <T : Actuator> get(key: String, modelClass: Class<T>): T {
        var actuator = mMap[key]

        if (modelClass.isInstance(actuator)) {
            return actuator as T
        }
        actuator = mFactory.create(modelClass)
        mMap[key] = actuator
        return actuator
    }

    /**
     * 清除操作
     */
    fun clear() {
        val values = mMap.values
        values.forEach { it.onCleared() }
    }


}