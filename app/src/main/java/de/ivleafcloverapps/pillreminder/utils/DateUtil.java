package de.ivleafcloverapps.pillreminder.utils;

import android.content.SharedPreferences;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import de.ivleafcloverapps.pillreminder.constants.DateFormatConstants;

/**
 * Created by Christian on 19.08.2017.
 */

public class DateUtil {

    private final SharedPreferences sharedPreferences;

    public DateUtil(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public Calendar getDateFromSharedPreferences(String field, String defaultValue) {
        try {
            String nextBreakBeginString = sharedPreferences.getString(field, defaultValue);
            Date nextBreakBeginDate = DateFormatConstants.DATE_FORMAT.parse(nextBreakBeginString);
            Calendar nextBreakDay = Calendar.getInstance();
            // I am sorry, but there is no good way to handle this problem without using deprecated methods or third party libraries
            nextBreakDay.set(Calendar.YEAR, nextBreakBeginDate.getYear() + 1900);
            nextBreakDay.set(Calendar.MONTH, nextBreakBeginDate.getMonth());
            nextBreakDay.set(Calendar.DAY_OF_MONTH, nextBreakBeginDate.getDate());
            return nextBreakDay;
        } catch (ParseException e) {
            // this should never happen
            return null;
        }
    }

    /**
     * sets hours, minutes, seconds and milliseconds of a calendar to 0
     *
     * @param currentNotificationDay
     */
    public void setHoursAndMinutesToZero(Calendar currentNotificationDay) {
        currentNotificationDay.set(Calendar.HOUR_OF_DAY, 0);
        currentNotificationDay.set(Calendar.MINUTE, 0);
        currentNotificationDay.set(Calendar.SECOND, 0);
        currentNotificationDay.set(Calendar.MILLISECOND, 0);
    }

}
