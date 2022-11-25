package org.sheedon.tool.ext

import android.app.Activity
import android.content.ClipData
import android.content.Context
import android.graphics.Paint
import android.provider.Settings
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.Display
import android.view.View
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * 通用拓展函数
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/1/5 8:03 下午
 */

/**
 * 核实是否为真
 * 若为true，则执行trueAction
 * 若为false，则执行falseAction
 * 并且返回当前值
 */
fun Boolean.checkValue(trueAction: () -> Unit = {}, falseAction: () -> Unit = {}): Boolean {
    if (this) {
        trueAction.invoke()
    } else {
        falseAction.invoke()
    }
    return this
}

/**
 * 判断是否为空 并传入相关操作
 */
@Deprecated("已迁移到Any扩展类中", ReplaceWith("Any -> notNull"))
inline fun <reified T> T?.notNull(notNullAction: (T) -> Unit, nullAction: () -> Unit = {}) {
    if (this != null) {
        notNullAction.invoke(this)
    } else {
        nullAction.invoke()
    }
}

/**
 * 将px值转换为dip或dp值，保证尺寸大小不变
 *
 * @param px px值
 * @return dip 值
 */
fun Context.px2dip(px: Float): Int {
    val scale = resources.displayMetrics.density
    return (px / scale + 0.5f).toInt()
}

/**
 * 将dip或dp值转换为px值，保证尺寸大小不变
 *
 * @param dip dip值
 * @return px 值
 */
fun Context.dip2px(dip: Float): Int {
    val scale = resources.displayMetrics.density
    return (dip * scale + 0.5f).toInt()
}

/**
 * 将px值转换为sp值，保证文字大小不变
 *
 * @param pxValue px值
 * @return sp值
 */
fun Context.px2sp(px: Float): Int {
    val fontScale = resources.displayMetrics.density
    return (px / fontScale + 0.5f).toInt()
}

/**
 * 将sp值转换为px值，保证文字大小不变
 *
 * @param sp sp值
 * @return px值
 */
fun Context.sp2px(sp: Float): Int {
    val fontScale = resources.displayMetrics.density
    return (sp / fontScale + 0.5f).toInt()
}

/**
 * 获取当前手机屏幕尺寸
 */
fun Context.getPingMuSize(): Float {
    val xdpi = resources.displayMetrics.xdpi
    val ydpi = resources.displayMetrics.ydpi
    val width = resources.displayMetrics.widthPixels
    val height = resources.displayMetrics.heightPixels

    // 这样可以计算屏幕的物理尺寸
    val width2 = (width / xdpi) * (width / xdpi)
    val height2 = (height / ydpi) * (width / xdpi)

    return (sqrt(width2 + height2))
}

fun Activity.getScreenPhysicalSize(): Double {
    val dm = DisplayMetrics()
    @Suppress("DEPRECATION")
    windowManager.defaultDisplay.getMetrics(dm)
    val diagonalPixels = sqrt(
        dm.widthPixels.toDouble().pow(2.0) + dm.heightPixels.toDouble().pow(2.0)
    )
    return diagonalPixels / (160 * dm.density)
}


fun Context.getDefaultDisplay(): Display? {
    @Suppress("DEPRECATION")
    return windowManager?.defaultDisplay
}

/**
 * 获取屏幕宽度
 */
val Context.screenWidth
    get() = resources.displayMetrics.widthPixels

/**
 * 获取屏幕高度
 */
val Context.screenHeight
    get() = resources.displayMetrics.heightPixels

/**
 * 测量文字高度
 *
 * @return 文字高度
 */
fun Paint.measureTextHeight(): Float {
    return (abs(fontMetrics.ascent) - fontMetrics.descent)
}

/**
 * 复制文本到粘贴板
 */
fun Context.copyToClipboard(text: String, label: String = "") {
    val clipData = ClipData.newPlainText(label, text)
    clipboardManager?.setPrimaryClip(clipData)
}

/**
 * 检查是否启用无障碍服务
 */
fun Context.checkAccessibilityServiceEnabled(serviceName: String): Boolean {
    Settings.Secure.getString(
        applicationContext.contentResolver,
        Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
    )
    var result = false
    val splitter = TextUtils.SimpleStringSplitter(':')
    while (splitter.hasNext()) {
        if (splitter.next().equals(serviceName, true)) {
            result = true
            break
        }
    }
    return result
}

/**
 * 设置点击事件
 * @param views 需要设置点击事件的view
 * @param onClick 点击触发的方法
 */
fun setOnclick(vararg views: View?, onClick: (View) -> Unit) {
    views.forEach {
        it?.setOnClickListener { view ->
            onClick.invoke(view)
        }
    }
}

/**
 * 设置防止重复点击事件
 * @param views 需要设置点击事件的view集合
 * @param interval 时间间隔 默认0.5秒
 * @param onClick 点击触发的方法
 */
fun setOnclickNoRepeat(vararg views: View?, interval: Long = 500, onClick: (View) -> Unit) {
    views.forEach {
        it?.clickNoRepeat(interval = interval) { view ->
            onClick.invoke(view)
        }
    }
}
