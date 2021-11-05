package org.sheedon.notification;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import java.util.Objects;

/**
 * 通知消息
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2021/11/3 10:07 上午
 */
public class NotificationMessage {

    private final String title;
    private final String message;
    private final Intent activityIntent;

    private NotificationMessage(Builder builder) {
        this.title = builder.title;
        this.message = builder.message;
        this.activityIntent = builder.activityIntent;
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

    public Intent getActivityIntent() {
        return activityIntent;
    }

    public static class Builder {
        private String title;
        private String message;
        private Intent activityIntent;

        public Builder() {
        }

        public Builder title(String title) {
            this.title = Objects.requireNonNull(title, "title == null");
            return this;
        }

        public Builder message(String message) {
            this.message = Objects.requireNonNull(message, "message == null");
            return this;
        }

        public Builder activityIntent(Intent activityIntent) {
            this.activityIntent = Objects.requireNonNull(activityIntent, "activityIntent == null");
            return this;
        }

        public NotificationMessage build() {
            return new NotificationMessage(this);
        }


    }
}
