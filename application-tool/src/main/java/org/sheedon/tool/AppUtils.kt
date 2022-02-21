package org.sheedon.tool

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager

/**
 * App基础工具
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/2/21 3:03 下午
 */
object AppUtils {

    /**
     * 获取应用程序名称
     */
    fun getAppName(context: Context): String? {
        val packageInfo = getPackageInfo(context)
        val labelRes = packageInfo?.applicationInfo?.labelRes
        labelRes?.run {
            return context.resources.getString(labelRes)
        }
        return null
    }

    /**
     * 获取应用程序版本名称信息
     *
     * @param context
     * @return 当前应用的版本名称
     */
    fun getVersionName(context: Context): String? {
        val packageInfo = getPackageInfo(context)
        return packageInfo?.versionName
    }

    /**
     * @return 当前程序的版本号
     */
    fun getVersionCode(context: Context): Int {
        val packageInfo = getPackageInfo(context)
        return packageInfo?.versionCode ?: return 0
    }

    fun getPackageInfo(context: Context): PackageInfo? {
        try {
            val pm = context.packageManager
            return pm.getPackageInfo(context.packageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return null
    }
}