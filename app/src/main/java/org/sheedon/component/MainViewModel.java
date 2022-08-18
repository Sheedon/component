package org.sheedon.component;

import androidx.databinding.ObservableField;

import org.sheedon.mvvm.viewmodel.AbstractViewModel;

/**
 * java类作用描述
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/6/22 17:51
 */
public class MainViewModel extends AbstractViewModel {

    public final ObservableField<String> name = new ObservableField<>("Hello World!");
}
