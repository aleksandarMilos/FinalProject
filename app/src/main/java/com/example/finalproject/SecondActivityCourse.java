package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.HandlerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SecondActivityCourse extends AppCompatActivity {

    FloatingActionButton newCourseButton;
    ListView listViewData;
    MyDatabase db; //For creating the database

    //This activity along with its associated activities (Course.java, CourseDAO.java, CustomListAdapter.java, MyDatabase.java) is mainly what we did in Week10RoomDatabase, but tuned to our
    // own use case for keeping track of Courses in relation to the User that has logged in
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private Handler handler = HandlerCompat.createAsync(Looper.getMainLooper());
    CustomListAdapter customListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_course);

        newCourseButton = findViewById(R.id.addCourseBtn);
        listViewData = findViewById(R.id.listViewData);
        db = Room.databaseBuilder(getApplicationContext(), MyDatabase.class, "UserCourse_db").fallbackToDestructiveMigration().build(); //Building the database once


        //Retrieving Username and password from first activity
        Intent intent_fromMain = getIntent();
        String username = intent_fromMain.getStringExtra("username");
        String password = intent_fromMain.getStringExtra("password");

        saveUserPassData(username, password);
        showData();


        //TODO implement complete database functionality
        // I have passed in the username and password to this SecondActivity. Here we will check if the Username and Password
        // Correspond to an already exisiting user in the database, if it doesn't then we send them back to the MainActivity and give them a toast message
        // Or some sort warning mesage that the user doesn't exist or that the username/password is incorrect

        //TODO Implement some signout here to return to first MainActivity, so that User can choose to login with a different username/password

        //TODO Probably floatingActionbutton will lead to another Activity (I think implementing it as a fragment might be better UI wise, but idk how to do that)
        // The other activity (or fragment if I figure it out) is where you'll enter the CourseNum and CourseName
        // Also figure out proper database functionality with this, where it adds CourseNum and CourseName to the corresponding UserName/Password within the same table
        //  ^   Maybe I have to split the database into two tables, one table holding the Username/Password, other table holding CourseNum and CourseName with username as the foreign key

        // TODO Other activity once you enter CourseNum and CourseName and click "Submit" or whatever, you get redirected back to this activity, where it will add on (to the bottom of the ListView)
        //      your newly entered CourseNum + CourseName. Currently I have it just showing the Username and Password just to make sure I properly inserted those into a data table.

    }

    //Method for saving the data
    public void saveUserPassData(String username, String password){ //Adding the Username and Password to the database
        Course course = new Course();
        course.setUsername(username);
        course.setPassword(password);
        executorService.execute(new Runnable() { //For running in a separate thread because this is for database entry
            @Override
            public void run() {
                long l = db.courseDAO().insertData(course); //Note this insertData returns a long value (refer in UserDao.java)
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(l>0){ //means some row has been changed/modified
                            Toast.makeText(SecondActivityCourse.this, "The values has been inserted!", Toast.LENGTH_SHORT).show();
                        }
                        else{ //Not inserted/failed
                            Toast.makeText(SecondActivityCourse.this, "The values have not been saved.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    public void showData(){ //showData(View view)
        executorService.execute(new Runnable() {
            //ArrayList<String> stringArray = new ArrayList<>(); //needed to give this to the adapter, along with context
            String string;
            @Override
            public void run() {
                List<Course> courseList = db.courseDAO().getAllCourse(); //Originally did this to getAllUsers, using the appropriate method
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        customListAdapter = new CustomListAdapter(SecondActivityCourse.this, courseList);
                        listViewData.setAdapter(customListAdapter);
                    }
                });
            }
        });
    }

}