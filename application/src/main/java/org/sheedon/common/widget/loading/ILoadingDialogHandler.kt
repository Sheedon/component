package org.sheedon.common.widget.loading

import android.content.Context

/**
 * 加载弹窗处理者
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/1/5 2:12 下午
 */
interface ILoadingDialogHandler {

    /**
     * 显示描述弹窗
     *
     * @param message 描述
     */
    fun showLoading(message: String? = null)

    /**
     * 隐藏弹窗
     */
    fun hideLoading()

    open class LoadingDialogFactory {

        protected open fun createLoadingDialog(content: Context): ILoadingDialogHandler {
            return LoadingDialog(content)
        }
    }

    /**
     * 加载框执行类
     */
    class LoadingDialogHandler private constructor() {

        // 加载框创建工厂
        var factory: LoadingDialogFactory? = null
            get() {
                // 获取弹窗工厂类，用于调用者通过该类创建弹窗
                if (field == null) {
                    field = LoadingDialogFactory()
                }
                return field
            }
            private set

        companion object {
            private val INSTANCE = LoadingDialogHandler()

            @JvmStatic
            fun getInstance() = INSTANCE
        }

        /**
         * 附加替换加载弹窗工厂
         *
         * @param factory 弹窗工厂
         */
        fun attachLoadingDialogFactory(factory: LoadingDialogFactory) {
            this.factory = factory
        }
    }

}