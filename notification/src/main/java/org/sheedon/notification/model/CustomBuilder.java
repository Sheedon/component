package org.sheedon.notification.model;

import android.content.Context;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import org.sheedon.notification.pendingintent.ClickPendingIntentActivity;
import org.sheedon.notification.pendingintent.ClickPendingIntentBroadCast;

/**
 * 自定义构建器
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2021/11/9 4:40 下午
 */
public class CustomBuilder extends SimpleBuilder {

    private RemoteViews remoteView;

    public CustomBuilder remoteView(RemoteViews remoteView) {
        this.remoteView = remoteView;
        return this;
    }

    @Override
    public CustomBuilder notificationId(int notificationId) {
        super.notificationId(notificationId);
        return this;
    }

    @Override
    public CustomBuilder channelId(String channelId) {
        super.channelId(channelId);
        return this;
    }

    @Override
    public CustomBuilder title(String title) {
        super.title(title);
        return this;
    }

    @Override
    public CustomBuilder message(String message) {
        super.message(message);
        return this;
    }

    @Override
    public CustomBuilder activityIntent(ClickPendingIntentActivity activityIntent) {
        super.activityIntent(activityIntent);
        return this;
    }

    @Override
    public CustomBuilder broadCastIntent(ClickPendingIntentBroadCast broadCastIntent) {
        super.broadCastIntent(broadCastIntent);
        return this;
    }

    @Override
    public NotificationCompat.Builder build(Context context) {
        return super.build(context).setContent(remoteView);
    }
}
