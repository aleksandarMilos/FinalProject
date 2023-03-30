package com.example.finalproject;

import androidx.room.ColumnInfo;
import androidx.room.Entity; //We imported this
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "Course_table",
        foreignKeys = {@ForeignKey(entity = UserPass.class,
        parentColumns = "Username",
        childColumns = "uName",
        onDelete = ForeignKey.CASCADE)}) //Foreign key is the Username because we want each unique user to have their own courses that they added to showup for them
public class Course {

    //Main things to keep track of in this table is the CourseNum and CourseName
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int _id;

    public int get_id() {
        return _id;
    }
    public void set_id(int _id) { this._id = _id; }

    @ColumnInfo(name = "Course")
    private String course;

    public String getCourse() { return this.course; }
    public void setCourse(String course) { this.course = course; }

    @ColumnInfo(name = "uName")
    public String uName;

    public String getuName() { return this.uName; }
    public void setuName(String uName) { this.uName = uName; }

}
