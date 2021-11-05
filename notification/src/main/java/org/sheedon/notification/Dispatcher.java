package org.sheedon.notification;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

/**
 * 消息执行器
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2021/11/3 10:36 上午
 */
public class Dispatcher {

    private static final String CHANNEL_ID = "PUSH_ID";

    void init(Context context) {
        createNotificationChannel(context);
    }

    /**
     * 创建通知栏渠道
     *
     * @param context 上下文
     */
    private void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.channel_name);
            String description = context.getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    public void sendNotification(Context context, NotificationMessage message) {
        NotificationCompat.Builder builder = createBuilder(context, message);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        notificationManager.notify(1, builder.build());
    }

    /**
     * 创建通知Builder
     *
     * @param context 上下文
     * @param message 通知信息
     * @return NotificationCompat.Builder
     */
    private NotificationCompat.Builder createBuilder(Context context, NotificationMessage message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(message.getTitle(context))
                .setContentText(message.getMessage(context))
                .setLargeIcon(BitmapFactory.decodeResource(
                        context.getResources(),
                        R.drawable.icon_large))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        Intent activityIntent = message.getActivityIntent();
        if (activityIntent != null) {
            if (activityIntent.getFlags() == 0) {
                activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            @SuppressLint("UnspecifiedImmutableFlag")
            PendingIntent notifyPendingIntent =
                    PendingIntent.getActivity(context,0,
                            activityIntent,PendingIntent.FLAG_UPDATE_CURRENT);

            builder.setContentIntent(notifyPendingIntent);
        }


        return builder;
    }

}
