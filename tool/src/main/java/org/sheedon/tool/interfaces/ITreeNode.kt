package org.sheedon.tool.interfaces

/**
 * 树结构
 * T:构成树结构的数据结构
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/11/25 11:24
 */
interface ITreeNode<T : IChildrenNode<*>> {

    /**
     * 加载当前树的节点
     * */
    fun loadChildren(): List<ITreeNode<T>>

    /**
     * 添加子节点
     * */
    fun addChildrenList(vararg children: ITreeNode<T>)


    /**
     * 将数据转化为树结构
     * @param t:需转化的数据
     * */
    fun convertTree(t: T): ITreeNode<T>
}