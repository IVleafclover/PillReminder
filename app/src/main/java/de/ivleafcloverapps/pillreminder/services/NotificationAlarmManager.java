package de.ivleafcloverapps.pillreminder.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import de.ivleafcloverapps.pillreminder.constants.DateFormatConstants;
import de.ivleafcloverapps.pillreminder.constants.SharedPreferenceConstants;

/**
 * Created by Christian on 26.05.2017.
 */

public class NotificationAlarmManager {

    public static void startAlarmManager(Context context, boolean isUpdate) {
        startAlarmManager(context, isUpdate, false);
    }

    public static void startAlarmManager(Context context, boolean isUpdate, boolean isFirstStartTomorrow) {
        // check if AlarmManager is already running but not when Settings were changed and the AlarmManager has to be updated
        if (!isUpdate && isAlarmManagerRunning(context)) {
            // when yes, then do not start a new one
            return;
        }

        // create AlarmManager
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        Intent startNotificationServiceIntent = new Intent(context, NotificationService.class);
        PendingIntent startNotificationServicePendingIntent = PendingIntent.getService(context, 0, startNotificationServiceIntent, 0);

        // TODO check if it is a break + check if it is next day
        // set time and intervall by loading from SharedPreferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        //periodBegin.setText(sharedPreferences.getString(SharedPreferenceConstants.PERIOD_BEGIN, ""));
        String notificationTime = sharedPreferences.getString(SharedPreferenceConstants.NOTIFICATION_TIME, SharedPreferenceConstants.DEFAULT_NOTIFICATION_TIME);
        String notificationPeriod = sharedPreferences.getString(SharedPreferenceConstants.NOTIFICATION_PERIOD, SharedPreferenceConstants.DEFAULT_NOTIFICATION_PERIOD);
        //periodType.setSelection(sharedPreferences.getInt(SharedPreferenceConstants.PERIOD_TYPE, 0));

        try {
            Date notificationTimeDate = DateFormatConstants.TIME_FORMAT.parse(notificationTime);
            java.util.Calendar calendarNotificationTime = java.util.Calendar.getInstance();
            calendarNotificationTime.setTimeInMillis(System.currentTimeMillis());
            // I am sorry, but there is no good way to handle this problem without using deprecated methods or third party libraries
            calendarNotificationTime.set(java.util.Calendar.HOUR_OF_DAY, notificationTimeDate.getHours());
            calendarNotificationTime.set(java.util.Calendar.MINUTE, notificationTimeDate.getMinutes());
            // if it should start the next day, then add 1 day
            if (isFirstStartTomorrow) {
                calendarNotificationTime.add(Calendar.DATE, 1);
            }

            java.util.Calendar calendarNotificationPeriod = java.util.Calendar.getInstance();
            calendarNotificationPeriod.setTimeInMillis(System.currentTimeMillis());
            calendarNotificationPeriod.setTime(DateFormatConstants.TIME_FORMAT.parse(notificationPeriod));

            int notificationPeriodInMilliSeconds = (calendarNotificationPeriod.get(java.util.Calendar.HOUR_OF_DAY) * 60 + calendarNotificationPeriod.get(java.util.Calendar.MINUTE)) * 1000 * 60;
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendarNotificationTime.getTimeInMillis(), notificationPeriodInMilliSeconds, startNotificationServicePendingIntent);
        } catch (ParseException e) {
            // this sould never happen
        }
    }

    public static void updateAlarmManager(Context context) {
        /*// TODO
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        PendingIntent startNotificationServicePendingIntent = getAlarmManager(context);
        // TODO check if it is a break and load period informations
        // set time and intervall by loading from SharedPreferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        //periodBegin.setText(sharedPreferences.getString(SharedPreferenceConstants.PERIOD_BEGIN, ""));
        String notificationTime = sharedPreferences.getString(SharedPreferenceConstants.NOTIFICATION_TIME, "00:00");
        String notificationPeriod = sharedPreferences.getString(SharedPreferenceConstants.NOTIFICATION_PERIOD, "00:10");
        //periodType.setSelection(sharedPreferences.getInt(SharedPreferenceConstants.PERIOD_TYPE, 0));

        try {
            java.util.Calendar calendarNotificationTime = java.util.Calendar.getInstance();
            calendarNotificationTime.setTime(DateFormatConstants.TIME_FORMAT.parse(notificationTime));
            java.util.Calendar calendarNotificationPeriod = java.util.Calendar.getInstance();
            calendarNotificationPeriod.setTime(DateFormatConstants.TIME_FORMAT.parse(notificationPeriod));
            int notificationPeriodInMilliSeconds = (calendarNotificationTime.get(java.util.Calendar.HOUR_OF_DAY) * 60 + calendarNotificationTime.get(java.util.Calendar.MINUTE)) * 1000 * 60;
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendarNotificationTime.getTimeInMillis(), notificationPeriodInMilliSeconds, startNotificationServicePendingIntent);
        } catch (ParseException e) {
            // this sould never happen
        }*/
    }

    private static PendingIntent getAlarmManager(Context context) {
        Intent startNotificationServiceIntent = new Intent(context, NotificationService.class);
        return PendingIntent.getService(context, 0, startNotificationServiceIntent, PendingIntent.FLAG_NO_CREATE);
    }

    private static boolean isAlarmManagerRunning(Context context) {
        return getAlarmManager(context) != null;
    }
}
