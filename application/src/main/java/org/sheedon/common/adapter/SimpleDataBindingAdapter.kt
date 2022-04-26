package org.sheedon.common.adapter

import android.content.Context
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import org.sheedon.common.data.model.IDiffModel

/**
 * 简单数据绑定适配器
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/1/14 3:30 下午
 */

// 单一数据类型的recyclerView适配器
abstract class SimpleDataBindingAdapter<M, B : ViewDataBinding>(
    context: Context,
    val layout: Int,
    diffCallback: DiffUtil.ItemCallback<M>
) : BaseDataBindingAdapter<M, B>(context, diffCallback) {

    override fun getLayoutResId(viewType: Int) = layout
}

// 单一数据类型的recyclerView适配器，model实现IDiffModel，当前类中执行比较方法
abstract class SimpleDiffDataBindingAdapter<M : IDiffModel, B : ViewDataBinding>(
    context: Context,
    layout: Int
) : SimpleDataBindingAdapter<M, B>(context, layout, object : DiffUtil.ItemCallback<M>() {
    override fun areItemsTheSame(oldItem: M, newItem: M) = oldItem.isSame(newItem)

    override fun areContentsTheSame(oldItem: M, newItem: M) = oldItem.isSameContent(newItem)
})