package org.sheedon.common.handler;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import androidx.annotation.StringRes;

import org.sheedon.common.app.BaseApplication;
import org.sheedon.common.utils.ToastUtils;

/**
 * 消息提示者
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2021/10/19 6:04 下午
 */
public class ToastHandler {

    private static final ToastHandler INSTANCE = new ToastHandler();
    // ui 处理器
    private final Handler uiHandler = BaseApplication.getUiHandler();

    private ToastHandler() {
    }

    /**
     * 显示一个Toast
     *
     * @param msg 字符串
     */
    public static void showToast(final String msg) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        if (Looper.myLooper() != INSTANCE.uiHandler.getLooper()) {
            INSTANCE.uiHandler.post(() -> ToastUtils.showToast(BaseApplication.getInstance(), msg));
        }
    }

    /**
     * 显示一个Toast
     *
     * @param msgId 传递字符串的资源
     */
    public static void showToast(@StringRes int msgId) {
        Application application = BaseApplication.getInstance();
        String msg = application.getString(msgId);
        showToast(msg);
    }

}
