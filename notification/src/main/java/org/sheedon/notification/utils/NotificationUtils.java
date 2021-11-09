package org.sheedon.notification.utils;

import android.app.AppOpsManager;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationManagerCompat;

import org.sheedon.notification.model.NotificationChannelModel;
import org.sheedon.notification.model.GroupModel;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 通知基础工具
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2021/11/9 11:44 上午
 */
public class NotificationUtils {

    private static final String CHECK_OP_NO_THROW = "checkOpNoThrow";
    private static final String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

    /**
     * 创建消息渠道
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static boolean createNotificationChannel(Context context, @NonNull NotificationChannelModel channel) {
        if (channel == null) {
            return false;
        }

        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel.build());
        return true;
    }

    /**
     * 创建消息渠道
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static boolean createNotificationChannel(Context context, @NonNull List<NotificationChannelModel> channels) {
        if (channels == null || channels.isEmpty()) {
            return false;
        }

        List<NotificationChannel> channelList = new ArrayList<>();
        for (NotificationChannelModel channel : channels) {
            channelList.add(channel.build());
        }

        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannels(channelList);
        return true;
    }

    /**
     * 删除消息通知渠道
     *
     * @param context   上下文
     * @param channelId 渠道ID
     * @return 是否成功
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static boolean deleteNotificationChannel(Context context, String channelId) {
        if (context == null || channelId == null || channelId.isEmpty()) {
            return false;
        }
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(
                Context.NOTIFICATION_SERVICE);
        notificationManager.deleteNotificationChannel(channelId);
        return true;
    }

    /**
     * 判断应用渠道通知是否打开（适配8.0）
     *
     * @return true 打开
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static boolean openNotificationChannel(Context context, NotificationManager manager, String channelId) {
        //判断通知是否有打开
        if (!isNotificationEnabled(context)) {
            toNotifySetting(context, null);
            return false;
        }
        //判断渠道通知是否打开
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = manager.getNotificationChannel(channelId);
            if (channel.getImportance() == NotificationManager.IMPORTANCE_NONE) {
                //没打开调往设置界面
                toNotifySetting(context, channel.getId());
                return false;
            }
        }

        return true;
    }

    /**
     * 跳转到应用通知
     */
    public static void toNotifySetting(Context context, String channelId) {
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { //适配 8.0及8.0以上(8.0需要先打开应用通知，再打开渠道通知)
            if (TextUtils.isEmpty(channelId)) {
                intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.getPackageName());
            } else {
                intent.setAction(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.getPackageName());
                intent.putExtra(Settings.EXTRA_CHANNEL_ID, channelId);
            }
        } else {//适配 5.0及5.0以上
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.putExtra("app_package", context.getPackageName());
            intent.putExtra("app_uid", context.getApplicationInfo().uid);
        }
        context.startActivity(intent);
    }

    /**
     * @return 判断应用通知是否打开
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static boolean isNotificationEnabled(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
            return notificationManagerCompat.areNotificationsEnabled();
        }
        AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        Class appOpsClass;
        /* Context.APP_OPS_MANAGER */
        try {
            appOpsClass = Class.forName(AppOpsManager.class.getName());
            Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE,
                    String.class);
            Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);
            int value = (Integer) opPostNotificationValue.get(Integer.class);
            return ((Integer) checkOpNoThrowMethod.invoke(mAppOps, value, context.getApplicationInfo().uid, context.getPackageName()) == AppOpsManager.MODE_ALLOWED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 创建通知组
     *
     * @param context   上下文
     * @param groupId   组ID
     * @param groupName 组名称
     * @return 是否创建成功
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static boolean createNotificationGroup(Context context, String groupId, String groupName) {
        if (context == null) {
            return false;
        }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(
                Context.NOTIFICATION_SERVICE);
        return createNotificationGroup(notificationManager, groupId, groupName);
    }

    /**
     * 创建通知组
     *
     * @param manager   消息管理者
     * @param groupId   组ID
     * @param groupName 组名称
     * @return 是否创建成功
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static boolean createNotificationGroup(NotificationManager manager, String groupId, String groupName) {
        if (manager == null)
            return false;

        NotificationChannelGroup group = createNotificationChannelGroup(groupId, groupName);
        return createNotificationGroup(manager, group);
    }

    /**
     * 创建通知组
     *
     * @param context 上下文
     * @param group   通知渠道组
     * @return 是否创建成功
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static boolean createNotificationGroup(Context context, NotificationChannelGroup group) {
        if (context == null) {
            return false;
        }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(
                Context.NOTIFICATION_SERVICE);
        return createNotificationGroup(notificationManager, group);
    }

    /**
     * 创建通知组
     *
     * @param manager 消息管理者
     * @param group   通知渠道组
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static boolean createNotificationGroup(NotificationManager manager, NotificationChannelGroup group) {
        if (manager == null || group == null) {
            return false;
        }

        manager.createNotificationChannelGroup(group);
        return true;
    }

    /**
     * 通知组集合
     *
     * @param context 上下文
     * @param groups  组
     * @return 是否创建成功
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static boolean createNotificationGroups(Context context, List<NotificationChannelGroup> groups) {
        if (context == null || groups == null || groups.isEmpty()) {
            return false;
        }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(
                Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannelGroups(groups);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void createNotificationGroupArray(Context context, List<GroupModel> groupModels) {
        if (context == null || groupModels == null || groupModels.isEmpty()) {
            return;
        }

        List<NotificationChannelGroup> groups = new ArrayList<>();
        for (GroupModel groupModel : groupModels) {
            groups.add(createNotificationChannelGroup(groupModel));
        }
        createNotificationGroups(context, groups);
    }

    /**
     * 删除消息通知组
     *
     * @param context 上下文
     * @param groupId 组ID
     * @return 是否删除成功
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static boolean deleteGroup(Context context, String groupId) {
        if (context == null || groupId == null || groupId.isEmpty()) {
            return false;
        }
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(
                Context.NOTIFICATION_SERVICE);
        notificationManager.deleteNotificationChannelGroup(groupId);
        return true;
    }

    /**
     * create NotificationChannelGroup
     *
     * @param model 组Model
     * @return NotificationChannelGroup
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static NotificationChannelGroup createNotificationChannelGroup(GroupModel model) {
        if (model == null) {
            return null;
        }

        String groupId = model.getGroupId();
        String groupName = model.getGroupName();
        String description = model.getDescription();

        if (description == null || description.isEmpty() || Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            return createNotificationChannelGroup(groupId, groupName);
        }

        return createNotificationChannelGroup(groupId, groupName, description);
    }

    /**
     * create NotificationChannelGroup
     *
     * @param groupId   组ID
     * @param groupName 组名称
     * @return NotificationChannelGroup
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static NotificationChannelGroup createNotificationChannelGroup(String groupId, String groupName) {
        return new NotificationChannelGroup(groupId, groupName);
    }

    /**
     * create NotificationChannelGroup
     *
     * @param groupId     组ID
     * @param groupName   组名称
     * @param description 描述
     * @return NotificationChannelGroup
     */
    @RequiresApi(api = Build.VERSION_CODES.P)
    public static NotificationChannelGroup createNotificationChannelGroup(String groupId, String groupName, String description) {
        NotificationChannelGroup channelGroup = new NotificationChannelGroup(groupId, groupName);
        channelGroup.setDescription(description);
        return channelGroup;
    }

}
