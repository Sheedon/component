package org.sheedon.mvvm.ui.activities

import org.sheedon.common.app.DataBindingActivity
import org.sheedon.common.data.DataBindingConfig
import org.sheedon.common.handler.ToastHandler
import org.sheedon.common.utils.checkIsFalse
import org.sheedon.mvvm.BR
import org.sheedon.mvvm.viewmodel.BaseViewModel

/**
 * ViewModel Activity 类
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/1/6 3:12 下午
 */
abstract class BaseVMActivity<VM : BaseViewModel> : DataBindingActivity() {

    private lateinit var mState: VM

    override fun initViewModel() {
        mState = getActivityViewModel()
        mState.initActuators()
    }

    /**
     * 获取Activity所需要绑定的ViewModel
     *
     * @return VM
     */
    protected abstract fun getActivityViewModel(): VM

    /**
     * 追加绑定xml中的ViewModel
     *
     * <code>
     * <layout >
     * <data>
     * <variable
     * name="vm"
     * type="org.sheedon.mvvm.viewmodel.BaseViewModel" />
     * </data>
     * ...
     * </layout>
     * </code>
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

        // 错误消息发送
        mState.getMessageEmitter().observe(this, {
            hideLoading()
            it.isNullOrEmpty().checkIsFalse {
                ToastHandler.showToast(it)
            }
        })

        // 处理动作
        mState.getHandleAction().observe(this, { status ->
            hideLoading()
            status?.let {
                onHandleAction(status)
            }
        })
    }

    /**
     * 由子类取完成处理动作
     *
     * @param status 处理行为状态
     */
    protected open fun onHandleAction(status: Int) {

    }
}