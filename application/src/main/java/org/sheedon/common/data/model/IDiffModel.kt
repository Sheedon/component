package org.sheedon.common.data.model

/**
 * model 比较
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/4/13 3:38 下午
 */
interface IDiffModel {
    fun isSame(o: Any?): Boolean
    fun isSameContent(o: Any?): Boolean
}