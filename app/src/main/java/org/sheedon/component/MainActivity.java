package org.sheedon.component;

import android.util.Log;
import android.view.View;

import org.sheedon.common.app.BaseToolbarActivity;
import org.sheedon.upgradedispatcher.UpgradeInstaller;

public class MainActivity extends BaseToolbarActivity {

    @Override
    protected ToolbarModel buildToolbarEvent() {
        return new ToolbarModel(R.string.app_name, View.VISIBLE, View.GONE, "编辑");
    }

    @Override
    protected int getChildContentLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViewModel() {

    }

    @Override
    protected void initData() {
        super.initData();
        UpgradeInstaller.setUp(this, () -> Log.v("SXD","onCompleted"));
    }

    @Override
    protected void onMenuClick() {
        super.onMenuClick();


//        doNext();

        UpgradeInstaller.upgradeApp(this,"10","http://file.yanhangtec.com/prodectfile/fileResource/upload/2ea4d6f8-a537-4295-be50-788f08dfd5ef.apk");
//        UpdateFragment
//        NextActivity.show(this);
    }

}