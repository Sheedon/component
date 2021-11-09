package org.sheedon.notification.pendingintent;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import org.sheedon.notification.BroadcastActions;
import org.sheedon.notification.interfaces.PendingIntentNotification;

public class ClickPendingIntentActivity implements PendingIntentNotification {
    private final Class<?> mActivity;
    private final Bundle mBundle;
    private final int mIdentifier;

    public ClickPendingIntentActivity(Class<?> activity, Bundle bundle, int identifier) {
        this.mActivity = activity;
        this.mBundle = bundle;
        this.mIdentifier = identifier;
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    @Override
    public PendingIntent onSettingPendingIntent(Context context) {
        Intent clickIntentActivity = new Intent(context, mActivity);
        clickIntentActivity.setAction(BroadcastActions.ACTION_NOTIFICATION_CLICK_INTENT);
        clickIntentActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        clickIntentActivity.setPackage(context.getPackageName());

        if (mBundle != null) {
            clickIntentActivity.putExtras(mBundle);
        }
        return PendingIntent.getActivity(context, mIdentifier, clickIntentActivity,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
