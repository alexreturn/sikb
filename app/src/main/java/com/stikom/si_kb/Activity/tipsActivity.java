package com.stikom.si_kb.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.stikom.si_kb.Config.Config;
import com.stikom.si_kb.Config.RequestHandler;
import com.stikom.si_kb.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class tipsActivity extends AppCompatActivity {
    ListView lispiu;
    JSONArray result;
    private String JSON_STRING;
    private ProgressDialog loading;
    SimpleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips);

        lispiu = (ListView)findViewById(R.id.listview);

        getJSONTips();
    }

    private void showTips(){
        JSONObject jsonObject = null;
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();

        try {
            jsonObject = new JSONObject(JSON_STRING);
            result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);

            for(int i = 0; i<result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);


                String  judul = jo.getString("judul");
                String  isi = jo.getString("isi");
                String  tanggal = jo.getString("tanggal");


                HashMap<String, String> employees = new HashMap<>();

                employees.put(Config.TAG_TIPS_judul, judul);
                employees.put(Config.TAG_TIPS_isi, isi);
                employees.put(Config.TAG_TIPS_tanggal, tanggal);

                list.add(employees);
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }

        adapter = new SimpleAdapter(
                tipsActivity.this, list, R.layout.row_data_tips,
                new String[]{Config.TAG_TIPS_judul, Config.TAG_TIPS_tanggal,Config.TAG_TIPS_isi,},
                new int[]{R.id.txtjudul,R.id.txttanggal, R.id.txtisi});

        lispiu.setAdapter(adapter);
        lispiu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                HashMap<String,String> map =(HashMap)parent.getItemAtPosition(position);
                String idlokasi=map.get(Config.TAG_LOKASI_id_lokasi);
                Toast.makeText(tipsActivity.this,idlokasi+" INI ID LOAKSI",Toast.LENGTH_SHORT).show();

            }
        });
    }


    private void getJSONTips(){
        SharedPreferences sharedPreferences = tipsActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        final String id_user = sharedPreferences.getString(Config.id_user_SHARED_PREF, "null");

        class GetJSON extends AsyncTask<Void,Void,String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(tipsActivity.this,"Fetching Data","Wait...",false,false);
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showTips();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String  s = null;
                s = rh.sendGetRequest(Config.TIPS_URL);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }
}