package com.example.yigit.vcutkitlendeksi;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AlarmGuncelleSilDialog extends AppCompatDialogFragment {
    TimePicker timePicker;
    SeekBar seekBar;
    TextView bardakAdetiTxt;
    Button alarmSilBtn, alarmGuncelleBtn;


    VeritabaniYonetici vy;

    int alarmId;
    String zaman;
    int adet;
    int kalanMiktar;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_alarm_duzenle_sil, null);
        builder.setView(view);

        timePicker = view.findViewById(R.id.timePicker1);
        seekBar = view.findViewById(R.id.seekBar);
        bardakAdetiTxt = view.findViewById(R.id.txt_adet);
        alarmGuncelleBtn =view.findViewById(R.id.btn_guncelle);
        alarmSilBtn = view.findViewById(R.id.btn_sil);
        vy = new VeritabaniYonetici(getContext());

        timePicker.setIs24HourView(true);

        Bundle bundle = getArguments();
        alarmId = bundle.getInt("id");
        zaman = bundle.getString("saat");
        adet = bundle.getInt("adet");
        kalanMiktar=bundle.getInt("kalan_miktar");

        int gelenSaat = formatiDegistir(zaman)[0];
        int gelenDakika = formatiDegistir(zaman)[1];
        if (Build.VERSION.SDK_INT >= 23 ) {
            timePicker.setHour(gelenSaat);
            timePicker.setMinute(gelenDakika);
        }
        else {
            timePicker.setCurrentHour(gelenSaat);
            timePicker.setCurrentMinute(gelenDakika);
        }
        bardakAdetiTxt.setText(String.valueOf(adet));
        seekBar.setProgress(adet-1);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
                bardakAdetiTxt.setText(String.valueOf(progress + 1));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        alarmSilBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vy.alarmSil(alarmId);
                alarmIptalEt();
                dismiss();
            }
        });

        alarmGuncelleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (kalanMiktar<(seekBar.getProgress()+1)-adet){
                    Toast.makeText(getContext(),"İçmeniz gereken su miktarı aşıldı.",Toast.LENGTH_LONG).show();
                    return;
                }
                int saat;
                int dakika;
                if (Build.VERSION.SDK_INT >= 23 ) {
                    saat = timePicker.getHour();
                    dakika = timePicker.getMinute();
                }
                else {
                    saat = timePicker.getCurrentHour();
                    dakika = timePicker.getCurrentMinute();
                }

                Calendar secilenZaman = Calendar.getInstance();
                secilenZaman.set(Calendar.HOUR_OF_DAY, saat);
                secilenZaman.set(Calendar.MINUTE, dakika);
                secilenZaman.set(Calendar.SECOND, 0);
                String guncellenenSaat = DateFormat.getTimeInstance(DateFormat.SHORT).format(secilenZaman.getTime());
                Alarm alarm = new Alarm();
                alarm.setId(alarmId);
                alarm.setSaat(guncellenenSaat);
                alarm.setAdet(seekBar.getProgress() + 1);
                vy.alarmGuncelle(alarm);
                alarmGuncelle(secilenZaman, alarmId);
                dismiss();
            }
        });

        return builder.create();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        final Activity activity = getActivity();
        if (activity instanceof DialogInterface.OnDismissListener) {
            ((DialogInterface.OnDismissListener) activity).onDismiss(dialog);
        }
    }

    private int[] formatiDegistir(String datetime) {
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String saatOutputPattern = "HH";
        String dakikaOutputPattern = "mm";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern, Locale.getDefault());
        SimpleDateFormat saatOutputFormat = new SimpleDateFormat(saatOutputPattern, Locale.getDefault());
        SimpleDateFormat dakikaOutputFormat = new SimpleDateFormat(dakikaOutputPattern, Locale.getDefault());

        int[] sonuclar = new int[2];
        try {
            Date date = inputFormat.parse(datetime);
            sonuclar[0] = Integer.parseInt(saatOutputFormat.format(date));
            sonuclar[1] = Integer.parseInt(dakikaOutputFormat.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sonuclar;
    }

    private void alarmIptalEt(){
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        Intent myIntent = new Intent(getContext().getApplicationContext(), Bildirim.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getContext().getApplicationContext(), alarmId, myIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.cancel(pendingIntent);
    }

    private void alarmGuncelle(Calendar secilenZaman, int requestCode) {
        AlarmManager alarmManager = (AlarmManager)getContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getContext(), Bildirim.class);
        intent.putExtra("adet", vy.idyeGoreAdetGetir(requestCode));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), requestCode, intent, 0);
        if (android.os.Build.VERSION.SDK_INT >= 19) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, secilenZaman.getTimeInMillis(), pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, secilenZaman.getTimeInMillis(), pendingIntent);
        }
    }
}
