package org.sheedon.common.app;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import org.sheedon.common.handler.ViewModelProviderHandler;

/**
 * 基础 绑定ViewModel的Activity
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2021/10/21 9:34 上午
 */
public abstract class BaseVMActivity extends DataBindingActivity {

    private ViewModelProvider mActivityProvider;
    private ViewModelProvider mApplicationProvider;


    /**
     * 获取Activity范围视图模型
     *
     * @param modelClass ViewModel's class
     * @param <T>        ViewModel
     * @return ViewModel
     */
    protected <T extends ViewModel> T getActivityScopeViewModel(@NonNull Class<T> modelClass) {
        return ViewModelProviderHandler.getActivityScopeViewModel(mActivityProvider,
                this, modelClass);
    }

    /**
     * 获取全局范围视图模型
     *
     * @param modelClass ViewModel's class
     * @param <T>        ViewModel
     * @return ViewModel
     */
    protected <T extends ViewModel> T getApplicationScopeViewModel(@NonNull Class<T> modelClass) {
        return ViewModelProviderHandler.getApplicationScopeViewModel(mApplicationProvider,
                modelClass);
    }
}
