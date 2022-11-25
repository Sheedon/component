package org.sheedon.tool.ext

/**
 * 数字
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/9/23 11:24
 */
fun Double.round(decimals: Int = 2): Double = "%.${decimals}f".format(this).toDouble()

fun Float.round(decimals: Int = 2): Float = "%.${decimals}f".format(this).toFloat()