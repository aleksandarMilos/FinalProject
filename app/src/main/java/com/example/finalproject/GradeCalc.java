package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class GradeCalc extends AppCompatActivity {

    Button button_Add, button_ShowGrade, button_Home;
    EditText grade, weight;
    TextView displayGrade, finalGrade;
    ArrayList<Float> grades, weights;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade_calc);

        setTitle("Grade Calculator");
        Intent calculator = getIntent();
        String username = calculator.getStringExtra("username");


        grades = new ArrayList<>();
        weights = new ArrayList<>();
        button_Add = findViewById(R.id.btn_add);
        button_ShowGrade = findViewById(R.id.btn_show);
        button_Home = findViewById(R.id.btn_Home);
        grade = findViewById(R.id.et_Grade);
        weight = findViewById(R.id.et_weight);
        displayGrade = findViewById(R.id.grade_Dsiplayed);
        finalGrade = findViewById(R.id.finalMark);

        button_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Error checking if they entered stuff other than numbers
                float grade_entered = 0;
                float weight_entered = 0;
                try{
                    grade_entered = Float.parseFloat(grade.getText().toString());
                    weight_entered = Float.parseFloat(weight.getText().toString());
                } catch (NumberFormatException e){
                    Toast.makeText(GradeCalc.this, "Please enter a valid Grade/Weight", Toast.LENGTH_SHORT).show();
                    return;
                }
                grades.add(grade_entered);
                weights.add(weight_entered);
                grade.setText("");
                weight.setText("");
            }
        });

        button_ShowGrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float userGrade = 0;
                float totalWeight = 0;
                for(int i=0; i<grades.size(); i++) {
                    userGrade = userGrade + (grades.get(i) * weights.get(i));
                    totalWeight = totalWeight + weights.get(i);
                }
                userGrade = userGrade / totalWeight;
                displayGrade.setText("Your current grade is:");
                String s = String.format("%.2f", userGrade);
                finalGrade.setText(s + "%");
            }
        });

        button_Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent home = new Intent(GradeCalc.this, CourseListActivity.class);
                home.putExtra("username", username);
                startActivity(home);
                finish();
            }
        });
    }
}