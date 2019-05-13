package com.example.yigit.vcutkitlendeksi;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import me.himanshusoni.quantityview.QuantityView;

public class AlarmKurActivity extends AppCompatActivity  implements TimePickerDialog.OnTimeSetListener{

    public static int kalanMiktar;
    ListeAdapter adapter;
    ListView liste;
    TextView kalanMiktarTV;
    ArrayList<Alarm> alarmlar = new ArrayList<>();
    FloatingActionButton ekleButon;
    VeritabaniYonetici vy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        liste = findViewById(R.id.liste);
        kalanMiktarTV = findViewById(R.id.txt_kalan_miktar);
        ekleButon = findViewById(R.id.btn_ekle);
        kalanMiktar = 15;

        vy = new VeritabaniYonetici(AlarmKurActivity.this);
        alarmlar = vy.butunAlarmlariGetir();
        adapter = new ListeAdapter(AlarmKurActivity.this, R.layout.alarm_liste_elemani, alarmlar);
        liste.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        kalanMiktar -= vy.toplamMiktarGetir();
        kalanMiktarTV.setText(String.valueOf(kalanMiktar));

        ekleButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });

        adapter = new ListeAdapter(AlarmKurActivity.this, R.layout.alarm_liste_elemani, alarmlar);
        liste.setAdapter(adapter);
/*
        for (int i=0;i<adapter.getCount();i++){
            View view = adapter.getView(i, null, liste);
            QuantityView quantityView = view.findViewById(R.id.quantityView);
            quantityView.setOnQuantityChangeListener(new QuantityView.OnQuantityChangeListener() {
                @Override
                public void onQuantityChanged(int oldQuantity, int newQuantity, boolean programmatically) {
                    kalanMiktar -= (newQuantity - oldQuantity);
                    kalanMiktarTV.setText(String.valueOf(kalanMiktar));
                }

                @Override
                public void onLimitReached() {

                }
            });
        }
        */
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar suankiZaman = Calendar.getInstance();
        Calendar secilenZaman = Calendar.getInstance();
        secilenZaman.set(Calendar.HOUR_OF_DAY, hourOfDay);
        secilenZaman.set(Calendar.MINUTE, minute);
        if(suankiZaman.getTimeInMillis() <= secilenZaman.getTimeInMillis()) {
            String saat = DateFormat.getTimeInstance(DateFormat.SHORT).format(secilenZaman.getTime());
            Alarm alarm = new Alarm(saat, 1);
            if(vy.alarmEkle(alarm) == -1){
                Toast.makeText(AlarmKurActivity.this, "Hata Oluştu", Toast.LENGTH_LONG).show();
                return;
            }
            alarmlar.clear();
            alarmlar = vy.butunAlarmlariGetir();
            adapter = new ListeAdapter(AlarmKurActivity.this, R.layout.alarm_liste_elemani, alarmlar);
            liste.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }else {
            Toast.makeText(AlarmKurActivity.this, "Seçilen zaman geride kaldı.", Toast.LENGTH_LONG).show();
        }
    }
}
