package com.example.finalproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.HandlerCompat;
import androidx.room.Room;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ToDoList extends AppCompatActivity{

    private EditText titleInput;
    private EditText descriptionInput;
    private EditText dateInput;
    private EditText timeInput;
    private Button addButton;
    private ListView todoList;

    //Testing
    MyDatabase mydb;

    CustomItemListAdapter customItemListAdapter;

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    private Handler handler = HandlerCompat.createAsync(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todolist);

        titleInput = findViewById(R.id.title_input);
        descriptionInput = findViewById(R.id.description_input);
        dateInput = findViewById(R.id.date_input);
        timeInput = findViewById(R.id.time_input);
        addButton = findViewById(R.id.add_button);
        todoList = findViewById(R.id.todo_list);

        mydb = Room.databaseBuilder(getApplicationContext(), MyDatabase.class, "UserPass_db").fallbackToDestructiveMigration().build();

        //TODO Future Work, currently if the user enters Title/Description and then random Strings into Date and Time (but doesn't press Select Date or Select Time), and then presses Add task
        // It'll add the task with those random Strings representing Dates/Time. In the future, ideally they should only be able to add a task with valid Dates and Times
        // So currently can add wrong "Dates" and "Times"

        //TODO Future Work -> Maybe make it so the task inputting and adding button half of the XML is part of a fragment, or something so that we have more space for the actual list and cleaner UI

        //Getting what User along with their specific Course from the previous ListView got us here
        Intent intent_fromCourse = getIntent();
        int courseID = intent_fromCourse.getIntExtra("courseID", 0);
        String usName = intent_fromCourse.getStringExtra("usName");
        String courseName = intent_fromCourse.getStringExtra("courseName");

        setTitle(courseName + " Tasks");

        //This is to display pre-existing TodoList tasks for the specific User and Course
        showItemdata(usName, courseID);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = titleInput.getText().toString();
                String description = descriptionInput.getText().toString();
                String date = dateInput.getText().toString();
                String time = timeInput.getText().toString();

                if (!title.isEmpty() && !description.isEmpty() && !date.isEmpty() && !time.isEmpty()) {
                    String task = title + " - " + description + " - " + date + " - " + time;
                    Item item = new Item();
                    item.setItem(task);
                    item.setCourseID(courseID);
                    item.setUsName(usName);
                    executorService.execute(new Runnable() { //For running in a separate thread because this is for database entry
                        @Override
                        public void run() {
                            long l = mydb.itemDao().insertData(item);
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if(l>0){ //means some row has been changed/modified
                                        Toast.makeText(ToDoList.this, "New task inserted!", Toast.LENGTH_SHORT).show();
                                    }
                                    else{ //Not inserted/failed
                                        Toast.makeText(ToDoList.this, "New task was not been created...", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });
                    titleInput.setText("");
                    descriptionInput.setText("");
                    dateInput.setText("");
                    timeInput.setText("");
                    showItemdata(usName, courseID);
                } else {
                    Toast.makeText(ToDoList.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Get references to the Date and Time buttons
        Button dateButton = findViewById(R.id.date_button);
        Button timeButton = findViewById(R.id.time_button);

        // Set onClickListeners for the Date and Time buttons
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the current date from the Date input field, or use today's date if it's empty
                String dateString = dateInput.getText().toString();
                LocalDate date = null;

                //This is to prevent a crash if the user enters a random String into the field and then presses the date button
                try{
                    date = dateString.isEmpty() ? LocalDate.now() : LocalDate.parse(dateString);
                } catch (DateTimeParseException e){
                    Toast.makeText(ToDoList.this, "Please enter a valid date in the format: YYYY-MM-DD", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Create a DatePickerDialog and show it
                DatePickerDialog datePicker = new DatePickerDialog(ToDoList.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                                // Update the Date input field with the selected date
                                dateInput.setText(String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth));
                            }
                        },
                        date.getYear(), date.getMonthValue() - 1, date.getDayOfMonth());
                datePicker.show();
            }
        });

        //Time Button
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the current time from the Time input field, or use the current time if it's empty
                String timeString = timeInput.getText().toString();
                LocalTime time = null;

                //Same check here as Date, to prevent a crash if user enters random String
                try{
                    time = timeString.isEmpty() ? LocalTime.now() : LocalTime.parse(timeString);
                } catch (DateTimeParseException e){
                    Toast.makeText(ToDoList.this, "Please enter a valid 24 hour time in the format HH:MM", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Create a TimePickerDialog and show it
                TimePickerDialog timePicker = new TimePickerDialog(ToDoList.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                                // Update the Time input field with the selected time
                                timeInput.setText(String.format("%02d:%02d", hourOfDay, minute));
                            }
                        },
                        time.getHour(), time.getMinute(), true);
                timePicker.show();
            }
        });

        //DeleteTask Functionality from the TodoList ListView
        todoList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Item item = (Item)customItemListAdapter.getItem(position);

                //Confirmation box to make sure we want to delete the task
                AlertDialog.Builder confirm = new AlertDialog.Builder(ToDoList.this);
                confirm.setTitle("Delete Task");
                confirm.setMessage("Are you sure you want to delete this?");
                confirm.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        executorService.execute(new Runnable() {
                            @Override
                            public void run() {
                                int del = mydb.itemDao().deleteData(item);
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (del > 0){
                                            customItemListAdapter.removeList(position);
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
                return true;
            }
        });
    }

    public void showItemdata(String username, int courseID){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                List<Item> itemList = mydb.itemDao().getAllTasksforUserCourse(username, courseID);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        customItemListAdapter = new CustomItemListAdapter(ToDoList.this, itemList);
                        todoList.setAdapter(customItemListAdapter);
                    }
                });
            }
        });
    }
}
