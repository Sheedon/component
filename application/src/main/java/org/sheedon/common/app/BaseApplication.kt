package org.sheedon.common.app

import android.app.Application
import android.os.Handler
import android.os.Looper

/**
 * 基础应用模块,上层集成请基础该类
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/1/5 5:28 下午
 */
class BaseApplication : Application() {

    // 转UI执行
    private val uiHandler = Handler(Looper.getMainLooper())

    companion object {
        private var instance: BaseApplication? = null

        fun getInstance() = instance

        fun getUiHandler() = instance!!.uiHandler
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}