package org.sheedon.common.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import org.sheedon.common.handler.ILoadingDialogHandler;
import org.sheedon.common.handler.ResConvertHandler;
import org.sheedon.common.handler.ToastHandler;

/**
 * 基础Fragment
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2021/10/19 6:10 下午
 */
public abstract class BaseFragment extends Fragment {

    private ILoadingDialogHandler loadingHandler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        int layId = getContentLayoutId();
        View rootView = bindContentView(layId, inflater, container);
        initWidget(rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 当View创建完成后初始化数据
        initData(view);
    }

    /**
     * 得到当前界面的资源文件Id
     *
     * @return 资源文件Id
     */
    @LayoutRes
    protected abstract int getContentLayoutId();

    /**
     * 将 layId 绑定到 布局View 上
     *
     * @param layId     视图ID
     * @param inflater  布局填充者
     * @param container 容器
     * @return 根View
     */
    protected View bindContentView(int layId, LayoutInflater inflater, @Nullable ViewGroup container) {
        return inflater.inflate(layId, container, false);
    }


    /**
     * 初始化控件
     */
    protected void initWidget(View root) {
    }

    /**
     * 初始化数据
     */
    protected void initData(View view) {
        this.initData();
    }

    /**
     * 初始化数据
     */
    protected void initData() {

    }


    /**
     * 显示加载框
     */
    protected void showLoading() {
        showLoading("");
    }

    /**
     * 显示加载框
     *
     * @param res 文字资源
     */
    protected void showLoading(@StringRes int res) {
        showLoading(ResConvertHandler.getInstance().convertString(res));
    }

    /**
     * 显示加载框
     *
     * @param message 描述内容
     */
    protected void showLoading(String message) {
        if (loadingHandler == null) {
            // 通过LoadingDialogFactory 工厂类创建弹窗
            ILoadingDialogHandler.LoadingDialogHandler dialogHandler
                    = ILoadingDialogHandler.LoadingDialogHandler.getInstance();
            ILoadingDialogHandler.LoadingDialogFactory factory = dialogHandler.getFactory();
            loadingHandler = factory.createLoadingDialog(getContext());
        }
        loadingHandler.showLoading(message);
    }

    /**
     * 显示错误信息
     *
     * @param res 错误信息资源
     */
    protected void showError(int res) {
        showError(ResConvertHandler.getInstance().convertString(res));
    }

    /**
     * 显示错误信息
     *
     * @param msg 错误信息
     */
    protected void showError(String msg) {
        hideLoading();
        ToastHandler.showToast(msg);
    }

    /**
     * 关闭弹窗
     */
    protected void hideLoading() {
        if (loadingHandler != null) {
            loadingHandler.hideLoading();
        }
    }
}
