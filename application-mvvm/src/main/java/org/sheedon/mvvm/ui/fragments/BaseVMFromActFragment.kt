package org.sheedon.mvvm.ui.fragments

import org.sheedon.common.app.DataBindingFragment
import org.sheedon.common.data.DataBindingConfig
import org.sheedon.common.handler.ToastHandler
import org.sheedon.mvvm.BR
import org.sheedon.mvvm.ext.getVmClazz
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

    protected lateinit var mState: VM

    override fun initViewModel() {
        mState = getFragmentViewModel()
        mState.initActuators()
    }

    /**
     * 获取Fragment所需要绑定的ViewModel
     *
     * @return VM
     */
    protected open fun getFragmentViewModel(): VM {
        return getFragmentScopeViewModel(getVmClazz(this))
    }

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

        registerVMObserver()
        notifyInitVMData()
    }


    /**
     * 注册ViewModel的订阅对象
     */
    protected open fun registerVMObserver() {
        // 监听显示Loading
        mState.getNotifier().getShowLoading().observeInFragment(this, this::showLoading)

        // 监听隐藏Loading
        mState.getNotifier().getDismissLoading().observeInFragment(this) { hideLoading() }

        // 错误消息发送
        mState.getNotifier().getMessageEmitter().observeInFragment(this) {
            it.isEmpty().checkValue {
                ToastHandler.showToast(it)
            }
        }

        // 处理动作
        mState.getNotifier().getHandleAction().observeInFragment(this) { status ->
            status?.let {
                onHandleAction(status)
            }
        }
    }

    /**
     * 通知初始化ViewModel的数据
     */
    protected open fun notifyInitVMData() {
        mState.initData()
    }

    /**
     * 由子类取完成处理动作
     *
     * @param status 处理行为状态
     */
    protected open fun onHandleAction(status: Int) {

    }

}