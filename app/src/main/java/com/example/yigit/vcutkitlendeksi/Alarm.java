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

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Alarm extends AppCompatActivity  implements TimePickerDialog.OnTimeSetListener{

    int kalanMiktar;
    ListeAdapter adapter;
    ListView liste;
    TextView kalanMiktarTV;
    ArrayList<AlarmEleman> alarmlar = new ArrayList<>();
    FloatingActionButton ekleButon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        liste = findViewById(R.id.liste);
        kalanMiktarTV = findViewById(R.id.txt_kalan_miktar);
        ekleButon = findViewById(R.id.btn_ekle);
        ekleButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });

        adapter = new ListeAdapter(Alarm.this, R.layout.alarm_liste_elemani, alarmlar);
        liste.setAdapter(adapter);

    }

    private void alarmEkle() {
        DialogFragment timePicker = new TimePickerFragment();
        timePicker.show(getSupportFragmentManager(), "time picker");
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        String saat = DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());
        AlarmEleman alarm = new AlarmEleman(saat, 1);
        alarmlar.add(alarm);
        adapter.notifyDataSetChanged();
    }
}
