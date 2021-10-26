package org.sheedon.common.data.model;

import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.MutableLiveData;

/**
 * 导航栏功能
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2021/10/21 1:49 下午
 */
public interface IToolbarModel {

    // 标题
    MutableLiveData<Integer> getTitle();

    // 菜单标题
    ObservableField<String> getMenuTitle();

    // 菜单是否显示
    ObservableInt getMenuVisibility();

    // 返回是否显示
    int getBackVisibility();

    // 设置标题
    void setTitle(int title);

    // 返回按钮触发
    void onBackClick();

    // 菜单按钮触发
    void onMenuClick();
}
