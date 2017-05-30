package de.ivleafcloverapps.pillreminder.fragments;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import de.ivleafcloverapps.pillreminder.R;
import de.ivleafcloverapps.pillreminder.constants.SharedPreferenceConstants;

/**
 * Created by Christian on 11.05.2017.
 */

public class CalendarFragment extends Fragment {

    private TextView info;
    private Button takePill;
    private boolean takenToday;

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

        // load and set texts from saved preferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getView().getContext());
        String lastTakenDay = sharedPreferences.getString(SharedPreferenceConstants.LAST_TAKEN_DAY, "");

        // TODO set takenToday
        // TODO set label + button label

        takePill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO change status of taken + label + button label
            }
        });
    }
}
