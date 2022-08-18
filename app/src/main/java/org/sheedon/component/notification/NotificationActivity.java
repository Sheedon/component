package org.sheedon.component.notification;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;


import org.sheedon.component.MainActivity;
import org.sheedon.component.R;
import org.sheedon.notification.BroadcastActions;
import org.sheedon.notification.NotificationClient;
import org.sheedon.notification.model.ProgressBuilder;
import org.sheedon.notification.model.SimpleBuilder;
import org.sheedon.notification.pendingintent.ClickPendingIntentActivity;
import org.sheedon.notification.pendingintent.ClickPendingIntentBroadCast;

import java.util.concurrent.TimeUnit;

public class NotificationActivity extends AppCompatActivity {

    private NotificationClient client;

    public static void show(Context context) {
        context.startActivity(new Intent(context, NotificationActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        client = new NotificationClient.Builder()
                .application(this)
                .build();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BroadcastActions.ACTION_NOTIFICATION_CLICK_INTENT);
        registerReceiver(notificationBroadcast, intentFilter);

    }

    public void onTouchClick(View view) {
//        ClickPendingIntentActivity activity = new ClickPendingIntentActivity(MainActivity.class, null, 0);
//        ProgressBuilder message = new ProgressBuilder()
//                .progress(0, 0)
//                .channelId(getString(R.string.default_channel_id))
//                .activityIntent(activity);
//        client.sendNotification(message);
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                for (int index = 0; index < 100; index++) {
//                    try {
//                        TimeUnit.MILLISECONDS.sleep(100);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    message.progress(100, index);
//                    client.sendNotification(message);
//                }
//            }
//        }).start();

        Bundle bundle = new Bundle();
//        bundle.putParcelable(EQMSBusiness.RouterBundle.model, model);

        ClickPendingIntentBroadCast broadCast = new ClickPendingIntentBroadCast(bundle, 0);

        SimpleBuilder builder = new SimpleBuilder()
                .broadCastIntent(broadCast);
        client.sendNotification(builder);

    }

    // 通知广播
    private final BroadcastReceiver notificationBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(notificationBroadcast);
    }
}