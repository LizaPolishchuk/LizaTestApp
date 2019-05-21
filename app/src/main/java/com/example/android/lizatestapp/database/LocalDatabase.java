package com.example.android.lizatestapp.database;

import android.content.Context;

import com.example.android.lizatestapp.model.Contact;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Contact.class}, version = 1, exportSchema = false)
public abstract class LocalDatabase extends RoomDatabase {
    private static LocalDatabase database;

    public static LocalDatabase getDatabase(Context context) {
        if (database == null) {
            database = Room.databaseBuilder(context.getApplicationContext(),
                    LocalDatabase.class, "database_contacts")
                    .build();
        }
        return database;
    }

    public abstract DaoContact getDaoContact();
}
