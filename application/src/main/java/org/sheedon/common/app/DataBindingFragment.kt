package org.sheedon.common.app

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.util.forEach
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.sheedon.common.data.DataBindingConfig
import org.sheedon.common.handler.ViewModelProviderHandler

/**
 * 数据绑定 Fragment
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/1/5 10:46 下午
 */
abstract class DataBindingFragment : BaseFragment() {

    protected lateinit var mActivity: DataBindingActivity
    private var mBinding: ViewDataBinding? = null

    // 绑定参数
    private val dataBindingConfig = DataBindingConfig()

    private var mFragmentProvider: ViewModelProvider? = null
    private var mActivityProvider: ViewModelProvider? = null
    private var mApplicationProvider: ViewModelProvider? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as DataBindingActivity
    }

    override fun bindContentView(
        layId: Int,
        inflater: LayoutInflater,
        container: ViewGroup?
    ): View {
        initViewModel()

        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            inflater,
            layId,
            container,
            false
        )
            ?.apply {
                lifecycleOwner = this@DataBindingFragment
            } ?: return inflater.inflate(layId, container, false)

        val bindingConfig = appendBindingParam()
        val bindingParams = bindingConfig.getBindingParams()
        bindingParams.forEach { key, value ->
            binding.setVariable(key, value)
        }

        onViewDataBinding(binding)
        mBinding = binding
        return binding.root
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
     * @param binding View绑定者
     */
    protected open fun onViewDataBinding(binding: ViewDataBinding) {

    }

    /**
     * 获取Fragment范围视图模型
     *
     * @param modelClass ViewModel's class
     * @param <T>        ViewModel
     * @return ViewModel
     */
    protected open fun <T : ViewModel> getFragmentScopeViewModel(modelClass: Class<T>): T {
        return ViewModelProviderHandler.getFragmentScopeViewModel(
            mFragmentProvider,
            this,
            modelClass
        )
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
            mActivity, modelClass
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

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding?.unbind()
        mBinding = null
    }

}