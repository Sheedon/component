package org.sheedon.common.handler

import android.os.Looper
import androidx.annotation.StringRes
import org.sheedon.common.app.BaseApplication
import org.sheedon.common.utils.ToastUtils
import java.lang.Exception

/**
 * 消息提示者
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/1/5 6:15 下午
 */
class ToastHandler private constructor() {

    // ui 处理器
    private val uiHandler = BaseApplication.getUiHandler()

    companion object {
        private val INSTANCE = ToastHandler()

        @JvmStatic
        fun showToast(@StringRes res: Int) {
            try {
                val msg = ResConvertHandler.getInstance().convertString(res)
                showToast(msg)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }


        @JvmStatic
        fun showToast(msg: String?) {
            if (msg.isNullOrEmpty()) {
                return
            }
            if (Looper.myLooper() != INSTANCE.uiHandler.looper) {
                INSTANCE.uiHandler.post {
                    ToastUtils.showToast(BaseApplication.getInstance(), msg)
                }
            } else {
                ToastUtils.showToast(BaseApplication.getInstance(), msg)
            }
        }
    }

}