package org.sheedon.tool.interfaces

/**
 * 子节点
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/11/25 11:46
 */
interface IChildrenNode<T> {

    /**
     * 加载当前ID
     * */
    fun loadCurrentId(): T

    /**
     * 加载父ID
     * */
    fun loadParentId(): T

}