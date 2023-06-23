package com.example.birthdayreminderapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class ReminderBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String name = intent.getStringExtra("Name");
        String age = intent.getStringExtra("Age");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "birthdayReminder")
                .setContentTitle("Birthday!")
                .setContentText("It is " + name + "'s birthday today! They are getting " + age + " years old!")
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);

        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            throw new RuntimeException("Not the necessary permissions granted!");
        }
        notificationManagerCompat.notify(100, builder.build());
    }


}
