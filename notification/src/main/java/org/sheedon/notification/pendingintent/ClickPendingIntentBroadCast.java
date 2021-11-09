package org.sheedon.notification.pendingintent;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import org.sheedon.notification.BroadcastActions;
import org.sheedon.notification.interfaces.PendingIntentNotification;

public class ClickPendingIntentBroadCast implements PendingIntentNotification {
    private final Bundle mBundle;
    private final int mIdentifier;

    public ClickPendingIntentBroadCast(Bundle bundle, int identifier) {
        this.mBundle = bundle;
        this.mIdentifier = identifier;
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    @Override
    public PendingIntent onSettingPendingIntent(Context context) {
        Intent clickIntentBroadcast = new Intent(BroadcastActions.ACTION_NOTIFICATION_CLICK_INTENT);
        clickIntentBroadcast.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        clickIntentBroadcast.setPackage(context.getPackageName());
        if (mBundle != null) {
            clickIntentBroadcast.putExtras(mBundle);
        }

        return PendingIntent.getBroadcast(context, mIdentifier, clickIntentBroadcast,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

}
