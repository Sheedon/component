package org.sheedon.component;

import android.util.Log;
import android.view.View;


import org.sheedon.common.app.BaseToolbarActivity;
import org.sheedon.component.databinding.ActivityMainBinding;
import org.sheedon.component.recyclerlayout.EmptyActivity;

import java.util.TimeZone;


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
//        UpgradeInstaller.setUp(this, () -> Log.v("SXD", "onCompleted"));

    }

    public void onTouchClick(View view) {


//        channelClient.deleteChannel(this);
//        client.notifyInfo();
//        NextActivity.show(this);
//        NotificationActivity.show(this);
        EmptyActivity.show(this);
//        UpgradeActivity.show(this);

//        ARouter.getInstance().build("/Test/TargetActivity")
//                .withLong("id", 666L)
//                .withString("name", "888")
//                .navigation();

    }


    @Override
    protected void onMenuClick() {
        super.onMenuClick();
//        doNext();

//        UpgradeInstaller.upgradeApp(this, "9", "http://file.yanhangtec.com/prodectfile/fileResource/upload/01abd093-bb46-49b6-8c9a-1a204a6e1587.apk");
//        UpdateFragment
//        NextActivity.show(this);



        KotlinActivity.show(this);

    }

}