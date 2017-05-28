package de.ivleafcloverapps.pillreminder.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import de.ivleafcloverapps.pillreminder.services.NotificationAlarmManager;

/**
 * Created by Christian on 26.05.2017.
 */

public class NotificationBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // receive the boot notification
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            NotificationAlarmManager.startAlarmManager(context, false);
        }
    }
}
