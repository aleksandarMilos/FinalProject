package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button loginBtn, createUserBtn;
    EditText etUsername, etPassword;
    CheckBox rememberCheck;

    SharedPreferences sp;
    SharedPreferences.Editor spe;

    boolean signedIn = false; //Variable used with sharedpreferences to keep track if the user has previously SignedIn (without signingout)

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginBtn = findViewById(R.id.login_button);
        etUsername = findViewById(R.id.user_Name);
        etPassword = findViewById(R.id.user_Password);
        rememberCheck = findViewById(R.id.checkBox);
        createUserBtn = findViewById(R.id.createNewUserBtn);


        spLoadData(); //Loading the shared-preferences which keeps track of our Username/password

        //TODO/Future work if too hard: Automatically logging the user out after a set amount of time the next time they open the app, e.g. 24 hours or a week

        //--------------------------------------------------------------------------------------
        Intent fromSecondActivity = getIntent();
        boolean signedOut = false;
        boolean invalidUserPass = false;

        //Signed out Toast message
        signedOut = fromSecondActivity.getBooleanExtra("signout", false);
        if (signedOut == true){
            Toast.makeText(this, "Successfully signed out!", Toast.LENGTH_SHORT).show();
            spRemoveSigninData(); //clearing SignIn data just in case (aka signedIn = false)
        }

        //Login Attempt error message for Invalid Username/Password
        invalidUserPass = fromSecondActivity.getBooleanExtra("invalidUserPass", false);
        if (invalidUserPass == true){
            Toast.makeText(this, "Invalid Username/Password! Please Try again.", Toast.LENGTH_SHORT).show();
            spRemoveSigninData(); //clearing SignIn data just in case (aka signedIn = false)
        }
        spLoadSigninData(); //Loading to see if the User has previously signed in
        //--------------------------------------------------------------------------------------


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_course = new Intent(MainActivity.this, SecondActivityCourse.class);
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                if (username.equals("")){
                    Toast.makeText(MainActivity.this, "Please enter a Username", Toast.LENGTH_SHORT).show();
                }
                else if (password.equals("")){
                    Toast.makeText(MainActivity.this, "Please enter a valid password", Toast.LENGTH_SHORT).show();
                }
                else{
                    intent_course.putExtra("username", username);
                    intent_course.putExtra("password", password);
                    intent_course.putExtra("vialogin", true);
                    spSaveSigninData();
                    startActivity(intent_course);
                }
            }
        });

        createUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_create_user = new Intent(MainActivity.this, CreateUserActivity.class);
                startActivity(intent_create_user);
            }
        });


    }

    @Override
    protected void onPause() {
        super.onPause();
        if (rememberCheck.isChecked()) {
            spSaveData();
        }
        else {
            spRemoveData();
            spRemoveSigninData();
        }
    }

    public void spSaveData(){
        sp = getPreferences(MODE_PRIVATE);
        spe = sp.edit();
        spe.putString("key_username", etUsername.getText().toString());
        spe.putString("key_pass", etPassword.getText().toString());
        spe.putBoolean("key_rem", rememberCheck.isChecked());
        spe.commit();
    }

    public void spLoadData(){
        sp = getPreferences(MODE_PRIVATE);
        etUsername.setText(sp.getString("key_username", null));
        etPassword.setText(sp.getString("key_pass",null));
        rememberCheck.setChecked(sp.getBoolean("key_rem", false));
    }

    public void spRemoveData(){
        sp = getPreferences(MODE_PRIVATE);
        spe = sp.edit();
        spe.remove("key_username");
        spe.remove("key_pass");
        spe.remove("key_rem");
        spe.commit();
    }

    //--------------------------------------------------------------------------------------
    //For remembering previously signed in functionality
    public void spSaveSigninData(){
        sp = getPreferences(MODE_PRIVATE);
        spe = sp.edit();
        spe.putBoolean("signedIn", true);
        spe.commit();
    }
    public void spRemoveSigninData(){
        sp = getPreferences(MODE_PRIVATE);
        spe = sp.edit();
        spe.putBoolean("signedIn", false);
        spe.commit();
    }
    public void spLoadSigninData(){
        sp = getPreferences(MODE_PRIVATE);
        signedIn = sp.getBoolean("signedIn", false);
        if (signedIn == true){ //If SignedIn previously (but never signed out), then we can go to SecondActivity right away
            Intent intent_course = new Intent(MainActivity.this, SecondActivityCourse.class);
            intent_course.putExtra("username", sp.getString("key_username", null));
            intent_course.putExtra("password", sp.getString("key_pass",null));
            intent_course.putExtra("vialogin", true);
            spSaveSigninData();
            startActivity(intent_course);
        }
    }
    //--------------------------------------------------------------------------------------

}