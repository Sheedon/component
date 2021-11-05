package org.sheedon.component;

import android.util.Log;

import com.alibaba.android.arouter.launcher.ARouter;

import org.sheedon.common.app.BaseApplication;
import org.sheedon.upgradedispatcher.UpgradeInstaller;

/**
 * java类作用描述
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2021/10/27 10:42 上午
 */
public class App extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        // 升级模块初始化
        UpgradeInstaller.setUp(this, () -> Log.v("SXD", "onCompleted"));

        ARouter.init(this);
    }
}
