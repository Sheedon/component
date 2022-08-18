package org.sheedon.component.module.scan

import org.sheedon.binging.EventCallback

/**
 * 调度
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/8/8 10:54
 */
interface IScanCallback : EventCallback {

    fun scan(code: String)
}