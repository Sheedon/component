package org.sheedon.component.notification;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import org.sheedon.component.MainActivity;
import org.sheedon.component.R;
import org.sheedon.notification.NotificationClient;
import org.sheedon.notification.NotificationMessage;

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

    }

    public void onTouchClick(View view) {
        NotificationMessage message = new NotificationMessage.Builder()
                .activityIntent(new Intent(this, MainActivity.class)).build();
        client.sendNotification(message);

    }
}