package com.example.android.lizatestapp.view.callbacks;

public interface OnPhoneEmailActionsListener {
    void addPhoneEmail(int position);

    void updatePhoneEmail(int position, String text);

    void removePhoneEmail(int position);
}
