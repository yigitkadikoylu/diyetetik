package com.example.yigit.vcutkitlendeksi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class VeritabaniYonetici extends SQLiteOpenHelper {

    private static final int VERITABANI_VERSION = 3;

    private static final String VERITABANI_ADI = "Diyetetik.db";

    private static final String TABLO_ALARM = "alarm";

    private static final String SUTUN_ALARM_ID = "id";
    private static final String SUTUN_ALARM_ZAMAN = "zaman";
    private static final String SUTUN_ALARM_MIKTAR = "miktar";

    private static final String ALARM_TABLOSU_OLUSTUR = "CREATE TABLE " + TABLO_ALARM + "(" +
            SUTUN_ALARM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            SUTUN_ALARM_ZAMAN + " DATETIME NOT NULL, " +
            SUTUN_ALARM_MIKTAR + " INTEGER NOT NULL DEFAULT 1" + ")";

    private static final String ALARM_TABLOSU_SIL = "DROP TABLE IF EXISTS " + TABLO_ALARM;

    public VeritabaniYonetici(Context context) {
        super(context, VERITABANI_ADI, null, VERITABANI_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ALARM_TABLOSU_OLUSTUR);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(ALARM_TABLOSU_SIL);
        onCreate(db);
    }

    public long alarmEkle(Alarm alarm){
        SQLiteDatabase db = this.getWritableDatabase();

        String bugunString = new SimpleDateFormat("yyyy-MM-dd ", Locale.getDefault()).format(new Date());
        String datetime = bugunString + alarm.getSaat() + ":00";

        ContentValues values = new ContentValues();
        values.put(SUTUN_ALARM_ZAMAN, datetime);
        values.put(SUTUN_ALARM_MIKTAR, alarm.getAdet());

        long degisenSatir = db.insert(TABLO_ALARM, null, values);
        db.close();
        return degisenSatir;
    }

    public void alarmSil(Alarm alarm){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLO_ALARM, SUTUN_ALARM_ID, new String[]{String.valueOf(alarm.getId())});
        db.close();
    }

    public int alarmGuncelle(Alarm alarm){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SUTUN_ALARM_ZAMAN, alarm.getSaat());
        values.put(SUTUN_ALARM_MIKTAR, alarm.getAdet());

        int degisenSatir = db.update(TABLO_ALARM, values, SUTUN_ALARM_ID + " = ?",
                new String[]{String.valueOf(alarm.getId())});
        db.close();
        return degisenSatir;
    }

    public ArrayList<Alarm> butunAlarmlariGetir(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Alarm> alarmlar = new ArrayList<>();
        String sorgu = "SELECT * FROM " + TABLO_ALARM;

        Log.i("yigit", sorgu);

        Cursor c = db.rawQuery(sorgu, null);
        if(c.moveToFirst()){
            do{
                String tarihSaat = c.getString(c.getColumnIndex(SUTUN_ALARM_ZAMAN));
                int miktar = c.getInt(c.getColumnIndex(SUTUN_ALARM_MIKTAR));

                Alarm alarm = new Alarm(tarihSaat, miktar);
                alarmlar.add(alarm);
            }while(c.moveToNext());
        }
        db.close();
        c.close();
        return alarmlar;
    }

}
