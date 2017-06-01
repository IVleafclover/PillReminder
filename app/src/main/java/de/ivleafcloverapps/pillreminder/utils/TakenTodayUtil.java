package de.ivleafcloverapps.pillreminder.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Calendar;

import de.ivleafcloverapps.pillreminder.constants.DateFormatConstants;
import de.ivleafcloverapps.pillreminder.constants.SharedPreferenceConstants;

/**
 * Created by Christian on 30.05.2017.
 *
 * a helper class to check if the pill is taken already today
 */

public class TakenTodayUtil {

    /**
     * checks if the current day is the same day as the last taken day, which is stored in the SharedPreferences
     *
     * @return
     */
    public static boolean checkIfIsTakenToday(Context context) {
        // load and set texts from saved preferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String lastTakenDay = sharedPreferences.getString(SharedPreferenceConstants.LAST_TAKEN_DAY, SharedPreferenceConstants.DEFAULT_LAST_TAKEN_DAY);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        String currentDay = DateFormatConstants.DATE_FORMAT.format(calendar.getTime());
        return currentDay.equals(lastTakenDay);
    }
}
