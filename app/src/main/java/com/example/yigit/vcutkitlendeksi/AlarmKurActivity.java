package com.example.yigit.vcutkitlendeksi;

import android.app.TimePickerDialog;
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

public class AlarmKurActivity extends AppCompatActivity  implements TimePickerDialog.OnTimeSetListener{

    int kalanMiktar;
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

        vy = new VeritabaniYonetici(AlarmKurActivity.this);
        alarmlar = vy.butunAlarmlariGetir();
        adapter = new ListeAdapter(AlarmKurActivity.this, R.layout.alarm_liste_elemani, alarmlar);
        liste.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        ekleButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });

        adapter = new ListeAdapter(AlarmKurActivity.this, R.layout.alarm_liste_elemani, alarmlar);
        liste.setAdapter(adapter);

    }

    private void alarmEkle() {
        DialogFragment timePicker = new TimePickerFragment();
        timePicker.show(getSupportFragmentManager(), "time picker");
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
