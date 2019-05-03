package com.example.yigit.vcutkitlendeksi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements TextWatcher {
    private EditText edtboy;
    private TextView txtBoyDeger,txtKiloDeger,txtİdealKiloDeger,txtDurumAcikla;
    private SeekBar seekBarKilo;
    private RadioGroup radioGroupBayBayan;
    private RadioButton rdioBay,rdioBayan;
    private boolean erkekmi=true;
    private double boy=0.0;
    private int kilogram=50;
    private Button btnDevam;
    private Button.OnClickListener btnDevamOlayisleyicisi= new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                Intent intent=new Intent(getApplicationContext(),Alarm.class);
                startActivity(intent);

            }catch (Exception e){
                Toast.makeText(getApplicationContext(),e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    };
    private RadioGroup.OnCheckedChangeListener radioGroupBayBayanOlayİsleyicisi=new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId==R.id.rdioBay){
                erkekmi=true;

            }
            else if (checkedId==R.id.rdioBayan)
                erkekmi=false;
            güncelle();
        }
    };

    private SeekBar.OnSeekBarChangeListener seekBarOlayİsleyicisi=new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            kilogram=30+progress;
            güncelle();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtboy=(EditText)findViewById(R.id.edtBoy);
        txtKiloDeger=(TextView)findViewById(R.id.txtKiloDeger);
        txtBoyDeger=(TextView)findViewById(R.id.txtBoyDeger);
        txtDurumAcikla=(TextView)findViewById(R.id.txtDurumAcikla);
        txtİdealKiloDeger=(TextView)findViewById(R.id.txtİdealKiloDeger);
        seekBarKilo=(SeekBar)findViewById(R.id.seekBarKilo);
        radioGroupBayBayan=(RadioGroup)findViewById(R.id.radioGroupBayBayan);
        rdioBay=(RadioButton)findViewById(R.id.rdioBay);
        rdioBayan=(RadioButton)findViewById(R.id.rdioBayan);
        btnDevam=(Button)findViewById(R.id.btnDevam);

        güncelle();
        edtboy.addTextChangedListener(this);
        seekBarKilo.setOnSeekBarChangeListener(seekBarOlayİsleyicisi);
        radioGroupBayBayan.setOnCheckedChangeListener(radioGroupBayBayanOlayİsleyicisi);
        btnDevam.setOnClickListener(btnDevamOlayisleyicisi);





    }

    private void güncelle(){
        txtKiloDeger.setText(String.valueOf(kilogram)+"kg");
        txtBoyDeger.setText(String.valueOf(boy)+"m");
        int ideal_kiloBay=(int)(50+2.3*((boy*100*0.4)-60));
        int ideal_kiloBayan=(int)(45.5+2.3*((boy*100*0.4)-60));//www.boykilo.org tan alınmıstır
        double vucutkitleindeksi=kilogram/(boy*boy);
        if (erkekmi){
            //erkek ise
            txtİdealKiloDeger.setText(String.valueOf(ideal_kiloBay));
                if (vucutkitleindeksi<=17.5){
                    txtDurumAcikla.setText(R.string.cokzayif);
                }
                else if (vucutkitleindeksi>17.5&&vucutkitleindeksi<=20.7){
                    txtDurumAcikla.setText(R.string.zayif);
                }
                else if (vucutkitleindeksi>20.7&&vucutkitleindeksi<=26.4){
                    txtDurumAcikla.setText(R.string.sagliklikiloagirligi);
                }
                else if (vucutkitleindeksi>26.4&&vucutkitleindeksi<=27.8){
                    txtDurumAcikla.setText(R.string.normalkilodanfazla);
                }
                else if (vucutkitleindeksi>27.8&&vucutkitleindeksi<=31.1){
                    txtDurumAcikla.setText(R.string.fazlakilolu);
                }
                else if (vucutkitleindeksi>31.1&&vucutkitleindeksi<=34.9){
                    txtDurumAcikla.setText(R.string.cokfazlakilolu);
                }
                else if (vucutkitleindeksi>34.9&&vucutkitleindeksi<=40){
                    txtDurumAcikla.setText(R.string.yüksekrisk);
                }
                else if (vucutkitleindeksi>40&&vucutkitleindeksi<=50){
                    txtDurumAcikla.setText(R.string.hastalıklı);
                }
                else if (vucutkitleindeksi>50&&vucutkitleindeksi<=60){
                    txtDurumAcikla.setText(R.string.süperasirikilolu);
                }
        }else{
            //bayan ise
            txtİdealKiloDeger.setText(String.valueOf(ideal_kiloBayan));
            if (vucutkitleindeksi<=17.5){
                txtDurumAcikla.setText(R.string.cokzayif);
            }
            else if (vucutkitleindeksi>17.5&&vucutkitleindeksi<=19.1){
                txtDurumAcikla.setText(R.string.zayif);
            }
            else if (vucutkitleindeksi>19.1&&vucutkitleindeksi<=25.8){
                txtDurumAcikla.setText(R.string.sagliklikiloagirligi);
            }
            else if (vucutkitleindeksi>25.8&&vucutkitleindeksi<=27.3){
                txtDurumAcikla.setText(R.string.normalkilodanfazla);
            }
            else if (vucutkitleindeksi>27.3&&vucutkitleindeksi<=32.3){
                txtDurumAcikla.setText(R.string.fazlakilolu);
            }
            else if (vucutkitleindeksi>32.3&&vucutkitleindeksi<=34.9){
                txtDurumAcikla.setText(R.string.cokfazlakilolu);
            }
            else if (vucutkitleindeksi>34.9&&vucutkitleindeksi<=40){
                txtDurumAcikla.setText(R.string.yüksekrisk);
            }
            else if (vucutkitleindeksi>40&&vucutkitleindeksi<=50){
                txtDurumAcikla.setText(R.string.hastalıklı);
            }
            else if (vucutkitleindeksi>50&&vucutkitleindeksi<=60){
                txtDurumAcikla.setText(R.string.süperasirikilolu);
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {//textwatcher dan implement edildi

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {//textwatcher dan implement edildi
        try {
            boy=Double.parseDouble(s.toString())/100.0;

        }catch (NumberFormatException e){
            boy=0.0;


        }
        güncelle();
    }


    @Override
    public void afterTextChanged(Editable s) {//textwatcher dan implement edildi

    }


}
