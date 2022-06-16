package org.sheedon.common.listener

import android.app.Activity
import android.app.Application
import android.os.Bundle

/**
 * 简易版Activity生命周期回调
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/6/15 18:27
 */
open class SimpleActivityLifecycleCallback : Application.ActivityLifecycleCallbacks {
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityResumed(activity: Activity) {
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
    }

}