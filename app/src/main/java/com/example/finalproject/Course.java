package com.example.finalproject;

import androidx.room.ColumnInfo;
import androidx.room.Entity; //We imported this
import androidx.room.PrimaryKey;

@Entity(tableName = "Course_table")
public class Course {

    //Main things to keep track of is Username, password, CourseNum, and CourseName
    @PrimaryKey(autoGenerate = true) //TODO when I figure this out later, the PrimaryKey here should most likely be Username, and similarly, the Password should be a Candidate key
    @ColumnInfo(name = "id")
    private int _id;

    public int get_id() {
        return _id;
    }
    public void set_id(int _id) { this._id = _id; }

    @ColumnInfo(name = "Username")
    private String username;

    public String getUsername() { return this.username; }
    public void setUsername(String username) { this.username = username; }

    @ColumnInfo(name = "Password")
    private String password;

    public String getPassword() { return this.password; }
    public void setPassword(String password) { this.password = password; }

    @ColumnInfo(name = "CourseNum") //Example in the format COMP3150 hence a string
    private String courseNum;

    public String getCourseNum() { return this.courseNum; }
    public void setCourseNum(String courseNum) { this.courseNum = courseNum; }

    @ColumnInfo(name = "CourseName")
    private String courseName;

    public String getCourseName() { return this.courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }




}
