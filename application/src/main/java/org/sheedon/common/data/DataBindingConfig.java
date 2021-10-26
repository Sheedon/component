package org.sheedon.common.data;

import android.util.SparseArray;

import androidx.annotation.NonNull;

/**
 * tip:
 * 将 DataBinding 实例限制于 base 页面中，默认不向子类暴露，
 * 通过这样的方式，来彻底解决 视图调用的一致性问题，
 * 如此，视图实例的安全性将和基于函数式编程思想的 Jetpack Compose 持平。
 * 而 DataBindingConfig 就是在这样的背景下，用于为 base 页面中的 DataBinding 提供绑定项。
 *
 * @Author: KunMinX
 * @Date: 2020/04/18 5:47 下午
 */
public class DataBindingConfig {

    private final SparseArray<Object> bindingParams = new SparseArray<>();

    public SparseArray getBindingParams() {
        return bindingParams;
    }

    public DataBindingConfig addBindingParam(@NonNull Integer variableId,
                                             @NonNull Object object) {
        bindingParams.put(variableId, object);
        return this;
    }

    /**
     * 清除
     */
    public void clear() {
        bindingParams.clear();
    }
}
