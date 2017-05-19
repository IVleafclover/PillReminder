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

import java.util.Date;

import de.ivleafcloverapps.pillreminder.R;
import de.ivleafcloverapps.pillreminder.dialogs.ISpinnerDatePickerDialogListener;
import de.ivleafcloverapps.pillreminder.dialogs.SpinnerDatePickerDialog;

/**
 * Created by Christian on 11.05.2017.
 */

public class SettingsFragment extends Fragment implements ISpinnerDatePickerDialogListener {

    // ids of the dialogs
    private final int PERIOD_BEGIN_DIALOG_ID = 0;
    private final int NOTIFICATION_TIME_DIALOG_ID = 1;
    private final int NOTIFICATION_PERIOD_DIALOG_ID = 2;

    // gui elements
    EditText periodBegin;
    EditText notificationTime;
    EditText notificationPeriod;

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

        // load and set inputs from saved preferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getView().getContext());
        periodBegin.setText(sharedPreferences.getString("periodBegin", ""));
        notificationTime.setText(sharedPreferences.getString("notificationTime", ""));
        notificationPeriod.setText(sharedPreferences.getString("notificationPeriod", ""));
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
                SpinnerDatePickerDialog datePickerDialog = new SpinnerDatePickerDialog(this);
                datePickerDialog.show(getFragmentManager(), datePickerDialog.getTAG());
                break;
            case NOTIFICATION_TIME_DIALOG_ID:
                //TimePickerDialogWithId dialog = new TimePickerDialogWithId(NOTIFICATION_TIME_DIALOG_ID, getView().getContext(), SettingsFragment.this, 0, 0, true);
                //dialog.show();
                break;
            case NOTIFICATION_PERIOD_DIALOG_ID:
                //TimePickerDialogWithId dialog2 = new TimePickerDialogWithId(NOTIFICATION_PERIOD_DIALOG_ID, getView().getContext(), SettingsFragment.this, 0, 0, true);
                //dialog2.show();
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

        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("dd.MM.yyyy");
        String formattedDate = format.format(new Date(year - 1900, month, day));
        periodBegin.setText(formattedDate);
    }

    @Override
    public void onSpinnerDateDialogNegativeClick(SpinnerDatePickerDialog dialog) {
        // Nothing to do here
    }
}
