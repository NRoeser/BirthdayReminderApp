package com.example.birthdayreminderapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class ReminderBroadcast extends BroadcastReceiver {
    public static int ID = 200;
    @Override
    public void onReceive(Context context, Intent intent) {
        //Get name and new age from intent extra
        String extra = intent.getStringExtra("Name");
        String name = extra.substring(0,extra.lastIndexOf("?"));
        String year = extra.substring(extra.lastIndexOf("?")+1);
        //Create Notification and set icons, text and title
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "birthdayReminder")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Birthday!")
                .setContentText("It is " + name + "'s birthday today! They are getting " + year + " years old!")
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            throw new RuntimeException("Not the necessary permissions granted!");
        }
        notificationManagerCompat.notify(ID, builder.build());
    }


}
