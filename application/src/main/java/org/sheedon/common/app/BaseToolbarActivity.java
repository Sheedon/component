package org.sheedon.common.app;

import android.annotation.SuppressLint;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.databinding.ViewDataBinding;
import androidx.databinding.library.baseAdapters.BR;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.sheedon.common.data.DataBindingConfig;
import org.sheedon.common.data.model.IToolbarModel;
import org.sheedon.common.databinding.LayoutToolbarBinding;
import org.sheedon.common.handler.ConfigHandler;

/**
 * 带Toolbar的Activity
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2021/10/21 9:30 上午
 */
public abstract class BaseToolbarActivity extends DataBindingActivity {

    private ViewDataBinding mToolbarBinding;
    private boolean appendToolbarParam = false;

    @Override
    protected int getContentLayoutId() {
        return ConfigHandler.getToolbarId();
    }

    @Override
    protected DataBindingConfig appendBindingParam() {
        if (!appendToolbarParam) {
            return super.appendBindingParam()
                    .addBindingParam(BR.toolbar, buildToolbarEvent());
        }
        return super.appendBindingParam();
    }

    /**
     * 构建标题栏事件
     */
    protected abstract ToolbarModel buildToolbarEvent();


    @Override
    protected void onViewDataBinding(ViewDataBinding parentBinding) {
        super.onViewDataBinding(parentBinding);

        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this), getChildContentLayoutId(),
                ((LayoutToolbarBinding) parentBinding).flChild, true);
        if (binding == null) {
            View.inflate(this, getChildContentLayoutId(), ((LayoutToolbarBinding) parentBinding).flChild);
            return;
        }
        binding.setLifecycleOwner(this);

        // 清除父级 绑定的数据
        super.appendBindingParam().clear();
        // 之后获取的都是 toolbar 中的 DataBindingConfig
        appendToolbarParam = true;
        DataBindingConfig bindingConfig = appendBindingParam();
        SparseArray bindingParams = bindingConfig.getBindingParams();
        for (int index = 0, length = bindingParams.size(); index < length; index++) {
            binding.setVariable(bindingParams.keyAt(index), bindingParams.valueAt(index));
        }

        onToolbarViewDataBinding(binding);
        mToolbarBinding = binding;
    }

    /**
     * 得到子界面的资源文件Id
     */
    protected abstract int getChildContentLayoutId();

    /**
     * ViewDataBinding 加载完成，建议只是在当前方法中使用，
     * 非必要情况下，尽可能不在子类中拿到 binding 实例乃至获取 view 实例。使用即埋下隐患。
     *
     * @param binding View绑定
     */
    protected void onToolbarViewDataBinding(ViewDataBinding binding) {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mToolbarBinding != null)
            mToolbarBinding.unbind();
        mToolbarBinding = null;
    }

    @SuppressLint("StaticFieldLeak")
    protected class ToolbarModel extends ViewModel implements IToolbarModel {

        // 标题
        private final MutableLiveData<Integer> title = new MutableLiveData<>();
        // 菜单标题
        private final ObservableField<String> menuTitle = new ObservableField<>();
        // 菜单是否显示
        private final ObservableInt menuVisibility = new ObservableInt();
        // 返回键是否显示
        private final int backVisibility;

        public ToolbarModel(int title) {
            this(title, View.GONE, View.VISIBLE);
        }

        public ToolbarModel(int title, String menuTitle) {
            this(title, View.VISIBLE, View.VISIBLE, menuTitle);
        }

        public ToolbarModel(int title, int menuVisibility, int backVisibility) {
            this(title, menuVisibility, backVisibility, "");
        }

        public ToolbarModel(int title, int menuVisibility, int backVisibility, String menuTitle) {
            this.title.setValue(title);
            this.menuVisibility.set(menuVisibility);
            this.backVisibility = backVisibility;
            this.menuTitle.set(menuTitle);
        }

        @Override
        public MutableLiveData<Integer> getTitle() {
            return title;
        }

        @Override
        public ObservableField<String> getMenuTitle() {
            return menuTitle;
        }

        @Override
        public ObservableInt getMenuVisibility() {
            return menuVisibility;
        }

        @Override
        public int getBackVisibility() {
            return backVisibility;
        }

        @Override
        public void setTitle(int title) {
            this.title.postValue(title);
        }

        @Override
        public void onBackClick() {
            BaseToolbarActivity.this.onBackClick();
        }

        @Override
        public void onMenuClick() {
            BaseToolbarActivity.this.onMenuClick();
        }
    }

    /**
     * 返回
     */
    protected void onBackClick() {
        finish();
    }

    /**
     * 菜单点击
     */
    protected void onMenuClick() {

    }
}
