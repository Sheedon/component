package org.sheedon.component.lifecycle;

import androidx.lifecycle.ViewModel;

import org.sheedon.lifecycle.LifecycleManager;

/**
 * 持有生命周期 ViewModel
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2021/12/20 6:30 下午
 */
public class LifecycleViewModel extends ViewModel {

    private LifecycleManager lifecycleManager;

    /**
     * 初始化
     */
    public void initConfig() {
        lifecycleManager = new LifecycleManager();

        LifecycleElement lifecycleElement = LifecycleElement.get();
        lifecycleManager.addLifecycle(LifecycleElementWrapper.build(lifecycleElement));
    }

    /**
     * 清除/销毁
     */
    @Override
    protected void onCleared() {
        super.onCleared();
        if (lifecycleManager != null) {
            lifecycleManager.onDestroy();
        }
    }
}
