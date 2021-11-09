package org.sheedon.notification;

import android.content.Context;

import androidx.core.app.NotificationCompat;

import org.sheedon.notification.model.NotificationChannelModel;
import org.sheedon.notification.model.GroupModel;
import org.sheedon.notification.model.SimpleBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 通知客户端
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2021/11/3 10:31 上午
 */
public class NotificationClient {

    // 消息执行器
    private final Dispatcher dispatcher;

    private NotificationClient(Builder builder) {
        this.dispatcher = builder.dispatcher;
    }

    /**
     * 发送一条通知
     *
     * @param message 通知消息
     */
    public void sendNotification(SimpleBuilder message) {
        if (message == null) return;
        dispatcher.sendNotification(message);
    }

    /**
     * 发送一条通知
     *
     * @param builder 通知消息
     */
    public void sendNotification(int notificationId, NotificationCompat.Builder builder) {
        if (builder == null) return;
        dispatcher.sendNotification(notificationId, builder);
    }

    public static class Builder {

        private Context context;
        private Dispatcher dispatcher;
        private List<NotificationChannelModel> channelBuilders = new ArrayList<>();
        private List<GroupModel> groupModels = new ArrayList<>();

        public Builder() {
        }

        public Builder application(Context context) {
            this.context = Objects.requireNonNull(context, "context == null")
                    .getApplicationContext();
            return this;
        }

        public Builder channelBuilders(List<NotificationChannelModel> channelBuilders) {
            this.channelBuilders = Objects.requireNonNull(channelBuilders, "channelBuilders == null");
            return this;
        }

        public Builder appendChannel(NotificationChannelModel channelBuilder) {
            this.channelBuilders.add(Objects.requireNonNull(channelBuilder, "channelBuilder == null"));
            return this;
        }

        public Builder groupModels(List<GroupModel> groupModels) {
            this.groupModels = Objects.requireNonNull(groupModels, "groupModels == null");
            return this;
        }

        public Builder appendGroup(GroupModel groupModel) {
            this.groupModels.add(Objects.requireNonNull(groupModel, "groupModel == null"));
            return this;
        }

        public NotificationClient build() {
            Objects.requireNonNull(context, "please use application(Content content) to set content");
            this.dispatcher = Dispatcher.with(context);
            this.dispatcher.initChannels(channelBuilders, groupModels);
            return new NotificationClient(this);
        }
    }

}
