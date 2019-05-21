package com.example.android.lizatestapp.viewmodel;

import android.app.Application;

public class RepositoryProvider {

    private static ContactsRepository contactsRepository;

    public static ContactsRepository getContactsRepository(Application application) {
        if (contactsRepository == null) {
            contactsRepository = new ContactsRepository(application);
        }
        return contactsRepository;
    }
}
