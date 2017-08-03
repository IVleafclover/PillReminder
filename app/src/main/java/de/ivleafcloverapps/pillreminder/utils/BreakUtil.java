package de.ivleafcloverapps.pillreminder.utils;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import de.ivleafcloverapps.pillreminder.constants.DateFormatConstants;
import de.ivleafcloverapps.pillreminder.constants.SharedPreferenceConstants;

/**
 * Created by Christian on 24.06.2017.
 */

public class BreakUtil {

    private SharedPreferences sharedPreferences;

    public BreakUtil(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public boolean isBreak(Calendar notificationDay, boolean isYesterdayCheck) {
        // set notification time to 0 hours and 0 minutes
        Calendar currentNotificationDay = (Calendar) notificationDay.clone();
        setHoursAndMinutesToZero(currentNotificationDay);

        Calendar nextBreakDay = getDateFromSharedPreferencesWithoutHoursAndMinutes(SharedPreferenceConstants.NEXT_BREAK_BEGIN, SharedPreferenceConstants.DEFAULT_NEXT_BREAK_BEGIN);

        // if it is yesterday check, than compare if yesterday was before the actual revenue begin day
        if (isYesterdayCheck) {
            Calendar lastRevenueBeginDay = getDateFromSharedPreferencesWithoutHoursAndMinutes(SharedPreferenceConstants.LAST_REVENUE_BEGIN, SharedPreferenceConstants.DEFAULT_LAST_REVENUE_BEGIN);
            if (currentNotificationDay.compareTo(lastRevenueBeginDay) < 0) {
                return true;
            }
        }

        if (currentNotificationDay.compareTo(nextBreakDay) < 0) {
            return false;
        } else {
            Calendar nextRevenueDay = getDateFromSharedPreferencesWithoutHoursAndMinutes(SharedPreferenceConstants.NEXT_REVENUE_BEGIN, SharedPreferenceConstants.DEFAULT_NEXT_REVENUE_BEGIN);
            if (currentNotificationDay.compareTo(nextRevenueDay) < 0) {
                return true;
            } else {
                // recalculate next break and next revenue day
                recalculateNextDays();
                return false;
            }
        }
    }

    public Calendar getNextDayAfterBreak() {
        return getDateFromSharedPreferences(SharedPreferenceConstants.NEXT_REVENUE_BEGIN, SharedPreferenceConstants.DEFAULT_NEXT_REVENUE_BEGIN);
    }

    public void calculateNextDays() {
        Calendar today = Calendar.getInstance();
        setHoursAndMinutesToZero(today);

        Calendar revenueBegin = getDateFromSharedPreferencesWithoutHoursAndMinutes(SharedPreferenceConstants.REVENUE_BEGIN, SharedPreferenceConstants.DEFAULT_REVENUE_BEGIN);
        int revenueDays = sharedPreferences.getInt(SharedPreferenceConstants.REVENUE_DAYS, SharedPreferenceConstants.DEFAULT_REVENUE_DAYS);
        int breakDays = sharedPreferences.getInt(SharedPreferenceConstants.BREAK_DAYS, SharedPreferenceConstants.DEFAULT_BREAK_DAYS);

        // be sure the date is not to far in the past, so we get the last revenue begin date before today
        while (revenueBegin.compareTo(today) < 0) {
            revenueBegin.add(Calendar.DATE, revenueDays + breakDays);
        }

        // be sure the date is not in the future, so we get the last revenue begin date before today
        while (revenueBegin.compareTo(today) > 0) {
            revenueBegin.add(Calendar.DATE, 0 - revenueDays - breakDays);
        }

        // calculate next break and revenue day and save the dates
        SharedPreferences.Editor editor = sharedPreferences.edit();
        revenueBegin.add(Calendar.DATE, revenueDays);
        editor.putString(SharedPreferenceConstants.NEXT_BREAK_BEGIN, DateFormatConstants.DATE_FORMAT.format(revenueBegin.getTime()));
        revenueBegin.add(Calendar.DATE, breakDays);
        editor.putString(SharedPreferenceConstants.NEXT_REVENUE_BEGIN, DateFormatConstants.DATE_FORMAT.format(revenueBegin.getTime()));
        editor.apply();
    }

    private void recalculateNextDays() {
        Calendar nextBreakBegin = getDateFromSharedPreferences(SharedPreferenceConstants.NEXT_BREAK_BEGIN, SharedPreferenceConstants.DEFAULT_NEXT_BREAK_BEGIN);
        Calendar nextRevenueBegin = getDateFromSharedPreferences(SharedPreferenceConstants.NEXT_REVENUE_BEGIN, SharedPreferenceConstants.DEFAULT_NEXT_REVENUE_BEGIN);
        int revenueDays = sharedPreferences.getInt(SharedPreferenceConstants.REVENUE_DAYS, SharedPreferenceConstants.DEFAULT_REVENUE_DAYS);
        int breakDays = sharedPreferences.getInt(SharedPreferenceConstants.BREAK_DAYS, SharedPreferenceConstants.DEFAULT_BREAK_DAYS);

        // set both dates to the next period
        nextBreakBegin.add(Calendar.DATE, revenueDays + breakDays);
        nextRevenueBegin.add(Calendar.DATE, revenueDays + breakDays);

        // save the changed dates
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // set next revenue begin to last revenue begin, before overwriting it
        editor.putString(SharedPreferenceConstants.LAST_REVENUE_BEGIN, sharedPreferences.getString(SharedPreferenceConstants.NEXT_REVENUE_BEGIN, SharedPreferenceConstants.DEFAULT_NEXT_REVENUE_BEGIN));
        editor.putString(SharedPreferenceConstants.NEXT_BREAK_BEGIN, DateFormatConstants.DATE_FORMAT.format(nextBreakBegin.getTime()));
        editor.putString(SharedPreferenceConstants.NEXT_REVENUE_BEGIN, DateFormatConstants.DATE_FORMAT.format(nextRevenueBegin.getTime()));
        editor.apply();
    }

    @NonNull
    private Calendar getDateFromSharedPreferencesWithoutHoursAndMinutes(String field, String defaultValue) {
        Calendar nextBreakDay = getDateFromSharedPreferences(field, defaultValue);
        setHoursAndMinutesToZero(nextBreakDay);
        return nextBreakDay;
    }

    @NonNull
    private Calendar getDateFromSharedPreferences(String field, String defaultValue) {
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

    private void setHoursAndMinutesToZero(Calendar currentNotificationDay) {
        currentNotificationDay.set(Calendar.HOUR_OF_DAY, 0);
        currentNotificationDay.set(Calendar.MINUTE, 0);
        currentNotificationDay.set(Calendar.SECOND, 0);
        currentNotificationDay.set(Calendar.MILLISECOND, 0);
    }
}
