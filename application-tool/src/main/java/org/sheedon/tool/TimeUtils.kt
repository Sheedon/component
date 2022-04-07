package org.sheedon.tool

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

/**
 * 时间拓展函数
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/3/18 10:59 上午
 */
object TimeUtils {

    @SuppressLint("SimpleDateFormat")
    val format_y_m_d = SimpleDateFormat("yyyy-MM-dd")

    /**
     * 获取今天时间
     */
    fun today(format: SimpleDateFormat = format_y_m_d): String {
        return format.format(Date())
    }

    /**
     * 获取昨天时间
     */
    fun yesterday(format: SimpleDateFormat = format_y_m_d): String {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DATE, -1)
        val time = cal.time
        return format.format(time)
    }

    /**
     * 获取本月开始日期
     */
    fun monthStart(format: SimpleDateFormat = format_y_m_d): String {
        val cal = Calendar.getInstance()
        cal.add(Calendar.MONTH, 0)
        cal[Calendar.DAY_OF_MONTH] = 1
        val time = cal.time
        return format.format(time)
    }

    /**
     * 获取本月最后一天
     */
    fun monthEnd(format: SimpleDateFormat = format_y_m_d): String {
        val cal = Calendar.getInstance()
        cal[Calendar.DAY_OF_MONTH] = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        val time = cal.time
        return format.format(time)
    }

    /**
     * 获取本季度开始日期
     */
    fun quarterStart(format: SimpleDateFormat = format_y_m_d): String {
        val cal = Calendar.getInstance()

        when (cal.get(Calendar.MONTH) + 1) {
            in 1..3 -> cal.set(Calendar.MONTH, 1)
            in 4..6 -> cal.set(Calendar.MONTH, 3)
            in 7..9 -> cal.set(Calendar.MONTH, 4)
            in 10..12 -> cal.set(Calendar.MONTH, 9)
        }
        cal.set(Calendar.DATE, 1)
        return format.format(cal.time)
    }

    /**
     * 获取本季度结束日期
     */
    fun quarterEnd(format: SimpleDateFormat = format_y_m_d): String {
        val cal = Calendar.getInstance()

        when (cal.get(Calendar.MONTH) + 1) {
            in 1..3 -> {
                cal.set(Calendar.MONTH, 2)
                cal.set(Calendar.DATE, 31)
            }
            in 4..6 -> {
                cal.set(Calendar.MONTH, 5)
                cal.set(Calendar.DATE, 30)
            }
            in 7..9 -> {
                cal.set(Calendar.MONTH, 8)
                cal.set(Calendar.DATE, 30)
            }
            in 10..12 -> {
                cal.set(Calendar.MONTH, 11)
                cal.set(Calendar.DATE, 31)
            }
        }
        return format.format(cal.time)
    }

    /**
     * 获取本周的第一天
     * @return String
     */
    fun weekStart(format: SimpleDateFormat = format_y_m_d): String {
        val cal = Calendar.getInstance()
        cal.add(Calendar.WEEK_OF_MONTH, 0)
        cal[Calendar.DAY_OF_WEEK] = 2
        val time = cal.time
        return format.format(time)
    }

    /**
     * 获取本周的最后一天
     * */
    fun weekEnd(format: SimpleDateFormat = format_y_m_d): String {
        val cal = Calendar.getInstance()
        cal[Calendar.DAY_OF_WEEK] = cal.getActualMaximum(Calendar.DAY_OF_WEEK)
        cal.add(Calendar.DAY_OF_WEEK, 1)
        val time = cal.time
        return format.format(time)
    }

    /**
     * 获取本年的第一天
     * **/
    @SuppressLint("SimpleDateFormat")
    fun yearStart(): String {
        return SimpleDateFormat("yyyy").format(Date()) + "-01-01"
    }

    /**
     * 获取本年的最后一天
     * **/
    fun yearEnd(format: SimpleDateFormat = format_y_m_d): String {
        val calendar = Calendar.getInstance()
        calendar[Calendar.MONTH] = calendar.getActualMaximum(Calendar.MONTH)
        calendar[Calendar.DAY_OF_MONTH] = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        val currYearLast = calendar.time
        return format.format(currYearLast)
    }

    /**
     * 转换时间
     */
    fun convertTime(date: Date, format: SimpleDateFormat = format_y_m_d): String {
        return format.format(date)
    }

}