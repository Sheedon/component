package org.sheedon.tool.ext

/**
 * String 扩展函数
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/8/24 22:37
 */

/**
 * 将字符串按 [regex] 分割， 返回Array为[count]个数的数组
 * @param regex 分割符
 * @param count 目标数组个数
 * @return Array<String?> 数组，若分割数据少于目标个数，则不足个数的数组为null
 */
fun String.toArray(regex: String, count: Int): Array<String?> {
    if (count < 0) {
        return arrayOf()
    }

    val args = arrayOfNulls<String>(count)
    val split: Array<String> = split(regex.toRegex()).toTypedArray()
    if (args.size >= split.size) {
        System.arraycopy(split, 0, args, 0, split.size)
        return args
    }

    System.arraycopy(split, 0, args, 0, args.size)

    return args
}

/**
 * 将字符串按 [regex] 分割，转化为List<String>集合
 * @param regex 分割符
 * @param count 目标数组个数
 * @return List<String> 集合
 */
fun String.toList(regex: String): List<String> {
    if (isNullOrEmpty()) {
        return ArrayList()
    }

    val array: MutableList<String> = ArrayList()
    val strings: Array<String> = split(regex.toRegex()).toTypedArray()
    for (string in strings) {
        if (string.isEmpty()) {
            continue
        }
        array.add(string)
    }

    return array
}