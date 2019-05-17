package com.example.yigit.vcutkitlendeksi;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import me.himanshusoni.quantityview.QuantityView;

public class AlarmKurActivity extends AppCompatActivity  implements AlarmKurDialog.AlarmKurDialogListener{

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
                DialogFragment dialog = new AlarmKurDialog();
                dialog.show(getSupportFragmentManager(), "Alarm Kur");
                //DialogFragment timePicker = new TimePickerFragment();
                //timePicker.show(getSupportFragmentManager(), "time picker");
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

    private void alarmKur(Calendar secilenZaman, int requestCode) {
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, Bildirim.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, requestCode, intent, 0);
        if (android.os.Build.VERSION.SDK_INT >= 19) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, secilenZaman.getTimeInMillis(), pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, secilenZaman.getTimeInMillis(), pendingIntent);
        }
    }

    @Override
    public void alarmBilgisiGetir(int gelenSaat, int gelenDakika, int bardakSayisi) {
        Calendar suankiZaman = Calendar.getInstance();
        Calendar secilenZaman = Calendar.getInstance();
        secilenZaman.set(Calendar.HOUR_OF_DAY, gelenSaat);
        secilenZaman.set(Calendar.MINUTE, gelenDakika);
        secilenZaman.set(Calendar.SECOND, 0);
        Log.i("mert", gelenSaat + ":" + gelenDakika);
        Log.i("mert", suankiZaman.get(Calendar.HOUR_OF_DAY) + ":" + suankiZaman.get(Calendar.MINUTE) + " - " +
                secilenZaman.get(Calendar.HOUR_OF_DAY) + ":" + secilenZaman.get(Calendar.MINUTE));
        if(suankiZaman.getTimeInMillis() <= secilenZaman.getTimeInMillis()) {
            String saat = DateFormat.getTimeInstance(DateFormat.SHORT).format(secilenZaman.getTime());
            Alarm alarm = new Alarm(saat, bardakSayisi);
            if(vy.alarmEkle(alarm) == -1){
                Toast.makeText(AlarmKurActivity.this, "Hata Oluştu", Toast.LENGTH_LONG).show();
                return;
            }
            int alarmId =  vy.sonAlarmIdGetir();
            alarmKur(secilenZaman, alarmId);
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
