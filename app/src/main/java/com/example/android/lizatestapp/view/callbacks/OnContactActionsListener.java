package com.example.android.lizatestapp.view.callbacks;

import com.example.android.lizatestapp.model.Contact;

public interface OnContactActionsListener {
    void addNewContact(Contact contact);

    void editContact(Contact contact, int position);
}
