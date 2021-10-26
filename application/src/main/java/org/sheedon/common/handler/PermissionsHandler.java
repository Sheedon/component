package org.sheedon.common.handler;

import android.content.Context;

import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;

import org.sheedon.common.R;

/**
 * 权限处理执行者
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2021/5/24 1:08 下午
 */
public class PermissionsHandler {

    /**
     * 请求摄像头权限
     *
     * @param context 上下文
     */
    public void requestCameraPermission(Context context, OnPermissionCallback callback) {
        if (XXPermissions.isGranted(context, Permission.CAMERA)) {
            callbackPermissionResult(callback, true);
            return;
        }

        XXPermissions.with(context)
                .permission(Permission.CAMERA)
                .request((permissions, all) -> {
                    showToast(context, all, R.string.common_permission_camera);
                    callbackPermissionResult(callback, all);
                });
    }

    /**
     * 请求麦克风录音权限
     *
     * @param context 上下文
     */
    public void requestAudioPermission(Context context, OnPermissionCallback callback) {

        if (XXPermissions.isGranted(context, Permission.RECORD_AUDIO)) {
            callbackPermissionResult(callback, true);
            return;
        }

        XXPermissions.with(context)
                .permission(Permission.RECORD_AUDIO)
                .request((permissions, all) -> {
                    showToast(context, all, R.string.common_permission_microphone);
                    callbackPermissionResult(callback, all);
                });
    }

    /**
     * 请求定位权限
     *
     * @param context 上下文
     */
    public void requestLocationPermission(Context context, OnPermissionCallback callback) {

        if (XXPermissions.isGranted(context, new String[]{
                Permission.ACCESS_COARSE_LOCATION,
                Permission.ACCESS_FINE_LOCATION,
                Permission.ACCESS_BACKGROUND_LOCATION
        })) {
            callbackPermissionResult(callback, true);
            return;
        }

        XXPermissions.with(context)
                .permission(Permission.ACCESS_COARSE_LOCATION)
                .permission(Permission.ACCESS_FINE_LOCATION)
                .permission(Permission.ACCESS_BACKGROUND_LOCATION)
                .request((permissions, all) -> {
                    showToast(context, all, R.string.common_permission_location);
                    callbackPermissionResult(callback, all);
                });
    }

    /**
     * 请求存储权限
     *
     * @param context 上下文
     */
    public void requestStoragePermission(Context context, OnPermissionCallback callback) {
        if (XXPermissions.isGranted(context, Permission.MANAGE_EXTERNAL_STORAGE)) {
            callbackPermissionResult(callback, true);
            return;
        }

        XXPermissions.with(context)
                .permission(Permission.MANAGE_EXTERNAL_STORAGE)
                .request((permissions, all) -> {
                    showToast(context, all, R.string.common_permission_storage);
                    callbackPermissionResult(callback, all);
                });
    }

    /**
     * 请求安装包权限
     *
     * @param context 上下文
     */
    public void requestInstallPackagesPermission(Context context, OnPermissionCallback callback) {
        if (XXPermissions.isGranted(context, Permission.REQUEST_INSTALL_PACKAGES)) {
            callbackPermissionResult(callback, true);
            return;
        }

        XXPermissions.with(context)
                .permission(Permission.REQUEST_INSTALL_PACKAGES)
                .request((permissions, all) -> {
                    showToast(context, all, R.string.common_permission_install);
                    callbackPermissionResult(callback, all);
                });
    }

    /**
     * 请求悬浮窗权限
     *
     * @param context 上下文
     */
    public void requestAlertWindowPermission(Context context, OnPermissionCallback callback) {
        if (XXPermissions.isGranted(context, Permission.SYSTEM_ALERT_WINDOW)) {
            callbackPermissionResult(callback, true);
            return;
        }

        XXPermissions.with(context)
                .permission(Permission.SYSTEM_ALERT_WINDOW)
                .request((permissions, all) -> {
                    showToast(context, all, R.string.common_permission_window);
                    callbackPermissionResult(callback, all);
                });
    }

    /**
     * 请求通知栏权限
     *
     * @param context 上下文
     */
    public void requestNotificationPermission(Context context, OnPermissionCallback callback) {
        if (XXPermissions.isGranted(context, Permission.NOTIFICATION_SERVICE)) {
            callbackPermissionResult(callback, true);
            return;
        }

        XXPermissions.with(context)
                .permission(Permission.NOTIFICATION_SERVICE)
                .request((permissions, all) -> {
                    showToast(context, all, R.string.common_permission_notification);
                    callbackPermissionResult(callback, all);
                });
    }

    /**
     * 请求系统设置权限
     *
     * @param context 上下文
     */
    public void requestWriteSettingsPermission(Context context, OnPermissionCallback callback) {
        if (XXPermissions.isGranted(context, Permission.WRITE_SETTINGS)) {
            callbackPermissionResult(callback, true);
            return;
        }

        XXPermissions.with(context)
                .permission(Permission.WRITE_SETTINGS)
                .request((permissions, all) -> {
                    showToast(context, all, R.string.common_permission_setting);
                    callbackPermissionResult(callback, all);
                });
    }

    /**
     * 跳转到应用详情页
     *
     * @param context 上下文
     */
    public void startPermissionActivity(Context context) {
        XXPermissions.startPermissionActivity(context);
    }


    /**
     * 显示toast
     *
     * @param context 上下午
     * @param all     是否全部
     * @param res     权限资源
     */
    private void showToast(Context context, boolean all, int res) {
        if (all) {
            ToastHandler.showToast(String.format(
                    context.getResources().getString(R.string.common_permission_success),
                    context.getResources().getString(res)));
        }
    }

    /**
     * 反馈权限结果
     *
     * @param callback  反馈持有
     * @param isSuccess 是否成功
     */
    private void callbackPermissionResult(OnPermissionCallback callback, boolean isSuccess) {
        if (callback == null)
            return;

        callback.onPermissionResult(isSuccess);
    }

    public interface OnPermissionCallback {
        void onPermissionResult(boolean isSuccess);
    }
}
