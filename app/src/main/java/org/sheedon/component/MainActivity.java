package org.sheedon.component;

import android.content.Intent;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import androidx.lifecycle.MutableLiveData;

import com.alibaba.android.arouter.launcher.ARouter;
import com.google.android.material.color.MaterialColors;
import com.google.android.material.transition.Hold;
import com.google.android.material.transition.MaterialContainerTransform;

import org.sheedon.common.app.BaseToolbarActivity;
import org.sheedon.component.arouter.NotificationClient;
import org.sheedon.component.notification.NotificationActivity;
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
//        UpgradeInstaller.setUp(this, () -> Log.v("SXD", "onCompleted"));

    }

    private NotificationClient client = new NotificationClient();
    public void onTouchClick(View view) {

        client.notifyInfo();
//        NextActivity.show(this);
//        NotificationActivity.show(this);

//        ARouter.getInstance().build("/Test/TargetActivity")
//                .withLong("id", 666L)
//                .withString("name", "888")
//                .navigation();

    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
    }


    @Override
    protected void onMenuClick() {
        super.onMenuClick();
//        doNext();

//        UpgradeInstaller.upgradeApp(this, "9", "http://file.yanhangtec.com/prodectfile/fileResource/upload/01abd093-bb46-49b6-8c9a-1a204a6e1587.apk");
//        UpdateFragment
        NextActivity.show(this);
    }

}