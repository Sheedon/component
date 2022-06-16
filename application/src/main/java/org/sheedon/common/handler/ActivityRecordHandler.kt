package org.sheedon.common.handler

import android.app.Activity
import android.app.Application
import android.os.Bundle
import org.sheedon.common.listener.SimpleActivityLifecycleCallback
import java.lang.ref.WeakReference

/**
 * Activity记录者
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/6/16 09:20
 */
class ActivityRecordHandler(val application: Application) {

    private val activityMap = mutableMapOf<String, WeakReference<Activity>>()

    // Activity生命周期监听
    private val lifecycleCallback = object : SimpleActivityLifecycleCallback() {
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            val context = activity.parent ?: activity
            val componentName = context.componentName
            activityMap[componentName.packageName + "|" + componentName.className] =
                WeakReference(context)
        }

        override fun onActivityDestroyed(activity: Activity) {
            val context = activity.parent ?: activity
            val componentName = context.componentName
            activityMap.remove(componentName.packageName + "|" + componentName.className)
        }
    }

    init {
        config()
    }

    /**
     * 初始化，用于注册监听Activity的情况
     */
    private fun config() {
        application.registerActivityLifecycleCallbacks(lifecycleCallback)
    }

    /**
     * 获取所有还在Map中的Activity
     */
    fun getActivities(): List<Activity> {
        return activityMap.values.mapNotNull {
            it.get()
        }
    }

    /**
     * 清空所有Activity，一般销毁的时候使用
     */
    fun clearActivities() {
        activityMap.clear()
    }

    /**
     * 销毁
     */
    fun destroy() {
        clearActivities()
        application.unregisterActivityLifecycleCallbacks(lifecycleCallback)
    }


}