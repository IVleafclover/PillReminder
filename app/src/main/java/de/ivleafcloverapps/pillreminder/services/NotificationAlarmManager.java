package de.ivleafcloverapps.pillreminder.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by Christian on 26.05.2017.
 */

public class NotificationAlarmManager {

    public static void startAlarmManager(Context context) {
        // check if AlarmManager is already running
        if (isAlarmManagerRunning(context)) {
            // when yes, then do not start a new one
            Toast.makeText(context, "It is running", Toast.LENGTH_SHORT).show();
            return;
        }

        // create AlarmManager
        Toast.makeText(context, "It is not running", Toast.LENGTH_SHORT).show();
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        Intent startNotificationServiceIntent = new Intent(context, NotificationService.class);
        PendingIntent startNotificationServicePendingIntent = PendingIntent.getService(context, 0, startNotificationServiceIntent, 0);

        // TODO
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000 * 60 * 1, startNotificationServicePendingIntent);
    }

    public static void updateAlarmManager() {
        // TODO
    }

    private static PendingIntent getAlarmManager(Context context) {
        Intent startNotificationServiceIntent = new Intent(context, NotificationService.class);
        return PendingIntent.getService(context, 0, startNotificationServiceIntent, PendingIntent.FLAG_NO_CREATE);
    }

    private static boolean isAlarmManagerRunning(Context context) {
        return getAlarmManager(context) != null;
    }
}
