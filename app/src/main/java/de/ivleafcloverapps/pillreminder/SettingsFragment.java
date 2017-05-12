package de.ivleafcloverapps.pillreminder;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.Date;

/**
 * Created by Christian on 11.05.2017.
 */

public class SettingsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        EditText periodBegin = (EditText) getView().findViewById(R.id.editPeriodBegin);
        EditText notificationTime = (EditText) getView().findViewById(R.id.editNotificationTime);
        EditText notificationPeriod = (EditText) getView().findViewById(R.id.editNotificationPeriod);

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
}
