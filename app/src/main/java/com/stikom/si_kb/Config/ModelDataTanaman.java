package com.stikom.si_kb.Config;

import java.util.ArrayList;
import java.util.List;

public class ModelDataTanaman {
    private String id,nama,jenis,info,musim,Jml_planterbag,Waktu_panen,umur_tanaman,Timestamp,durasi_panen,estimasi_panen,estimasi_harga;

    public ModelDataTanaman() {
    }
    public ModelDataTanaman(String id, String nama, String jenis,
                            String info, String musim, String Jml_planterbag, String Waktu_panen,String umur_tanaman, String Timestamp,String durasi_panen,String estimasi_panen,String estimasi_harga) {
        this.id = id;
        this.nama = nama;
        this.jenis = jenis;
        this.info = info;
        this.musim = musim;
        this.Jml_planterbag = Jml_planterbag;
        this.Waktu_panen = Waktu_panen;
        this.umur_tanaman = umur_tanaman;
        this.Timestamp = Timestamp;

        this.durasi_panen=durasi_panen;
        this.estimasi_panen=estimasi_panen;
        this.estimasi_harga=estimasi_harga;
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

    public String getumur_tanaman() {
        return umur_tanaman;
    }
    public void setumur_tanaman(String umur_tanaman) {
        this.umur_tanaman = umur_tanaman;
    }



    public String getdurasi_panen() {
        return durasi_panen;
    }
    public void setdurasi_panen(String durasi_panen) {
        this.durasi_panen = durasi_panen;
    }

    public String getestimasi_panen() {
        return estimasi_panen;
    }
    public void setestimasi_panen(String estimasi_panen) {
        this.estimasi_panen = estimasi_panen;
    }
    public String getestimasi_harga() {
        return estimasi_harga;
    }
    public void setestimasi_harga(String estimasi_harga) {
        this.estimasi_harga = estimasi_harga;
    }

    public String getTimestamp() {
        return Timestamp;
    }
    public void setTimestamp(String Timestamp) {
        this.Timestamp = Timestamp;
    }


}
