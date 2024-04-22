package org.sheedon.tool.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

/**
 * 时间拓展函数
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/3/18 10:59 上午
 */
@Suppress("SimpleDateFormat")
object TimeUtils {

    private val threadLocal = ThreadLocal<HashMap<String, SimpleDateFormat>>()

    const val PATTERN_Y_M_D = "yyyy-MM-dd"
    const val PATTERN_Y_M_D_H_M_S = "yyyy-MM-dd HH:mm:ss"
    const val PATTERN_Y_M_D_H_M_S_S = "yyyy-MM-dd'T'HH:mm:ss.SSS"
    const val PATTERN_Y_M_D_H_ZH = "yyyy年MM月dd日HH时"
    const val PATTERN_Y_M_D_H_M_ZH = "yyyy年MM月dd日HH时mm分"
    const val PATTERN_M_D_H_M_S_ZH = "MM月dd日 HH:mm:ss"

    val format_y_m_d = SimpleDateFormat("yyyy-MM-dd")
    val format_y_m_d_h_m_s = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val format_y_m_d_h_m_s_s = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
    val format_y_m_d_h_zh = SimpleDateFormat("yyyy年MM月dd日HH时")
    val format_y_m_d_h_m_zh = SimpleDateFormat("yyyy年MM月dd日HH时mm分")
    val format_m_d_h_m_s_zh = SimpleDateFormat("MM月dd日 HH:mm:ss")

    /**
     * 获取时间格式
     * 从线程中获取SimpleDateFormat，避免多线程问题
     * @param pattern 时间格式
     */
    @JvmStatic
    @JvmOverloads
    fun getDateFormat(pattern: String = PATTERN_Y_M_D): SimpleDateFormat {
        var map = threadLocal.get()
        if (map == null) {
            map = HashMap()
            threadLocal.set(map)
        }

        val df = map[pattern]
        if (df == null) {
            val simpleDateFormat = SimpleDateFormat(pattern)
            map[pattern] = simpleDateFormat
            return simpleDateFormat
        }
        return df
    }

    /**
     * 格式化时间
     * @param date 时间
     * @param pattern 时间格式
     */
    @JvmStatic
    @JvmOverloads
    fun formatDate(date: Date = Date(), pattern: String = PATTERN_Y_M_D): String {
        try {
            return getDateFormat(pattern).format(date)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    /**
     * 解析时间
     * @param strDate 时间字符串
     * @param pattern 时间格式
     */
    @Throws(ParseException::class)
    fun parse(strDate: String): Date {
        try {
            return getDateFormat().parse(strDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return Date()
    }

    /**
     * 获取今天时间
     */
    fun today(format: SimpleDateFormat = format_y_m_d): String {
        return convertToday(format.toPattern())
    }

    @JvmStatic
    @JvmOverloads
    fun convertToday(pattern: String = PATTERN_Y_M_D): String {
        return formatDate(Date(), pattern)
    }

    /**
     * 获取昨天时间
     */
    fun yesterday(format: SimpleDateFormat = format_y_m_d): String {
        return fromTodayDay(-1, format)
    }

    @JvmStatic
    @JvmOverloads
    fun convertYesterday(pattern: String = PATTERN_Y_M_D): String {
        return fromTodayDays(-1, pattern)
    }

    /**
     * 获取距离今天为n天的日期
     */
    fun fromTodayDay(num: Int, format: SimpleDateFormat = format_y_m_d): String {
        return fromTodayDays(num, format.toPattern())
    }

    @JvmStatic
    @JvmOverloads
    fun fromTodayDays(num: Int, pattern: String = PATTERN_Y_M_D): String {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DATE, num)
        val time = cal.time
        return formatDate(time, pattern)
    }

    /**
     * 获取本月开始日期
     */
    fun monthStart(format: SimpleDateFormat = format_y_m_d): String {
        return monthStartDate(format.toPattern())
    }

    /**
     * 获取本月开始日期
     */
    fun monthStartDate(pattern: String = PATTERN_Y_M_D): String {
        val cal = Calendar.getInstance()
        cal.add(Calendar.MONTH, 0)
        cal[Calendar.DAY_OF_MONTH] = 1
        val time = cal.time
        return formatDate(time, pattern)
    }

    /**
     * 获取本月最后一天
     */
    fun monthEnd(format: SimpleDateFormat = format_y_m_d): String {
        return monthEndDate(format.toPattern())
    }

    /**
     * 获取本月最后一天
     */
    fun monthEndDate(pattern: String = PATTERN_Y_M_D): String {
        val cal = Calendar.getInstance()
        cal[Calendar.DAY_OF_MONTH] = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        val time = cal.time
        return formatDate(time, pattern)
    }

    /**
     * 获取本季度开始日期
     */
    fun quarterStart(format: SimpleDateFormat = format_y_m_d): String {
        return quarterStartDate(format.toPattern())
    }

    /**
     * 获取本季度结束日期
     */
    fun quarterStartDate(pattern: String = PATTERN_Y_M_D): String {
        val cal = Calendar.getInstance()

        when (cal.get(Calendar.MONTH) + 1) {
            in 1..3 -> cal.set(Calendar.MONTH, 1)
            in 4..6 -> cal.set(Calendar.MONTH, 3)
            in 7..9 -> cal.set(Calendar.MONTH, 4)
            in 10..12 -> cal.set(Calendar.MONTH, 9)
        }
        cal.set(Calendar.DATE, 1)
        return formatDate(cal.time, pattern)
    }

    /**
     * 获取本季度结束日期
     */
    fun quarterEnd(format: SimpleDateFormat = format_y_m_d): String {
        return quarterEndDate(format.toPattern())
    }

    fun quarterEndDate(pattern: String = PATTERN_Y_M_D): String {
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
        return formatDate(cal.time, pattern)
    }

    /**
     * 获取本周的第一天
     * @return String
     */
    fun weekStart(format: SimpleDateFormat = format_y_m_d): String {
        return weekStartDate(format.toPattern())
    }

    fun weekStartDate(pattern: String = PATTERN_Y_M_D): String {
        val cal = Calendar.getInstance()
        cal.add(Calendar.WEEK_OF_MONTH, 0)
        cal[Calendar.DAY_OF_WEEK] = 2
        val time = cal.time
        return formatDate(time, pattern)
    }

    /**
     * 获取本周的最后一天
     * */
    fun weekEnd(format: SimpleDateFormat = format_y_m_d): String {
        return weekEndDate(format.toPattern())
    }

    fun weekEndDate(pattern: String = PATTERN_Y_M_D): String {
        val cal = Calendar.getInstance()
        cal[Calendar.DAY_OF_WEEK] = cal.getActualMaximum(Calendar.DAY_OF_WEEK)
        cal.add(Calendar.DAY_OF_WEEK, 1)
        val time = cal.time
        return formatDate(time, pattern)
    }

    /**
     * 获取本年的第一天
     * **/
    fun yearStart(): String {
        return yearStartDate()
    }

    fun yearStartDate(): String {
        return getDateFormat("yyyy").format("yyyy") + "-01-01"
    }

    /**
     * 获取本年的最后一天
     * **/
    fun yearEnd(format: SimpleDateFormat = format_y_m_d): String {
        return yearEndDate()
    }

    /**
     * 获取本年的最后一天
     * **/
    fun yearEndDate(): String {
        return getDateFormat("yyyy").format("yyyy") + "-12-31"
    }

    /**
     * 转换时间
     */
    fun convertTime(date: Date, format: SimpleDateFormat = format_y_m_d): String {
        return convertTime(date, format.toPattern())
    }

    fun convertTime(date: Date, pattern: String): String {
        return getDateFormat(pattern).format(date)
    }

    /**
     * 转换时间
     */
    fun convertTime(
        time: String,
        currentFormat: SimpleDateFormat,
        targetFormat: SimpleDateFormat
    ): String {
        return convertTime(time, currentFormat.toPattern(), targetFormat.toPattern())
    }

    fun convertTime(
        time: String,
        currentPattern: String,
        targetPattern: String
    ): String {
        return try {
            val date = getDateFormat(currentPattern).parse(time)
            convertTime(date, targetPattern)
        } catch (e: ParseException) {
            time
        }
    }

    fun convertDate(
        time: String,
        format: SimpleDateFormat = format_y_m_d
    ): Date {
        return convertDate(time, format.toPattern())
    }

    fun convertDate(
        time: String,
        pattern: String
    ): Date {
        return getDateFormat(pattern).parse(time)
    }

    fun convertToDay(time: String): String {
        if (time.length > 10) {
            return time.substring(0, 10)
        }

        return time
    }

    /**
     * 是否早于当前
     */
    fun isEarlierThanNowTime(date: Date): Boolean {
        return date.time < System.currentTimeMillis()
    }

    /**
     * 后一个时间是否不晚于前一个时间
     *
     * @param lastTime 上一次时间
     * @param time     当前时间
     * @return boolean
     */
    fun isEarlierLastTime(lastTime: Date, time: Date): Boolean {
        try {
            return time.time <= lastTime.time
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return false
    }

    /**
     * 获取上个月最后一天时间
     */
    fun getLastMonthEndTime(): Date {
        val calendar = Calendar.getInstance()
        calendar[Calendar.DAY_OF_MONTH] = 0
        return calendar.time
    }

    /**
     * 获取今天0点的时间戳
     * */
    fun getTodayZeroTime(): Long {
        val calendar = Calendar.getInstance()
        calendar[Calendar.HOUR_OF_DAY] = 0
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.MILLISECOND] = 0
        return calendar.timeInMillis
    }

    /**
     * 获取今天23点59分59秒的时间戳
     * */
    fun getTodayEndTime(): Long {
        val calendar = Calendar.getInstance()
        calendar[Calendar.HOUR_OF_DAY] = 23
        calendar[Calendar.SECOND] = 59
        calendar[Calendar.MINUTE] = 59
        calendar[Calendar.MILLISECOND] = 0
        return calendar.timeInMillis
    }

    /**
     * 明天0点时间戳
     * */
    fun getTomorrowZeroTime(): Long {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        calendar[Calendar.HOUR_OF_DAY] = 0
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.MILLISECOND] = 0
        return calendar.timeInMillis
    }

    /**
     * 明天23点59分59秒的时间戳
     * */
    fun getTomorrowEndTime(): Long {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        calendar[Calendar.HOUR_OF_DAY] = 23
        calendar[Calendar.SECOND] = 59
        calendar[Calendar.MINUTE] = 59
        calendar[Calendar.MILLISECOND] = 0
        return calendar.timeInMillis
    }

}