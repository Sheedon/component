package org.sheedon.common.app;

import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import org.sheedon.common.handler.ConfigHandler;
import org.sheedon.common.handler.ILoadingDialogHandler;
import org.sheedon.common.handler.NotifyAppStateHandler;
import org.sheedon.common.handler.ResConvertHandler;
import org.sheedon.common.handler.ToastHandler;

import java.util.List;

/**
 * 基础Activity
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2021/10/19 4:57 下午
 */
public abstract class BaseActivity extends AppCompatActivity {

    private ILoadingDialogHandler loadingHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 在界面未初始化之前调用的初始化窗口
        initWidows();
        // 得到界面Id并设置到Activity界面中
        int layId = getContentLayoutId();
        bindContentView(layId);
        initBefore();
        initWidget();
        initData();
        NotifyAppStateHandler.addActivity(this);
    }

    /**
     * 初始化窗口
     */
    protected void initWidows() {
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
     * @param layId 视图ID
     */
    protected void bindContentView(int layId) {
        setContentView(layId);
    }

    /**
     * 初始化控件调用之前
     */
    protected void initBefore() {

    }

    /**
     * 初始化控件
     */
    protected void initWidget() {
        ConfigHandler.setStatusBarMode(this);
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
            loadingHandler = factory.createLoadingDialog(this);
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
        hideFragmentLoading();
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


    /**
     * 隐藏Fragment中的弹窗
     */
    private void hideFragmentLoading() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        // 判断是否为空
        if (fragments.size() > 0) {
            for (Fragment fragment : fragments) {
                // 判断是否为我们能够处理的Fragment类型
                if (fragment instanceof BaseFragment) {
                    ((BaseFragment) fragment).hideLoading();
                } else if (fragment instanceof NavHostFragment) {
                    List<Fragment> fragments1 = fragment.getChildFragmentManager().getFragments();

                    for (Fragment fragment1 : fragments1) {
                        if (fragment1 instanceof BaseFragment) {
                            ((BaseFragment) fragment1).hideLoading();
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        // 当点击界面导航返回时，Finish当前界面
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NotifyAppStateHandler.removeActivity(this);
    }

    @Nullable
    @Override
    public ActionBar getSupportActionBar() {
        return super.getSupportActionBar();
    }
}
