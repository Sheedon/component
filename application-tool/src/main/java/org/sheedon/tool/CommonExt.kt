package org.sheedon.tool

/**
 * 通用拓展函数
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/1/5 8:03 下午
 */

/**
 * 核实是否为真
 * 若为true，则执行trueAction
 * 若为false，则执行falseAction
 * 并且返回当前值
 */
fun Boolean.checkValue(trueAction: () -> Unit = {}, falseAction: () -> Unit = {}): Boolean {
    if (this) {
        trueAction.invoke()
    } else {
        falseAction.invoke()
    }
    return this
}

