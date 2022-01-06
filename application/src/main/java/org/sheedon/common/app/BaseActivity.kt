package org.sheedon.common.app

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import org.sheedon.common.R
import org.sheedon.common.handler.*

/**
 * 基础Activity
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/1/5 2:10 下午
 */
abstract class BaseActivity : AppCompatActivity() {

    private var loadingHandler: ILoadingDialogHandler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 在界面未初始化之前调用的初始化窗口
        initWidows()
        if (initArgs(intent.extras)) {
            // 得到界面Id并设置到Activity界面中
            val layId = getContentLayoutId()
            bindContentView(layId)
            initBefore()
            initWidget()
            initData()
            NotifyAppStateHandler.addActivity(this)
        } else {
            showError(getArgsMissing())
            finish()
        }
    }

    /**
     * 初始化窗口
     */
    protected open fun initWidows() {

    }

    /**
     * 初始化相关参数
     * 在需要传值的页面 只有参数完整 才去获取资源id，布局，数据
     *
     * @param bundle 传入参数
     * @return 默认true 子类复写
     */
    protected open fun initArgs(bundle: Bundle?): Boolean {
        return true
    }

    /**
     * 获取初始化相关参数缺失错误信息
     *
     * @return 缺失描述
     */
    protected open fun getArgsMissing(): Int {
        return R.string.common_args_missing
    }

    /**
     * 得到当前界面的资源文件Id
     *
     * @return 资源文件Id
     */
    @LayoutRes
    protected abstract fun getContentLayoutId(): Int

    /**
     * 将 layId 绑定到 布局View 上
     *
     * @param layId 视图ID
     */
    protected open fun bindContentView(layId: Int) {
        setContentView(layId)
    }

    /**
     * 初始化控件调用之前
     */
    protected open fun initBefore() {

    }

    /**
     * 初始化控件
     */
    protected open fun initWidget() {
        ConfigHandler.setStatusBarMode(this)
    }

    /**
     * 初始化数据
     */
    protected open fun initData() {
    }

    /**
     * 显示加载框
     *
     * @param res 文字资源
     */
    protected open fun showLoading(@StringRes res: Int) {
        showLoading(ResConvertHandler.getInstance().convertString(res))
    }

    /**
     * 显示加载框
     *
     * @param message 描述内容
     */
    protected open fun showLoading(message: String = "") {
        if (loadingHandler == null) {
            val dialogHandler = ILoadingDialogHandler.LoadingDialogHandler.getInstance()
            val factory = dialogHandler.factory
            loadingHandler = factory?.createLoadingDialog(this)
        }
        loadingHandler?.showLoading(message)
    }

    /**
     * 显示错误信息
     *
     * @param res 错误信息资源
     */
    protected open fun showError(@StringRes res: Int) {
        showError(ResConvertHandler.getInstance().convertString(res))
    }

    /**
     * 显示错误信息
     *
     * @param msg 错误信息
     */
    protected open fun showError(msg: String) {
        hideLoading()
        hideFragmentLoading()
        ToastHandler.showToast(msg)
    }

    /**
     * 关闭弹窗
     */
    protected open fun hideLoading() {
        loadingHandler?.hideLoading()
    }

    /**
     * 隐藏Fragment中的弹窗
     */
    private fun hideFragmentLoading() {
        val fragments = supportFragmentManager.fragments
        if (fragments.size <= 0) {
            return
        }
        fragments.forEach {
            when (it) {
                is BaseFragment -> it.hideLoading()
                is NavHostFragment -> it.childFragmentManager.fragments.forEach { fragment ->
                    if (fragment is BaseFragment) {
                        fragment.hideLoading()
                    }
                }
            }
        }
    }

    /**
     * 当点击界面导航返回时，Finish当前界面
     */
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }


    override fun onDestroy() {
        super.onDestroy()
        NotifyAppStateHandler.removeActivity(this);
    }
}