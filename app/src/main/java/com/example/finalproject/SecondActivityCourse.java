package com.example.finalproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.HandlerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.room.Room;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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
    Button signoutBtn, gradeCalculator;
    ListView listViewData;
    MyDatabase mydb; //For creating the database
    TextView displayUsername;
    Switch deleteSwitch;

    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private Handler handler = HandlerCompat.createAsync(Looper.getMainLooper());
    CustomListAdapter customListAdapter;

    String courseInput = ""; //this is for the Dialog to add a course


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_course);


        newCourseButton = findViewById(R.id.addCourseBtn);
        listViewData = findViewById(R.id.listViewData);
        displayUsername = findViewById(R.id.displayCurrentUser);
        deleteSwitch = findViewById(R.id.deleteSwitch);
        signoutBtn = findViewById(R.id.signoutBtn);
        gradeCalculator = findViewById(R.id.calculator);
        mydb = Room.databaseBuilder(getApplicationContext(), MyDatabase.class, "UserPass_db").fallbackToDestructiveMigration().build(); //Building the database once

        //Retrieving Username and password from first activity or Retrieving it from Create user
        Intent intent_fromElsewhere = getIntent();
        String username = intent_fromElsewhere.getStringExtra("username");
        String password = intent_fromElsewhere.getStringExtra("password");
        boolean viaLogin = intent_fromElsewhere.getBooleanExtra("vialogin", false);
        boolean viaCreateUser = intent_fromElsewhere.getBooleanExtra("viaCreateUser", false);

        //Setting Activity label to be unique to the user
        setTitle(username + "'s Courses");

        //Intents towards MainActivity
        Intent intent_toMain = new Intent(SecondActivityCourse.this, MainActivity.class);


        //TODO/Future Work Maybe have a check password thing, so that user if they forgot their password, can check it via the app idk? <- This could be a further implementation where we do a "Settings" button
        //TODO/Future Work Probably add a "settings" button on this Activity. But yeah we'll have to fix up the UI if we want to implement more buttons/functionality
        //TODO/Future Work Email address functionality

        //TODO Possible idea: Like tips/tricks page, or User manual on how to use certain parts of the app. (?) Question mark button, where user's click into it, it'll show How to use the app
        // Probably a future works thing

        //TODO/FutureWork: Multiple Course delete functionality. Maybe instead of current delete functionality
        // Make it so long click you can delete a course that way, but when the Delete Switch is checked, you can select a number of courses, and then delete them that way
        //FutureWork: Rearrange Courses/Tasks in the order you want

        //--------------------------------------------------------------------------------------
        //This code is to check for existing user via the Login button
        //Case sensitive
        if (viaLogin == true){
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    boolean existUser = mydb.userPassDAO().checkUserPass(username, password);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (existUser == true){
                                Toast.makeText(SecondActivityCourse.this, "Welcome " + username + "!", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                intent_toMain.putExtra("invalidUserPass", true);
                                startActivity(intent_toMain);
                                finish();
                            }
                        }
                    });
                }
            });
        }
        //--------------------------------------------------------------------------------------

        //--------------------------------------------------------------------------------------
        //Code for if we came from CreateUser, check for if the user already exists in the database, if not, add them to the database, if they exist, send them back to the Create User page and display error message there
        Intent intent_toCreate = new Intent(SecondActivityCourse.this, CreateUserActivity.class);
        if (viaCreateUser == true){
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    boolean existUser = mydb.userPassDAO().checkUser(username); //Only have to check if User already exists in our database
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //Also have to check if the Newly Created User already exists
                            if (existUser == false){ //Only want to Create new User if they don't exist already
                                saveUserPassData(username, password); //this creates the new account in the database
                            }
                            else{
                                intent_toCreate.putExtra("existingUser", true);
                                startActivity(intent_toCreate);
                                finish();
                            }
                        }
                    });
                }
            });
        }
        //--------------------------------------------------------------------------------------

        //Shows all the course data with associated User
        showCourseData(username);

        //--------------------------------------------------------------------------------------
        //FloatingActionButton functionality to create a new Course
        // https://stackoverflow.com/questions/10903754/input-text-dialog-android
        newCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SecondActivityCourse.this);
                builder.setTitle("Add a new Course");

                // Set up the input
                final EditText input = new EditText(SecondActivityCourse.this);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        courseInput = input.getText().toString();
                        if (courseInput.trim().length() == 0){ //Don't want them to enter an empty course or empty spaces
                            dialog.cancel();
                            Toast.makeText(SecondActivityCourse.this, "Please enter a course name.", Toast.LENGTH_SHORT).show();
                            return; //Get out of this "Add" part of the code
                        }
                        saveCourseData(courseInput, username);
                        showCourseData(username);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });
        //--------------------------------------------------------------------------------------


        //--------------------------------------------------------------------------------------
        // Delete Switch Functionality, similar to Week10 Lecture functionality
        // https://stackoverflow.com/questions/2478517/how-to-display-a-yes-no-dialog-box-on-android
        // Also functionality to go to TodoList, with each TodoList specific to the clicked course
        listViewData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Course course = (Course)customListAdapter.getItem(position); //Gets the "Course" at the position clicked, which is the one we want to delete

                if (deleteSwitch.isChecked()){ //Only do this if our delete switch is switched on
                    //AlertDialog (Confirmation box) functionality
                    AlertDialog.Builder confirm = new AlertDialog.Builder(SecondActivityCourse.this);
                    confirm.setTitle("Confirm");
                    confirm.setMessage("Are you sure you want to delete this?");
                    confirm.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            executorService.execute(new Runnable() {
                                @Override
                                public void run() {
                                    int del = mydb.courseDAO().deleteData(course);
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (del > 0){
                                                customListAdapter.removeList(position);
                                            }
                                        }
                                    });
                                }
                            });
                        }
                    });
                    confirm.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    confirm.show();
                }
                else{ //This occurs when you're just clicking on one of the Courses regularly
                    //Regular Course Click Functionality to go to TodoList for that course
                    Intent intent_toTodo = new Intent(SecondActivityCourse.this, ToDoList.class);
                    int courseID = course.get_id();
                    String courseName = course.getCourse();
                    //Passing all this information to the TodoList so that it can be utilized/added to the Database
                    intent_toTodo.putExtra("courseID", courseID);
                    intent_toTodo.putExtra("usName", username);
                    intent_toTodo.putExtra("courseName", courseName);
                    startActivity(intent_toTodo);
                }
            }
        });
        //--------------------------------------------------------------------------------------


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

        gradeCalculator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent calc = new Intent(SecondActivityCourse.this, GradeCalc.class);
                calc.putExtra("username", username);
                startActivity(calc);
            }
        });
    }

    //Method for Saving the Username/Password Data into the database dbUserPass
    public void saveUserPassData(String username, String password){ //Adding the Username and Password to the database
        UserPass userpass = new UserPass();
        userpass.setUsername(username);
        userpass.setPassword(password);
        executorService.execute(new Runnable() { //For running in a separate thread because this is for database entry
            @Override
            public void run() {
                long l = mydb.userPassDAO().insertData(userpass); //Note this insertData returns a long value (refer in UserPass.java)
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(l>0){ //means some row has been changed/modified
                            Toast.makeText(SecondActivityCourse.this, "New User " + username + " Created!", Toast.LENGTH_SHORT).show();
                        }
                        else{ //Not inserted/failed
                            Toast.makeText(SecondActivityCourse.this, "New User has not been created...", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    //Adding new course along with the Current User that added this new course to the Database (Tying Users to their CourseList)
    public void saveCourseData(String newCourse, String username){
        Course course = new Course();
        course.setCourse(newCourse);
        course.setuName(username);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                long l = mydb.courseDAO().insertData(course);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(l>0){ //means some row has been changed/modified
                            //Toast.makeText(SecondActivityCourse.this, "New Course " + newCourse + "Added!", Toast.LENGTH_SHORT).show();  //Uncomment these checks if needed
                        }
                        else{ //Not inserted/failed
                            Toast.makeText(SecondActivityCourse.this, "New course has not been added...", Toast.LENGTH_SHORT).show(); //This should not pop up if everything's functioning correctly, I'll leave this check here
                        }
                    }
                });
            }
        });
    }


    public void showCourseData(String username){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                List<Course> courseList = mydb.courseDAO().getAllCourse(username);
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