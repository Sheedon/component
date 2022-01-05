package org.sheedon.common.handler

import android.app.Activity

/**
 * 通知App状态更改处理者
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/1/5 9:38 下午
 */
class NotifyAppStateHandler private constructor() {

    var listener: OnNotifyChangeListener? = null

    companion object {
        private val instance = NotifyAppStateHandler()

        /**
         * 附加监听器
         * @param listener 通知App状态更改监听器
         */
        fun attachListener(listener: OnNotifyChangeListener) {
            instance.listener = listener
        }

        /**
         * 添加Activity
         * @param activity Activity
         */
        fun addActivity(activity:Activity){
            instance.listener?.addActivity(activity)
        }

        /**
         * 移除Activity
         * @param activity Activity
         */
        fun removeActivity(activity:Activity){
            instance.listener?.removeActivity(activity)
        }

    }

    /**
     * 通知更改监听器
     */
    interface OnNotifyChangeListener {
        fun addActivity(activity: Activity)

        fun removeActivity(activity: Activity)
    }
}