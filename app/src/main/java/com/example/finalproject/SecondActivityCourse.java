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
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SecondActivityCourse extends AppCompatActivity {

    FloatingActionButton newCourseButton;
    Button signoutBtn;
    ListView listViewData;
    MyDatabase dbUserPass, dbCourse; //For creating the database
    TextView displayUsername;
    Switch deleteSwitch;

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
        displayUsername = findViewById(R.id.displayCurrentUser);
        deleteSwitch = findViewById(R.id.deleteSwitch);
        signoutBtn = findViewById(R.id.signoutBtn);
        dbUserPass = Room.databaseBuilder(getApplicationContext(), MyDatabase.class, "UserPass_db").fallbackToDestructiveMigration().build(); //Building the database once
        dbCourse = Room.databaseBuilder(getApplicationContext(), MyDatabase.class, "Course_db").fallbackToDestructiveMigration().build();

        //Retrieving Username and password from first activity
        Intent intent_fromMain = getIntent();
        String username = intent_fromMain.getStringExtra("username");
        String password = intent_fromMain.getStringExtra("password");
        boolean viaLogin = intent_fromMain.getBooleanExtra("vialogin", false);

        //Intents
        Intent intent_toMain = new Intent(SecondActivityCourse.this, MainActivity.class);

        //--------------------------------------------------------------------------------------
        //This code is to check for existing user. Note we only want to do this if they clicked it via the login button. If not, then only other way is they came from create user activity
        if (viaLogin == true){
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    boolean existUser = dbUserPass.userPassDAO().checkUserPass(username, password);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (existUser == true){
                                Toast.makeText(SecondActivityCourse.this, "Welcome " + username + "!", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                intent_toMain.putExtra("invalidUserPass", true);
                                startActivity(intent_toMain);
                            }
                        }
                    });
                }
            });
        }
        else{
            saveUserPassData(username, password);
        }
        //--------------------------------------------------------------------------------------

//        showData();


        //TODO Probably floatingActionbutton FUNCTIONALITY
        // Will lead to another Activity (I think implementing it as a fragment might be better UI wise, but idk how to do that)
        // The other activity (or fragment if I figure it out) is where you'll enter the CourseNum and CourseName
        // Also figure out proper database functionality with this, where it adds CourseNum and CourseName to the corresponding UserName/Password within the same table
        //  ^   Maybe I have to split the database into two tables, one table holding the Username/Password, other table holding CourseNum and CourseName with username as the foreign key

        // TODO Other activity once you enter CourseNum and CourseName and click "Submit" or whatever, you get redirected back to this activity, where it will add on (to the bottom of the ListView)
        //      your newly entered CourseNum + CourseName. Currently I have it just showing the Username and Password just to make sure I properly inserted those into a data table.

        //TODO Add functionality to deleteSwitch button (this should be done after above is implemented)

        //TODO Cleanup the SecondActivityCourse xml UI design (Sorry guys I really suck at it LOL)

        //TODO Maybe have a check password thing?


        //--------------------------------------------------------------------------------------
        //Signout button functionality
        signoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent_toMain.putExtra("signout", true);
                startActivity(intent_toMain);
            }
        });
        //--------------------------------------------------------------------------------------

        displayUsername.setText("Current User: " + username); //Displaying current user name in the Textview @ the bottom right corner beside the signout button

    }

    //Method for Saving the Username/Password Data into the database dbUserPass
    public void saveUserPassData(String username, String password){ //Adding the Username and Password to the database
        UserPass userpass = new UserPass();
        userpass.setUsername(username);
        userpass.setPassword(password);
        executorService.execute(new Runnable() { //For running in a separate thread because this is for database entry
            @Override
            public void run() {
                long l = dbUserPass.userPassDAO().insertData(userpass); //Note this insertData returns a long value (refer in UserPass.java
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(l>0){ //means some row has been changed/modified
                            Toast.makeText(SecondActivityCourse.this, "The UserPass has been inserted!", Toast.LENGTH_SHORT).show();
                        }
                        else{ //Not inserted/failed
                            Toast.makeText(SecondActivityCourse.this, "The UserPass have not been saved.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

//    public void showData(){ //showData(View view)
//        executorService.execute(new Runnable() {
//            //ArrayList<String> stringArray = new ArrayList<>(); //needed to give this to the adapter, along with context
//            String string;
//            @Override
//            public void run() {
//                List<Course> courseList = dbCourse.courseDAO().getAllCourse(); //Originally did this to getAllUsers, using the appropriate method
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        customListAdapter = new CustomListAdapter(SecondActivityCourse.this, courseList);
//                        listViewData.setAdapter(customListAdapter);
//                    }
//                });
//            }
//        });
//    }
}