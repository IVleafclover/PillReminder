package de.ivleafcloverapps.pillreminder.fragments;

import android.app.Fragment;
import android.content.SharedPreferences;
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
import java.text.SimpleDateFormat;
import java.util.Date;

import de.ivleafcloverapps.pillreminder.R;
import de.ivleafcloverapps.pillreminder.dialogs.ISpinnerDatePickerDialogListener;
import de.ivleafcloverapps.pillreminder.dialogs.ISpinnerTimePickerDialogListener;
import de.ivleafcloverapps.pillreminder.dialogs.SpinnerDatePickerDialog;
import de.ivleafcloverapps.pillreminder.dialogs.SpinnerTimePickerDialog;

/**
 * Created by Christian on 11.05.2017.
 */

public class SettingsFragment extends Fragment implements ISpinnerDatePickerDialogListener, ISpinnerTimePickerDialogListener {

    // ids of the dialogs
    private final int PERIOD_BEGIN_DIALOG_ID = 0;
    private final int NOTIFICATION_TIME_DIALOG_ID = 1;
    private final int NOTIFICATION_PERIOD_DIALOG_ID = 2;
    private final SimpleDateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy");
    // gui elements
    EditText periodBegin;
    EditText notificationTime;
    EditText notificationPeriod;
    Spinner periodType;

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
        periodBegin.setText(sharedPreferences.getString("periodBegin", ""));
        notificationTime.setText(sharedPreferences.getString("notificationTime", ""));
        notificationPeriod.setText(sharedPreferences.getString("notificationPeriod", ""));
        periodType.setSelection(sharedPreferences.getInt("periodType", 0));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // TODO save every fragment_settings made in sharedPreferences

        //periodBegin = (EditText) getView().findViewById(R.id.editPeriodBegin);
        //notificationTime = (EditText) getView().findViewById(R.id.editNotificationTime);
        //notificationPeriod = (EditText) getView().findViewById(R.id.editNotificationPeriod);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getView().getContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        try {
            editor.putString("periodBegin", periodBegin.getText().toString());
        } catch (NullPointerException e) {
            // Nothing to do here
        }
        try {
            editor.putString("notificationTime", notificationTime.getText().toString());
        } catch (NullPointerException e) {
            // Nothing to do here
        }
        try {
            editor.putString("notificationPeriod", notificationPeriod.getText().toString());
        } catch (NullPointerException e) {
            // Nothing to do here
        }
        try {
            editor.putInt("periodType", periodType.getSelectedItemPosition());
        } catch (NullPointerException e) {
            // Nothing to do here
        }
        editor.apply();
    }

    /**
     * opens dialog for id
     *
     * @param id
     */
    private void showDialog(int id) {
        switch(id) {
            case PERIOD_BEGIN_DIALOG_ID:
                SpinnerDatePickerDialog datePickerDialog;
                try {
                    Date periodBeginDate = FORMAT.parse(periodBegin.getText().toString());
                    // TODO
                    datePickerDialog = new SpinnerDatePickerDialog(this, periodBeginDate.getYear() + 1900, periodBeginDate.getMonth(), periodBeginDate.getDate());
                } catch (ParseException e) {
                    datePickerDialog = new SpinnerDatePickerDialog(this);
                }
                datePickerDialog.show(getFragmentManager(), datePickerDialog.getTAG());
                break;
            case NOTIFICATION_TIME_DIALOG_ID:
                SpinnerTimePickerDialog timePickerDialog;
                try {
                    // TODO
                    String[] timeParts = notificationTime.getText().toString().split(":");
                    timePickerDialog = new SpinnerTimePickerDialog(this, NOTIFICATION_TIME_DIALOG_ID, Integer.parseInt(timeParts[0]), Integer.parseInt(timeParts[1]));
                } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
                    timePickerDialog = new SpinnerTimePickerDialog(this, NOTIFICATION_TIME_DIALOG_ID);
                }
                timePickerDialog.show(getFragmentManager(), timePickerDialog.getTAG());
                break;
            case NOTIFICATION_PERIOD_DIALOG_ID:
                SpinnerTimePickerDialog timePickerDialog2;
                try {
                    // TODO
                    String[] timeParts = notificationPeriod.getText().toString().split(":");
                    timePickerDialog2 = new SpinnerTimePickerDialog(this, NOTIFICATION_PERIOD_DIALOG_ID, Integer.parseInt(timeParts[0]), Integer.parseInt(timeParts[1]));
                } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
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
        String formattedDate = FORMAT.format(new Date(year - 1900, month, day));
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
        int hour = timePicker.getCurrentHour();
        int minute = timePicker.getCurrentMinute();

        // TODO
        switch (dialog.getDialogId()) {
            case NOTIFICATION_TIME_DIALOG_ID:
                notificationTime.setText(hour + ":" + minute);
                break;
            case NOTIFICATION_PERIOD_DIALOG_ID:
                notificationPeriod.setText(hour + ":" + minute);
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
