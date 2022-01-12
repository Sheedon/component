package org.sheedon.tool

import android.view.View
import kotlin.math.min

/**
 * dp、sp 转换为 px 的工具类
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/1/5 3:07 下午
 */
object DisplayUtil {

    /**
     * 获取数值精度格式化字符串
     *
     * @param precision 精度
     * @return 精度字符串
     */
    fun getPrecisionFormat(precision: Int): String {
        return "%.${precision}f"
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

}