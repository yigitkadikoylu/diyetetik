package com.example.yigit.vcutkitlendeksi;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import me.himanshusoni.quantityview.QuantityView;

public class AlarmKurActivity extends AppCompatActivity  implements AlarmKurDialog.AlarmKurDialogListener, DialogInterface.OnDismissListener {

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
            }
        });

        liste.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Alarm alarm = alarmlar.get(position);

                Bundle bundle = new Bundle();
                bundle.putInt("id", alarm.getId());
                bundle.putString("saat", alarm.getSaat());
                bundle.putInt("adet", alarm.getAdet());
                bundle.putInt("kalan_miktar",kalanMiktar);
                DialogFragment dialog = new AlarmGuncelleSilDialog();
                dialog.setArguments(bundle);
                dialog.show(AlarmKurActivity.this.getSupportFragmentManager(), "Alarm Düzenle - Sil");

            }
        });

        adapter = new ListeAdapter(AlarmKurActivity.this, R.layout.alarm_liste_elemani, alarmlar);
        liste.setAdapter(adapter);

    }

    private void alarmKur(Calendar secilenZaman, int requestCode) {
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, Bildirim.class);
        intent.putExtra("adet", vy.idyeGoreAdetGetir(requestCode));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, requestCode, intent, 0);
        if (android.os.Build.VERSION.SDK_INT >= 19) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, secilenZaman.getTimeInMillis(), pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, secilenZaman.getTimeInMillis(), pendingIntent);
        }
    }

    @Override
    public void alarmBilgisiGetir(int gelenSaat, int gelenDakika, int bardakSayisi) {
        if(kalanMiktar < bardakSayisi){
            Toast.makeText(AlarmKurActivity.this, "Daha fazla su içmeyin.", Toast.LENGTH_LONG).show();
            return;
        }
        Calendar suankiZaman = Calendar.getInstance();
        Calendar secilenZaman = Calendar.getInstance();
        secilenZaman.set(Calendar.HOUR_OF_DAY, gelenSaat);
        secilenZaman.set(Calendar.MINUTE, gelenDakika);
        secilenZaman.set(Calendar.SECOND, 0);
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
            kalanMiktar -= bardakSayisi;
            kalanMiktarTV.setText(String.valueOf(kalanMiktar));
        }else {
            Toast.makeText(AlarmKurActivity.this, "Seçilen zaman geride kaldı.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        alarmlar.clear();
        alarmlar = vy.butunAlarmlariGetir();
        adapter = new ListeAdapter(AlarmKurActivity.this, R.layout.alarm_liste_elemani, alarmlar);
        liste.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        finish();
        startActivity(getIntent());
    }
}
