package com.example.finalproject;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "item_table")
public class Item {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int _id;

    public int get_id() {
        return _id;
    }
    public void set_id(int id){ this._id = id;}

    @ColumnInfo(name = "Item")
    private String item;

    public String getItem(){ return this.item; }

    public void setItem(String item) { this.item = item; }

//    @ColumnInfo(name = "ItemName")
//    private String item_name;
//
//    @ColumnInfo(name = "descr")
//    private String descr;
//
//    @ColumnInfo(name = "date")
//    private String date;
//
//    @ColumnInfo(name = "time")
//    private String time;
//
//
//    public String get_item_name(){ return item_name; }
//    public void set_item_name(String item_name) { this.item_name = item_name; }
//
//    public String get_descr(){ return descr; }
//    public void set_descr(String descr){ this.descr = descr; }
//
//    public String get_date(){ return date; }
//    public void set_date(String date){ this.date = date; }
//
//    public String get_time(){ return time; }
//    public void set_time(String time){ this.time = time; }


}
