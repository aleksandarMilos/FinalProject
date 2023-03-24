package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.HandlerCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    Button loginBtn, createUserBtn;
    EditText etUsername, etPassword;
    CheckBox rememberCheck;

    SharedPreferences sp;
    SharedPreferences.Editor spe;

    //Doing SharedPreference to remember the previous Username (From Week5 SharedPreference)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginBtn = findViewById(R.id.login_button);
        etUsername = findViewById(R.id.user_Name);
        etPassword = findViewById(R.id.user_Password);
        rememberCheck = findViewById(R.id.checkBox);
        createUserBtn = findViewById(R.id.createNewUserBtn);

        spLoadData(); //For loading the data when the checkbox is checked

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
                    intent_course.putExtra("username", username); //sending over the username to the second activity
                    intent_course.putExtra("password", password); //sending over the password to the second activity
                    startActivity(intent_course);
                }
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
            spRemoveData(); //make sure the fields aren't remembered on next startup
        }
    }

    public void spSaveData(){
        sp = getPreferences(MODE_PRIVATE); //Whatever data we want to store, we will be keeping it private in the app
        spe = sp.edit();
        spe.putString("key_username", etUsername.getText().toString());
        spe.putString("key_pass", etPassword.getText().toString());
        spe.putBoolean("key_rem", rememberCheck.isChecked());
        spe.commit(); //NEED THIS, otherwise data not applied/saved
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

}