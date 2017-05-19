package de.ivleafcloverapps.pillreminder.dialogs;

import android.app.TimePickerDialog;
import android.content.Context;

/**
 * Created by Christian on 13.05.2017.
 */

public class TimePickerDialogWithId extends TimePickerDialog {
    private int id;

    public TimePickerDialogWithId(int id, Context context, OnTimeSetListener listener, int hourOfDay, int minute, boolean is24HourView) {
        super(context, listener, hourOfDay, minute, is24HourView);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
