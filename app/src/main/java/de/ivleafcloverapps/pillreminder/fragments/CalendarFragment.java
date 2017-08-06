package de.ivleafcloverapps.pillreminder.fragments;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;

import de.ivleafcloverapps.pillreminder.R;
import de.ivleafcloverapps.pillreminder.constants.DateFormatConstants;
import de.ivleafcloverapps.pillreminder.constants.SharedPreferenceConstants;
import de.ivleafcloverapps.pillreminder.services.NotificationAlarmManager;
import de.ivleafcloverapps.pillreminder.utils.BreakUtil;
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
        if (!takenToday) {
            final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getView().getContext());
            boolean isTakenYesterday = isTakenYesterday(sharedPreferences);
            if (!isTakenYesterday) {
                initializeYesterdayNotTakenElements(sharedPreferences);
            }
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
