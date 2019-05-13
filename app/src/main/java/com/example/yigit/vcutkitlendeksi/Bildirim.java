package com.example.yigit.vcutkitlendeksi;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

public class Bildirim extends BroadcastReceiver {

    int MID = 0;
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "laas", Toast.LENGTH_LONG).show();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(context);

        mNotifyBuilder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Drink Water Title")
                .setContentText("Drink Water!!")
                .setSound(alarmSound)
                //.setAutoCancel(true).setWhen(when)
                //.setContentIntent(pendingIntent)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});

        notificationManager.notify(MID, mNotifyBuilder.build());
        MID++;
    }
}
