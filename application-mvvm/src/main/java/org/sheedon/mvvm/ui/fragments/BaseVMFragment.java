package org.sheedon.mvvm.ui.fragments;

import android.text.TextUtils;

import androidx.databinding.library.baseAdapters.BR;

import org.sheedon.common.app.DataBindingFragment;
import org.sheedon.common.data.DataBindingConfig;
import org.sheedon.common.handler.ToastHandler;
import org.sheedon.mvvm.viewmodel.BaseViewModel;

/**
 * ViewModel Fragment 类
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2021/12/16 3:28 下午
 */
public abstract class BaseVMFragment<VM extends BaseViewModel> extends DataBindingFragment {

    private VM mState;


    @Override
    protected void initViewModel() {
        mState = getFragmentViewModel();
        mState.initActuators();
    }

    /**
     * 获取Fragment所需要绑定的ViewModel
     *
     * @return VM
     */
    protected abstract VM getFragmentViewModel();

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
    @Override
    protected DataBindingConfig appendBindingParam() {
        if (needAutoBindXml()) {
            return super.appendBindingParam()
                    .addBindingParam(BR.vm, mState);
        }
        return super.appendBindingParam();
    }

    /**
     * 是否自动化绑定XML
     */
    protected boolean needAutoBindXml() {
        return true;
    }

    @Override
    protected void initData() {
        super.initData();
        // 错误消息发送
        mState.getMessageEmitter().observe(this, message -> {
            hideLoading();
            if (TextUtils.isEmpty(message)) {
                return;
            }

            ToastHandler.showToast(message);
        });
        // 处理动作
        mState.getHandleAction().observe(this, status -> {
            hideLoading();
            if (status == null) {
                return;
            }
            onHandleAction(status);
        });
    }

    /**
     * 由子类取完成处理动作
     *
     * @param status 处理行为状态
     */
    protected void onHandleAction(int status) {

    }
}
