package com.example.finalproject;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Item.class}, version = 1)
public abstract class MyDatabaseItem extends RoomDatabase {
    public abstract ItemDAO itemDao();
}
