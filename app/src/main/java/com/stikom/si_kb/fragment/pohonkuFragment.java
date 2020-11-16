package com.stikom.si_kb.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.stikom.si_kb.Activity.DetailTanamanActivity;
import com.stikom.si_kb.Activity.LokasiActivity;
import com.stikom.si_kb.Activity.TanamankuActivity;
import com.stikom.si_kb.Activity.tipsActivity;
import com.stikom.si_kb.Config.Config;
import com.stikom.si_kb.Config.RequestHandler;
import com.stikom.si_kb.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class pohonkuFragment extends Fragment {
    ListView lispiu;
    JSONArray result;
    private String JSON_STRING;
    private ProgressDialog loading;
    SimpleAdapter adapter;
    ImageButton btnTmbhTanaman; @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pohonku, container, false);

        lispiu = (ListView)view.findViewById(R.id.listview);

        getJSONLokasi();

        return view;
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
                String  id_tanaman = jo.getString(Config.KEY_TANAMANKU_id_tanaman);
                String  id_lokasi = jo.getString(Config.KEY_TANAMANKU_id_lokasi);
                String  id_user = jo.getString(Config.KEY_TANAMANKU_id_user);
                String  id_pohon = jo.getString(Config.KEY_TANAMANKU_id_pohon);
                String  nama = jo.getString(Config.KEY_TANAMANKU_nama);
                String  tanggal_tanam = jo.getString(Config.KEY_TANAMANKU_tanggal_tanam);
                String  panen = jo.getString(Config.KEY_TANAMANKU_panen);
                String  jml_panen = jo.getString(Config.KEY_TANAMANKU_jml_panen);
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
                employees.put(Config.TAG_TANAMANKU_jml_panen, jml_panen);
                employees.put(Config.TAG_TANAMANKU_status, status);
                employees.put(Config.TAG_TANAMANKU_keterangan, keterangan);
                employees.put(Config.TAG_TANAMANKU_timestamp, timestamp);

                list.add(employees);
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }

        adapter = new SimpleAdapter(
                getActivity(), list, R.layout.row_data_pohonku,
                new String[]{ Config.TAG_TANAMANKU_nama,Config.TAG_TANAMANKU_tanggal_tanam,Config.TAG_TANAMANKU_panen,},
                new int[]{R.id.txtNama, R.id.txtWaktu, R.id.txtPanen});

        lispiu.setAdapter(adapter);
        lispiu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                HashMap<String,String> map =(HashMap)parent.getItemAtPosition(position);
//                String idTanaman=map.get(Config.TAG_TANAMANKU_id_tanaman);
//
//                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.putString(Config.ID_TANAMAN_SIMPAN_TANAMAN, idTanaman);
//                editor.commit();
//
//                Intent i =new Intent(getApplicationContext(), DetailTanamanActivity.class);
//                startActivity(i);
            }
        });
    }


    private void getJSONLokasi(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        final String id_lokasi = sharedPreferences.getString(Config.LOKASI_SIMPAN_LOKASI, "null");

        class GetJSON extends AsyncTask<Void,Void,String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getActivity(),"Fetching Data","Wait...",false,false);
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
                s = rh.sendGetRequest(Config.TANAMANKU_URL+id_lokasi);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }
}