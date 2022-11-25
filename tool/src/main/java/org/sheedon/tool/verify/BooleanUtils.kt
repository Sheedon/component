package org.sheedon.tool.verify

/**
 * 布尔型工具类
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/8/24 17:39
 */
object BooleanUtils {


    /**
     * 核实是否为Boolean类型，是则返回对应值，否则返回false
     */
    @JvmStatic
    @JvmOverloads
    fun valueOf(value: Any?, defaultValue: Boolean = false): Boolean {
        val finalValue = value ?: defaultValue
        return if (finalValue is Boolean) {
            return finalValue
        } else {
            defaultValue
        }
    }

    /**
     * 若value为空，则返回默认值，否则返回当前value
     */
    @JvmStatic
    @JvmOverloads
    fun valueOf(value: Boolean?, defaultValue: Boolean = false): Boolean {
        return value ?: defaultValue
    }
}