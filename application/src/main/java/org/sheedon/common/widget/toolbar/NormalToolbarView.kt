package org.sheedon.common.widget.toolbar

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import org.sheedon.common.R
import org.sheedon.common.data.model.IToolbarModel
import org.sheedon.common.databinding.LayToolbarBinding

/**
 * 简单标题栏
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/8/11 22:00
 */
class NormalToolbarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), IToolbarView {

    private val binding: LayToolbarBinding

    init {
        val view = inflate(context, R.layout.lay_toolbar, this)
        view.tag = TAG
        binding = DataBindingUtil.bind(view)!!
    }

    /**
     * 加载当前类-标题栏
     */
    override fun loadToolbarView(): ViewGroup = this

    /**
     * 设置标题栏中配置信息
     * 返回/标题栏/菜单键
     */
    override fun setToolbarModel(model: IToolbarModel) {
        binding.toolbar = model
    }

    /**
     * 销毁动作
     */
    override fun destroy() {
        binding.unbind()
    }


    companion object {
        private const val TAG = "layout/lay_toolbar_0"
    }
}