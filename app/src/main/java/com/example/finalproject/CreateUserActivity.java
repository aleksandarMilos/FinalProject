package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CreateUserActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        // Get references to the EditText fields
        usernameEditText = findViewById(R.id.user_Name);
        passwordEditText = findViewById(R.id.user_Password);

        //TODO Maybe we we have to passwordEditText, so that the user has to enter the same password twice, like most create account things on websites, app etc.
        // E.g. basically just a couple of extra lines, where one of them will be (passwordEditText1.getText().toString() == passwordEditText2.getText().toString())

        //TODO Maybe we'll have an exit button (say they don't want to actually create an account) and this'll redirect you back to the MainActivity login page
        // Probably don't need this, as I think Android user's nowadays are used to swiping back if they want to exit or go back from a specific part of the app

        //This is if they failed to create a new user (aka the User with that Username already exists)
        Intent intent_fromSecond = getIntent();
        boolean existingUser = intent_fromSecond.getBooleanExtra("existingUser", false);
        if (existingUser == true){
            Toast.makeText(this, "User already Exists! Please try again.", Toast.LENGTH_SHORT).show();
        }

        // Set up the button to create the user account
        Button createAccountButton = findViewById(R.id.buttonCreate);
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                // Check if the username and password are valid
                if (isValidUsername(username) && isValidPassword(password)) {
                    // Save the username and password to the SharedPreferences
                    saveCredentials(username, password);

                    // After creating the user account, we go directly to the SecondActivity
                    Intent intent = new Intent(CreateUserActivity.this, SecondActivityCourse.class);
                    intent.putExtra("username", username);
                    intent.putExtra("password", password);
                    intent.putExtra("viaCreateUser", true);
                    startActivity(intent);
                    finish();
                } else {
                    // Show an error message
                    //TODO ~Thomson, I see that you specified the length to be at least 8. Maybe we do a specialized message so that the User knows it has to be of length 8
                    // Or display it somewhere on the UI
                    Toast.makeText(CreateUserActivity.this, "Invalid username or password. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isValidUsername(String username) {
        // TODO: Implement your own validation logic for the username
        return !username.isEmpty();
    }

    private boolean isValidPassword(String password) {
        // TODO: Implement your own validation logic for the password
        return password.length() >= 8;
    }

    private void saveCredentials(String username, String password) {
        // TODO: Implement your own logic for saving the username and password to the SharedPreferences
    }
}

