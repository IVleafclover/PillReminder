package de.ivleafcloverapps.pillreminder.fragments;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.util.Calendar;
import java.util.Date;

import de.ivleafcloverapps.pillreminder.R;
import de.ivleafcloverapps.pillreminder.constants.DateFormatConstants;
import de.ivleafcloverapps.pillreminder.constants.SharedPreferenceConstants;
import de.ivleafcloverapps.pillreminder.services.NotificationAlarmManager;
import de.ivleafcloverapps.pillreminder.utils.BreakUtil;
import de.ivleafcloverapps.pillreminder.utils.DateUtil;
import de.ivleafcloverapps.pillreminder.utils.TakenTodayUtil;

/**
 * Created by Christian on 11.05.2017.
 *
 * a fragment where the user checks if he has taken the pill
 */

public class CalendarFragment extends Fragment {

    private TextView info;
    private Button takePill;
    private boolean takenToday;
    private TextView infoYesterday;
    private Button takePillYesterday;
    private TextView infoYesterdayTaken;
    private CompactCalendarView compactCalendarView;
    private TextView labelCalendar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // initialize gui objects
        takePill = (Button) getView().findViewById(R.id.buttonTakePill);
        info = (TextView) getView().findViewById(R.id.labelInfo);
        takePillYesterday = (Button) getView().findViewById(R.id.buttonTakePillYesterday);
        infoYesterday = (TextView) getView().findViewById(R.id.labelInfoYesterday);
        infoYesterdayTaken = (TextView) getView().findViewById(R.id.labelInfoYesterdayTaken);
        labelCalendar = (TextView) getView().findViewById(R.id.labelCalendar);

        // check if pill is already taken today
        takenToday = TakenTodayUtil.checkIfIsTakenToday(getView().getContext());

        // set label and button label
        setTextOfButtonAndLabel();

        takePill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // change status of taken and label + button label
                toggleTakenStatus();
            }
        });

        // check if pill was not taken yesterday, when it was no break and set text and button if that is the case
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getView().getContext());
        if (!takenToday) {
            boolean isTakenYesterday = isTakenYesterday(sharedPreferences);
            if (!isTakenYesterday) {
                initializeYesterdayNotTakenElements(sharedPreferences);
            }
        }

        // set calendar highligts and label
        highlightCalendarDays(sharedPreferences);
        setCalendarListeners();
        compactCalendarView.shouldDrawIndicatorsBelowSelectedDays(true);
    }

    /**
     * initialize a listener that changes the calendar label, when the calendar month is changed
     */
    private void setCalendarListeners() {
        // define a listener to receive callbacks when certain events happen.
        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                // TODO add listener for day click
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                // change calendar label
                labelCalendar.setText(DateFormatConstants.CALENDAR_DESIGNATOR_FORMAT.format(firstDayOfNewMonth));
            }
        });
    }

    /**
     * highlights the actual period days in different colours <br />
     * green - days in the past <br />
     * red - days in the future <br />
     * blue - break days
     *
     * @param sharedPreferences the sharedPreferences
     */
    private void highlightCalendarDays(SharedPreferences sharedPreferences) {
        // load dates for the highlights
        DateUtil dateUtil = new DateUtil(sharedPreferences);
        Calendar lastReveueBegin = dateUtil.getDateFromSharedPreferences(SharedPreferenceConstants.LAST_REVENUE_BEGIN, SharedPreferenceConstants.DEFAULT_LAST_REVENUE_BEGIN);
        dateUtil.setHoursAndMinutesToZero(lastReveueBegin);
        Calendar nextBreakBegin = dateUtil.getDateFromSharedPreferences(SharedPreferenceConstants.NEXT_BREAK_BEGIN, SharedPreferenceConstants.DEFAULT_NEXT_BREAK_BEGIN);
        dateUtil.setHoursAndMinutesToZero(nextBreakBegin);
        Calendar nextReveueBegin = dateUtil.getDateFromSharedPreferences(SharedPreferenceConstants.NEXT_REVENUE_BEGIN, SharedPreferenceConstants.DEFAULT_NEXT_REVENUE_BEGIN);
        dateUtil.setHoursAndMinutesToZero(nextReveueBegin);
        Calendar today = Calendar.getInstance();
        dateUtil.setHoursAndMinutesToZero(today);

        // set label of calendar
        labelCalendar.setText(DateFormatConstants.CALENDAR_DESIGNATOR_FORMAT.format(today.getTime()));

        // highlight the dates
        compactCalendarView = (CompactCalendarView) getView().findViewById(R.id.compactCalendarView);
        while (lastReveueBegin.compareTo(today) < 0 && lastReveueBegin.compareTo(nextBreakBegin) < 0) {
            compactCalendarView.addEvent(new Event(Color.rgb(0, 128, 0), lastReveueBegin.getTimeInMillis()), false);
            lastReveueBegin.add(Calendar.DATE, 1);
        }
        while (today.compareTo(nextBreakBegin) < 0) {
            compactCalendarView.addEvent(new Event(Color.rgb(255, 0, 0), today.getTimeInMillis()), false);
            today.add(Calendar.DATE, 1);
        }
        while (nextBreakBegin.compareTo(nextReveueBegin) < 0) {
            compactCalendarView.addEvent(new Event(Color.rgb(0, 0, 255), nextBreakBegin.getTimeInMillis()), false);
            nextBreakBegin.add(Calendar.DATE, 1);
        }
    }

    /**
     * checks if pill was taken yesterday if it had to be taken yesterday
     *
     * @param sharedPreferences
     * @return
     */
    private boolean isTakenYesterday(SharedPreferences sharedPreferences) {
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DATE, -1);
        String lastTakenDay = sharedPreferences.getString(SharedPreferenceConstants.LAST_TAKEN_DAY, SharedPreferenceConstants.DEFAULT_LAST_TAKEN_DAY);

        // check if last taken day was yesterday
        if (!DateFormatConstants.DATE_FORMAT.format(yesterday.getTime()).equals(lastTakenDay)) {
            // check if yesterday was no break
            if (!new BreakUtil(sharedPreferences).isBreak(yesterday, true)) {
                return false;
            }
        }
        return true;
    }

    /**
     * initializes the hidden yesterday elements
     *
     * @param sharedPreferences
     */
    private void initializeYesterdayNotTakenElements(final SharedPreferences sharedPreferences) {
        // show text and button for taking yesterday
        takePillYesterday.setVisibility(View.VISIBLE);
        infoYesterday.setVisibility(View.VISIBLE);

        takePillYesterday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // change status of taken yesterday
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE, -1);
                String newLastTakenDay = DateFormatConstants.DATE_FORMAT.format(calendar.getTime());
                editor.putString(SharedPreferenceConstants.LAST_TAKEN_DAY, newLastTakenDay);
                editor.apply();

                // hide yesterday elements and show new message
                takePillYesterday.setVisibility(View.GONE);
                infoYesterday.setVisibility(View.GONE);
                infoYesterdayTaken.setVisibility(View.VISIBLE);

                // change AlarmManager
                NotificationAlarmManager.startAlarmManager(getView().getContext(), true);
            }
        });
    }

    /**
     * sets the text of the button and the label for the taken today status
     */
    private void setTextOfButtonAndLabel() {
        // check if it is a break
        if (new BreakUtil(PreferenceManager.getDefaultSharedPreferences(getView().getContext())).isBreak(Calendar.getInstance(), false)) {
            // if it is a break
            takePill.setText(R.string.take);
            takePill.setEnabled(false);
            info.setText(R.string.revenue_break);
            // check version again to not take deprecated methods
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                info.setTextColor(getResources().getColor(R.color.colorBlue, getView().getContext().getTheme()));
            } else {
                info.setTextColor(getResources().getColor(R.color.colorBlue));
            }
        } else {
            // if it is no break
            if (takenToday) {
                takePill.setText(R.string.take_not);
                info.setText(R.string.taken);
                // check version again to not take deprecated methods
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    info.setTextColor(getResources().getColor(R.color.colorGreen, getView().getContext().getTheme()));
                } else {
                    info.setTextColor(getResources().getColor(R.color.colorGreen));
                }
            } else {
                takePill.setText(R.string.take);
                info.setText(R.string.not_taken);
                // check version again to not take deprecated methods
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    info.setTextColor(getResources().getColor(R.color.colorRed, getView().getContext().getTheme()));
                } else {
                    info.setTextColor(getResources().getColor(R.color.colorRed));
                }
            }
        }
    }

    /**
     * changes the date when the pill was last taken
     */
    private void toggleTakenStatus() {
        // change status of taken the pill
        takenToday = !takenToday;

        // change text of button and label
        setTextOfButtonAndLabel();

        // edit lastTakenDay in SharedPreferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getView().getContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Calendar calendar = Calendar.getInstance();
        // TODO is this necessary?
        //calendar.setTimeInMillis(System.currentTimeMillis());
        String newLastTakenDay;
        if (takenToday) {
            newLastTakenDay = DateFormatConstants.DATE_FORMAT.format(calendar.getTime());
        } else {
            // if it is not longer taken today, set the lastTakenToday to yesterday
            calendar.add(Calendar.DATE, -1);
            newLastTakenDay = DateFormatConstants.DATE_FORMAT.format(calendar.getTime());
        }
        editor.putString(SharedPreferenceConstants.LAST_TAKEN_DAY, newLastTakenDay);
        editor.apply();

        // change AlarmManager
        NotificationAlarmManager.startAlarmManager(getView().getContext(), true);

        // if these elements are visible, they have to be hidden after that action
        takePillYesterday.setVisibility(View.GONE);
        infoYesterday.setVisibility(View.GONE);
    }
}
