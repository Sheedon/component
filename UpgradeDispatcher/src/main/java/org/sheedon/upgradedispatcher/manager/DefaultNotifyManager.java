package org.sheedon.upgradedispatcher.manager;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import org.sheedon.upgradedispatcher.R;
import org.sheedon.upgradedispatcher.listener.UpgradeListener;

/**
 * 通知窗体管理者
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2021/10/26 1:21 下午
 */
public class DefaultNotifyManager extends UpgradeListener.UpgradeNotifyFactory {


    @Override
    public UpgradeListener createWindow(Context context) {
        return new NotificationWindow(context);
    }


    private static class NotificationWindow implements UpgradeListener {

        private NotificationUtils utils;

        public NotificationWindow(Context context) {
            this.utils = new NotificationUtils(context);
        }


        @Override
        public void onUpgradeFailure(int code, String message) {

        }

        @Override
        public void onProgress(int progress) {
            utils.showNotification(progress);
        }

        @Override
        public void onStartDownload() {

        }

        @Override
        public void onDownloadSuccess() {

        }
    }
}
