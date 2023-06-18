package com.example.birthdayreminderapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AddEntry extends AppCompatActivity {


    private Button saveDate;
    private Button goToMainMenu;
    private TextView dateText;
    private DatePicker birthdayPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_entry);
        goToMainMenu = (Button) findViewById(R.id.entryToMainMenuButton);
        saveDate = (Button) findViewById(R.id.saveNewEntry);
        dateText = (TextView) findViewById(R.id.selectedDate);
        birthdayPicker = (DatePicker) findViewById(R.id.chooseBirthdate);
        goToMainMenu.setOnClickListener(mgoToMainMenu);
        saveDate.setOnClickListener(msaveDate);

    }


    private View.OnClickListener msaveDate = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int day = birthdayPicker.getDayOfMonth();
            int month = birthdayPicker.getMonth() + 1;
            int year = birthdayPicker.getYear();
            String date = day + " " + month + " " + year;
            dateText.setText(date);
        }
    };

    private View.OnClickListener mgoToMainMenu = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(AddEntry.this, MainActivity.class);
            startActivity(intent);
        }
    };

}
