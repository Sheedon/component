package org.sheedon.component.module.search

import org.sheedon.binging.EventHandler

/**
 * java类作用描述
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/8/16 23:20
 */
interface SearchDataHandler : EventHandler {

    fun search(searchDate: String)

}