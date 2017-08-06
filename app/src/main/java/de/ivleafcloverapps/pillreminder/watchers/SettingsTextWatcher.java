package de.ivleafcloverapps.pillreminder.watchers;

import android.annotation.SuppressLint;
import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by Christian on 12.06.2017.
 *
 * a text watcher, that checks which setting fields where changed, so that only specific options had to be made, when the settings are closed
 */

public class SettingsTextWatcher implements TextWatcher {

    private final ISettingsTextWatcher listener;

    // this is no clean android fragment constructor, but we want to use it here
    @SuppressLint("ValidFragment")
    public SettingsTextWatcher(ISettingsTextWatcher listener) {
        super();
        this.listener = listener;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // nothing to do here
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // nothing to do here
    }

    @Override
    public void afterTextChanged(Editable s) {
        listener.onTextChanged();
    }
}
