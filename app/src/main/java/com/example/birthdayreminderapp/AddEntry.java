package com.example.birthdayreminderapp;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddEntry extends AppCompatActivity {

    private Button saveDate;
    private Button goToMainMenu;
    private EditText newNameField;
    private DatePicker birthdayPicker;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences reminderTimePreferences;

    private static final Map<Integer, String> monthStringMap = new HashMap<Integer, String>(){{
        put(1, "Jan");
        put(2, "Feb");
        put(3, "Mar");
        put(4, "Apr");
        put(5, "May");
        put(6, "Jun");
        put(7, "Jul");
        put(8, "Aug");
        put(9, "Sep");
        put(10, "Oct");
        put(11, "Now");
        put(12, "Dez");
    }};
    public static final String SAVE_LOCATION = "BIRTHDAYS";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_entry);
        goToMainMenu = (Button) findViewById(R.id.entryToMainMenuButton);
        saveDate = (Button) findViewById(R.id.saveNewEntry);
        birthdayPicker = (DatePicker) findViewById(R.id.chooseBirthdate);
        newNameField = (EditText) findViewById(R.id.newNameField);
        goToMainMenu.setOnClickListener(mgoToMainMenu);
        saveDate.setOnClickListener(mSaveDate);
        createNotificationChannel();
        mSharedPreferences = getSharedPreferences(SAVE_LOCATION, MODE_PRIVATE);
        reminderTimePreferences = getSharedPreferences(MainActivity.SAVE_LOCATION, MODE_PRIVATE);

    }


    private View.OnClickListener mSaveDate = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //Check if name field is not empty (no empty names allowed)
            String name = newNameField.getText().toString();
            if (name.equals("")){
                Toast.makeText(AddEntry.this, "Name cannot be empty!", Toast.LENGTH_LONG).show();
                return;
            }
            //Get date from date picker and save it in SharedPreferences
            //Key = NAME, Value = BIRTH DATE
            int day = birthdayPicker.getDayOfMonth();
            int month = birthdayPicker.getMonth();
            int year = birthdayPicker.getYear();
            String date = createDateString(day, month + 1, year);
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putString(name, date);
            //Update user on whether saving was successful
            if (editor.commit()){
                String toastText = "Saved birthday and made reminder for '" + name + "'!";
                Toast.makeText(AddEntry.this, toastText, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(AddEntry.this, "Was not able to make change", Toast.LENGTH_LONG).show();
            }

            //Create notifications for the next 4 years
            for (int i = 0; i<4; i++) {
                Calendar currentDate = Calendar.getInstance();
                Calendar myAlarmDate = Calendar.getInstance();

                //Create date and time for notification using the saved hour and minute settings
                myAlarmDate.set(Calendar.YEAR, currentDate.get(Calendar.YEAR) + i);
                myAlarmDate.set(Calendar.MONTH, month);
                //Unsure about why day need to be -1, sometimes the date picker was correct
                //Sometimes, the date picker returned a day after day that was picked for some reason
                myAlarmDate.set(Calendar.DATE, day-1);
                myAlarmDate.set(Calendar.HOUR, reminderTimePreferences.getInt(MainActivity.HOUR_KEY, 10));
                myAlarmDate.set(Calendar.MINUTE,reminderTimePreferences.getInt(MainActivity.MINUTE_KEY, 0));



                //Create pending intent for AlarmManager
                Intent intent = new Intent(AddEntry.this, ReminderBroadcast.class);
                String nameAndAge = name + "?" +(currentDate.get(Calendar.YEAR)-year);
                intent.putExtra("Name", nameAndAge);
                //Retrieve request code and increase it so it stays unique
                int requestCode = reminderTimePreferences.getInt("requestCode", 0);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(AddEntry.this,
                        requestCode, intent, PendingIntent.FLAG_MUTABLE);
                requestCode++;
                SharedPreferences.Editor requestCodeEditor = reminderTimePreferences.edit();
                requestCodeEditor.putInt("requestCode", requestCode);
                requestCodeEditor.commit();
                //Use AlarmManager to create Notification
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, myAlarmDate.getTimeInMillis(), pendingIntent);


            }
        }
    };

    private View.OnClickListener mgoToMainMenu = new View.OnClickListener() {
        @Override
        //Return to MainActivity
        public void onClick(View view) {
            finish();
        }
    };

    public static String createDateString(int day, int month, int year){
        //Format birth date string given the day, month and year
        String monthString = AddEntry.monthStringMap.get(month);
        String date = day + "." + monthString + "." + year;
        return date;
    }

    private void createNotificationChannel(){
        //Create Notification Channel for birthday reminder notifications
        String name = "birthdayReminder";
        String description = "Notification channel for the birthday reminder app";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel("birthdayReminder", name, importance);
        channel.setDescription(description);

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

}
