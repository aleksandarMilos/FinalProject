package com.example.finalproject;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ItemDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertData(Item item);

    @Query("SELECT * FROM item_table")
    List<Item> getAllItem();

    @Update
    int updateData(Item item);

    @Delete
    int deleteData(Item item);
}
