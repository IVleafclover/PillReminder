package de.ivleafcloverapps.pillreminder.dialogs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TimePicker;

import de.ivleafcloverapps.pillreminder.R;

/**
 * Created by Christian on 19.05.2017.
 *
 * the spinner time dialog popup
 */

public class SpinnerTimePickerDialog extends DialogFragment {

    private final String TAG = this.getClass().getSimpleName();
    /**
     * Listener object, which gets the results from the DatePicker
     */
    private ISpinnerTimePickerDialogListener listener;
    private TimePicker timePicker;
    private int defaultHour;
    private int defaultMinute;
    private int dialogId;

    public SpinnerTimePickerDialog() {
        super();
    }

    // this is no clean android fragment constructor, but we want to use it here
    @SuppressLint("ValidFragment")
    public SpinnerTimePickerDialog(ISpinnerTimePickerDialogListener listener, int dialogId) {
        super();
        this.listener = listener;
        this.dialogId = dialogId;
    }

    // this is no clean android fragment constructor, but we want to use it here
    @SuppressLint("ValidFragment")
    public SpinnerTimePickerDialog(ISpinnerTimePickerDialogListener listener, int dialogId, java.util.Calendar calendar) {
        super();
        this.listener = listener;
        this.dialogId = dialogId;
        this.defaultHour = calendar.get(java.util.Calendar.HOUR_OF_DAY);
        this.defaultMinute = calendar.get(java.util.Calendar.MINUTE);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Build the dialog and set up the button click handlers
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_time, null);
        timePicker = (TimePicker) view.findViewById(R.id.spinnerTimePicker);
        timePicker.setIs24HourView(true);

        // we have to check versions here because the methods are only replaced in newer apk versions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePicker.setHour(defaultHour);
            timePicker.setMinute(defaultMinute);
        } else {
            timePicker.setCurrentHour(defaultHour);
            timePicker.setCurrentMinute(defaultMinute);
        }

        builder.setView(view);
        builder
                .setPositiveButton(R.string.submit, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the positive button event back to the host activity
                        listener.onSpinnerTimeDialogPositiveClick(SpinnerTimePickerDialog.this);
                    }
                });
        return builder.create();
    }

    public TimePicker getTimePicker() {
        return timePicker;
    }

    public int getDialogId() {
        return dialogId;
    }

    public String getTAG() {
        return TAG;
    }
}
