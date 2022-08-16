package org.sheedon.mvvm.ui.fragments

import org.sheedon.binging.*
import org.sheedon.common.app.DataBindingFragment
import org.sheedon.mvvm.BR
import org.sheedon.mvvm.ext.getVmClazz
import org.sheedon.mvvm.ext.registerObserver
import org.sheedon.mvvm.viewmodel.AbstractViewModel

/**
 * 基础模块的Fragment
 * 用于组合不同模块的Fragment，以减少重复编写具体功能的Fragment。
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/7/1 16:49
 */
abstract class AbstractVMFragment<VM : AbstractViewModel> :
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