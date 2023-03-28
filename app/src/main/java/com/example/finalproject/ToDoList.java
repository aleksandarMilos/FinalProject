package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.HandlerCompat;
import androidx.room.Room;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ToDoList extends AppCompatActivity {

    private EditText titleInput;
    private EditText descriptionInput;
    private EditText dateInput;
    private EditText timeInput;
    private Button addButton;
    private ListView todoList;

    //Database functionality
    MyDatabaseItem mydbItem;
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

        mydbItem = Room.databaseBuilder(getApplicationContext(), MyDatabaseItem.class, "TodoItem_table").fallbackToDestructiveMigration().build(); //Building the database once

        //Showing our to-list previously entered stuff via database
        showItemdata();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = titleInput.getText().toString();
                String description = descriptionInput.getText().toString();
                String date = dateInput.getText().toString();
                String time = timeInput.getText().toString();

                if (!title.isEmpty() && !description.isEmpty() && !date.isEmpty() && !time.isEmpty()) {
                    String task = title + " - " + description + " - " + date + " - " + time;
                    //tasks.add(task);
                    //adapter.notifyDataSetChanged();
                    Item item = new Item();
                    item.setItem(task);
                    executorService.execute(new Runnable() { //For running in a separate thread because this is for database entry
                        @Override
                        public void run() {
                            long l = mydbItem.itemDao().insertData(item);
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
                    showItemdata();
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
                LocalDate date = dateString.isEmpty() ? LocalDate.now() : LocalDate.parse(dateString);

                // Create a DatePickerDialog and show it
                DatePickerDialog datePicker = new DatePickerDialog(ToDoList.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                                // Update the Date input field with the selected date
                                dateInput.setText(String.format("%04d/%02d/%02d", year, month + 1, dayOfMonth));
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
                LocalTime time = timeString.isEmpty() ? LocalTime.now() : LocalTime.parse(timeString);

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

        //Remove functionality from ListView
        todoList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Item item = (Item)customItemListAdapter.getItem(position);
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        int del = mydbItem.itemDao().deleteData(item);
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
                return true;
            }
        });
    }

    public void showItemdata(){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                List<Item> itemList = mydbItem.itemDao().getAllItem();
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
