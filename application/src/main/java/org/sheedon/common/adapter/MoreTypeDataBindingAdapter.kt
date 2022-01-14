package org.sheedon.common.adapter

import android.content.Context
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil

/**
 * 多类型Adapter
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/1/14 3:37 下午
 */
abstract class MoreTypeDataBindingAdapter<M, B : ViewDataBinding>(
    context: Context,
    diffCallback: DiffUtil.ItemCallback<M>
) : BaseDataBindingAdapter<M, B>(context, diffCallback) {

    /**
     * 复写默认的布局类型返回
     *
     * @param position 坐标
     * @return 类型，其实复写后返回的都是XML文件的ID
     */
    override fun getItemViewType(position: Int) = getItemViewType(position, getItem(position))

    /**
     * 得到布局的类型
     *
     * @param position 坐标
     * @param data     当前的数据
     */
    @LayoutRes
    protected abstract fun getItemViewType(position: Int, data: M): Int
}