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
import java.util.Map;

public class AddEntry extends AppCompatActivity {

    private Button saveDate;
    private Button goToMainMenu;
    private EditText newNameField;
    private DatePicker birthdayPicker;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences reminderTimePreferences;
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
            String name = newNameField.getText().toString();
            if (name.equals("")){
                Toast.makeText(AddEntry.this, "Name cannot be empty!", Toast.LENGTH_LONG).show();
                return;
            }
            int day = birthdayPicker.getDayOfMonth();
            int month = birthdayPicker.getMonth();
            int year = birthdayPicker.getYear();
            String date = createDateString(day, month + 1, year);
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putString(name, date);
            String toastText = "Saved birthday and made reminder for '" + name + "'!";
            if (editor.commit()){
                Toast.makeText(AddEntry.this, toastText, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(AddEntry.this, "Was not able to make change", Toast.LENGTH_LONG).show();
            }

            for (int i = 0; i<4; i++) {
                Calendar currentDate = Calendar.getInstance();
                Calendar myAlarmDate = Calendar.getInstance();

                myAlarmDate.set(Calendar.YEAR, currentDate.get(Calendar.YEAR) + i);
                myAlarmDate.set(Calendar.MONTH, month);
                myAlarmDate.set(Calendar.DATE, day);
                myAlarmDate.set(Calendar.HOUR, reminderTimePreferences.getInt(MainActivity.HOUR_KEY, 10));
                myAlarmDate.set(Calendar.MINUTE,reminderTimePreferences.getInt(MainActivity.MINUTE_KEY, 0));

                if (!myAlarmDate.getTime().before(currentDate.getTime())){
                    Intent intent = new Intent(AddEntry.this, ReminderBroadcast.class);
                    String nameAndAge = name + "?" +(currentDate.get(Calendar.YEAR)-year);
                    intent.putExtra("Name", nameAndAge);
                    int requestCode = reminderTimePreferences.getInt("requestCode", 0);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(AddEntry.this,
                            requestCode, intent, PendingIntent.FLAG_MUTABLE);
                    requestCode++;
                    SharedPreferences.Editor requestCodeEditor = reminderTimePreferences.edit();
                    requestCodeEditor.putInt("requestCode", requestCode);
                    requestCodeEditor.commit();
                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, myAlarmDate.getTimeInMillis(), pendingIntent);
                }

            }
        }
    };

    private View.OnClickListener mgoToMainMenu = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    };

    public static String createDateString(int day, int month, int year){
        String date = day + "." + month + "." + year;
        return date;
    }

    private void createNotificationChannel(){
        String name = "birthdayReminder";
        String description = "Notification channel for the birthday reminder app";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel("birthdayReminder", name, importance);
        channel.setDescription(description);

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

}
