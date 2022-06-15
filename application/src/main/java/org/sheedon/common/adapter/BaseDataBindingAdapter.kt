package org.sheedon.common.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

/**
 * RecyclerAdapter
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/1/14 2:59 下午
 */
abstract class BaseDataBindingAdapter<M, B : ViewDataBinding>(
    val mContext: Context,
    diffCallback: DiffUtil.ItemCallback<M>
) : ListAdapter<M, RecyclerView.ViewHolder>(diffCallback) {

    // 单击监听器
    protected var mOnItemClickListener: OnItemClickListener<M>? = null
    protected var mOnBindViewClickListener: OnBindViewClickListener<M, B>? = null

    // 长按监听器
    protected var mOnItemLongClickListener: OnItemLongClickListener<M>? = null


    override fun submitList(list: MutableList<M>?) {
        super.submitList(list) {
            super.submitList(ArrayList(list ?: ArrayList()))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = DataBindingUtil.inflate<B>(
            LayoutInflater.from(mContext),
            getLayoutResId(viewType),
            parent,
            false
        )
        val holder = BaseBindingViewHolder(binding.root)

        holder.itemView.setOnClickListener {
            val position = holder.bindingAdapterPosition
            mOnItemClickListener?.onItemClick(holder.itemView.id, getItem(position), position)
            mOnBindViewClickListener?.onItemClick(binding, getItem(position), position)
        }

        holder.itemView.setOnLongClickListener {
            val position = holder.bindingAdapterPosition
            mOnItemLongClickListener?.onItemLongClick(
                holder.itemView.id,
                getItem(position),
                position
            )
            true
        }

        onCreateViewHolder(binding)
        return holder
    }

    /**
     * 创建ViewHolder后，可配置处理binding数据
     */
    protected open fun onCreateViewHolder(binding: B) {

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = DataBindingUtil.getBinding<B>(holder.itemView)
        onBindItem(binding, getItem(position), holder)
        binding?.executePendingBindings()
    }

    @LayoutRes
    protected abstract fun getLayoutResId(viewType: Int): Int

    /**
     * 注意：
     * RecyclerView 中的数据有位置改变（比如删除）时一般不会重新调用 onBindViewHolder() 方法，除非这个元素不可用。
     * 为了实时获取元素的位置，我们通过 ViewHolder.getBindingAdapterPosition() 方法。
     *
     * @param binding .
     * @param item    .
     * @param holder  .
     */
    protected abstract fun onBindItem(binding: B?, item: M, holder: RecyclerView.ViewHolder)

    /**
     * 基础绑定的ViewHolder
     */
    class BaseBindingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    interface OnItemClickListener<M> {
        fun onItemClick(viewId: Int, item: M, position: Int)
    }

    interface OnBindViewClickListener<M, B> {
        fun onItemClick(binding: B, item: M, position: Int)
    }

    interface OnItemLongClickListener<M> {
        fun onItemLongClick(viewId: Int, item: M, position: Int)
    }
}