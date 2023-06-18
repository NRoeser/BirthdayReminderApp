package com.example.birthdayreminderapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.sql.Time;

public class MainActivity extends AppCompatActivity {


    private Button saveDate;
    private Button goToAddEntry;
    private TextView dateText;
    private TimePicker reminderPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        goToAddEntry = (Button) findViewById(R.id.goToAddEntryMenu);
        reminderPicker = (TimePicker) findViewById(R.id.reminderPicker);
        reminderPicker.setIs24HourView(true);
        goToAddEntry.setOnClickListener(mgoToAddEntryMenu);
    }

    public View.OnClickListener mgoToAddEntryMenu = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this, AddEntry.class);
            startActivity(intent);
        }
    };

    public void goToMainMenu(View view){
        setContentView(R.layout.activity_main);
    }


}