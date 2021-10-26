package org.sheedon.upgradedispatcher;

import android.content.Context;

import org.sheedon.upgradedispatcher.listener.InitializeListener;
import org.sheedon.upgradedispatcher.listener.UpgradeListener;

/**
 * 升级安装程序
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2021/10/25 10:05 上午
 */
public class UpgradeInstaller {
    private static final String TAG = "Upgrade.UpgradeInstaller";

    public static Upgrade setUp(Context context, InitializeListener listener) {
        return new Upgrade.Builder(context).initializeListener(listener).build();
    }

    /**
     * 安装的升级包
     *
     * @param context     上下文
     * @param versionName 版本名称
     * @param apkUrl      apk 路径
     * @param listener    更新监听器
     */
    public static void upgradeApp(Context context, String versionName, String apkUrl, UpgradeListener listener) {
        Upgrade.with(context).upgradeApp(context, versionName, apkUrl, listener);
    }

    /**
     * 安装的升级包
     *
     * @param context     上下文
     * @param versionName 版本名称
     * @param apkUrl      apk 路径
     */
    public static void upgradeApp(Context context, String versionName, String apkUrl) {
        upgradeApp(context, versionName, apkUrl, null);
    }

    public static void cancel(Context context) {
        Upgrade.with(context).cancel();
    }
}
