package org.sheedon.tool.verify

import org.sheedon.tool.ext.computeIfBuild
import org.sheedon.tool.interfaces.IChildrenNode
import org.sheedon.tool.interfaces.ITreeNode

/**
 * 树结构转化工具类
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/11/25 11:38
 */
object TreeUtils {

    /**
     * 将一个包含父ID的元素集合转化为树结构。
     *
     * @param <T> list item对象
     * @param <K> item对象 id 类型
     * */
    fun <T : IChildrenNode<K>, K> toTree(
        list: List<T>,
        buildTreeNode: (T) -> ITreeNode<T>
    ): List<ITreeNode<T>> {
        // 父map，以父ID为键，子树节点为值的list
        val parentMap = HashMap<K, ArrayList<ITreeNode<T>>>()
        // 构造的树节点map，以当前ID为键，构建而成的树节点为值
        val treeNodeMap = HashMap<K, ITreeNode<T>>()

        list.forEach {
            val node = buildTreeNode(it)
            treeNodeMap[it.loadCurrentId()] = node
            // 将当前节点插入到父节点中
            insertNodeToParent(it, node, parentMap, treeNodeMap)
        }

        // 得到parentMap中还存在的节点，都认为添加到根节点上，若内部是环形，则可能返回数据为空
        val rootList = arrayListOf<ITreeNode<T>>()
        val rootChildren = parentMap.values
        rootChildren.forEach {
            rootList.addAll(it)
        }

        return rootList
    }

    /**
     * 将新增节点[node]插入到父节点[parentMap]/[treeNodeMap]下。
     * 首先父节点是否已经创建是未知的。
     * a. 因此我们对于未创建父节点的插入，暂存在[parentMap]中，[parentMap]以父节点ID为键，子节点集合为值的map。
     * b. 而对于那些已经创建了父节点的插入，则存储在对应父节点的children中，已存在的父节点可以通过[treeNodeMap]根据
     * 父id查找得到。
     * c. 若当前节点id在[parentMap]中存在，则需要向其中移除，并且其中的数据添加到[treeNodeMap]中，
     * 以保证[parentMap]为key的节点都是不存在的，并且以存在的关系不丢失，从而便于上述两点的执行。
     *
     * @param item          元素项
     * @param node          当前构造出的节点
     * @param parentMap     存放父节点的map,以父ID为键，子树节点为值的list
     * @param treeNodeMap   树节点map，以当前ID为键，构建而成的树节点为值
     * */
    private fun <T : IChildrenNode<K>, K> insertNodeToParent(
        item: T,
        node: ITreeNode<T>,
        parentMap: HashMap<K, ArrayList<ITreeNode<T>>>,
        treeNodeMap: HashMap<K, ITreeNode<T>>
    ) {
        val currentId = item.loadCurrentId()
        val parentId = item.loadParentId()

        // 当前的节点在[parentMap]中存在需要进行移除，并将数据添加到node节点中。
        parentMap[currentId]?.also {
            parentMap.remove(parentId)
            node.addChildrenList(*it.toTypedArray())
        }

        // 未创建父节点的插入，暂存在[parentMap]中
        val treeNode = treeNodeMap[parentId]
        if (treeNode == null) {
            parentMap.computeIfBuild(parentId, ::arrayListOf)
                .also { list -> list.add(node) }
            return
        }

        // 已经创建了父节点的插入，则存储在对应父节点的children中
        treeNode.addChildrenList(node)
    }

}