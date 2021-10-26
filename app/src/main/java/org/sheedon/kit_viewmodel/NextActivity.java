package org.sheedon.kit_viewmodel;


import android.content.Context;
import android.content.Intent;

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
}