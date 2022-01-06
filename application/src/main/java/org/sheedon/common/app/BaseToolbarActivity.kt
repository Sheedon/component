package org.sheedon.common.app

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import androidx.core.util.forEach
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.sheedon.common.BR
import org.sheedon.common.data.DataBindingConfig
import org.sheedon.common.data.model.IToolbarModel
import org.sheedon.common.databinding.LayoutToolbarBinding
import org.sheedon.common.handler.ConfigHandler

/**
 * 带Toolbar的Activity
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/1/5 9:53 下午
 */
abstract class BaseToolbarActivity : DataBindingActivity() {

    var mToolbarBinding: ViewDataBinding? = null
    var appendToolbarParam = false

    override fun getContentLayoutId(): Int {
        return ConfigHandler.getToolbarId()
    }

    override fun appendBindingParam(): DataBindingConfig {
        return if (appendToolbarParam) {
            super.appendBindingParam()
        } else {
            super.appendBindingParam()
                .addBindingParam(BR.toolbar, buildToolbarEvent())
        }
    }

    /**
     * 构建标题栏事件
     */
    protected abstract fun buildToolbarEvent(): ToolbarModel


    override fun onViewDataBinding(parentBinding: ViewDataBinding) {
        super.onViewDataBinding(parentBinding)

        val binding: ViewDataBinding? = DataBindingUtil.inflate(
            LayoutInflater.from(this), getChildContentLayoutId(),
            (parentBinding as LayoutToolbarBinding).flChild, true
        )
        if (binding == null) {
            View.inflate(
                this,
                getChildContentLayoutId(),
                parentBinding.flChild
            )
            return
        }
        binding.lifecycleOwner = this

        // 清除父级 绑定的数据
        super.appendBindingParam().clear()
        // 之后获取的都是 toolbar 中的 DataBindingConfig
        appendToolbarParam = true
        val bindingConfig = appendBindingParam()
        val bindingParams = bindingConfig.getBindingParams()

        bindingParams.forEach { key, value ->
            binding.setVariable(key, value)
        }

        onToolbarViewDataBinding(binding)
        mToolbarBinding = binding
    }

    /**
     * 得到子界面的资源文件Id
     */
    protected abstract fun getChildContentLayoutId(): Int

    /**
     * ViewDataBinding 加载完成，建议只是在当前方法中使用，
     * 非必要情况下，尽可能不在子类中拿到 binding 实例乃至获取 view 实例。使用即埋下隐患。
     *
     * @param binding View绑定
     */
    protected open fun onToolbarViewDataBinding(binding: ViewDataBinding) {

    }

    override fun onDestroy() {
        super.onDestroy()
        mToolbarBinding?.unbind()
        mToolbarBinding = null
    }


    @SuppressLint("StaticFieldLeak")
    protected inner class ToolbarModel(
        _title: Int,
        _menuVisibility: Int = View.GONE,
        _backVisibility: Int = View.VISIBLE,
        _menuTitle: String = ""
    ) : ViewModel(), IToolbarModel {
        constructor(_title: Int) : this(_title, View.GONE, View.VISIBLE, "")

        // 标题
        private val title: MutableLiveData<Int> = MutableLiveData<Int>()
        override fun getTitle(): MutableLiveData<Int> {
            return title
        }

        // 菜单标题
        private val menuTitle = ObservableField<String>()
        override fun getMenuTitle(): ObservableField<String> {
            return menuTitle
        }

        // 菜单是否显示
        private val menuVisibility = ObservableInt()
        override fun getMenuVisibility(): ObservableInt {
            return menuVisibility
        }

        // 返回键是否显示
        val backVisibility: ObservableInt = ObservableInt(View.VISIBLE)
        override fun getBackVisibility(): Int {
            return backVisibility.get()
        }

        init {
            this.title.value = _title
            this.menuVisibility.set(_menuVisibility)
            this.backVisibility.set(_backVisibility)
            this.menuTitle.set(_menuTitle)
        }


        override fun setTitle(title: Int) {
            this.title.value = title
        }

        override fun onBackClick() {
            this@BaseToolbarActivity.onBackClick()
        }

        override fun onMenuClick() {
            this@BaseToolbarActivity.onMenuClick()
        }
    }

    /**
     * 返回
     */
    protected open fun onBackClick() {
        finish()
    }

    /**
     * 菜单点击
     */
    protected open fun onMenuClick() {

    }
}