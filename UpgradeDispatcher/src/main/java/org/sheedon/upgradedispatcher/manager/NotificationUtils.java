package org.sheedon.upgradedispatcher.manager;


import static android.app.Notification.VISIBILITY_SECRET;

import static androidx.core.app.NotificationCompat.PRIORITY_DEFAULT;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import org.sheedon.upgradedispatcher.R;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://www.jianshu.com/p/514eb6193a06
 *     time  : 2018/2/10
 *     desc  : 通知栏工具类
 *     revise:
 * </pre>
 */
public class NotificationUtils extends ContextWrapper {


    public static final String CHANNEL_ID = "default";
    private static final String CHANNEL_NAME = "Default_Channel";
    private NotificationManager mManager;
    private NotificationChannel channel;
    private Notification notification;
    private int[] flags;

    private RemoteViews remoteViews;

    public NotificationUtils(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //android 8.0以上需要特殊处理，也就是targetSDKVersion为26以上
            channel = createNotificationChannel();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private NotificationChannel createNotificationChannel() {
        //第一个参数：channel_id
        //第二个参数：channel_name
        //第三个参数：设置通知重要性级别
        //注意：该级别必须要在 NotificationChannel 的构造函数中指定，总共要五个级别；
        //范围是从 NotificationManager.IMPORTANCE_NONE(0) ~ NotificationManager.IMPORTANCE_HIGH(4)
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW);
        channel.canBypassDnd();//是否绕过请勿打扰模式
        channel.enableLights(true);//是否在桌面icon右上角展示小红点
        channel.setLockscreenVisibility(VISIBILITY_SECRET);//锁屏显示通知
        channel.setLightColor(Color.RED);//闪关灯的灯光颜色
        channel.canShowBadge();//桌面launcher的消息角标
        //channel.enableVibration(false);//是否允许震动
        channel.getAudioAttributes();//获取系统通知响铃声音的配置
        channel.getGroup();//获取通知取到组
        channel.setBypassDnd(true);//设置可绕过 请勿打扰模式
        channel.setSound(null, null);
        //channel.setVibrationPattern(new long[]{100, 100, 200});//设置震动模式
        channel.shouldShowLights();//是否会有灯光
        channel.setShowBadge(true); //是否在久按桌面图标时显示此渠道的通知
        getManager().createNotificationChannel(channel);
        return channel;
    }

    /**
     * 获取创建一个NotificationManager的对象
     *
     * @return NotificationManager对象
     */
    public NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }

    private RemoteViews getRemoteViews(int progress) {
        if (remoteViews == null) {
            remoteViews = new RemoteViews(this.getPackageName(), R.layout.remote_notification_view);
            remoteViews.setTextViewText(R.id.tvTitle, "下载apk");
        }
        remoteViews.setProgressBar(R.id.pb, 100, progress, false);
        return remoteViews;
    }

    private Notification createNotification(int progress) {
        if (notification != null) {
            notification.contentView = getRemoteViews(progress);
            return notification;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            RemoteViews remoteViews = new RemoteViews(this.getPackageName(), R.layout.remote_notification_view);
            remoteViews.setTextViewText(R.id.tvTitle, getApplicationContext().getText(R.string.app_update_app));
            notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setCustomContentView(getRemoteViews(progress))
                    .setSmallIcon(R.mipmap.ic_launcher).build();
        } else {
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setCustomContentView(getRemoteViews(progress))
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setOngoing(true);
//                    .setChannel(id);//无效
            notification = notificationBuilder.build();
        }
        return notification;
    }

    public void showNotification(int progress) {
        //下载成功或者失败
        if (progress == 100 || progress == -1) {
            getManager().cancelAll();
        } else {
            getManager().notify(1, createNotification(progress));
        }
    }


}
