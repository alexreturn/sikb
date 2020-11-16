package com.stikom.si_kb.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.stikom.si_kb.Config.Config;
import com.stikom.si_kb.Config.RequestHandler;
import com.stikom.si_kb.LoginActivity;
import com.stikom.si_kb.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class LokasiActivity extends AppCompatActivity {

    ListView lispiu;
    JSONArray result;
    private String JSON_STRING;
    private ProgressDialog loading;
    SimpleAdapter adapter;
    ImageButton btnTmbhLokasi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lokasi);

        btnTmbhLokasi = (ImageButton) findViewById(R.id.btnTmbhLokasi);
        btnTmbhLokasi.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent( LokasiActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });

        lispiu = (ListView)findViewById(R.id.listview);

        getJSONLokasi();
    }

    private void showLokasi(){
        JSONObject jsonObject = null;
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();

        try {
            jsonObject = new JSONObject(JSON_STRING);
            result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);

            for(int i = 0; i<result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);

                String  No = i+1+"";
                String  id_lokasi = jo.getString(Config.KEY_LOKASI_id_lokasi);
                String  id_user = jo.getString(Config.KEY_LOKASI_id_user);
                String  nama_lokasi = jo.getString(Config.KEY_LOKASI_nama_lokasi);
                String  longitude = jo.getString(Config.KEY_LOKASI_longitude);
                String  latitude = jo.getString(Config.KEY_LOKASI_latitude);
                String  status = jo.getString(Config.KEY_LOKASI_status);


                HashMap<String, String> employees = new HashMap<>();
                employees.put(Config.TAG_LOKASI_No, No);
                employees.put(Config.TAG_LOKASI_id_lokasi, id_lokasi);
                employees.put(Config.TAG_LOKASI_id_user, id_user);
                employees.put(Config.TAG_LOKASI_nama_lokasi, nama_lokasi);
                employees.put(Config.TAG_LOKASI_longitude, longitude);
                employees.put(Config.TAG_LOKASI_latitude, latitude);
                employees.put(Config.TAG_LOKASI_status, status);

                list.add(employees);
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }

        adapter = new SimpleAdapter(
                LokasiActivity.this, list, R.layout.row_data_lokasi,
                new String[]{Config.TAG_LOKASI_No, Config.TAG_LOKASI_nama_lokasi,Config.TAG_LOKASI_id_user,},
                new int[]{R.id.txtNo,R.id.txtNama, R.id.txtJml});

        lispiu.setAdapter(adapter);
        lispiu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                HashMap<String,String> map =(HashMap)parent.getItemAtPosition(position);
                String idlokasi=map.get(Config.TAG_LOKASI_id_lokasi);
                SharedPreferences sharedPreferences = LokasiActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(Config.LOKASI_SIMPAN_LOKASI, idlokasi);

                editor.commit();

                Intent i =new Intent(getApplicationContext(),TanamankuActivity.class);
                startActivity(i);
            }
        });
    }


    private void getJSONLokasi(){
        SharedPreferences sharedPreferences = LokasiActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        final String id_user = sharedPreferences.getString(Config.id_user_SHARED_PREF, "null");

        class GetJSON extends AsyncTask<Void,Void,String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(LokasiActivity.this,"Fetching Data","Wait...",false,false);
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
                s = rh.sendGetRequest(Config.LOKASI_URL+id_user);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }
}