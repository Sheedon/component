package org.sheedon.mvvm.ui.fragments

import org.sheedon.common.app.DataBindingFragment
import org.sheedon.common.data.DataBindingConfig
import org.sheedon.common.handler.ToastHandler
import org.sheedon.mvvm.BR
import org.sheedon.mvvm.viewmodel.BaseViewModel
import org.sheedon.tool.ext.checkValue

/**
 * ViewModel Fragment 类
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/1/6 2:34 下午
 */
abstract class BaseVMFragment<VM : BaseViewModel> :
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
        mState.getShowLoading().observe(this, this::showLoading)

        // 错误消息发送
        mState.getMessageEmitter().observe(this) {
            hideLoading()
            it.isEmpty().checkValue {
                ToastHandler.showToast(it)
            }
        }

        // 处理动作
        mState.getHandleAction().observe(this) { status ->
            hideLoading()
            status?.let {
                onHandleAction(status)
            }
        }

        notifyInitVMData()
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