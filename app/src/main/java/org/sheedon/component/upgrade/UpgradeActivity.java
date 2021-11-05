package org.sheedon.component.upgrade;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.sheedon.component.R;
import org.sheedon.upgradedispatcher.UpgradeInstaller;
import org.sheedon.upgradedispatcher.listener.UpgradeListener;

public class UpgradeActivity extends AppCompatActivity {

    private final String TAG = UpgradeActivity.class.getName();

    public static void show(Context context) {
        context.startActivity(new Intent(context, UpgradeActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade);
    }

    public void onUpgradeClick(View view) {
        UpgradeInstaller.upgradeApp(this, "9", "http://file.yanhangtec.com/prodectfile/fileResource/upload/01abd093-bb46-49b6-8c9a-1a204a6e1587.apk");
    }

    public void onBindUpgradeClick(View view) {
        UpgradeInstaller.upgradeApp(this, "9", "http://file.yanhangtec.com/prodectfile/fileResource/upload/01abd093-bb46-49b6-8c9a-1a204a6e1587.apk", new UpgradeListener() {
            @Override
            public void onUpgradeFailure(int code, String message) {
                Log.v(TAG, "code:" + code);
                Log.v(TAG, "message:" + message);
            }

            @Override
            public void onProgress(int progress) {
                Log.v(TAG, "progress:" + progress);
            }

            @Override
            public void onStartDownload() {
                Log.v(TAG, "onStartDownload");
            }

            @Override
            public void onDownloadSuccess() {
                Log.v(TAG, "onDownloadSuccess");
            }
        });
    }
}