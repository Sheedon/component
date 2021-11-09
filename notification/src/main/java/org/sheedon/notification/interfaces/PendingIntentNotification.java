package org.sheedon.notification.interfaces;

import android.app.PendingIntent;
import android.content.Context;

public interface PendingIntentNotification {

    PendingIntent onSettingPendingIntent(Context context);

}
