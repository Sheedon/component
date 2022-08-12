package org.sheedon.common.app

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import org.sheedon.common.app.center.IShowAndHideLoading
import org.sheedon.common.widget.loading.ILoadingDialogHandler
import org.sheedon.common.handler.ResConvertHandler
import org.sheedon.common.handler.ToastHandler
import org.sheedon.tool.ext.hideSoftKeyboard

/**
 * 基础Fragment
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/1/5 9:06 下午
 */
abstract class BaseFragment : Fragment(), IShowAndHideLoading {

    // 用于延迟处理动作
    private val handler = Handler(Looper.getMainLooper())

    // 加载框处理者
    private val loadingHandler: ILoadingDialogHandler by lazy {
        ILoadingDialogHandler.LoadingDialogHandler
            .getInstance()
            .factory!!
            .createLoadingDialog(requireContext())
    }

    // 是否第一次加载
    private var firstLoad = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layId = getContentLayoutId()
        val rootView = bindContentView(layId, inflater, container)
        initWidget(rootView)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 当View创建完成后初始化数据
        initData(view)
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
     * @param layId     视图ID
     * @param inflater  布局填充者
     * @param container 容器
     * @return 根View
     */
    protected open fun bindContentView(
        layId: Int,
        inflater: LayoutInflater,
        container: ViewGroup?
    ): View {
        return inflater.inflate(layId, container, false)
    }

    /**
     * 初始化控件
     */
    protected open fun initWidget(root: View) {

    }

    /**
     * 初始化数据
     */
    protected open fun initData(view: View) {
        this.initData()
    }

    /**
     * 初始化数据，建议这个方法用于一系列监听行为
     */
    protected open fun initData() {
    }

    override fun onResume() {
        super.onResume()
        onVisible()
    }

    /**
     * 是否需要懒加载
     */
    private fun onVisible() {
        if (lifecycle.currentState == Lifecycle.State.STARTED && firstLoad) {
            // 延迟加载 防止 切换动画还没执行完毕时数据就已经加载好了，这时页面会有渲染卡顿
            firstLoad = false
            handler.postDelayed({
                lazyLoadData()
            }, lazyLoadTime())
        }
    }

    /**
     * 页面显示后，首次加载数据
     */
    protected open fun lazyLoadData() {
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
     * 关闭当前Fragment弹窗，并且尝试关闭Activity中的弹窗
     */
    override fun hideLoading() {
        loadingHandler.hideLoading()

        // 并且尝试关闭activity中的弹窗
        val currentActivity = activity
        if (currentActivity is BaseActivity) {
            currentActivity.hideActivityLoading()
        }
    }

    override fun onPause() {
        super.onPause()
        hideLoading()
        hideSoftKeyboard(activity)
    }

    /**
     * 延迟加载 防止 切换动画还没执行完毕时数据就已经加载好了，这时页面会有渲染卡顿  bug
     * 这里传入你想要延迟的时间，延迟时间可以设置比转场动画时间长一点 单位： 毫秒
     * 不传默认 300毫秒
     * @return Long
     */
    open fun lazyLoadTime(): Long {
        return 300
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }

}