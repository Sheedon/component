package org.sheedon.component;


import android.content.Context;
import android.content.Intent;
import android.transition.AutoTransition;
import android.transition.Explode;
import android.transition.Slide;
import android.view.Gravity;
import android.view.Window;

import org.sheedon.common.app.BaseToolbarActivity;

public class NextActivity extends BaseToolbarActivity {

    public static void show(Context context) {
        context.startActivity(new Intent(context, NextActivity.class));
    }

    @Override
    protected ToolbarModel buildToolbarEvent() {
        return new ToolbarModel(R.string.prompt_error);
    }

    @Override
    protected int getChildContentLayoutId() {
        return R.layout.activity_next;
    }

    @Override
    protected void initViewModel() {
    }

    @Override
    protected void initWidows() {
        super.initWidows();

//        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
//        // 设置进入过度效果-爆炸式
//        getWindow().setEnterTransition(new Explode());
//        // 从当前Activity返回上个Activity时，采用默认过度效果
//        getWindow().setReturnTransition(new AutoTransition());
    }
}