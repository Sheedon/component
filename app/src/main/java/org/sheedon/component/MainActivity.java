package org.sheedon.component;

import android.view.View;



import org.sheedon.mvvm.ui.activities.AbstractVMActivity;


public class MainActivity extends AbstractVMActivity<MainViewModel> {

    @Override
    protected ToolbarModel buildToolbarEvent() {
        return new ToolbarModel(R.string.app_name, "", View.VISIBLE, View.GONE, "编辑");
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    public void onTouchClick(View view) {
        mState.name.set("test_test");


//        channelClient.deleteChannel(this);
//        client.notifyInfo();
//        NextActivity.show(this);
//        NotificationActivity.show(this);
//        ResActivity.show(this);
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