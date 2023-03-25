package com.example.finalproject;

import androidx.room.ColumnInfo;
import androidx.room.Entity; //We imported this
import androidx.room.PrimaryKey;

@Entity(tableName = "Course_table")
public class Course {

    //Main things to keep track of in this table is the CourseNum and CourseName
    @PrimaryKey(autoGenerate = true) //TODO Maybe this primary key will be CourseNum?
    @ColumnInfo(name = "id")
    private int _id;

    //TODO Probably need to include Username as a FOREIGN Key, this is how we'll correlate specific Username to their added courses
    public int get_id() {
        return _id;
    }
    public void set_id(int _id) { this._id = _id; }

    @ColumnInfo(name = "CourseNum") //Example in the format COMP3150 hence a string
    private String courseNum;

    public String getCourseNum() { return this.courseNum; }
    public void setCourseNum(String courseNum) { this.courseNum = courseNum; }

    @ColumnInfo(name = "CourseName")
    private String courseName;

    public String getCourseName() { return this.courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }




}
