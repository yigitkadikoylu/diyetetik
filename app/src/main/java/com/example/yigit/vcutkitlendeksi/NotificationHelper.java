package com.example.yigit.vcutkitlendeksi;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.support.v4.app.NotificationCompat;

import static android.os.Build.*;

public class NotificationHelper extends ContextWrapper {
    public static final String channelID = "channelID";
    public static final String channelName = "Channel Name";

    private NotificationManager mManager;

    int adet;

    public NotificationHelper(Context base, int adet) {
        super(base);
        this.adet = adet;
        if (VERSION.SDK_INT >= VERSION_CODES.O) {
            createChannel();
        }
    }

    @TargetApi(VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);

        getManager().createNotificationChannel(channel);
    }

    public NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        return mManager;
    }

    public NotificationCompat.Builder getChannelNotification() {
        return new NotificationCompat.Builder(getApplicationContext(), channelID)
                .setContentTitle("Su içme zamanınız geldi")
                .setContentText(adet + " bardak su için.")
                .setSmallIcon(R.drawable.ic_damla);
    }
}
