package org.sheedon.mvvm.ui.activities;

import android.text.TextUtils;

import androidx.databinding.library.baseAdapters.BR;

import org.sheedon.common.app.BaseVMActivity;
import org.sheedon.common.data.DataBindingConfig;
import org.sheedon.common.handler.ToastHandler;
import org.sheedon.mvvm.viewmodel.BaseNavViewModel;

/**
 * ViewModel 包装类 Activity 类
 * 内部包裹 Fragment
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2021/10/27 6:43 下午
 */
public abstract class BaseVMWrapperNavActivity<VM extends BaseNavViewModel>
        extends BaseVMActivity {

    private VM mState;

    @Override
    protected void initViewModel() {
        mState = getActivityViewModel();
        mState.initActuators();
    }

    /**
     * 获取Activity所需要绑定的ViewModel
     *
     * @return VM
     */
    protected abstract VM getActivityViewModel();

    /**
     * 追加绑定xml中的ViewModel
     *
     * <code><layout >
     * <data>
     * <variable
     * name="vm"
     * type="org.sheedon.mvvm.viewmodel.BaseViewModel" />
     * </data>
     * ...
     * </layout></code>
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
        mState.getMessageEmitter().observeInActivity(this, message -> {
            hideLoading();
            if (TextUtils.isEmpty(message)) {
                return;
            }

            ToastHandler.showToast(message);
        });
        mState.getHandleAction().observeInActivity(this, status -> {
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
