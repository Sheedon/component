package org.sheedon.common.support.adapter

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.sheedon.common.support.manager.WrapContentLinearLayoutNotCanScrollManager

/**
 * RecyclerView adapter
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/11/7 20:00
 */
object RecyclerAdapter {

    @JvmStatic
    @BindingAdapter(value = ["submitList", "fixedSize"], requireAll = false)
    fun <T> submitList(recyclerView: RecyclerView, list: List<T>?, size: Int) {
        if (recyclerView.adapter != null) {
            @Suppress("UNCHECKED_CAST")
            val adapter = recyclerView.adapter as ListAdapter<T, *>?
            adapter!!.submitList(list) {
                fixedCount(
                    recyclerView,
                    size
                )
            }
        }
    }

    /**
     * 固定item需要显示的个数
     *
     * @param recyclerView RecyclerView
     * @param size         item个数
     */
    @JvmStatic
    private fun fixedCount(recyclerView: RecyclerView, size: Int) {
        if (size == 0) {
            return
        }
        recyclerView.post {
            val adapter =
                recyclerView.adapter ?: return@post
            if (adapter.itemCount <= size) {
                return@post
            }
            val item = recyclerView.getChildAt(0)
            val height = item.height * size
            val layoutParams = recyclerView.layoutParams
            layoutParams.height = height
            recyclerView.layoutParams = layoutParams
        }
    }


    @JvmStatic
    @BindingAdapter(value = ["setOrientation"], requireAll = false)
    fun <T> setOrientation(recyclerView: RecyclerView, orientation: Int?) {
        if (orientation == null) {
            return
        }
        recyclerView.layoutManager = WrapContentLinearLayoutNotCanScrollManager(
            recyclerView.context,
            orientation,
            false
        )
    }

}