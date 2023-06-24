package com.example.birthdayreminderapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;

public class ViewListOfBirthdays extends AppCompatActivity {

    private Button goBackButton;
    private Button deleteEntryButton;
    private int selectedEntry;
    private ListView listOfBirthdays;
    private ArrayList<String> listOfBirthdayItems;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_list_of_birthdays);
        goBackButton = (Button) findViewById(R.id.backButton);
        deleteEntryButton = (Button) findViewById(R.id.deleteEntry);
        listOfBirthdays = (ListView) findViewById(R.id.listOfBirthdays);
        mSharedPreferences = getSharedPreferences(AddEntry.SAVE_LOCATION,MODE_PRIVATE);
        goBackButton.setOnClickListener(mGoBack);
        deleteEntryButton.setOnClickListener(mDeleteEntry);
        updateList();
    }


    public View.OnClickListener mGoBack = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    };

    public AdapterView.OnItemClickListener clickListItem = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            selectedEntry = i;
        }
    };

    public View.OnClickListener mDeleteEntry = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (selectedEntry == 0){
                Toast.makeText(ViewListOfBirthdays.this, "Select a valid entry first", Toast.LENGTH_LONG).show();
            } else {
                String entryString = listOfBirthdayItems.get(selectedEntry);
                String key = entryString.substring(0, entryString.lastIndexOf(":"));
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.remove(key);
                editor.commit();
                updateList();
                String toastText = "Deleted birthday entry for '" + key + "'!";
                Toast.makeText(ViewListOfBirthdays.this, toastText, Toast.LENGTH_LONG).show();
            }
        }
    };

    private void updateList(){
        listOfBirthdayItems = returnAllBirthdays();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                listOfBirthdayItems);
        listOfBirthdays.setAdapter(arrayAdapter);
        listOfBirthdays.setOnItemClickListener(clickListItem);
    }

    private ArrayList<String> returnAllBirthdays(){
        ArrayList<String> arrayOfAllBirthdays = new ArrayList<>();
        arrayOfAllBirthdays.add("NAME:     BIRTHDAY");
        Map<String, ?> allBirthdays = mSharedPreferences.getAll();
        for (Map.Entry<String, ?> entry: allBirthdays.entrySet()){
            arrayOfAllBirthdays.add(entry.getKey() + ":   " + entry.getValue());
        }
        return arrayOfAllBirthdays;
    }
}