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

    @Override
    public void onReceive(Context context, Intent intent) {
        int adet = intent.getIntExtra("adet", 1);
        NotificationHelper notificationHelper = new NotificationHelper(context, adet);
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification();
        notificationHelper.getManager().notify(1, nb.build());
    }
}
