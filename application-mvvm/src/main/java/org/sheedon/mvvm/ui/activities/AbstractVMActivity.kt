package org.sheedon.mvvm.ui.activities

import org.sheedon.binging.DataBindingConfig
import org.sheedon.common.app.DataBindingActivity
import org.sheedon.mvvm.BR
import org.sheedon.mvvm.ext.getVmClazz
import org.sheedon.mvvm.ext.registerObserver
import org.sheedon.mvvm.viewmodel.AbstractViewModel

/**
 * 绑定ViewModel的抽象类
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/8/16 14:03
 */
abstract class AbstractVMActivity<VM : AbstractViewModel>
    : DataBindingActivity() {

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
        return if (needAutoBindXml()) {
            super.appendBindingParam()
                .addBindingParam(BR.vm, mState)
        } else {
            super.appendBindingParam()
        }
    }

    /**
     * 是否自动化绑定XML
     */
    protected open fun needAutoBindXml() = true

    override fun initData() {
        super.initData()

        registerVMObserver()
        initVMData()
    }

    /**
     * 注册ViewModel的订阅对象
     */
    protected open fun registerVMObserver() {
        registerObserver(mState, this::onHandleAction)
    }


    /**
     * 通知初始化ViewModel的数据
     */
    protected open fun initVMData() {
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