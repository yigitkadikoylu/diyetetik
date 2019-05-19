package com.example.yigit.vcutkitlendeksi;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import me.himanshusoni.quantityview.QuantityView;

public class ListeAdapter extends ArrayAdapter<Alarm> {

    private Context context;
    private int resource;
    VeritabaniYonetici vy = new VeritabaniYonetici(getContext());

    public ListeAdapter(@NonNull Context context, int resource, ArrayList<Alarm> alarm) {
        super(context, resource, alarm);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(final int position, @NonNull View convertView, @NonNull ViewGroup parent) {

        String saat = formatiDegistir(getItem(position).getSaat());
        int adet = getItem(position).getAdet();

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(R.layout.alarm_liste_elemani, parent, false);

        TextView saatTV = convertView.findViewById(R.id.txt_saat);
        TextView miktarTV = convertView.findViewById(R.id.txt_miktar);

        saatTV.setText(saat);
        miktarTV.setText(String.valueOf(adet));

        return convertView;
    }
    private String formatiDegistir(String datetime) {
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "HH:mm";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern, Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern, Locale.getDefault());

        Date date = null;
        String sonuc = null;

        try {
            date = inputFormat.parse(datetime);
            sonuc = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sonuc;
    }
}