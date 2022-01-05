package org.sheedon.common.utils

/**
 * 全局方法
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/1/5 8:03 下午
 */

/**
 * 核实是否为真
 * 若为true则返回true
 * 否则返回null
 */
fun Boolean.checkIsTrue(block: () -> Unit): Boolean? {
    return if (this) {
        block()
        true
    } else null
}

/**
 * 核实是否为假
 * 若为false则返回true
 * 否则返回null
 */
fun Boolean.checkIsFalse(block: () -> Unit): Boolean? {
    return if (!this) {
        block()
        true
    } else null
}

