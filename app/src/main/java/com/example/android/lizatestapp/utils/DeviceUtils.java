package com.example.android.lizatestapp.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class DeviceUtils {

    public static void hideKeyboard(Context context) {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            View focusedView = activity.getCurrentFocus();
            if (focusedView != null) {
                focusedView.clearFocus();
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) imm.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);
            }
        }
    }

    public static void showKeyboard(Context context) {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            View focusedView = activity.getCurrentFocus();
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) imm.showSoftInput(focusedView, 0);
        }
    }
}
