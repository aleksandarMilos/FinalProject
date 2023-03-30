package com.example.finalproject;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Course.class, UserPass.class, Item.class}, version = 3)
public abstract class MyDatabase extends RoomDatabase { //Abstract class for our database
    public abstract CourseDAO courseDAO();
    public abstract UserPassDAO userPassDAO();
    public abstract ItemDAO itemDao();
}
