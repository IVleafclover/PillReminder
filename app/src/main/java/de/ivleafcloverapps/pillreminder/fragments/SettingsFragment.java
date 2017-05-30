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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import de.ivleafcloverapps.pillreminder.R;
import de.ivleafcloverapps.pillreminder.constants.DateFormatConstants;
import de.ivleafcloverapps.pillreminder.constants.SharedPreferenceConstants;
import de.ivleafcloverapps.pillreminder.dialogs.ISpinnerDatePickerDialogListener;
import de.ivleafcloverapps.pillreminder.dialogs.ISpinnerTimePickerDialogListener;
import de.ivleafcloverapps.pillreminder.dialogs.SpinnerDatePickerDialog;
import de.ivleafcloverapps.pillreminder.dialogs.SpinnerTimePickerDialog;
import de.ivleafcloverapps.pillreminder.services.NotificationAlarmManager;

/**
 * Created by Christian on 11.05.2017.
 */

public class SettingsFragment extends Fragment implements ISpinnerDatePickerDialogListener, ISpinnerTimePickerDialogListener {

    // ids of the dialogs
    private final int PERIOD_BEGIN_DIALOG_ID = 0;
    private final int NOTIFICATION_TIME_DIALOG_ID = 1;
    private final int NOTIFICATION_PERIOD_DIALOG_ID = 2;
    // gui elements
    private EditText periodBegin;
    private EditText notificationTime;
    private EditText notificationPeriod;
    private Spinner periodType;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // initialize listeners for text inputs, to open picker dialogs
        periodBegin = (EditText) getView().findViewById(R.id.editPeriodBegin);
        periodBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(PERIOD_BEGIN_DIALOG_ID);
            }
        });

        notificationTime = (EditText) getView().findViewById(R.id.editNotificationTime);
        notificationTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(NOTIFICATION_TIME_DIALOG_ID);
            }
        });

        notificationPeriod = (EditText) getView().findViewById(R.id.editNotificationPeriod);
        notificationPeriod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(NOTIFICATION_PERIOD_DIALOG_ID);
            }
        });

        // initialize spinner
        periodType = (Spinner) getView().findViewById(R.id.spinnerPeriodType);

        // load and set inputs from saved preferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getView().getContext());
        periodBegin.setText(sharedPreferences.getString(SharedPreferenceConstants.PERIOD_BEGIN, SharedPreferenceConstants.DEFAULT_PERIOD_BEGIN));
        notificationTime.setText(sharedPreferences.getString(SharedPreferenceConstants.NOTIFICATION_TIME, SharedPreferenceConstants.DEFAULT_NOTIFICATION_TIME));
        notificationPeriod.setText(sharedPreferences.getString(SharedPreferenceConstants.NOTIFICATION_PERIOD, SharedPreferenceConstants.DEFAULT_NOTIFICATION_PERIOD));
        periodType.setSelection(sharedPreferences.getInt(SharedPreferenceConstants.PERIOD_TYPE, SharedPreferenceConstants.DEFAULT_PERIOD_TYPE));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // save every fragment_settings made in sharedPreferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getView().getContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        try {
            editor.putString(SharedPreferenceConstants.PERIOD_BEGIN, periodBegin.getText().toString());
        } catch (NullPointerException e) {
            // Nothing to do here
        }
        try {
            editor.putString(SharedPreferenceConstants.NOTIFICATION_TIME, notificationTime.getText().toString());
        } catch (NullPointerException e) {
            // Nothing to do here
        }
        try {
            editor.putString(SharedPreferenceConstants.NOTIFICATION_PERIOD, notificationPeriod.getText().toString());
        } catch (NullPointerException e) {
            // Nothing to do here
        }
        try {
            editor.putInt(SharedPreferenceConstants.PERIOD_TYPE, periodType.getSelectedItemPosition());
        } catch (NullPointerException e) {
            // Nothing to do here
        }
        editor.apply();

        // update the notification timer
        NotificationAlarmManager.startAlarmManager(getView().getContext(), true);
    }

    /**
     * opens dialog for id
     *
     * @param id
     */
    private void showDialog(int id) {
        switch (id) {
            case PERIOD_BEGIN_DIALOG_ID:
                SpinnerDatePickerDialog datePickerDialog;
                try {
                    Date periodBeginDate = DateFormatConstants.DATE_FORMAT.parse(periodBegin.getText().toString());
                    java.util.Calendar calendar = java.util.Calendar.getInstance();
                    calendar.setTime(periodBeginDate);
                    datePickerDialog = new SpinnerDatePickerDialog(this, calendar);
                } catch (ParseException e) {
                    datePickerDialog = new SpinnerDatePickerDialog(this);
                }
                datePickerDialog.show(getFragmentManager(), datePickerDialog.getTAG());
                break;
            case NOTIFICATION_TIME_DIALOG_ID:
                SpinnerTimePickerDialog timePickerDialog;
                try {
                    Date notificationTimeDate = DateFormatConstants.TIME_FORMAT.parse(notificationTime.getText().toString());
                    java.util.Calendar calendar = java.util.Calendar.getInstance();
                    calendar.setTime(notificationTimeDate);
                    timePickerDialog = new SpinnerTimePickerDialog(this, NOTIFICATION_TIME_DIALOG_ID, calendar);
                } catch (ArrayIndexOutOfBoundsException | NumberFormatException | ParseException e) {
                    timePickerDialog = new SpinnerTimePickerDialog(this, NOTIFICATION_TIME_DIALOG_ID);
                }
                timePickerDialog.show(getFragmentManager(), timePickerDialog.getTAG());
                break;
            case NOTIFICATION_PERIOD_DIALOG_ID:
                SpinnerTimePickerDialog timePickerDialog2;
                try {
                    Date notificationPeriodDate = DateFormatConstants.TIME_FORMAT.parse(notificationPeriod.getText().toString());
                    java.util.Calendar calendar = java.util.Calendar.getInstance();
                    calendar.setTime(notificationPeriodDate);
                    timePickerDialog2 = new SpinnerTimePickerDialog(this, NOTIFICATION_PERIOD_DIALOG_ID, calendar);
                } catch (ArrayIndexOutOfBoundsException | NumberFormatException | ParseException e) {
                    timePickerDialog2 = new SpinnerTimePickerDialog(this, NOTIFICATION_PERIOD_DIALOG_ID);
                }
                timePickerDialog2.show(getFragmentManager(), timePickerDialog2.getTAG());
                break;
        }
    }

    @Override
    public void onSpinnerDateDialogPositiveClick(SpinnerDatePickerDialog dialog) {
        // TODO send DatePicker values with listener
        // load DatePicker from dialog and set them to EditText text
        DatePicker datePicker = dialog.getDatePicker();
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();

        // TODO
        String formattedDate = DateFormatConstants.DATE_FORMAT.format(new Date(year - 1900, month, day));
        periodBegin.setText(formattedDate);
    }

    @Override
    public void onSpinnerDateDialogNegativeClick(SpinnerDatePickerDialog dialog) {
        // Nothing to do here
    }

    @Override
    public void onSpinnerTimeDialogPositiveClick(SpinnerTimePickerDialog dialog) {
        // TODO send TimePicker values with listener
        // load TimePicker from dialog and set them to EditText text
        TimePicker timePicker = dialog.getTimePicker();
        int hour;
        int minute;
        // check version to not use deprecated methods in supported versions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            hour = timePicker.getHour();
            minute = timePicker.getMinute();
        } else {
            hour = timePicker.getCurrentHour();
            minute = timePicker.getCurrentMinute();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        String formattedTime = DateFormatConstants.TIME_FORMAT.format(calendar.getTime());

        switch (dialog.getDialogId()) {
            case NOTIFICATION_TIME_DIALOG_ID:
                notificationTime.setText(formattedTime);
                break;
            case NOTIFICATION_PERIOD_DIALOG_ID:
                notificationPeriod.setText(formattedTime);
                break;
            default:
                // this should never happen
                break;
        }
    }

    @Override
    public void onSpinnerTimeDialogNegativeClick(SpinnerTimePickerDialog dialog) {
        // Nothing to do here
    }
}
