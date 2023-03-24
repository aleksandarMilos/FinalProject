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

                    // Show a success message
                    Toast.makeText(CreateUserActivity.this, "User account created successfully!", Toast.LENGTH_SHORT).show();

                    // Start the MainActivity
                    Intent intent = new Intent(CreateUserActivity.this, MainActivity.class);
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

