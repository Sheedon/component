package org.sheedon.common.app

import androidx.core.util.forEach
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.sheedon.common.data.DataBindingConfig
import org.sheedon.common.handler.ViewModelProviderHandler

/**
 * 数据绑定 Activity
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/1/5 9:54 下午
 */
abstract class DataBindingActivity : BaseActivity() {

    private var mBinding: ViewDataBinding? = null

    // 绑定参数
    private val dataBindingConfig = DataBindingConfig()

    private var mActivityProvider: ViewModelProvider? = null
    private var mApplicationProvider: ViewModelProvider? = null

    override fun bindContentView(layId: Int) {
        super.bindContentView(layId)

        initBeforeOfViewModel()
        initViewModel()

        val binding = DataBindingUtil.setContentView<ViewDataBinding>(this, layId)
            ?.apply {
                lifecycleOwner = this@DataBindingActivity
            }

        if (binding == null) {
            setContentView(layId)
            return
        }

        val bindingConfig = appendBindingParam()
        val bindingParams = bindingConfig.getBindingParams()
        bindingParams.forEach { key, value ->
            binding.setVariable(key, value)
        }

        onViewDataBinding(binding)
        mBinding = binding

    }

    /**
     * 初始化ViewModel之前操作
     */
    protected open fun initBeforeOfViewModel() {

    }

    /**
     * 加载ViewModel
     */
    protected abstract fun initViewModel()

    /**
     * 追加绑定参数
     *
     * @return SparseArray
     */
    protected open fun appendBindingParam() = dataBindingConfig

    /**
     * ViewDataBinding 加载完成，建议只是在当前方法中使用，
     * 非必要情况下，尽可能不在子类中拿到 binding 实例乃至获取 view 实例。使用即埋下隐患。
     *
     * @param binding View绑定
     */
    protected open fun onViewDataBinding(binding: ViewDataBinding) {

    }

    /**
     * 获取Activity范围视图模型
     *
     * @param modelClass ViewModel's class
     * @param <T>        ViewModel
     * @return ViewModel
     */
    protected open fun <T : ViewModel> getActivityScopeViewModel(modelClass: Class<T>): T {
        return ViewModelProviderHandler.getActivityScopeViewModel(
            mActivityProvider,
            this, modelClass
        )
    }

    /**
     * 获取全局范围视图模型
     *
     * @param modelClass ViewModel's class
     * @param <T>        ViewModel
     * @return ViewModel
     */
    protected open fun <T : ViewModel> getApplicationScopeViewModel(modelClass: Class<T>): T {
        return ViewModelProviderHandler.getApplicationScopeViewModel(
            mApplicationProvider,
            modelClass
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding?.unbind()
        mBinding = null
    }
}