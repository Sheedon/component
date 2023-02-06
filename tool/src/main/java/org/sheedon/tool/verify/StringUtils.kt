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


    /**
     * 是否存在大小写和数字
     */
    @JvmStatic
    fun hasCaseAndNumber(value: String?): Boolean {
        if (value == null || value.isEmpty()) {
            return false
        }
        var caseFlag = false
        var numberFlag = false
        var ch: Char
        for (element in value) {
            ch = element
            if (Character.isDigit(ch)) {
                numberFlag = true
            } else if (Character.isUpperCase(ch) || Character.isLowerCase(ch)) {
                caseFlag = true
            }
        }
        return numberFlag && caseFlag
    }

    /**
     * Object 信息转换为String
     *
     * @param value object 值
     * @return 实际String内容
     */
    @JvmStatic
    fun toValue(value: Any?): String {
        return if (value is String) {
            value
        } else value?.toString() ?: ""
    }
}