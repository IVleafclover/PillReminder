package de.ivleafcloverapps.pillreminder;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

/**
 * Created by Christian on 11.05.2017.
 */

public class SettingsFragment extends Fragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private final int PERIOD_BEGIN_DIALOG_ID = 0;
    private final int NOTIFICATION_TIME_DIALOG_ID = 1;
    private final int NOTIFICATION_PERIOD_DIALOG_ID = 2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        EditText periodBegin = (EditText) getView().findViewById(R.id.editPeriodBegin);
        periodBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showDialog(PERIOD_BEGIN_DIALOG_ID);
            }
        });

        EditText notificationTime = (EditText) getView().findViewById(R.id.editNotificationTime);
        notificationTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(NOTIFICATION_TIME_DIALOG_ID);
            }
        });

        EditText notificationPeriod = (EditText) getView().findViewById(R.id.editNotificationPeriod);
        notificationPeriod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(NOTIFICATION_PERIOD_DIALOG_ID);
            }
        });

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getView().getContext());

        periodBegin.setText(sharedPreferences.getString("periodBegin", ""));
        notificationTime.setText(sharedPreferences.getString("notificationTime", ""));
        notificationPeriod.setText(sharedPreferences.getString("notificationPeriod", ""));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // TODO save every fragment_settings made in sharedPreferences

        EditText periodBegin = (EditText) getView().findViewById(R.id.editPeriodBegin);
        EditText notificationTime = (EditText) getView().findViewById(R.id.editNotificationTime);
        EditText notificationPeriod = (EditText) getView().findViewById(R.id.editNotificationPeriod);

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
        editor.commit();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Log.d("PillReminder", "chosen date: " + year + " " + month + " " + dayOfMonth);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

    }

    private void showDialog(int id) {
        switch(id) {
            case PERIOD_BEGIN_DIALOG_ID:
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getView().getContext(), SettingsFragment.this, 2000 , 10, 1);
                datePickerDialog.show();
                break;
            case NOTIFICATION_TIME_DIALOG_ID:
                TimePickerDialogWithId dialog = new TimePickerDialogWithId(NOTIFICATION_TIME_DIALOG_ID, getView().getContext(), SettingsFragment.this, 0, 0, true);
                dialog.show();
                break;
            case NOTIFICATION_PERIOD_DIALOG_ID:
                TimePickerDialogWithId dialog2 = new TimePickerDialogWithId(NOTIFICATION_PERIOD_DIALOG_ID, getView().getContext(), SettingsFragment.this, 0, 0, true);
                dialog2.show();
                break;
        }
    }
}
