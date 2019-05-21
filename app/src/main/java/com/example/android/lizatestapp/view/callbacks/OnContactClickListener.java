package com.example.android.lizatestapp.view.callbacks;

import com.example.android.lizatestapp.model.Contact;

public interface OnContactClickListener {
    void clickOnContact(Contact contact, int position);

    void clickOnDelete(int position);
}
