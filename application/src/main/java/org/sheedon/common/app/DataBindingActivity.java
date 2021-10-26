package org.sheedon.common.app;

import android.util.SparseArray;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import org.sheedon.common.data.DataBindingConfig;

/**
 * 数据绑定 Activity
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2021/10/20 5:44 下午
 */
public abstract class DataBindingActivity extends BaseActivity {

    private ViewDataBinding mBinding;
    // 绑定参数
    private final DataBindingConfig dataBindingConfig = new DataBindingConfig();

    @Override
    protected void bindContentView(int layId) {
        initBeforeOfViewModel();
        initViewModel();

        ViewDataBinding binding = DataBindingUtil.setContentView(this, layId);
        binding.setLifecycleOwner(this);
        DataBindingConfig bindingConfig = appendBindingParam();
        SparseArray bindingParams = bindingConfig.getBindingParams();
        for (int index = 0, length = bindingParams.size(); index < length; index++) {
            binding.setVariable(bindingParams.keyAt(index), bindingParams.valueAt(index));
        }

        onViewDataBinding(binding);
        mBinding = binding;
    }


    /**
     * 初始化ViewModel之前操作
     */
    protected void initBeforeOfViewModel() {

    }

    /**
     * 加载ViewModel
     */
    protected abstract void initViewModel();


    /**
     * 追加绑定参数
     *
     * @return SparseArray
     */
    protected DataBindingConfig appendBindingParam() {
        return dataBindingConfig;
    }

    /**
     * ViewDataBinding 加载完成，建议只是在当前方法中使用，
     * 非必要情况下，尽可能不在子类中拿到 binding 实例乃至获取 view 实例。使用即埋下隐患。
     *
     * @param binding View绑定
     */
    protected void onViewDataBinding(ViewDataBinding binding) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBinding != null)
            mBinding.unbind();
        mBinding = null;
    }
}
