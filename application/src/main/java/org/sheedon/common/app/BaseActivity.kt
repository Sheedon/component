package org.sheedon.common.app

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.NavHostFragment
import org.sheedon.common.R
import org.sheedon.common.app.center.IShowAndHideLoading
import org.sheedon.common.data.model.IToolbarModel
import org.sheedon.common.handler.*
import org.sheedon.common.widget.loading.ILoadingDialogHandler
import org.sheedon.common.widget.rootview.IRootView
import org.sheedon.common.widget.toolbar.IToolbarView

/**
 * 基础Activity
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/1/5 2:10 下午
 */
abstract class BaseActivity : AppCompatActivity(), IShowAndHideLoading {

    private val loadingHandler: ILoadingDialogHandler by lazy {
        ILoadingDialogHandler.LoadingDialogHandler.getInstance()
            .factory!!.createLoadingDialog(this)
    }

    // 根布局View
    private var rootView: IRootView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 在界面未初始化之前调用的初始化窗口
        initWidows()
        if (initArgs(intent.extras)) {
            // 得到界面Id并设置到Activity界面中
            val layId = getContentLayoutId()
            bindContentView(layId)
            initWidget()
            initData()
        } else {
            showError(getArgsMissing())
            finish()
        }
    }

    /**
     * 初始化窗口
     */
    protected open fun initWidows() {
        ConfigHandler.setStatusBarMode(this)
    }

    /**
     * 初始化相关参数
     * 在需要传值的页面 只有参数完整 才去获取资源id，布局，数据
     *
     * @param bundle 传入参数
     * @return 默认true 子类复写
     */
    protected open fun initArgs(bundle: Bundle?) = true

    /**
     * 获取初始化相关参数缺失错误信息
     *
     * @return 缺失描述
     */
    protected open fun getArgsMissing() = R.string.common_args_missing

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
        if (!enableToolbar()) {
            attachContentView(layId)
            return
        }

        // 获取根布局
        rootView = loadRootView()
        setContentView(rootView!!.getRootView())
        rootView!!.setToolbarModel(buildToolbarEvent())

        // 设置子布局
        attachChildContentView(layId, rootView!!.getChildLayout())
    }

    /**
     * 附加非包含toolbar的布局
     * */
    protected open fun attachContentView(layId: Int) {
        setContentView(layId)
    }

    /**
     * 附加存在子布局
     * */
    protected open fun attachChildContentView(layId: Int, attachView: FrameLayout) {
        View.inflate(this, layId, attachView)
    }

    /**
     * 是否启用标题栏
     */
    protected open fun enableToolbar(): Boolean = false

    /**
     * 创建并加载根布局
     */
    protected open fun loadRootView(): IRootView {
        return ConfigHandler.loadRootView(this, loadToolbarView())
    }

    /**
     * 创建并加载标题栏布局
     */
    protected open fun loadToolbarView(): IToolbarView {
        return ConfigHandler.loadToolbarView(this)
    }

    /**
     * 构建标题栏事件
     */
    protected open fun buildToolbarEvent(): IToolbarModel {
        throw NullPointerException("please build ToolbarModel")
    }

    /**
     * 初始化控件
     */
    protected open fun initWidget() {

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
    override fun showLoading(@StringRes res: Int) {
        showLoading(ResConvertHandler.getInstance().convertString(res))
    }

    /**
     * 显示加载框
     *
     * @param message 描述内容
     */
    @JvmOverloads
    open fun showLoading(message: String? = null) {
        loadingHandler.showLoading(message)
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
        ToastHandler.showToast(msg)
    }

    /**
     * 关闭弹窗
     */
    override fun hideLoading() {
        hideActivityLoading()
        hideFragmentLoading()
    }

    /**
     * 隐藏当前Activity的加载框
     */
    internal fun hideActivityLoading() {
        loadingHandler.hideLoading()
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

    /**
     * 销毁根布局
     */
    override fun onDestroy() {
        super.onDestroy()
        rootView?.destroy()
    }


    inner class ToolbarModel @JvmOverloads constructor(
        _title: Int? = null,
        _titleMessage: String? = null,
        _menuVisibility: Int = View.GONE,
        _backVisibility: Int = View.VISIBLE,
        _menuTitle: String = ""
    ) : IToolbarModel {

        // 标题
        private val title: MutableLiveData<String> = MutableLiveData<String>()
        override fun getTitle(): MutableLiveData<String> {
            return title
        }

        // 菜单标题
        private val menuTitle = ObservableField<String>()
        override fun getMenuTitle(): ObservableField<String> {
            return menuTitle
        }

        // 菜单是否显示
        private val menuVisibility = ObservableInt()
        override fun getMenuVisibility(): ObservableInt {
            return menuVisibility
        }

        // 返回键是否显示
        val backVisibility: ObservableInt = ObservableInt(View.VISIBLE)
        override fun getBackVisibility(): Int {
            return backVisibility.get()
        }

        init {
            this.title.value = if (_title == null || _title == 0) {
                _titleMessage ?: ""
            } else {
                getString(_title)
            }
            this.menuVisibility.set(_menuVisibility)
            this.backVisibility.set(_backVisibility)
            this.menuTitle.set(_menuTitle)
        }


        override fun setTitle(title: String) {
            this.title.value = title
        }

        override fun onBackClick() {
            this@BaseActivity.onBackClick()
        }

        override fun onMenuClick() {
            this@BaseActivity.onMenuClick()
        }
    }

    /**
     * 返回
     */
    protected open fun onBackClick() {
        finish()
    }

    /**
     * 菜单点击
     */
    protected open fun onMenuClick() {

    }
}