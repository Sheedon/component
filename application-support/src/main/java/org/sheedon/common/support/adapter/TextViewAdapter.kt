package org.sheedon.common.support.adapter

import android.view.View
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.BindingAdapter

/**
 * TextView adapter
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/11/7 21:46
 */
object TextViewAdapter {

    @JvmStatic
    @BindingAdapter(
        value = ["drawableStart", "drawableLeft",
            "drawableTop", "drawableEnd",
            "drawableRight", "drawableBottom"], requireAll = false
    )
    fun visible(
        view: TextView,
        drawableStart: Int?,
        drawableLeft: Int?,
        drawableTop: Int?,
        drawableEnd: Int?,
        drawableRight: Int?,
        drawableBottom: Int?,
    ) {
        val start = (drawableStart ?: drawableLeft)?.run {
            AppCompatResources.getDrawable(view.context, this)
        }?.apply {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
        }
        val top = drawableTop?.run {
            AppCompatResources.getDrawable(view.context, this)
        }?.apply {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
        }
        val right = (drawableEnd ?: drawableRight)?.run {
            AppCompatResources.getDrawable(view.context, this)
        }?.apply {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
        }
        val bottom = drawableBottom?.run {
            AppCompatResources.getDrawable(view.context, this)
        }?.apply {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
        }
        view.setCompoundDrawables(start, top, right, bottom)
    }
}