package org.sheedon.tool.ext

/**
 * 集合扩展类
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/8/24 22:28
 */

/**
 * 将List<String>合并，并且以[regex]分割符
 */
fun Collection<String>?.merge(regex: String): String {
    if (this.isNullOrEmpty()) {
        return ""
    }

    val builder = StringBuilder()
    for (item in this) {
        builder.append(item).append(regex)
    }

    if (builder.isNotEmpty()) {
        builder.deleteCharAt(builder.length - 1)
    }

    return builder.toString()
}

/**
 * 将List<T>合并，并且以[regex]分割符,内容根据block方法获取
 */
fun <T> Collection<T>?.merge(regex: String, block: (T) -> String): String {
    if (this.isNullOrEmpty()) {
        return ""
    }

    val builder = StringBuilder()
    for (item in this) {
        val result = block.invoke(item)
        builder.append(result).append(regex)
    }

    if (builder.isNotEmpty()) {
        builder.deleteCharAt(builder.length - 1)
    }

    return builder.toString()
}