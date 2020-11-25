package com.stikom.si_kb.Activity_planterbag;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.stikom.si_kb.Activity_lahan.LokasiActivity;
import com.stikom.si_kb.Activity_lahan.TanamankuActivity;
import com.stikom.si_kb.Config.Config;
import com.stikom.si_kb.Config.RequestHandler;
import com.stikom.si_kb.MainActivity;
import com.stikom.si_kb.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class PlanterBagActivity extends AppCompatActivity {
    ListView lispiu;
    JSONArray result,result2;
    private String JSON_STRING;
    private ProgressDialog loading;
    SimpleAdapter adapter;
    ImageButton btnback;
    Button buttonTambah;
    private Dialog customDialog;
    private EditText txtInputName,txtInputUkuran;
    private Button btnInsertName,btnCancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planter_bag);

        btnback = (ImageButton) findViewById(R.id.btnback);
        btnback.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent( PlanterBagActivity.this, LokasiplanterActivity.class);
                startActivity(intent);
            }
        });

        buttonTambah = (Button) findViewById(R.id.buttonTambah);
        buttonTambah.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                customDialog.show();
            }
        });

        lispiu = (ListView)findViewById(R.id.listview);
        initCustomDialog();
        getJSONLokasi();

    }
    private void initCustomDialog(){
        customDialog = new Dialog(PlanterBagActivity.this);
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customDialog.setContentView(R.layout.popup_tambah_panter);
        customDialog.setCancelable(true);

        txtInputName = customDialog.findViewById(R.id.userName);
        txtInputUkuran = customDialog.findViewById(R.id.ukuran_planter);

        btnInsertName = customDialog.findViewById(R.id.btnsimpan);

        btnCancel = customDialog.findViewById(R.id.btncancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                customDialog.dismiss();
            }
        });
        btnInsertName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String nama=txtInputName.getText().toString();
            String ukuran=txtInputUkuran.getText().toString();
                customDialog.dismiss();
                TambahPlanter(nama,ukuran);
            }
        });
    }

    private void TambahPlanter(final String nama, final String ukuran) {
        SharedPreferences sharedPreferences = PlanterBagActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        final String id_lokasi = sharedPreferences.getString(Config.LOKASI_SIMPAN_LOKASI, "null");
        final String id_user = sharedPreferences.getString(Config.id_user_SHARED_PREF, "null");

        if (nama.equals("") || ukuran.equals("")) {
            Toast.makeText(PlanterBagActivity.this,"Nama Planter/Ukuran Tidak Boleh Kosong",Toast.LENGTH_SHORT).show();
        }else{
            class TambahData extends AsyncTask<Void, Void, String> {
                ProgressDialog loading;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    loading = ProgressDialog.show(PlanterBagActivity.this, "Proses Kirim Data...", "Wait...", false, false);
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    loading.dismiss();
//                            Toast.makeText(MapsActivity.this,id_user+" "+nama_lokasi+" "+longt+" "+latt+" ",Toast.LENGTH_SHORT).show();
                    Intent i =new Intent(getApplicationContext(),PlanterBagActivity.class);
                    startActivity(i);
                }

                @Override
                protected String doInBackground(Void... v) {

//                    $id_lokasi = $_POST['id_lokasi'];
//                    $id_user = $_POST['id_user'];
//                    $nama_planter = $_POST['nama_planter'];
//                    $ukuran_planter = $_POST['ukuran_planter'];

                    HashMap<String, String> params = new HashMap<>();
                    params.put(Config.KEY_LOKASI_id_user,id_user);
                    params.put(Config.KEY_LOKASI_id_lokasi,id_lokasi);
                    params.put(Config.KEY_LOKASI_nama_planter,nama);
                    params.put(Config.KEY_LOKASI_ukuran_planter,ukuran);

                    RequestHandler rh = new RequestHandler();
                    String res = rh.sendPostRequest(Config.TambahPlanter, params);
                    return res;
                }
            }
            TambahData ae = new TambahData();
            ae.execute();
        }
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
                String  id_planter = jo.getString(Config.KEY_LOKASI_id_planter);
                String  id_user = jo.getString(Config.KEY_LOKASI_id_user);
                String  nama_lokasi = jo.getString("nama_planter");
                String  ukuran_planter = jo.getString("ukuran_planter");
                String  jumlah = jo.getString("jumlah");


                HashMap<String, String> employees = new HashMap<>();
                employees.put(Config.TAG_LOKASI_No, No);
                employees.put(Config.TAG_LOKASI_id_lokasi, id_lokasi);
                employees.put(Config.TAG_LOKASI_id_planter, id_planter);
                employees.put(Config.TAG_LOKASI_id_user, id_user);
                employees.put(Config.TAG_LOKASI_nama_lokasi, nama_lokasi+"("+ukuran_planter+")");
                employees.put(Config.TAG_LOKASI_jumlah, jumlah+ "");

                list.add(employees);
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }

        adapter = new SimpleAdapter(
                PlanterBagActivity.this, list, R.layout.row_data_planter,
                new String[]{ Config.TAG_LOKASI_nama_lokasi,Config.TAG_LOKASI_jumlah,},
                new int[]{R.id.txtNama, R.id.txtJml});

        lispiu.setAdapter(adapter);
        lispiu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                HashMap<String,String> map =(HashMap)parent.getItemAtPosition(position);
                String idplanter=map.get(Config.TAG_LOKASI_id_planter);
                SharedPreferences sharedPreferences = PlanterBagActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(Config.LOKASI_PLANTER_SIMPAN, idplanter);

                editor.commit();

                Intent i =new Intent(getApplicationContext(),TanamankuPlanterActivity.class);
                startActivity(i);
            }
        });

        lispiu.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView parent, View view, int position, long id) {
                HashMap<String,String> map =(HashMap)parent.getItemAtPosition(position);
                final String idlokasi=map.get(Config.TAG_LOKASI_id_lokasi);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PlanterBagActivity.this);
                alertDialogBuilder.setTitle("Yakin Untuk Mengapus Lokasi?");
                alertDialogBuilder
                        .setMessage("Lokasi yang di hapus tidak dapat di kembalikan lagi!")
                        .setCancelable(false)
                        .setPositiveButton("Ya",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                HapusLokasi(idlokasi);
                            }
                        })
                        .setNegativeButton("Tidak",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                return true;
            }
        });
    }


    private void getJSONLokasi(){
        SharedPreferences sharedPreferences = PlanterBagActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        final String id_user = sharedPreferences.getString(Config.id_user_SHARED_PREF, "null");
        final String id_lokasi = sharedPreferences.getString(Config.LOKASI_SIMPAN_LOKASI, "null");

        class GetJSON extends AsyncTask<Void,Void,String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(PlanterBagActivity.this,"Fetching Data","Wait...",false,false);
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
                s = rh.sendGetRequest(Config.LOKASI_PLANTER_URL+id_user+"&id_lokasi="+id_lokasi);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    private void HapusLokasi(final String lokasi) {
        class TambahData extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(PlanterBagActivity.this, "Proses Kirim Data...", "Wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
//                            Toast.makeText(MapsActivity.this,id_user+" "+nama_lokasi+" "+longt+" "+latt+" ",Toast.LENGTH_SHORT).show();
                Intent i =new Intent(getApplicationContext(), LokasiActivity.class);
                startActivity(i);
                finish();
            }

            @Override
            protected String doInBackground(Void... v) {

                HashMap<String, String> params = new HashMap<>();

                params.put(Config.KEY_LOKASI_id_lokasi, lokasi);


                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Config.HAPUS_LOKASI_PLANTER_URL, params);
                return res;
            }
        }
        TambahData ae = new TambahData();
        ae.execute();

    }
}