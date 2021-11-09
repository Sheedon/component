package org.sheedon.notification.model;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.annotation.IntDef;
import androidx.annotation.RequiresApi;

import org.sheedon.notification.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Objects;

/**
 * 通知渠道构建者
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2021/11/9 1:28 下午
 */
@RequiresApi(api = Build.VERSION_CODES.N)
public class NotificationChannelModel {

    // 上下文¬
    private final Context context;
    // 通道Id
    private String channelId;
    // 通道名称
    private String channelName;
    // 通道描述
    private String channelDescription;
    // 通知质量
    private int importance = NotificationManager.IMPORTANCE_DEFAULT;
    // 组ID
    private String groupId;

    public NotificationChannelModel(Context context) {
        this.context = context;
    }

    @IntDef(value = {
            NotificationManager.IMPORTANCE_UNSPECIFIED,
            NotificationManager.IMPORTANCE_NONE,
            NotificationManager.IMPORTANCE_MIN,
            NotificationManager.IMPORTANCE_LOW,
            NotificationManager.IMPORTANCE_DEFAULT,
            NotificationManager.IMPORTANCE_HIGH
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface Importance {
    }

    /**
     * 设置渠道基础信息
     *
     * @param channelId   渠道ID
     * @param channelName 渠道名
     * @return NotificationChannelBuilder
     */
    public NotificationChannelModel channelInfo(String channelId, String channelName) {
        return channelId(channelId).channelName(channelName);
    }


    /**
     * 设置渠道ID
     *
     * @param channelId 渠道ID
     * @return NotificationChannelBuilder
     */
    public NotificationChannelModel channelId(String channelId) {
        this.channelId = Objects.requireNonNull(channelId, "channelId == null");
        return this;
    }

    /**
     * 设置渠道名
     *
     * @param channelName 渠道名
     * @return NotificationChannelBuilder
     */
    public NotificationChannelModel channelName(String channelName) {
        this.channelName = Objects.requireNonNull(channelName, "channelName == null");
        return this;
    }

    /**
     * 设置渠道描述
     *
     * @param description 渠道描述
     * @return NotificationChannelBuilder
     */
    public NotificationChannelModel channelDescription(String description) {
        this.channelDescription = Objects.requireNonNull(description, "description == null");
        return this;
    }

    /**
     * 设置通知渠道质量
     *
     * @param importance 通知渠道质量
     * @return NotificationChannelBuilder
     */
    public NotificationChannelModel importance(@Importance int importance) {
        this.importance = importance;
        return this;
    }

    /**
     * 设置组ID
     *
     * @param groupId 组ID
     * @return NotificationChannelBuilder
     */
    public NotificationChannelModel groupId(String groupId) {
        this.groupId = groupId;
        return this;
    }

    public String getChannelId() {
        return channelId;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public NotificationChannel build() {
        if (context == null) {
            throw new NullPointerException("context == null");
        }

        if (channelId == null || channelId.isEmpty()) {
            channelId = context.getString(R.string.default_channel_id);
        }

        if (channelName == null || channelName.isEmpty()) {
            channelName = context.getString(R.string.channel_name);
        }

        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);

        if (channelDescription != null && !channelDescription.isEmpty()) {
            channel.setDescription(channelDescription);
        }

        if (groupId != null && !groupId.isEmpty()) {
            channel.setGroup(groupId);
        }
        return channel;
    }
}
