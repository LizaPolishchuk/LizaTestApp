package com.example.android.lizatestapp.model;


import com.example.android.lizatestapp.database.ListConverter;

import java.util.List;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@Entity(tableName = "contacts_table")
public class Contact {
    @PrimaryKey(autoGenerate = true)
    private int key;

    private String name;
    @TypeConverters(ListConverter.class)
    private List<String> emails;
    @TypeConverters(ListConverter.class)
    private List<String> phones;
    private String gender;

    @Ignore
    public Contact() {
    }

    public Contact(String name, List<String> emails, List<String> phones, String gender) {
        this.name = name;
        this.emails = emails;
        this.phones = phones;
        this.gender = gender;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getEmails() {
        return emails;
    }

    public void setEmails(List<String> emails) {
        this.emails = emails;
    }

    public List<String> getPhones() {
        return phones;
    }

    public void setPhones(List<String> phones) {
        this.phones = phones;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
