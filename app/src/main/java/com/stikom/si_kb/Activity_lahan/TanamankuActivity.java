package com.stikom.si_kb.Activity_lahan;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.stikom.si_kb.Config.Config;
import com.stikom.si_kb.Config.RequestHandler;
import com.stikom.si_kb.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class TanamankuActivity extends AppCompatActivity {

    ListView lispiu;
    JSONArray result;
    private String JSON_STRING;
    private ProgressDialog loading;
    SimpleAdapter adapter;
    ImageButton btnback;
    Button buttonTambah;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tanamanku);
        btnback = (ImageButton) findViewById(R.id.btnback);
        btnback.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent( TanamankuActivity.this, LokasiActivity.class);
                startActivity(intent);
            }
        });
        buttonTambah = (Button) findViewById(R.id.buttonTambah);
        buttonTambah.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent( TanamankuActivity.this, TambahTanamanActivity.class);
                startActivity(intent);
            }
        });
        lispiu = (ListView)findViewById(R.id.listview);

        getJSONLokasi();
    }

    private void showLokasi(){
        JSONObject jsonObject = null;
        final ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();

        try {
            jsonObject = new JSONObject(JSON_STRING);
            result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);

            for(int i = 0; i<result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);

                String  No = i+1+"";
                String  id_tanaman = jo.getString(Config.KEY_TANAMANKU_id_tanaman);
                String  id_lokasi = jo.getString(Config.KEY_TANAMANKU_id_lokasi);
                String  id_user = jo.getString(Config.KEY_TANAMANKU_id_user);
                String  id_pohon = jo.getString(Config.KEY_TANAMANKU_id_pohon);
                String  nama = jo.getString(Config.KEY_TANAMANKU_nama);
                String  tanggal_tanam = jo.getString(Config.KEY_TANAMANKU_tanggal_tanam);
                String  panen = jo.getString(Config.KEY_TANAMANKU_panen);
                String  jumlah_panen = jo.getString(Config.KEY_TANAMANKU_jumlah_panen);
                String  status = jo.getString(Config.KEY_TANAMANKU_status);
                String  keterangan = jo.getString(Config.KEY_TANAMANKU_keterangan);
                String  timestamp = jo.getString(Config.KEY_TANAMANKU_timestamp);

                HashMap<String, String> employees = new HashMap<>();
                employees.put(Config.TAG_LOKASI_No, No);
                employees.put(Config.TAG_TANAMANKU_id_tanaman, id_tanaman);
                employees.put(Config.TAG_TANAMANKU_id_lokasi, id_lokasi);
                employees.put(Config.TAG_TANAMANKU_id_user, id_user);
                employees.put(Config.TAG_TANAMANKU_id_pohon, id_pohon);
                employees.put(Config.TAG_TANAMANKU_nama, nama);
                employees.put(Config.TAG_TANAMANKU_tanggal_tanam, tanggal_tanam);
                employees.put(Config.TAG_TANAMANKU_panen, panen);
                employees.put(Config.TAG_TANAMANKU_jumlah_panen, jumlah_panen);
                employees.put(Config.TAG_TANAMANKU_status, status);
                employees.put(Config.TAG_TANAMANKU_keterangan, keterangan);
                employees.put(Config.TAG_TANAMANKU_timestamp, timestamp);

                list.add(employees);
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }

        adapter = new SimpleAdapter(
                TanamankuActivity.this, list, R.layout.row_data_tanamanku,
                new String[]{Config.TAG_LOKASI_No, Config.TAG_TANAMANKU_nama,Config.TAG_TANAMANKU_tanggal_tanam,Config.TAG_TANAMANKU_panen,},
                new int[]{R.id.txtNo,R.id.txtNama, R.id.txtWaktu, R.id.txtPanen}) {

            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                String jumlahpanen = list.get(position).get(Config.TAG_TANAMANKU_jumlah_panen);
                String tglpanen = list.get(position).get(Config.TAG_TANAMANKU_panen);
                LinearLayout linBg = (LinearLayout) view.findViewById(R.id.linBg);

                int statusPanen=0;
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                Date strDate = null;
                try {
                    strDate = sdf.parse(tglpanen);
                    if (System.currentTimeMillis() > strDate.getTime()) {
                        statusPanen = 1;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                System.out.println("INI statusPanen "+statusPanen);

//                if(jumlahpanen.equals("0" )||jumlahpanen=="0" ){
//                    linBg.setBackgroundColor(Color.RED);
//                }else if(statusPanen==1){
//                    linBg.setBackgroundColor(Color.GREEN);
//                }else{
//                }

                return view;
            };
        };

        lispiu.setAdapter(adapter);
        lispiu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                HashMap<String,String> map =(HashMap)parent.getItemAtPosition(position);
                String idTanaman=map.get(Config.TAG_TANAMANKU_id_tanaman);

                SharedPreferences sharedPreferences = TanamankuActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(Config.ID_TANAMAN_SIMPAN_TANAMAN, idTanaman);
                editor.commit();

                Intent i =new Intent(getApplicationContext(),DetailTanamanActivity.class);
                startActivity(i);
            }
        });
    }


    private void getJSONLokasi(){
        SharedPreferences sharedPreferences = TanamankuActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        final String id_lokasi = sharedPreferences.getString(Config.LOKASI_SIMPAN_LOKASI, "null");

        class GetJSON extends AsyncTask<Void,Void,String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(TanamankuActivity.this,"Mengambil Data","Wait...",false,false);
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showLokasi();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String  s = null;
                s = rh.sendGetRequest(Config.TANAMAN_URL+id_lokasi);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }
}