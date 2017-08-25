package de.ivleafcloverapps.pillreminder.utils;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import java.util.Calendar;

import de.ivleafcloverapps.pillreminder.constants.DateFormatConstants;
import de.ivleafcloverapps.pillreminder.constants.SharedPreferenceConstants;

/**
 * Created by Christian on 24.06.2017.
 *
 * a util to check if a day was in a break and to recalculate the break period
 */

public class BreakUtil {

    private final SharedPreferences sharedPreferences;
    private DateUtil dateUtil;

    public BreakUtil(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
        this.dateUtil = new DateUtil(sharedPreferences);
        ;
    }

    /**
     * checks if the day is or was in a break
     *
     * @param notificationDay  the day to check
     * @param isYesterdayCheck when it is checked for yesterday, then we have to check if yesterday was in the old break
     * @return is or was the day a break
     */
    public boolean isBreak(Calendar notificationDay, boolean isYesterdayCheck) {
        // set notification time to 0 hours and 0 minutes
        Calendar currentNotificationDay = (Calendar) notificationDay.clone();
        dateUtil.setHoursAndMinutesToZero(currentNotificationDay);

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

    /**
     * loads next revenue day
     * @return next day revenue day
     */
    public Calendar getNextDayAfterBreak() {
        return dateUtil.getDateFromSharedPreferences(SharedPreferenceConstants.NEXT_REVENUE_BEGIN, SharedPreferenceConstants.DEFAULT_NEXT_REVENUE_BEGIN);
    }

    /**
     * calculates the next revenue begin and break begin day and sets the last revenue begin day to the last revenue begin attribute in the shared preferences
     */
    public void calculateNextDays() {
        Calendar today = Calendar.getInstance();
        dateUtil.setHoursAndMinutesToZero(today);

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
        editor.putString(SharedPreferenceConstants.LAST_REVENUE_BEGIN, DateFormatConstants.DATE_FORMAT.format(revenueBegin.getTime()));
        revenueBegin.add(Calendar.DATE, revenueDays);
        editor.putString(SharedPreferenceConstants.NEXT_BREAK_BEGIN, DateFormatConstants.DATE_FORMAT.format(revenueBegin.getTime()));
        revenueBegin.add(Calendar.DATE, breakDays);
        editor.putString(SharedPreferenceConstants.NEXT_REVENUE_BEGIN, DateFormatConstants.DATE_FORMAT.format(revenueBegin.getTime()));
        editor.apply();
    }

    /**
     * recalculates next break and revenue begin day, when both revenue and break begin day are in the past and sets the last revenue begin day to the last revenue begin attribute in the shared preferences
     */
    private void recalculateNextDays() {
        Calendar nextBreakBegin = dateUtil.getDateFromSharedPreferences(SharedPreferenceConstants.NEXT_BREAK_BEGIN, SharedPreferenceConstants.DEFAULT_NEXT_BREAK_BEGIN);
        Calendar nextRevenueBegin = dateUtil.getDateFromSharedPreferences(SharedPreferenceConstants.NEXT_REVENUE_BEGIN, SharedPreferenceConstants.DEFAULT_NEXT_REVENUE_BEGIN);
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
        Calendar nextBreakDay = dateUtil.getDateFromSharedPreferences(field, defaultValue);
        dateUtil.setHoursAndMinutesToZero(nextBreakDay);
        return nextBreakDay;
    }
}
