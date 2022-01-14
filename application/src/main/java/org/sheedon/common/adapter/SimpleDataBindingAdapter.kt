package org.sheedon.common.adapter

import android.content.Context
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil

/**
 * 简单数据绑定适配器
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/1/14 3:30 下午
 */
abstract class SimpleDataBindingAdapter<M, B : ViewDataBinding>(
    context: Context,
    val layout: Int,
    diffCallback: DiffUtil.ItemCallback<M>
) : BaseDataBindingAdapter<M, B>(context, diffCallback) {

    override fun getLayoutResId(viewType: Int) = layout
}