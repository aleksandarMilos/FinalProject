package com.example.finalproject;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "item_table", foreignKeys = {
        @ForeignKey(entity = Course.class,
                parentColumns = "id",
                childColumns = "courseID",
                onDelete = ForeignKey.CASCADE
        ),
        @ForeignKey(entity = UserPass.class,
                parentColumns = "Username",
                childColumns = "usName",
                onDelete = ForeignKey.CASCADE
        )
})
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

    @ColumnInfo(name = "courseID")
    public int courseID;

    public void setCourseID(int courseID){ this.courseID = courseID; }
    public int getCourseID() { return this.courseID; }

    @ColumnInfo(name = "usName")
    public String usName;

    public void setUsName(String usName){ this.usName = usName; }
    public String getUsName(){ return this.usName; }




}
