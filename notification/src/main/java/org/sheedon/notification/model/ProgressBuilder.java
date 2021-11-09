package org.sheedon.notification.model;

import android.content.Context;

import androidx.core.app.NotificationCompat;

import org.sheedon.notification.pendingintent.ClickPendingIntentActivity;
import org.sheedon.notification.pendingintent.ClickPendingIntentBroadCast;

/**
 * 进度通知构建者
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2021/11/9 4:19 下午
 */
public class ProgressBuilder extends SimpleBuilder {

    private int total;
    private int progress;
    private NotificationCompat.Builder builder;

    public ProgressBuilder progress(int total, int progress) {
        this.total = total;
        this.progress = progress;
        return this;
    }

    @Override
    public ProgressBuilder notificationId(int notificationId) {
        super.notificationId(notificationId);
        return this;
    }

    @Override
    public ProgressBuilder channelId(String channelId) {
        super.channelId(channelId);
        return this;
    }

    @Override
    public ProgressBuilder title(String title) {
        super.title(title);
        return this;
    }

    @Override
    public ProgressBuilder message(String message) {
        super.message(message);
        return this;
    }

    @Override
    public ProgressBuilder activityIntent(ClickPendingIntentActivity activityIntent) {
        super.activityIntent(activityIntent);
        return this;
    }

    @Override
    public ProgressBuilder broadCastIntent(ClickPendingIntentBroadCast activityIntent) {
        super.broadCastIntent(activityIntent);
        return this;
    }

    public int getTotal() {
        return total;
    }

    public int getProgress() {
        return progress;
    }

    @Override
    public NotificationCompat.Builder build(Context context) {
        if (builder == null) {
            builder = super.build(context);
        }
        return builder.setProgress(total, progress, false);
    }

    public NotificationCompat.Builder updateBuilder(Context context, int total, int progress) {
        if (builder == null) {
            builder = super.build(context);
        }
        return builder.setProgress(total, progress, false);
    }
}
