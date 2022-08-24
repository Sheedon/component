package org.sheedon.tool.verify

/**
 * String工具类
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/8/24 22:10
 */
object StringUtils {

    /**
     * 是否为空
     */
    @JvmStatic
    fun isEmpty(msg: String?): Boolean {
        return msg.isNullOrEmpty() || msg.trim() == "null"
    }
}