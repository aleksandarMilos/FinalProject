package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.HandlerCompat;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    Button loginBtn;
    EditText userName, password;
    CheckBox rememberCheck;

    //Refer to week10RoomDatabase, this is what I will try to implement here maybe
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    private Handler handler = HandlerCompat.createAsync(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginBtn = findViewById(R.id.login_button);
        userName = findViewById(R.id.user_Name);
        password = findViewById(R.id.user_Password);
        rememberCheck = findViewById(R.id.checkBox);


    }

}