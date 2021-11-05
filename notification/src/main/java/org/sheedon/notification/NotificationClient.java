package org.sheedon.notification;

import android.content.Context;

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
    private final Context context;

    private NotificationClient(Builder builder) {
        this.context = builder.context;
        this.dispatcher = builder.dispatcher;

        this.dispatcher.init(context);
    }

    /**
     * 发送一条通知
     *
     * @param message 通知消息
     */
    public void sendNotification(NotificationMessage message) {
        dispatcher.sendNotification(context, message);
    }

    public static class Builder {

        private Context context;
        private final Dispatcher dispatcher;

        public Builder() {
            dispatcher = new Dispatcher();
        }

        public Builder application(Context context) {
            this.context = Objects.requireNonNull(context, "context == null")
                    .getApplicationContext();
            return this;
        }

        public NotificationClient build() {
            Objects.requireNonNull(context, "please use application(Content content) to set content");
            return new NotificationClient(this);
        }
    }

}
