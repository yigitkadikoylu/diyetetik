package com.example.yigit.vcutkitlendeksi;

public class AlarmEleman {
    String saat;
    int adet;

    public AlarmEleman() {}

    public AlarmEleman(String saat, int adet) {
        this.saat = saat;
        this.adet = adet;
    }

    public String getSaat() {
        return saat;
    }

    public void setSaat(String saat) {
        this.saat = saat;
    }

    public int getAdet() {
        return adet;
    }

    public void setAdet(int adet) {
        this.adet = adet;
    }

}
