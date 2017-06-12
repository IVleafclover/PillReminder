package de.ivleafcloverapps.pillreminder.watchers;

import android.annotation.SuppressLint;
import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by Christian on 12.06.2017.
 */

public class SettingsTextWatcher implements TextWatcher {

    ISettingsTextWatcher listener;

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

    }
}
