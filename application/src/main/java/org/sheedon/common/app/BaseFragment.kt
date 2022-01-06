package org.sheedon.common.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import org.sheedon.common.handler.ILoadingDialogHandler
import org.sheedon.common.handler.ResConvertHandler
import org.sheedon.common.handler.ToastHandler

/**
 * 基础Fragment
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/1/5 9:06 下午
 */
abstract class BaseFragment : Fragment() {

    var loadingHandler: ILoadingDialogHandler? = null

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
     * 初始化数据
     */
    protected open fun initData() {

    }

    /**
     * 显示加载框
     */
    protected open fun showLoading() {
        showLoading("")
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
            loadingHandler = factory?.createLoadingDialog(requireContext())
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
        hideLoading();
        ToastHandler.showToast(msg)
    }

    /**
     * 关闭弹窗
     */
    fun hideLoading() {
        loadingHandler?.hideLoading()
    }
}