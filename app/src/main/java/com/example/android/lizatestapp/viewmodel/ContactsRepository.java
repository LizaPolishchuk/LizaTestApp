package com.example.android.lizatestapp.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;

import com.example.android.lizatestapp.database.DaoContact;
import com.example.android.lizatestapp.database.LocalDatabase;
import com.example.android.lizatestapp.model.Contact;
import com.example.android.lizatestapp.utils.ExtraConstants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.MutableLiveData;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ContactsRepository {

    private MutableLiveData<List<Contact>> contactListFromLocalDbLiveData;
    private MutableLiveData<List<Contact>> contactListFromDbLiveData;
    private DaoContact contactsDao;

    public ContactsRepository(Application application) {
        LocalDatabase database = LocalDatabase.getDatabase(application);
        contactsDao = database.getDaoContact();
        contactListFromLocalDbLiveData = new MutableLiveData<>();
        contactListFromDbLiveData = new MutableLiveData<>();
    }

    @SuppressLint("CheckResult")
    public void loadContactsListFromLocalDB() {
        contactsDao.getAllContacts()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(contactList -> contactListFromLocalDbLiveData.postValue(contactList));
    }

    public void loadContactsListFromDB() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            reference.child(ExtraConstants.CHILD_USERS).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<Contact> contactList = new ArrayList<>();
                    for (DataSnapshot contactData : dataSnapshot.getChildren()) {
                        Contact contact = contactData.getValue(Contact.class);
                        contactList.add(contact);
                    }
                    contactListFromDbLiveData.postValue(contactList);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
    }

    public void loadContactsToLocalDB(List<Contact> contactList) {
        Completable.fromAction(() -> {
            contactsDao.deleteAllContacts();
            contactsDao.insertContactList(contactList);
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public void clearLocalDb() {
        Completable.fromAction(() -> contactsDao.deleteAllContacts())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public MutableLiveData<List<Contact>> getContactListFromLocalDbLiveData() {
        return contactListFromLocalDbLiveData;
    }

    public MutableLiveData<List<Contact>> getContactListFromDbLiveData() {
        return contactListFromDbLiveData;
    }
}
