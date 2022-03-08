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

    override fun getContentLayoutId() = ConfigHandler.getToolbarId()

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

        val flChild = loadChildLayout(parentBinding)

        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(this), getChildContentLayoutId(),
            flChild, true
        )?.apply {
            lifecycleOwner = this@BaseToolbarActivity
        }

        if (binding == null) {
            View.inflate(
                this,
                getChildContentLayoutId(),
                flChild
            )
            return
        }

        // 获取的都是 toolbar 中的 DataBindingConfig
        appendToolbarParam = true
        val bindingConfig = appendChildBindingParam()
        val bindingParams = bindingConfig.getBindingParams()

        bindingParams.forEach { key, value ->
            binding.setVariable(key, value)
        }

        onToolbarViewDataBinding(binding)
        mToolbarBinding = binding
    }


    /**
     * 加载子布局在当前layout中所占的位置
     */
    protected open fun loadChildLayout(binding: ViewDataBinding) =
        ConfigHandler.getInstance().getFlChild(binding)

    /**
     * 得到子界面的资源文件Id
     */
    protected abstract fun getChildContentLayoutId(): Int

    /**
     * 获取子DataBinding
     */
    protected open fun appendChildBindingParam() = DataBindingConfig()

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
    inner class ToolbarModel @JvmOverloads constructor(
        _title: Int? = null,
        _titleMessage: String? = null,
        _menuVisibility: Int = View.GONE,
        _backVisibility: Int = View.VISIBLE,
        _menuTitle: String = ""
    ) : ViewModel(), IToolbarModel {

        // 标题
        private val title: MutableLiveData<String> = MutableLiveData<String>()
        override fun getTitle(): MutableLiveData<String> {
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
            this.title.value = if (_title == null || _title == 0) {
                _titleMessage ?: ""
            } else {
                getString(_title)
            }
            this.menuVisibility.set(_menuVisibility)
            this.backVisibility.set(_backVisibility)
            this.menuTitle.set(_menuTitle)
        }


        override fun setTitle(title: String) {
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