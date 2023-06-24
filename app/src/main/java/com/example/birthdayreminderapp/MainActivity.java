package com.example.birthdayreminderapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Time;

public class MainActivity extends AppCompatActivity {


    private Button saveTime;
    private Button goToAddEntry;
    private Button goToListOfBirthdays;
    private SharedPreferences mSharedPreferences;
    private TimePicker reminderPicker;

    public static final String SAVE_LOCATION = "REMINDER_TIME_SAVE";
    public static final String HOUR_KEY = "hour";
    public static final String MINUTE_KEY = "minute";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        goToAddEntry = (Button) findViewById(R.id.goToAddEntryMenu);
        saveTime = (Button) findViewById(R.id.saveTime);
        goToListOfBirthdays = (Button) findViewById(R.id.viewListOfBirthdays);
        reminderPicker = (TimePicker) findViewById(R.id.reminderPicker);
        reminderPicker.setIs24HourView(true);
        goToAddEntry.setOnClickListener(mGoToAddEntryMenu);
        saveTime.setOnClickListener(saveReminderTime);
        goToListOfBirthdays.setOnClickListener(mGoToListOfBirthdays);
        mSharedPreferences = getSharedPreferences(SAVE_LOCATION, MODE_PRIVATE);
    }


    public View.OnClickListener saveReminderTime = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //Get time from picker
            int hour = reminderPicker.getHour();
            int minute = reminderPicker.getMinute();
            //Save time in preferences
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putInt(HOUR_KEY, hour);
            editor.putInt(MINUTE_KEY, minute);
            //Update user on whether saving of new time was successful
            if (editor.commit()){
                Toast.makeText(MainActivity.this, "Changed reminder time!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(MainActivity.this, "Could not make change", Toast.LENGTH_LONG).show();
            }
        }
    };

    public View.OnClickListener mGoToAddEntryMenu = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //Go to and start AddEntry Activity
            Intent intent = new Intent(MainActivity.this, AddEntry.class);
            startActivity(intent);
        }
    };

    public View.OnClickListener mGoToListOfBirthdays = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //Go to and start ViewListOfBirthdays activity
            Intent intent = new Intent(MainActivity.this, ViewListOfBirthdays.class);
            startActivity(intent);
        }
    };
}