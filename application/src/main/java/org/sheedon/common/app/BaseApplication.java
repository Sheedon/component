package org.sheedon.common.app;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

/**
 * 基础应用模块,上层集成请基础该类
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2021/10/19 5:07 下午
 */
public class BaseApplication extends Application {

    private static BaseApplication instance;
    // 转UI执行
    private final Handler uiHandler = new Handler(Looper.getMainLooper());

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
    }

    public static Application getInstance() {
        return instance;
    }

    /**
     * 获取UI消息执行者
     */
    public static Handler getUiHandler() {
        return instance.uiHandler;
    }

}
