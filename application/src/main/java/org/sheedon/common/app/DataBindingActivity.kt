package org.sheedon.common.app

import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.util.forEach
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.sheedon.binging.DataBindingConfig
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

    // 获取Activity持有的ViewModel
    private val mActivityProvider: ViewModelProvider by lazy {
        ViewModelProvider(this)
    }

    // 获取全局持有的ViewModel
    private val mApplicationProvider: ViewModelProvider by lazy {
        ViewModelProvider(ViewModelProviderHandler.instance)
    }

    /**
     * 绑定内容布局，首先对ViewModel初始化，
     * 其次关联资源，若并不是通过ViewDataBinding配置，则采用[setContentView]设置，否则提取[appendBindingParam]
     * 依次[setVariable]，将数据和视图关联。
     *
     * @param layId 资源ID
     */
    override fun bindContentView(layId: Int) {
        initBeforeOfViewModel()
        initViewModel()
        super.bindContentView(layId)
    }

    override fun attachContentView(layId: Int) {
        val binding = DataBindingUtil.setContentView<ViewDataBinding>(this, layId)?.apply {
            lifecycleOwner = this@DataBindingActivity
        }

        if (binding == null) {
            super.attachContentView(layId)
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
     * 附加存在子布局
     * @param layId 资源ID
     * @param attachView 承载子布局的View
     * */
    override fun attachChildContentView(layId: Int, attachView: FrameLayout) {
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(this),
            layId, attachView, true
        )?.apply {
            lifecycleOwner = this@DataBindingActivity
        }

        // 若未采用ViewBinding配置，则获取不到 binding ，如若如此，则采用父方法。
        if (binding == null) {
            super.attachChildContentView(layId, attachView)
            return
        }

        // 绑定视图绑定参数
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
    protected open fun appendBindingParam() = DataBindingConfig()

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
            mActivityProvider, modelClass
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
        return ViewModelProviderHandler.getApplicationScopeViewModel(modelClass, application)
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding?.unbind()
        mBinding = null
    }
}