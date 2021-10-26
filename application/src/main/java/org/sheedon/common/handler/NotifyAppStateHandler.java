package org.sheedon.common.handler;

import android.app.Activity;

/**
 * 通知App状态更改处理者
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2021/7/26 6:20 下午
 */
public final class NotifyAppStateHandler {

    private static OnNotifyChangeListener listener;

    public static void setListener(OnNotifyChangeListener listener) {
        NotifyAppStateHandler.listener = listener;
    }

    public static void addActivity(Activity activity) {
        if (listener != null) {
            listener.addActivity(activity);
        }
    }

    public static void removeActivity(Activity activity) {
        if (listener != null) {
            listener.removeActivity(activity);
        }
    }

    public interface OnNotifyChangeListener {
        void addActivity(Activity activity);

        void removeActivity(Activity activity);
    }
}
