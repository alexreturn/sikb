package com.stikom.si_kb.Config;

import java.util.ArrayList;
import java.util.List;

public class ModelDataTanaman {
    private String id,nama,jenis,info,musim,Jml_planterbag,Waktu_panen,jml_panen,Timestamp;

    public ModelDataTanaman() {
    }
    public ModelDataTanaman(String id, String nama, String jenis,
                            String info, String musim, String Jml_planterbag, String Waktu_panen,String jml_panen, String Timestamp) {
        this.id = id;
        this.nama = nama;
        this.jenis = jenis;
        this.info = info;
        this.musim = musim;
        this.Jml_planterbag = Jml_planterbag;
        this.Waktu_panen = Waktu_panen;
        this.jml_panen = jml_panen;
        this.Timestamp = Timestamp;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }
    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getJenis() {
        return jenis;
    }
    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public String getMusim() {
        return musim;
    }
    public void setMusim(String musim) {
        this.musim = musim;
    }

    public String getInfo() {
        return info;
    }
    public void setInfo(String info) {
        this.info = info;
    }
    public String getJml_planterbag() {
        return Jml_planterbag;
    }
    public void setJml_planterbag(String Jml_planterbag) {
        this.Jml_planterbag = Jml_planterbag;
    }

    public String getWaktu_panen() {
        return Waktu_panen;
    }
    public void setWaktu_panen(String Waktu_panen) {
        this.Waktu_panen = Waktu_panen;
    }

    public String getJml_panen() {
        return jml_panen;
    }
    public void setJml_panen(String jml_panen) {
        this.jml_panen = jml_panen;
    }



    public String getTimestamp() {
        return Timestamp;
    }
    public void setTimestamp(String Timestamp) {
        this.Timestamp = Timestamp;
    }


}
