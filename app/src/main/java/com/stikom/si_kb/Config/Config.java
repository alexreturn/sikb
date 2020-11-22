package com.stikom.si_kb.Config;

public class Config {
//    public static final String URL = "http://192.168.18.31/ApiSIKB/";
    public static final String URL = "http://sikb.leakadev.xyz/ApiSIKB/";
    public static final String LOGIN_URL = URL+"login.php";
    public static final String USER_URL = URL+"getUser.php?username=";
    public static final String HOMEDETAIL_URL = URL+"getHomeDetail.php?id_user=";
    public static final String EDIT_USER_URL = URL+"editProfile.php";
    public static final String EDIT_PASS_URL = URL+"editPassword.php";
    public static final String LOKASI_URL = URL+"getLokasi.php?id_user=";
    public static final String TANAMAN_URL = URL+"getTanaman.php?id_lokasi=";
    public static final String TANAMANKU_URL = URL+"getTanamanku.php?id_user=";
    public static final String LOKASI_SIMPAN_URL = URL+"simpanKordinat.php";
    public static final String TANAMAN_SIMPAN_URL = URL+"simpanTanaman.php";
    public static final String DETAIL_TANAMAN_URL = URL+"getDetailTanaman.php?id_tanaman=";
    public static final String HAPUS_TANAMAN_URL = URL+"hapusTanaman.php?id_tanaman=";
    public static final String HAPUS_LOKASI_URL = URL+"hapusLokasi.php?id_lokasi=";
    public static final String getLogTanaman = URL+"getLogTanaman.php?id_tanaman=";
    public static final String simpanLogTanaman = URL+"simpanLogTanaman.php";
    public static final String TIPS_URL = URL+"getTips.php";
    public static final String DATA_TANAMAN = URL+"getDataTanaman.php";
    //////////////////////////////Login/////////////////////////////////////////

    public static final String KEY_EMP_id_user= "id_user";
    public static final String KEY_EMP_username= "username";
    public static final String KEY_EMP_nama= "nama";
    public static final String KEY_EMP_alamat= "alamat";
    public static final String KEY_EMP_telp= "telp";
    public static final String KEY_EMP_password= "password";
    public static final String KEY_EMP_longitude= "longitude";
    public static final String KEY_EMP_latitude= "latitude";
    public static final String KEY_EMP_status= "status";
    public static final String KEY_EMP_foto= "foto";


    //-----------------------LOKASI--------------------------------------------------------

    public static final String KEY_LOKASI_id_lokasi= "id_lokasi";
    public static final String KEY_LOKASI_id_user= "id_user";
    public static final String KEY_LOKASI_nama_lokasi= "nama_lokasi";
    public static final String KEY_LOKASI_luas_lahan= "luas_lahan";
    public static final String KEY_LOKASI_longitude= "longitude";
    public static final String KEY_LOKASI_latitude= "latitude";
    public static final String KEY_LOKASI_status= "status";
    public static final String KEY_LOKASI_kategori= "kategori";
    public static final String KEY_LOKASI_jumlah= "jumlah";

    public static final String TAG_LOKASI_No= "0";
    public static final String TAG_LOKASI_id_lokasi= "id_lokasi";
    public static final String TAG_LOKASI_id_user= "id_user";
    public static final String TAG_LOKASI_nama_lokasi= "nama_lokasi";
    public static final String TAG_LOKASI_luas_lahan= "luas_lahan";
    public static final String TAG_LOKASI_longitude= "longitude";
    public static final String TAG_LOKASI_latitude= "latitude";
    public static final String TAG_LOKASI_status= "status";
    public static final String TAG_LOKASI_jumlah= "jumlah";

    //-----------------------------TIPS ------------------------------------------------------
    public static final String TAG_TIPS_judul= "judul";
    public static final String TAG_TIPS_isi= "isi";
    public static final String TAG_TIPS_tanggal= "tanggal";

    //-------------------------------tanaman---------------------------------------------
    public static final String KEY_TANAMANKU_id_tanaman= "id_tanaman";
    public static final String KEY_TANAMANKU_id_lokasi= "id_lokasi";
    public static final String KEY_TANAMANKU_id_user= "id_user";
    public static final String KEY_TANAMANKU_id_pohon= "id_pohon";
    public static final String KEY_TANAMANKU_nama= "nama";
    public static final String KEY_TANAMANKU_tanggal_tanam= "tanggal_tanam";
    public static final String KEY_TANAMANKU_panen= "panen";
    public static final String KEY_TANAMANKU_jumlah_panen= "jumlah_panen";
    public static final String KEY_TANAMANKU_jml_panen= "jml_panen";
    public static final String KEY_TANAMANKU_status= "status";
    public static final String KEY_TANAMANKU_keterangan= "keterangan";
    public static final String KEY_TANAMANKU_timestamp= "timestamp";
    public static final String KEY_TANAMANKU_foto_tanaman= "foto_tanaman";
    public static final String KEY_TANAMANKU_status_tanaman= "status_tanaman";

    public static final String TAG_TANAMANKU_id_tanaman= "id_tanaman";
    public static final String TAG_TANAMANKU_id_lokasi= "id_lokasi";
    public static final String TAG_TANAMANKU_id_user= "id_user";
    public static final String TAG_TANAMANKU_id_pohon= "id_pohon";
    public static final String TAG_TANAMANKU_nama= "nama";
    public static final String TAG_TANAMANKU_tanggal_tanam= "tanggal_tanam";
    public static final String TAG_TANAMANKU_panen= "panen";
    public static final String TAG_TANAMANKU_jumlah_panen= "jumlah_panen";
    public static final String TAG_TANAMANKU_jml_panen= "jml_panen";
    public static final String TAG_TANAMANKU_status= "status";
    public static final String TAG_TANAMANKU_keterangan= "keterangan";
    public static final String TAG_TANAMANKU_timestamp= "timestamp";
    public static final String TAG_TANAMANKU_foto_tanaman= "foto_tanaman";
    public static final String TAG_TANAMANKU_status_tanaman= "status_tanaman";

    //-----------------------------LOG TANAMAN -------------------------------------------

    public static final String KEY_LOG_id_log= "id_log";
    public static final String KEY_LOG_id_tanaman= "id_tanaman";
    public static final String KEY_LOG_keterangan= "keterangan";
    public static final String KEY_LOG_foto= "foto";
    public static final String KEY_LOG_tanggal= "tanggal";

    public static final String TAG_LOG_No= "0";
    public static final String TAG_LOG_id_log= "id_log";
    public static final String TAG_LOG_id_tanaman= "id_tanaman";
    public static final String TAG_LOG_keterangan= "keterangan";
    public static final String TAG_LOG_foto= "foto";
    public static final String TAG_LOG_tanggal= "tanggal";

    //----------------------------DATA TANAMAN-------------------------------------------
    public static final String KEY_DATA_id= "id";
    public static final String KEY_DATA_nama= "nama";
    public static final String KEY_DATA_jenis= "jenis";
    public static final String KEY_DATA_info= "info";
    public static final String KEY_DATA_musim= "musim";
    public static final String KEY_DATA_jml_planterbag= "jml_planterbag";
    public static final String KEY_DATA_waktu_panen= "waktu_panen";
    public static final String KEY_DATA_timestamp= "timestamp";

    public static final String TAG_DATA_id= "id";
    public static final String TAG_DATA_nama= "nama";
    public static final String TAG_DATA_jenis= "jenis";
    public static final String TAG_DATA_info= "info";
    public static final String TAG_DATA_musim= "musim";
    public static final String TAG_DATA_jml_planterbag= "jml_planterbag";
    public static final String TAG_DATA_waktu_panen= "waktu_panen";
    public static final String TAG_DATA_timestamp= "timestamp";
    //-----------------------------------------------------------------------------------

    public static final String TUTORIAL_SIMPAN = "true";

    public static final String LOKASI_SIMPAN_LOKASI="id_lokasi";
    public static final String ID_TANAMAN_SIMPAN_TANAMAN="id_tanaman";


    public static final String TAG_JSON_ARRAY="result";
    public static final String TAG_JSON_ARRAY2="result2";
    public static final String LOGIN_SUCCESS = "success";
    public static final String LOGGEDIN_SHARED_PREF = "loggedin";
    public static final String SHARED_PREF_NAME = "myloginapp";
    public static final String id_user_SHARED_PREF = "id_user";
    public static final String USERNAME_SHARED_PREF = "username";
    public static final String password_SHARED_PREF = "password";
    public static final String nama_SHARED_PREF = "nama";
    public static final String alamat_SHARED_PREF = "alamat";
    public static final String telp_SHARED_PREF = "telp";
    public static final String longitude_SHARED_PREF = "longitude";
    public static final String latitude_SHARED_PREF = "latitude";
    public static final String foto_SHARED_PREF = "foto";

}
