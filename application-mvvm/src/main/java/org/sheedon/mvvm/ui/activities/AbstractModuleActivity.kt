package org.sheedon.mvvm.ui.activities

import org.sheedon.binging.*
import org.sheedon.mvvm.viewmodel.AbstractModuleViewModel

/**
 * 基础模块的Activity
 * 用于组合不同模块的Activity，以减少重复编写具体功能的Activity。
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/6/23 15:36
 */
abstract class AbstractModuleActivity<VM : AbstractModuleViewModel<Callback, Handler>,
        Callback : EventCallback, Handler : EventHandler>
    : AbstractVMActivity<VM>() {


    protected lateinit var eventComponent: AbstractEventComponent<Callback, Handler>

    override fun initViewModel() {
        mState = getActivityViewModel()
        mState.initActuators()
    }

    /**
     * 在加载ViewModel之前，实现事件组合类的初始化，
     * 并且将Activity注入其中
     * */
    override fun initBeforeOfViewModel() {
        super.initBeforeOfViewModel()
        eventComponent = loadEventComponent()
        eventComponent.inject(this)
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
        return super.appendBindingParam()
            .also {
                // 附加模块参数配置项
                // 将ViewModel中的事件监听，注入到事件组合类中
                eventComponent.inject(mState.loadCallback())

                // 得到数据绑定配置项，添加到视图中
                val bindingConfig = eventComponent.loadDataBindingConfig()
                it.addBindingParams(bindingConfig.getBindingParams())

                // 将事件组合类的处理者注入到ViewModel，用于执行完成事件后，通知改变其行为。
                mState.inject(eventComponent.provideHandler())
            }
    }

    /**
     * 加载功能模块转化组
     * 例如扫码/分页配置等都是通过该方法来构建关联
     * */
    protected abstract fun loadEventComponent(): AbstractEventComponent<Callback, Handler>

    /**
     * 销毁事件，防止事件中持有【生命周期长于Activity的引用】导致的内存泄露
     * */
    override fun onDestroy() {
        super.onDestroy()
        eventComponent.destroy()
    }

}