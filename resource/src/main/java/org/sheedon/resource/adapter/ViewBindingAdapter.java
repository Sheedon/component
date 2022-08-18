package org.sheedon.resource.adapter;

import android.graphics.drawable.GradientDrawable;
import android.view.View;

import androidx.databinding.BindingAdapter;

/**
 * View-BindingAdapter
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/8/18 13:02
 */
public class ViewBindingAdapter {

    /**
     * 设置View的渐变配置
     */
    @BindingAdapter(value = {"bindStartColor", "bindEndColor", "bindType"})
    public static void setGradientConfig(View view,
                                         int startColor, int endColor, GradientDrawable.Orientation bindType) {

        GradientDrawable aDrawable = new GradientDrawable(bindType,
                new int[]{startColor, endColor});
        view.setBackground(aDrawable);
    }

}
