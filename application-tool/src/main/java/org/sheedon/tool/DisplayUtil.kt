package org.sheedon.tool

import android.app.Activity
import android.content.Context
import android.graphics.Paint
import android.graphics.Point
import android.util.DisplayMetrics
import android.view.Display
import android.view.View
import android.view.WindowManager
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * dp、sp 转换为 px 的工具类
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/1/5 3:07 下午
 */
object DisplayUtil {

    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     *
     * @param pxValue px值
     * @return dip 值
     */
    fun px2dip(context: Context, pxValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param dipValue dip值
     * @return px值
     */
    fun dip2px(context: Context, dipValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue px值
     * @return sp值
     */
    fun px2sp(context: Context, pxValue: Float): Int {
        val fontScale = context.resources.displayMetrics.density
        return (pxValue / fontScale + 0.5f).toInt()
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue sp值
     * @return px值
     */
    fun sp2px(context: Context, spValue: Float): Int {
        val fontScale = context.resources.displayMetrics.density
        return (spValue / fontScale + 0.5f).toInt()
    }

    /**
     * @ 获取当前手机屏幕尺寸
     */
    fun getPingMuSize(context: Context): Float {
        val xdpi = context.resources.displayMetrics.xdpi
        val ydpi = context.resources.displayMetrics.ydpi
        val width = context.resources.displayMetrics.widthPixels
        val height = context.resources.displayMetrics.heightPixels

        // 这样可以计算屏幕的物理尺寸
        val width2 = (width / xdpi) * (width / xdpi)
        val height2 = (height / ydpi) * (width / xdpi)


        return (sqrt(width2 + height2))
    }

    fun getScreenPhysicalSize(ctx: Activity): Double {
        val dm = DisplayMetrics()
        ctx.windowManager.defaultDisplay.getMetrics(dm)
        val diagonalPixels = sqrt(
            dm.widthPixels.toDouble().pow(2.0) + dm.heightPixels.toDouble().pow(2.0)
        )
        return diagonalPixels / (160 * dm.density)
    }

    /**
     * 获取数值精度格式化字符串
     *
     * @param precision 精度
     * @return 精度字符串
     */
    fun getPrecisionFormat(precision: Int): String {
        return "%.${precision}f"
    }

    fun getDisplay(context: Context): Display? {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        return windowManager.defaultDisplay
    }

    fun getScreenHeight(context: Context):Int{
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val size = Point()
        wm.defaultDisplay.getSize(size)
        return size.y // 高  return size.x 宽
    }

    fun getScreenWidth(context: Context):Int{
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val size = Point()
        wm.defaultDisplay.getSize(size)
        return size.x // 高  return size.x 宽
    }

    /**
     * 测量 View
     *
     * @param measureSpec 测量规格
     * @param defaultSize View 的默认大小
     * @return view大小
     */
    fun measure(measureSpec:Int,defaultSize:Int):Int{
        var result = defaultSize
        val specMode = View.MeasureSpec.getMode(measureSpec)
        val specSize = View.MeasureSpec.getSize(measureSpec)

        if (specMode == View.MeasureSpec.EXACTLY) {
            result = specSize
        } else if (specMode == View.MeasureSpec.AT_MOST) {
            result = min(result, specSize)
        }
        return result
    }

    /**
     * 测量文字高度
     *
     * @param paint 绘制笔
     * @return 文字高度
     */
    fun measureTextHeight(paint:Paint):Float{
        val fontMetrics = paint.fontMetrics
        return (abs(fontMetrics.ascent) - fontMetrics.descent)
    }

}