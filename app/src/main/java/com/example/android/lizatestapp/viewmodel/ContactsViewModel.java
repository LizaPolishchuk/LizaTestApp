package com.example.android.lizatestapp.viewmodel;

import android.app.Application;

import com.example.android.lizatestapp.model.Contact;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class ContactsViewModel extends AndroidViewModel {

    private ContactsRepository contactsRepository;

    public ContactsViewModel(@NonNull Application application) {
        super(application);
        contactsRepository = RepositoryProvider.getContactsRepository(application);
    }

    public void loadContactsListFromLocalDB() {
        contactsRepository.loadContactsListFromLocalDB();
    }

    public void loadContactsListFromDB() {
        contactsRepository.loadContactsListFromDB();
    }

    public void loadContactsListToLocalDB(List<Contact> contactList) {
        contactsRepository.loadContactsToLocalDB(contactList);
    }

    public void clearLocalDb() {
        contactsRepository.clearLocalDb();
    }

    public LiveData<List<Contact>> getContactListFromLocalDbLiveData() {
        return contactsRepository.getContactListFromLocalDbLiveData();
    }

    public LiveData<List<Contact>> getContactListFromDbLiveData() {
        return contactsRepository.getContactListFromDbLiveData();
    }
}
