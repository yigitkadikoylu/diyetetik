package com.example.yigit.vcutkitlendeksi;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;

public class AlarmKurDialog extends AppCompatDialogFragment {

    TimePicker timePicker;
    SeekBar seekBar;
    TextView bardakAdetiTxt;
    Button alarmEkleBtn;

    int bardakSayisi;
    int saat;
    int dakika;

    AlarmKurDialogListener listener;

    public interface AlarmKurDialogListener{
        void alarmBilgisiGetir(int saat, int dakika, int bardakSayisi);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_alarm_kur, null);
        builder.setView(view);

        timePicker = view.findViewById(R.id.timePicker1);
        seekBar = view.findViewById(R.id.seekBar);
        bardakAdetiTxt = view.findViewById(R.id.txt_adet);
        alarmEkleBtn = view.findViewById(R.id.button);

        timePicker.setIs24HourView(true);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
                bardakSayisi = 1 + progress;
                bardakAdetiTxt.setText(String.valueOf(bardakSayisi));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });


        alarmEkleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23 ) {
                saat = timePicker.getHour();
                dakika = timePicker.getMinute();
            }
            else {
                saat = timePicker.getCurrentHour();
                dakika = timePicker.getCurrentMinute();
            }
                listener.alarmBilgisiGetir(saat, dakika, bardakSayisi);
                dismiss();
            }
        });

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            listener = (AlarmKurDialogListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString() + "AlarmKurDialogListener implement etmek gerekli");
        }
    }
}
