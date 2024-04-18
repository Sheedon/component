package org.sheedon.common.handler

import android.content.Context
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import org.sheedon.common.R

/**
 * 权限处理执行者
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/1/6 11:52 上午
 */
class PermissionsHandler {

    /**
     * 请求摄像头权限
     *
     * @param context 上下文
     */
    fun requestCameraPermission(context: Context, callback: OnPermissionCallback) {
        if (XXPermissions.isGranted(context, Permission.CAMERA)) {
            callbackPermissionResult(callback, true)
            return
        }

        XXPermissions.with(context)
            .permission(Permission.CAMERA)
            .request { _, all ->
                showFailToast(context, all, R.string.common_permission_camera)
                callbackPermissionResult(callback, all)
            }
    }

    /**
     * 请求麦克风录音权限
     *
     * @param context 上下文
     */
    fun requestAudioPermission(context: Context, callback: OnPermissionCallback) {

        if (XXPermissions.isGranted(context, Permission.RECORD_AUDIO)) {
            callbackPermissionResult(callback, true)
            return
        }

        XXPermissions.with(context)
            .permission(Permission.RECORD_AUDIO)
            .request { _, all ->
                showFailToast(context, all, R.string.common_permission_microphone)
                callbackPermissionResult(callback, all)
            }
    }

    /**
     * 请求定位权限
     *
     * @param context 上下文
     */
    fun requestLocationPermission(context: Context, callback: OnPermissionCallback) {

        val permissions = arrayOf(
            Permission.ACCESS_COARSE_LOCATION,
            Permission.ACCESS_FINE_LOCATION,
            Permission.ACCESS_BACKGROUND_LOCATION
        )

        if (XXPermissions.isGranted(context, permissions)) {
            callbackPermissionResult(callback, true)
            return
        }

        XXPermissions.with(context)
            .permission(Permission.ACCESS_COARSE_LOCATION)
            .permission(Permission.ACCESS_FINE_LOCATION)
            .permission(Permission.ACCESS_BACKGROUND_LOCATION)
            .request { _, all ->
                showFailToast(context, all, R.string.common_permission_location)
                callbackPermissionResult(callback, all)
            }
    }

    /**
     * 请求存储权限
     *
     * @param context 上下文
     */
    fun requestStoragePermission(context: Context, callback: OnPermissionCallback) {
        if (XXPermissions.isGranted(context, Permission.MANAGE_EXTERNAL_STORAGE)) {
            callbackPermissionResult(callback, true)
            return
        }

        XXPermissions.with(context)
            .permission(Permission.MANAGE_EXTERNAL_STORAGE)
            .request { _, all ->
                showFailToast(context, all, R.string.common_permission_storage)
                callbackPermissionResult(callback, all)
            }
    }

    /**
     * 请求安装包权限
     *
     * @param context 上下文
     */
    fun requestInstallPackagesPermission(context: Context, callback: OnPermissionCallback) {
        if (XXPermissions.isGranted(context, Permission.REQUEST_INSTALL_PACKAGES)) {
            callbackPermissionResult(callback, true)
            return
        }

        XXPermissions.with(context)
            .permission(Permission.REQUEST_INSTALL_PACKAGES)
            .request { _, all ->
                showFailToast(context, all, R.string.common_permission_install)
                callbackPermissionResult(callback, all)
            }
    }

    /**
     * 请求悬浮窗权限
     *
     * @param context 上下文
     */
    fun requestAlertWindowPermission(context: Context, callback: OnPermissionCallback) {
        if (XXPermissions.isGranted(context, Permission.SYSTEM_ALERT_WINDOW)) {
            callbackPermissionResult(callback, true)
            return
        }

        XXPermissions.with(context)
            .permission(Permission.SYSTEM_ALERT_WINDOW)
            .request { _, all ->
                showFailToast(context, all, R.string.common_permission_window)
                callbackPermissionResult(callback, all)
            }
    }

    /**
     * 请求通知栏权限
     *
     * @param context 上下文
     */
    fun requestNotificationPermission(context: Context, callback: OnPermissionCallback) {
        if (XXPermissions.isGranted(context, Permission.NOTIFICATION_SERVICE)) {
            callbackPermissionResult(callback, true)
            return
        }

        XXPermissions.with(context)
            .permission(Permission.NOTIFICATION_SERVICE)
            .request { _, all ->
                showFailToast(context, all, R.string.common_permission_notification)
                callbackPermissionResult(callback, all)
            }
    }

    /**
     * 请求系统设置权限
     *
     * @param context 上下文
     */
    fun requestWriteSettingsPermission(context: Context, callback: OnPermissionCallback) {
        if (XXPermissions.isGranted(context, Permission.WRITE_SETTINGS)) {
            callbackPermissionResult(callback, true)
            return
        }

        XXPermissions.with(context)
            .permission(Permission.WRITE_SETTINGS)
            .request { _, all ->
                showFailToast(context, all, R.string.common_permission_setting)
                callbackPermissionResult(callback, all)
            }
    }


    fun requestPermission(
        context: Context,
        desc: Int,
        vararg permissions: String,
        callback: OnPermissionCallback
    ) {
        if (permissions.isEmpty()) {
            callbackPermissionResult(callback, true)
            return
        }
        if (XXPermissions.isGranted(context, permissions)) {
            callbackPermissionResult(callback, true)
            return
        }

        val xxPermissions = XXPermissions.with(context)
        permissions.forEach {
            xxPermissions.permission(it)
        }
        xxPermissions
            .request { _, all ->
                showOuterToast(context, all, desc)
                callbackPermissionResult(callback, all)
            }
    }

    /**
     * 跳转到应用详情页
     *
     * @param context 上下文
     */
    fun startPermissionActivity(context: Context) {
        XXPermissions.startPermissionActivity(context)
    }


    /**
     * 显示toast
     *
     * @param context 上下午
     * @param all     是否全部
     * @param res     权限资源
     */
    private fun showToast(context: Context, all: Boolean, res: Int) {
        if (all) {
            ToastHandler.showToast(
                String.format(
                    context.resources.getString(R.string.common_permission_success),
                    context.resources.getString(res)
                )
            )
        }
    }

    private fun showFailToast(context: Context, success: Boolean, res: Int) {
        if (success) {
            return
        }

        ToastHandler.showToast(
            String.format(
                context.resources.getString(R.string.common_permission_fail_3),
                context.resources.getString(res)
            )
        )
    }

    /**
     * 显示外部错误消息
     */
    private fun showOuterToast(context: Context, success: Boolean, res: Int) {
        if (success) return

        ToastHandler.showToast(context.resources.getString(res))
    }

    /**
     * 反馈权限结果
     *
     * @param callback  反馈持有
     * @param isSuccess 是否成功
     */
    private fun callbackPermissionResult(callback: OnPermissionCallback?, isSuccess: Boolean) {
        callback?.onPermissionResult(isSuccess)
    }

    interface OnPermissionCallback {
        fun onPermissionResult(isSuccess: Boolean)
    }
}