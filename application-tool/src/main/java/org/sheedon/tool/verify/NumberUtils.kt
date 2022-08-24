package org.sheedon.tool.verify

/**
 * 数字工具类
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/8/24 18:33
 */
object NumberUtils {

    /**
     * 核实数值是否为空，若为空则返回默认值
     */
    @JvmStatic
    @JvmOverloads
    fun toInt(number: Int?, defaultValue: Int = 0): Int {
        return number ?: defaultValue
    }

    /**
     * 核实数值是否为空/Int类型，若为空或不为Int则返回默认值
     */
    @JvmStatic
    @JvmOverloads
    fun toInt(number: Any?, defaultValue: Int = 0): Int {
        val value = number ?: defaultValue
        return if (value is Int) {
            value
        } else {
            defaultValue
        }
    }

    /**
     * 核实数值是否为空，若为空则返回默认值
     */
    @JvmStatic
    @JvmOverloads
    fun toLong(number: Long?, defaultValue: Long = 0): Long {
        return number ?: defaultValue
    }

    /**
     * 核实数值是否为空/Long类型，若为空或不为Long则返回默认值
     */
    @JvmStatic
    @JvmOverloads
    fun toLong(number: Any?, defaultValue: Long = 0): Long {
        val value = number ?: defaultValue
        return if (value is Long) {
            value
        } else {
            defaultValue
        }
    }

    /**
     * 核实数值是否为空，若为空则返回默认值
     */
    @JvmStatic
    @JvmOverloads
    fun toFloat(number: Float?, defaultValue: Float = 0F): Float {
        return number ?: defaultValue
    }

    /**
     * 核实数值是否为空/Float类型，若为空或不为Float则返回默认值
     */
    @JvmStatic
    @JvmOverloads
    fun toFloat(number: Any?, defaultValue: Float = 0F): Float {
        val value = number ?: defaultValue
        return if (value is Float) {
            value
        } else {
            defaultValue
        }
    }

    /**
     * 核实数值是否为空，若为空则返回默认值
     */
    @JvmStatic
    @JvmOverloads
    fun toDouble(number: Double?, defaultValue: Double = 0.0): Double {
        return number ?: defaultValue
    }

    /**
     * 核实数值是否为空/Double类型，若为空或不为Double则返回默认值
     */
    @JvmStatic
    @JvmOverloads
    fun toDouble(number: Any?, defaultValue: Double = 0.0): Double {
        val value = number ?: defaultValue
        return if (value is Double) {
            value
        } else {
            defaultValue
        }
    }

    /**
     * 核实数值是否为空/Number类型，若为空或不为Number则返回默认值
     */
    @JvmStatic
    @JvmOverloads
    fun toNumber(number: Any?, defaultValue: Int = 0): Number {
        val value = number ?: defaultValue
        return if (value is Number) {
            value
        } else {
            defaultValue
        }
    }


}