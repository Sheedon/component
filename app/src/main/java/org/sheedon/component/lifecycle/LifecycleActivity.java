package org.sheedon.component.lifecycle;


import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import org.sheedon.common.app.DataBindingActivity;
import org.sheedon.component.R;

/**
 * 自定义生命周期处理
 */
public class LifecycleActivity extends DataBindingActivity {

    private LifecycleViewModel mState;

    public static void show(Context context) {
        context.startActivity(new Intent(context, LifecycleActivity.class));
    }

    @Override
    protected void initViewModel() {
        mState = getActivityScopeViewModel(LifecycleViewModel.class);
    }

    @NonNull
    @Override
    protected ToolbarModel buildToolbarEvent() {
        return new ToolbarModel(R.string.title_lifecycle);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_lifecycle;
    }

    @Override
    protected void initData() {
        super.initData();
        // 初始化配置
        mState.initConfig();
    }
}