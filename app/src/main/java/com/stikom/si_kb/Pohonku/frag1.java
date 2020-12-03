package com.stikom.si_kb.Pohonku;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.stikom.si_kb.Config.Config;
import com.stikom.si_kb.Config.RequestHandler;
import com.stikom.si_kb.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


public class frag1 extends Fragment {
    ListView lispiu;
    JSONArray result;
    private String JSON_STRING;
    private ProgressDialog loading;
    SimpleAdapter adapter;
    ImageButton btnTmbhTanaman; @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_frag1, container, false);

        lispiu = (ListView)view.findViewById(R.id.listview);

        getJSONDataTanamanku();

        return view;
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
                String  jml_panen = jo.getString(Config.KEY_TANAMANKU_jumlah_panen);
                String  status = jo.getString(Config.KEY_TANAMANKU_status);
                String  keterangan = jo.getString(Config.KEY_TANAMANKU_keterangan);
                String  umur_tanaman = jo.getString("umur_tanaman");
                String  nama_lokasi = jo.getString("nama_lokasi");
                String  kategori_lokasi = jo.getString("kategori");
                String  jumlahPanen = jo.getString("jumlahPanen");
                String  timestamp = jo.getString(Config.KEY_TANAMANKU_timestamp);

                if (kategori_lokasi.equals("LAHAN")) {
                    HashMap<String, String> employees = new HashMap<>();
                    employees.put(Config.TAG_LOKASI_No, No);
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

                    employees.put(Config.TAG_TANAMANKU_umur_tanaman, umur_tanaman);

                    employees.put(Config.TAG_TANAMANKU_nama_lokasi, nama_lokasi);
                    employees.put(Config.TAG_TANAMANKU_kategori_lokasi, kategori_lokasi);
                    employees.put(Config.TAG_TANAMANKU_jumlahPanen, jumlahPanen + " Kali");

                    list.add(employees);
                }
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }

        adapter = new SimpleAdapter(
                getActivity(), list, R.layout.row_data_pohonku,
                new String[]{ Config.TAG_TANAMANKU_nama,Config.TAG_TANAMANKU_nama_lokasi,Config.TAG_TANAMANKU_tanggal_tanam,Config.TAG_TANAMANKU_jumlahPanen,},
                new int[]{R.id.txtNama, R.id.txtLokasi, R.id.txtTanam, R.id.txtPanen}){

            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                String tgl_tanam = list.get(position).get(Config.TAG_TANAMANKU_tanggal_tanam);
                String umur = list.get(position).get(Config.TAG_TANAMANKU_umur_tanaman);
                String kategori = list.get(position).get(Config.TAG_TANAMANKU_kategori_lokasi);

                ImageView imageStatus= view.findViewById(R.id.imageStatus);
                ImageView imageKategori= view.findViewById(R.id.imageKategori);

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                Calendar c = Calendar.getInstance();
                try {
                    c.setTime(sdf.parse(tgl_tanam));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                c.add(Calendar.DATE, Integer.parseInt(umur));  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
                String output = sdf.format(c.getTime());
                String panen=output;

                Date d = new Date();
                CharSequence s  = DateFormat.format("dd-MM-yyyy", d.getTime());
                System.out.println(s+"CEK UMUR "+panen);

                SimpleDateFormat asdasd = new SimpleDateFormat("dd-MM-yyyy");
                Date strDate = null;
                try {
                    strDate = asdasd.parse(panen);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (new Date().after(strDate)) {
                    imageStatus.setImageResource(R.drawable.btnmerah);
                }

                if (kategori.equals("LAHAN")) {

                }else{
                    imageKategori.setImageResource(R.drawable.btnpaper);
                }
//

                return view;
            };
        };

        lispiu.setAdapter(adapter);
        lispiu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                HashMap<String,String> map =(HashMap)parent.getItemAtPosition(position);

                String idTanaman=map.get(Config.TAG_TANAMANKU_id_tanaman);

                System.out.println("ISIDIJSDJISDJI "+ idTanaman);
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(Config.ID_TANAMAN_SIMPAN_TANAMAN, idTanaman);
                editor.commit();

                Intent i =new Intent(getActivity(), DetailPohonkuActivity.class);
                startActivity(i);
            }
        });
    }


    public void getJSONDataTanamanku(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        final String id_User = sharedPreferences.getString(Config.id_user_SHARED_PREF, "null");

        class GetJSON extends AsyncTask<Void,Void,String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
//                loading = ProgressDialog.show(getActivity(),"Fetching Data","Wait...",false,false);
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
//                loading.dismiss();
                JSON_STRING = s;
                showLokasi();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String  s = null;
                s = rh.sendGetRequest(Config.TANAMANKU_URL+id_User);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    @Override
    public void onResume(){
        super.onResume();
        getJSONDataTanamanku();
        System.out.println("LANJUTTTTTTTT");

    }
}