package org.sheedon.notification.model;

import android.app.PendingIntent;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import androidx.core.app.NotificationCompat;

import org.sheedon.notification.R;
import org.sheedon.notification.pendingintent.ClickPendingIntentActivity;
import org.sheedon.notification.pendingintent.ClickPendingIntentBroadCast;

import java.util.Objects;

/**
 * 通知消息
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2021/11/3 10:07 上午
 */
public class SimpleBuilder {

    private int notificationId = 1;
    private String channelId;
    private String title;
    private String message;
    private int iconRes;
    private ClickPendingIntentActivity activityIntent;
    private ClickPendingIntentBroadCast broadCastIntent;

    public SimpleBuilder notificationId(int notificationId) {
        this.notificationId = notificationId;
        return this;
    }

    public SimpleBuilder channelId(String channelId) {
        this.channelId = Objects.requireNonNull(channelId, "title == null");
        return this;
    }

    public SimpleBuilder title(String title) {
        this.title = Objects.requireNonNull(title, "title == null");
        return this;
    }

    public SimpleBuilder message(String message) {
        this.message = Objects.requireNonNull(message, "message == null");
        return this;
    }

    public SimpleBuilder activityIntent(ClickPendingIntentActivity activityIntent) {
        this.activityIntent = Objects.requireNonNull(activityIntent, "activityIntent == null");
        return this;
    }

    public SimpleBuilder broadCastIntent(ClickPendingIntentBroadCast broadCastIntent) {
        this.broadCastIntent = Objects.requireNonNull(broadCastIntent, "broadCastIntent == null");
        return this;
    }

    public int getNotificationId() {
        return notificationId;
    }

    public String getChannelId() {
        return channelId;
    }

    public String getTitle(Context context) {
        if (TextUtils.isEmpty(title)) {
            return context.getString(R.string.default_title);
        }
        return title;
    }

    public String getMessage(Context context) {
        if (TextUtils.isEmpty(message)) {
            return context.getString(R.string.default_content);
        }
        return message;
    }

    public int getIconRes() {
        return iconRes;
    }

    public ClickPendingIntentActivity getActivityIntent() {
        return activityIntent;
    }

    public ClickPendingIntentBroadCast getBroadCastIntent() {
        return broadCastIntent;
    }

    public NotificationCompat.Builder build(Context context) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, getChannelId())
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(getTitle(context))
                .setContentText(getMessage(context))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        ClickPendingIntentActivity activityIntent = getActivityIntent();
        if (activityIntent != null) {
            PendingIntent notifyPendingIntent =
                    activityIntent.onSettingPendingIntent(context);

            builder.setContentIntent(notifyPendingIntent);
        }

        ClickPendingIntentBroadCast broadCastIntent = getBroadCastIntent();
        if (broadCastIntent != null) {
            PendingIntent notifyPendingIntent = broadCastIntent.onSettingPendingIntent(context);
            builder.setContentIntent(notifyPendingIntent);
        }

        return builder;
    }
}
