package com.example.android.lizatestapp.database;

import com.example.android.lizatestapp.model.Contact;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import io.reactivex.Flowable;

@Dao
public interface DaoContact {
    @Insert
    void insertContact(Contact contact);

    @Insert
    void insertContactList(List<Contact> contactList);

    @Query("DELETE FROM contacts_table")
    void deleteAllContacts();

    @Query("SELECT * FROM contacts_table")
    Flowable<List<Contact>> getAllContacts();

}
