package org.sheedon.common.handler;

import android.app.Application;

import androidx.annotation.ColorRes;
import androidx.annotation.StringRes;

import org.sheedon.common.app.BaseApplication;

/**
 * 文件资源转化处理器
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2021/10/20 10:00 上午
 */
public class ResConvertHandler {

    private static final ResConvertHandler INSTANCE = new ResConvertHandler();
    private Application application;

    private ResConvertHandler() {
    }

    public static ResConvertHandler getInstance() {
        return INSTANCE;
    }

    /**
     * 外部主动附加替换 Application
     *
     * @param application Application
     */
    public void attachApplication(Application application) {
        this.application = application;
    }

    /**
     * 获取附加的Application，用于转化操作
     *
     * @return Application
     */
    private Application getApplication() {
        if (application == null) {
            application = BaseApplication.getInstance();
        }
        return application;
    }

    /**
     * 文字资源转为String类型
     *
     * @param resId 文字资源
     * @return 文字
     */
    public String convertString(@StringRes int resId) {
        Application application = getApplication();
        return application.getString(resId);
    }

    /**
     * 颜色资源转为颜色数值
     *
     * @param resId 颜色资源
     * @return 颜色数值
     */
    public int convertColor(@ColorRes int resId) {
        Application application = getApplication();
        return application.getResources().getColor(resId);
    }
}
