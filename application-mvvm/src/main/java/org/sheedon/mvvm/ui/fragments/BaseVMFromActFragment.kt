package org.sheedon.mvvm.ui.fragments

import androidx.databinding.ViewDataBinding
import org.sheedon.common.app.DataBindingFragment
import org.sheedon.common.data.DataBindingConfig
import org.sheedon.common.handler.ToastHandler
import org.sheedon.mvvm.BR
import org.sheedon.mvvm.viewmodel.BaseNavViewModel
import org.sheedon.tool.ext.checkValue

/**
 * ViewModel 来自于 Activity 的 Fragment 类
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/1/6 3:03 下午
 */
abstract class BaseVMFromActFragment<VM : BaseNavViewModel> :
    DataBindingFragment() {

    private lateinit var mState: VM

    override fun initViewModel() {
        mState = getFragmentViewModel()
        mState.initActuators()
    }

    /**
     * 获取Fragment所需要绑定的ViewModel
     *
     * @return VM
     */
    protected abstract fun getFragmentViewModel(): VM

    /**
     * 追加绑定xml中的ViewModel
     *
     * <layout >
     * <data>
     * <variable
     * name="vm"
     * type="org.sheedon.mvvm.viewmodel.BaseViewModel" />
     * </data>
     * ...
     * </layout>
     */
    override fun appendBindingParam(): DataBindingConfig {
        if (needAutoBindXml()) {
            return super.appendBindingParam()
                .addBindingParam(BR.vm, mState)
        }
        return super.appendBindingParam()
    }

    /**
     * 是否自动化绑定XML
     */
    protected open fun needAutoBindXml() = true

    override fun initData() {
        super.initData()

        // 监听显示Loading
        mState.getShowLoading().observeInFragment(this, this::showLoading)

        // 错误消息发送
        mState.getMessageEmitter().observeInFragment(this) {
            hideLoading()
            it.isNullOrEmpty().checkValue {
                ToastHandler.showToast(it)
            }
        }

        // 处理动作
        mState.getHandleAction().observeInFragment(this) { status ->
            hideLoading()
            status?.let {
                onHandleAction(status)
            }
        }
    }

    /**
     * 由子类取完成处理动作
     *
     * @param status 处理行为状态
     */
    protected open fun onHandleAction(status: Int) {

    }

}