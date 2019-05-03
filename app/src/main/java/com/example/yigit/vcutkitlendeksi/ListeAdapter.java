package com.example.yigit.vcutkitlendeksi;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import me.himanshusoni.quantityview.QuantityView;

public class ListeAdapter extends ArrayAdapter<AlarmEleman> {

    private Context context;
    private int resource;

    public ListeAdapter(@NonNull Context context, int resource, ArrayList<AlarmEleman> alarm) {
        super(context, resource, alarm);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @NonNull View convertView, @NonNull ViewGroup parent) {

        String saat = getItem(position).getSaat();
        int adet = getItem(position).getAdet();

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(R.layout.alarm_liste_elemani, parent, false);

        TextView saatTV = convertView.findViewById(R.id.txt_saat);
        QuantityView quantityView = convertView.findViewById(R.id.quantityView);

        saatTV.setText(saat);
        quantityView.setQuantity(adet);

        return convertView;
    }
}