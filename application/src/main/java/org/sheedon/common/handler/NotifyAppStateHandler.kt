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
        @JvmStatic
        fun attachListener(listener: OnNotifyChangeListener) {
            instance.listener = listener
        }

        /**
         * activity入栈
         * @param activity Activity
         */
        @JvmStatic
        fun pushActivity(activity:Activity){
            instance.listener?.pushActivity(activity)
        }

        /**
         * activity出栈
         * @param activity Activity
         */
        @JvmStatic
        fun popActivity(activity:Activity){
            instance.listener?.popActivity(activity)
        }

    }

    /**
     * 通知更改监听器
     */
    interface OnNotifyChangeListener {
        fun pushActivity(activity: Activity)

        fun popActivity(activity: Activity)
    }
}