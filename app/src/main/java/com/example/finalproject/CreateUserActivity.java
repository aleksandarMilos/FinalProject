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
    private EditText reEnterPasswordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        // Get references to the EditText fields
        usernameEditText = findViewById(R.id.user_Name);
        passwordEditText = findViewById(R.id.user_Password);
        reEnterPasswordEditText = findViewById(R.id.user_Password2);

        //Coming back from SecondActivityCourse, this occurs if the user already exists
        //Thus want to show an error message
        Intent intent_fromCourse = getIntent();
        boolean existingUser = intent_fromCourse.getBooleanExtra("existingUser", false);
        if (existingUser == true){
            Toast.makeText(this, "User already exists! Try a different username.", Toast.LENGTH_SHORT).show();
        }


        // Set up the button to create the user account
        Button createAccountButton = findViewById(R.id.create_button);
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String reEnterPassword = reEnterPasswordEditText.getText().toString();

                // Check if the username and password are valid
                if (isValidUsername(username) && isValidPassword(password) && isMatchedPassword(password, reEnterPassword)) {

                    // After creating the user account, we go directly to the SecondActivity
                    Intent intent = new Intent(CreateUserActivity.this, SecondActivityCourse.class);
                    intent.putExtra("username", username); //Passing the entered credentials (Username and password) over to the SecondActivity
                    intent.putExtra("password", password);
                    intent.putExtra("viaCreateUser", true);
                    startActivity(intent);
                    finish();
                } else {
                    // Show an error message
                    Toast.makeText(CreateUserActivity.this, "Invalid username or password. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isValidUsername(String username) {
        // Implement your own validation logic for the username
        return !username.isEmpty();
    }

    private boolean isValidPassword(String password) {
        // Implement your own validation logic for the password
        return password.length() >= 8;
    }

    private boolean isMatchedPassword(String password, String reEnterPassword) {
        // Implement your own validation logic for the re-entered password
        return password.equals(reEnterPassword);
    }
}


