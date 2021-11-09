package org.sheedon.notification;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import org.sheedon.notification.model.NotificationChannelModel;
import org.sheedon.notification.model.GroupModel;
import org.sheedon.notification.model.SimpleBuilder;
import org.sheedon.notification.utils.NotificationUtils;

import java.util.List;

/**
 * 消息执行器
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2021/11/3 10:36 上午
 */
public class Dispatcher {
    @SuppressLint("StaticFieldLeak")
    static volatile Dispatcher instance;
    private final Context mContext;
    private String defaultChannelId;

    private Dispatcher(Context context) {
        mContext = context;
        defaultChannelId = context.getString(R.string.default_channel_id);
    }

    static Dispatcher with(Context context) {
        if (instance == null) {
            synchronized (Dispatcher.class) {
                if (instance == null) {
                    instance = new Dispatcher(context);
                }
            }
        }
        return instance;
    }

    /**
     * 初始化消息渠道
     *
     * @param channelBuilders 消息渠道
     * @param groupModels     消息组
     */
    void initChannels(List<NotificationChannelModel> channelBuilders, List<GroupModel> groupModels) {
        // 创建通知组
        if (groupModels != null && !groupModels.isEmpty()
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationUtils.createNotificationGroupArray(mContext, groupModels);
        }

        // 创建通知渠道
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (channelBuilders == null || channelBuilders.isEmpty()) {
                NotificationUtils.createNotificationChannel(mContext, new NotificationChannelModel(mContext));
                return;
            }

            // 记录默认消息渠道
            defaultChannelId = channelBuilders.get(0).getChannelId();
            NotificationUtils.createNotificationChannel(mContext, channelBuilders);
        }
    }


    /**
     * 发送消息
     *
     * @param message 消息
     */
    public <T extends SimpleBuilder> void sendNotification(T message) {
        if (message.getChannelId() == null) {
            message.channelId(defaultChannelId);
        }
        NotificationCompat.Builder builder = message.build(mContext);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mContext);

        notificationManager.notify(message.getNotificationId(), builder.build());
    }

    /**
     * 发送一条消息
     *
     * @param notificationId 通知ID
     * @param builder        消息构建器
     */
    public void sendNotification(int notificationId, NotificationCompat.Builder builder) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mContext);

        notificationManager.notify(notificationId, builder.build());
    }
}
