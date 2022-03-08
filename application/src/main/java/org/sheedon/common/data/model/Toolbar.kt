package org.sheedon.common.data.model

import androidx.databinding.ViewDataBinding
import org.sheedon.common.R
import org.sheedon.common.databinding.LayoutToolbarBinding

/**
 * Toolbar 包装类，配合ConfigHandler，配置头部状态栏使用
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/3/8 5:59 下午
 */
open class Toolbar(val toolbarId: Int = R.layout.layout_toolbar) {

    open fun getFlChild(binding: ViewDataBinding) = (binding as LayoutToolbarBinding).flChild
}