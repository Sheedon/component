package org.sheedon.common.support.adapter

import android.view.View
import androidx.databinding.BindingAdapter

/**
 * View基础 Adapter
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/11/7 13:54
 */
object ViewAdapter {

    @JvmStatic
    @BindingAdapter(value = ["listenVisibility"], requireAll = false)
    fun listenVisibility(
        view: View,
        isVisibility: Boolean
    ) {
        if (isVisibility) {
            view.visibility = View.VISIBLE
        } else {
            view.visibility = View.GONE
        }
    }
}