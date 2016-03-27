package com.example.adama.findmypharmacie.models;

import android.app.Activity;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.adama.findmypharmacie.R;

/**
 * Created by adama on 24/03/2016.
 */
public class ValidForm {

    public boolean validString(TextView tv, TextInputLayout textInputLayout, String err_msg, Activity activity){
        if (tv.getText().toString().trim().isEmpty()) {
            textInputLayout.setErrorEnabled(true);
            textInputLayout.setError(err_msg);
            requestFocus(tv, activity);
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
        }
        return true;
    }
    private void requestFocus(View view, Activity activity) {
        if (view.requestFocus()) {
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
}
