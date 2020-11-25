package com.stikom.si_kb.Activity_lahan;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.stikom.si_kb.Config.Config;
import com.stikom.si_kb.Config.RequestHandler;
import com.stikom.si_kb.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class DetailTanamanActivity extends AppCompatActivity {


    JSONArray result;
    private String JSON_STRING;
    private ProgressDialog loading;
    Button btnKembali,btnHapus;
    String id_tanaman;
    ImageView imageView2;
    TextView TxtnmTanaman,txtTanggal,txtKeterangan,txtWaktu,txtJml;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tanaman);
        SharedPreferences sharedPreferences = DetailTanamanActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        id_tanaman = sharedPreferences.getString(Config.ID_TANAMAN_SIMPAN_TANAMAN, "null");

        TxtnmTanaman=(TextView)findViewById(R.id.TxtnmTanaman);
        txtTanggal=(TextView)findViewById(R.id.txtTanggal);
        txtKeterangan=(TextView)findViewById(R.id.txtKeterangan);
        txtWaktu=(TextView)findViewById(R.id.txtWaktu);
        txtJml=(TextView)findViewById(R.id.txtJml);

        imageView2=(ImageView)findViewById(R.id.imageView2);

        btnKembali=(Button) findViewById(R.id.btnKembali);
        btnKembali.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                finish();
            }
        });

        btnHapus=(Button) findViewById(R.id.btnTambah);
        btnHapus.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DetailTanamanActivity.this);
                alertDialogBuilder.setTitle("Yakin Untuk Mengapus Tanaman??");
                alertDialogBuilder
                        .setMessage("Tanaman yang di hapus tidak dapat di kembalikan lagi!")
                        .setCancelable(false)
                        .setPositiveButton("Ya",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                HapusTanaman();
                            }
                        })
                        .setNegativeButton("Tidak",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
        getJSONLokasi();
    }

    private void showDetail(){
        JSONObject jsonObject = null;
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();

        try {
            jsonObject = new JSONObject(JSON_STRING);
            result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);

            for(int i = 0; i<result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);


                String  id_tanaman = jo.getString(Config.KEY_TANAMANKU_id_tanaman);
                String  id_lokasi = jo.getString(Config.KEY_TANAMANKU_id_lokasi);
                String  id_user = jo.getString(Config.KEY_TANAMANKU_id_user);
                String  id_pohon = jo.getString(Config.KEY_TANAMANKU_id_pohon);
                String  nama = jo.getString(Config.KEY_TANAMANKU_nama);
                String  tanggal_tanam = jo.getString(Config.KEY_TANAMANKU_tanggal_tanam);
                String  panen = jo.getString(Config.KEY_TANAMANKU_panen);
                String  jml_panen = jo.getString(Config.KEY_TANAMANKU_jumlah_panen);
                String  status = jo.getString(Config.KEY_TANAMANKU_status);
                String  keterangan = jo.getString(Config.KEY_TANAMANKU_keterangan);
                String  timestamp = jo.getString(Config.KEY_TANAMANKU_timestamp);
                String  foto_tanaman = jo.getString(Config.KEY_TANAMANKU_foto_tanaman);
                String  status_tanaman = jo.getString(Config.KEY_TANAMANKU_status_tanaman);

                HashMap<String, String> employees = new HashMap<>();

                employees.put(Config.TAG_TANAMANKU_id_tanaman, id_tanaman);
                employees.put(Config.TAG_TANAMANKU_id_lokasi, id_lokasi);
                employees.put(Config.TAG_TANAMANKU_id_user, id_user);
                employees.put(Config.TAG_TANAMANKU_id_pohon, id_pohon);
                employees.put(Config.TAG_TANAMANKU_nama, nama);
                employees.put(Config.TAG_TANAMANKU_tanggal_tanam, tanggal_tanam);
                employees.put(Config.TAG_TANAMANKU_panen, panen);
                employees.put(Config.TAG_TANAMANKU_jumlah_panen, jml_panen);
                employees.put(Config.TAG_TANAMANKU_status, status);
                employees.put(Config.TAG_TANAMANKU_keterangan, keterangan);
                employees.put(Config.TAG_TANAMANKU_timestamp, timestamp);
                employees.put(Config.TAG_TANAMANKU_foto_tanaman, foto_tanaman);
                employees.put(Config.TAG_TANAMANKU_status_tanaman, status_tanaman);

                list.add(employees);

                TxtnmTanaman.setText(nama);
                txtTanggal.setText(tanggal_tanam);
                txtKeterangan.setText(keterangan);
                txtWaktu.setText(panen);
                txtJml.setText(jml_panen+" Kali");
                if(foto_tanaman.equals("")||foto_tanaman==null){

                }else {
                    Picasso.get().load(Config.URL + foto_tanaman).transform(new CropCircleTransformation()).into(imageView2);
                }
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void getJSONLokasi(){

        class GetJSON extends AsyncTask<Void,Void,String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(DetailTanamanActivity.this,"Fetching Data","Wait...",false,false);
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showDetail();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String  s = null;
                s = rh.sendGetRequest(Config.DETAIL_TANAMAN_URL+id_tanaman);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }


    private void HapusTanaman() {
        class TambahData extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(DetailTanamanActivity.this, "Proses Kirim Data...", "Wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
//                            Toast.makeText(MapsActivity.this,id_user+" "+nama_lokasi+" "+longt+" "+latt+" ",Toast.LENGTH_SHORT).show();
                Intent i =new Intent(getApplicationContext(),TanamankuActivity.class);
                startActivity(i);
                finish();
            }

            @Override
            protected String doInBackground(Void... v) {

                HashMap<String, String> params = new HashMap<>();

                params.put(Config.TAG_TANAMANKU_id_tanaman, id_tanaman);


                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Config.HAPUS_TANAMAN_URL, params);
                return res;
            }
        }
        TambahData ae = new TambahData();
        ae.execute();

    }
}