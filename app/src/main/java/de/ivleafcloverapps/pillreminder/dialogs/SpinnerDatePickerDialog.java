package de.ivleafcloverapps.pillreminder.dialogs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;

import de.ivleafcloverapps.pillreminder.R;

/**
 * Created by Christian on 19.05.2017.
 */

public class SpinnerDatePickerDialog extends DialogFragment {

    private final String TAG = this.getClass().getSimpleName();
    int defaultYear;
    int defaultMonth;
    int defaultDay;
    /**
     * Listener object, which gets the results from the DatePicker
     */
    private ISpinnerDatePickerDialogListener listener;
    private DatePicker datePicker;

    public SpinnerDatePickerDialog() {
        super();
    }

    // this is no clean android fragment constructor, but we want to use it here
    @SuppressLint("ValidFragment")
    public SpinnerDatePickerDialog(ISpinnerDatePickerDialogListener listener) {
        super();
        this.listener = listener;
    }

    // this is no clean android fragment constructor, but we want to use it here
    @SuppressLint("ValidFragment")
    public SpinnerDatePickerDialog(ISpinnerDatePickerDialogListener listener, int year, int month, int day) {
        super();
        this.listener = listener;
        this.defaultYear = year;
        this.defaultMonth = month;
        this.defaultDay = day;
    }

    // this is no clean android fragment constructor, but we want to use it here
    @SuppressLint("ValidFragment")
    public SpinnerDatePickerDialog(ISpinnerDatePickerDialogListener listener, Calendar calendar) {
        super();
        this.listener = listener;
        this.defaultYear = calendar.get(Calendar.YEAR);
        this.defaultMonth = calendar.get(Calendar.MONTH);
        this.defaultDay = calendar.get(Calendar.DAY_OF_MONTH);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Build the dialog and set up the button click handlers
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_date, null);
        datePicker = (DatePicker) view.findViewById(R.id.spinnerDatePicker);
        datePicker.updateDate(defaultYear, defaultMonth, defaultDay);
        builder.setView(view);
        builder
                .setPositiveButton(R.string.submit, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the positive button event back to the host activity
                        listener.onSpinnerDateDialogPositiveClick(SpinnerDatePickerDialog.this);
                    }
                })
                .setNegativeButton(R.string.abort, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the negative button event back to the host activity
                        listener.onSpinnerDateDialogNegativeClick(SpinnerDatePickerDialog.this);
                    }
                });
        return builder.create();
    }

    public DatePicker getDatePicker() {
        return datePicker;
    }

    public void setDatePicker(DatePicker datePicker) {
        this.datePicker = datePicker;
    }

    public String getTAG() {
        return TAG;
    }
}
