package com.example.android.lizatestapp.database;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import androidx.room.TypeConverter;

public class ListConverter {
    @TypeConverter
    public String fromArray(List<String> tags) {
        return new Gson().toJson(tags);
    }

    @TypeConverter
    public List<String> toArray(String tags) {
        return new Gson().fromJson(tags, new TypeToken<List<String>>() {
        }.getType());
    }
}
