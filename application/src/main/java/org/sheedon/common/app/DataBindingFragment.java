package org.sheedon.common.app;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import org.sheedon.common.data.DataBindingConfig;
import org.sheedon.common.handler.ViewModelProviderHandler;

/**
 * 数据绑定 Fragment
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2021/10/20 6:25 下午
 */
public abstract class DataBindingFragment extends BaseFragment {

    protected DataBindingActivity mActivity;
    private ViewDataBinding mBinding;
    // 绑定参数
    private final DataBindingConfig dataBindingConfig = new DataBindingConfig();

    private ViewModelProvider mFragmentProvider;
    private ViewModelProvider mActivityProvider;
    private ViewModelProvider mApplicationProvider;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mActivity = (DataBindingActivity) context;
    }

    @Override
    protected View bindContentView(int layId, LayoutInflater inflater, @Nullable ViewGroup container) {
        initViewModel();

        ViewDataBinding binding = DataBindingUtil.inflate(inflater, layId, container, false);
        binding.setLifecycleOwner(this);
        DataBindingConfig bindingConfig = appendBindingParam();
        SparseArray bindingParams = bindingConfig.getBindingParams();
        for (int index = 0, length = bindingParams.size(); index < length; index++) {
            binding.setVariable(bindingParams.keyAt(index), bindingParams.valueAt(index));
        }

        onViewDataBinding(binding);
        mBinding = binding;
        return binding.getRoot();
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
     * @param binding View绑定者
     */
    protected void onViewDataBinding(ViewDataBinding binding) {

    }

    /**
     * 获取Fragment范围视图模型
     *
     * @param modelClass ViewModel's class
     * @param <T>        ViewModel
     * @return ViewModel
     */
    protected <T extends ViewModel> T getFragmentScopeViewModel(@NonNull Class<T> modelClass) {
        return ViewModelProviderHandler.getFragmentScopeViewModel(mFragmentProvider, this, modelClass);
    }

    /**
     * 获取Activity范围视图模型
     *
     * @param modelClass ViewModel's class
     * @param <T>        ViewModel
     * @return ViewModel
     */
    protected <T extends ViewModel> T getActivityScopeViewModel(@NonNull Class<T> modelClass) {
        return ViewModelProviderHandler.getActivityScopeViewModel(mActivityProvider,
                mActivity, modelClass);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mBinding != null)
            mBinding.unbind();
        mBinding = null;
    }
}
