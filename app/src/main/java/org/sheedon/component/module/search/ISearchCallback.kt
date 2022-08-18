package org.sheedon.component.module.search

import org.sheedon.binging.EventCallback

/**
 * java类作用描述
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/8/16 22:45
 */
interface ISearchCallback : EventCallback {
    fun scan(code: String)
}