package org.sheedon.common.handler;

import android.content.Context;

import org.sheedon.common.widget.LoadingDialog;

/**
 * 加载弹窗处理者
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2021/10/19 6:42 下午
 */
public interface ILoadingDialogHandler {

    /**
     * 显示描述弹窗
     *
     * @param message 描述
     */
    void showLoading(String message);

    /**
     * 隐藏弹窗
     */
    void hideLoading();


    class LoadingDialogFactory {

        public ILoadingDialogHandler createLoadingDialog(Context context) {
            return new LoadingDialog(context);
        }

    }

    /**
     * 加载框执行类
     */
    class LoadingDialogHandler {
        private static final LoadingDialogHandler INSTANCE = new LoadingDialogHandler();
        // 加载框创建工厂
        private LoadingDialogFactory factory;

        private LoadingDialogHandler() {

        }

        public static LoadingDialogHandler getInstance() {
            return INSTANCE;
        }

        /**
         * 附加替换加载弹窗工厂
         *
         * @param factory 弹窗工厂
         */
        public void attachLoadingDialogFactory(LoadingDialogFactory factory) {
            this.factory = factory;
        }

        /**
         * 获取弹窗工厂类，用于调用者通过该类创建弹窗
         */
        public LoadingDialogFactory getFactory() {
            if (factory == null) {
                factory = new LoadingDialogFactory();
            }
            return factory;
        }
    }

}
