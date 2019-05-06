package com.example.yigit.vcutkitlendeksi;

public class Alarm {
    private int id;
    private String saat;
    private int adet;

    public Alarm() {}

    public Alarm(String saat, int adet) {
        this.saat = saat;
        this.adet = adet;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
