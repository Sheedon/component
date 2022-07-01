package org.sheedon.mvvm.ui.activities

import org.sheedon.common.app.BaseToolbarActivity
import org.sheedon.common.data.DataBindingConfig
import org.sheedon.common.handler.ToastHandler
import org.sheedon.mvvm.BR
import org.sheedon.mvvm.ext.getVmClazz
import org.sheedon.mvvm.viewmodel.BaseViewModel
import org.sheedon.tool.ext.checkValue

/**
 * ViewModel Toolbar Activity 类
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/1/6 3:36 下午
 */
abstract class BaseVMToolbarActivity<VM : BaseViewModel> :
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
    protected open fun getActivityViewModel(): VM {
        return getActivityScopeViewModel(getVmClazz(this))
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

        registerVMObserver()
        notifyInitVMData()
    }

    /**
     * 注册ViewModel的订阅对象
     */
    protected open fun registerVMObserver() {
        // 监听显示Loading
        mState.getNotifier().getShowLoading().observe(this, this::showLoading)

        // 监听隐藏Loading
        mState.getNotifier().getDismissLoading().observe(this) { hideLoading() }

        // 错误消息发送
        mState.getNotifier().getMessageEmitter().observe(this) {
            it.isEmpty().checkValue {
                ToastHandler.showToast(it)
            }
        }

        // 处理动作
        mState.getNotifier().getHandleAction().observe(this) { status ->
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