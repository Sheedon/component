package org.sheedon.mvvm.ui.activities

import org.sheedon.common.app.BaseToolbarActivity
import org.sheedon.common.data.DataBindingConfig
import org.sheedon.common.handler.ToastHandler
import org.sheedon.mvvm.BR
import org.sheedon.mvvm.viewmodel.BaseNavViewModel
import org.sheedon.tool.ext.checkValue

/**
 * java类作用描述
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/1/6 3:33 下午
 */
abstract class BaseVMNavToolbarActivity<VM : BaseNavViewModel> :
    BaseToolbarActivity() {

    protected lateinit var mState: VM

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
     * <layout >
     * <data>
     * <variable
     * name="vm"
     * type="org.sheedon.mvvm.viewmodel.BaseViewModel" />
     * </data>
     * ...
     * </layout>
     */
    override fun appendChildBindingParam(): DataBindingConfig {
        if (needAutoBindXml()) {
            return super.appendChildBindingParam()
                .addBindingParam(BR.vm, mState)
        }
        return super.appendChildBindingParam()
    }

    /**
     * 是否自动化绑定XML
     */
    protected open fun needAutoBindXml() = true

    override fun initData() {
        super.initData()

        // 监听显示Loading
        mState.getShowLoading().observeInActivity(this, this::showLoading)

        // 错误消息发送
        mState.getMessageEmitter().observeInActivity(this) {
            hideLoading()
            it.isNullOrEmpty()
                .checkValue {
                    ToastHandler.showToast(it)
                }
        }

        // 处理动作
        mState.getHandleAction().observeInActivity(this) { status ->
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