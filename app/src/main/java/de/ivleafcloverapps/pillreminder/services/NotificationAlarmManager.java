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
import de.ivleafcloverapps.pillreminder.utils.BreakUtil;
import de.ivleafcloverapps.pillreminder.utils.TakenTodayUtil;

/**
 * Created by Christian on 26.05.2017.
 */

public class NotificationAlarmManager {

    /**
     * starts or updates a AlarmManager
     *
     * @param context
     * @param isUpdate
     */
    public static void startAlarmManager(Context context, boolean isUpdate) {
        // check if AlarmManager is already running but not when Settings were changed and the AlarmManager has to be updated
        if (!isUpdate && isAlarmManagerRunning(context)) {
            // when yes, then do not start a new one
            return;
        }

        // create AlarmManager
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        Intent startNotificationServiceIntent = new Intent(context, NotificationService.class);
        PendingIntent startNotificationServicePendingIntent = PendingIntent.getService(context, 0, startNotificationServiceIntent, 0);

        // set time and intervall by loading from SharedPreferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String notificationTime = sharedPreferences.getString(SharedPreferenceConstants.NOTIFICATION_TIME, SharedPreferenceConstants.DEFAULT_NOTIFICATION_TIME);
        String notificationPeriod = sharedPreferences.getString(SharedPreferenceConstants.NOTIFICATION_PERIOD, SharedPreferenceConstants.DEFAULT_NOTIFICATION_PERIOD);

        try {
            Date notificationTimeDate = DateFormatConstants.TIME_FORMAT.parse(notificationTime);
            java.util.Calendar calendarNotificationTime = java.util.Calendar.getInstance();
            // TODO is this necessary?
            calendarNotificationTime.setTimeInMillis(System.currentTimeMillis());
            // if it should start the next day, then add 1 day
            if (TakenTodayUtil.checkIfIsTakenToday(context)) {
                calendarNotificationTime.add(Calendar.DATE, 1);
            }

            // check if it is a break
            BreakUtil breakUtil = new BreakUtil(sharedPreferences);
            if (breakUtil.isBreak(calendarNotificationTime)) {
                // set to next notification date after break
                calendarNotificationTime = breakUtil.getNextDayAfterBreak();
            }

            // I am sorry, but there is no good way to handle this problem without using deprecated methods or third party libraries
            calendarNotificationTime.set(java.util.Calendar.HOUR_OF_DAY, notificationTimeDate.getHours());
            calendarNotificationTime.set(java.util.Calendar.MINUTE, notificationTimeDate.getMinutes());
            calendarNotificationTime.set(Calendar.SECOND, 0);
            calendarNotificationTime.set(Calendar.MILLISECOND, 0);

            java.util.Calendar calendarNotificationPeriod = java.util.Calendar.getInstance();
            // TODO is this necessary?
            calendarNotificationPeriod.setTimeInMillis(System.currentTimeMillis());
            calendarNotificationPeriod.setTime(DateFormatConstants.TIME_FORMAT.parse(notificationPeriod));

            int notificationPeriodInMilliSeconds = (calendarNotificationPeriod.get(java.util.Calendar.HOUR_OF_DAY) * 60 + calendarNotificationPeriod.get(java.util.Calendar.MINUTE)) * 1000 * 60;
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendarNotificationTime.getTimeInMillis(), notificationPeriodInMilliSeconds, startNotificationServicePendingIntent);
        } catch (ParseException e) {
            // this sould never happen
        }
    }

    /**
     * gets the AlarmManager Service Intent if it is already running
     *
     * @param context
     * @return
     */
    private static PendingIntent getAlarmManager(Context context) {
        Intent startNotificationServiceIntent = new Intent(context, NotificationService.class);
        return PendingIntent.getService(context, 0, startNotificationServiceIntent, PendingIntent.FLAG_NO_CREATE);
    }

    /**
     * checks if the notification service is already runnign
     *
     * @param context
     * @return
     */
    private static boolean isAlarmManagerRunning(Context context) {
        return getAlarmManager(context) != null;
    }
}
