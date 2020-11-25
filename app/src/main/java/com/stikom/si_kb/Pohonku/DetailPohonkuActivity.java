package com.stikom.si_kb.Pohonku;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.stikom.si_kb.Activity_lahan.DetailTanamanActivity;
import com.stikom.si_kb.Activity_lahan.LokasiActivity;
import com.stikom.si_kb.Activity_lahan.MapsActivity;
import com.stikom.si_kb.Activity_lahan.TambahTanamanActivity;
import com.stikom.si_kb.Activity_lahan.TanamankuActivity;
import com.stikom.si_kb.Config.Config;
import com.stikom.si_kb.Config.RequestHandler;
import com.stikom.si_kb.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class DetailPohonkuActivity extends AppCompatActivity {

    JSONArray result;
    private String JSON_STRING;
    private ProgressDialog loading;
    Button btnKembali,buttonAktifitas;
    String id_tanaman;
    ImageView imageView2,imageViewFotobesar;
    TextView TxtnmTanaman,txtTanggal,txtKeterangan,txtWaktu,txtJml,txtDurasi,txtestimasi_panen,txtHarga,txtEstimasiPanen;
    ListView listact;

    SimpleAdapter adapter;

    ImageView klikfoto;
    Spinner spinner;
    Button buttonSimpan;
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    int PICK_IMAGE_REQUEST = 1;
    Bitmap bitmap;
    ImageButton btnback;
    Button btnPanen ,btnPupuk, btnLain, btnTanamanmati;
    private Dialog customDialog,customDialog2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pohonku);
        SharedPreferences sharedPreferences = DetailPohonkuActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        id_tanaman = sharedPreferences.getString(Config.ID_TANAMAN_SIMPAN_TANAMAN, "null");

        TxtnmTanaman=(TextView)findViewById(R.id.TxtnmTanaman);
        txtTanggal=(TextView)findViewById(R.id.txtTanggal);
        txtKeterangan=(TextView)findViewById(R.id.txtKeterangan);
        txtWaktu=(TextView)findViewById(R.id.txtWaktu);
        txtJml=(TextView)findViewById(R.id.txtJml);
        txtDurasi=(TextView)findViewById(R.id.txtDurasi);
        txtestimasi_panen=(TextView)findViewById(R.id.txtestimasi_panen);
        txtHarga=(TextView)findViewById(R.id.txtHarga);
        txtEstimasiPanen=(TextView)findViewById(R.id.txtEstimasiPanen);

        listact=(ListView) findViewById(R.id.listact);

        imageView2=(ImageView)findViewById(R.id.imageView2);

        buttonAktifitas=(Button) findViewById(R.id.buttonAktifitas);
        buttonAktifitas.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                customDialog.show();
            }
        });

        btnback=(ImageButton) findViewById(R.id.btnback);
        btnback.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        btnHapus=(Button) findViewById(R.id.btnTambah);
//        btnHapus.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DetailPohonkuActivity.this);
//                alertDialogBuilder.setTitle("Yakin Untuk Mengapus Tanaman??");
//                alertDialogBuilder
//                        .setMessage("Tanaman yang di hapus tidak dapat di kembalikan lagi!")
//                        .setCancelable(false)
//                        .setPositiveButton("Ya",new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog,int id) {
//                                HapusTanaman();
//                            }
//                        })
//                        .setNegativeButton("Tidak",new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                dialog.cancel();
//                            }
//                        });
//                AlertDialog alertDialog = alertDialogBuilder.create();
//                alertDialog.show();
//            }
//        });
        getJSONLokasi();
        getJSONLog();
        initCustomDialog();
        initDialogFoto();
    }

    private void initCustomDialog(){
        customDialog = new Dialog(DetailPohonkuActivity.this);
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customDialog.setContentView(R.layout.popup_aktifitas);
        customDialog.setCancelable(true);
        spinner=customDialog.findViewById(R.id.spinner);
        String text = spinner.getSelectedItem().toString();

        buttonSimpan=customDialog.findViewById(R.id.buttonSimpan);
        buttonSimpan.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View view) {
                TambahLogTanaman();
            }
        });

        klikfoto=customDialog.findViewById(R.id.klikfoto);
        klikfoto.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View view) {
                if (DetailPohonkuActivity.this.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                } else {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            }
        });
    }

    private void initDialogFoto(){
        customDialog2 = new Dialog(DetailPohonkuActivity.this);
        customDialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customDialog2.setContentView(R.layout.popup_foto_besar);
        customDialog2.setCancelable(true);

        imageViewFotobesar = customDialog2.findViewById(R.id.imageViewFotobesar);

    }
    private void showLog(){
        JSONObject jsonObject = null;
        final ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);

            for(int i = 0; i<result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);
                String  No = i+1+"";
                String  id_log = jo.getString("id_log");
                String  id_tanaman = jo.getString("id_tanaman");
                String  keterangan = jo.getString("keterangan");
                String  foto = jo.getString("foto");
                String  tanggal = jo.getString("tanggal");

                HashMap<String, String> employees = new HashMap<>();
                employees.put(Config.TAG_LOG_No, No);
                employees.put(Config.TAG_LOG_id_log, id_log);
                employees.put(Config.TAG_LOG_id_tanaman, id_tanaman);
                employees.put(Config.TAG_LOG_keterangan, keterangan);
                employees.put(Config.TAG_LOG_foto, foto);
                employees.put(Config.TAG_LOG_tanggal, tanggal);

                list.add(employees);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        adapter = new SimpleAdapter(
                DetailPohonkuActivity.this, list, R.layout.row_data_log_tanaman,
                new String[]{ Config.TAG_LOG_keterangan,Config.TAG_LOG_tanggal,},
                new int[]{R.id.textAktifitas, R.id.txttgl}){

            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                String foto = list.get(position).get(Config.TAG_LOG_foto);

                ImageView imageView=(ImageView) view.findViewById(R.id.imageView);
                if(foto.equals("")||foto==null ) {
                    System.out.println("FOTO KOSONG");
                }else{
                    Picasso.get().load(Config.URL+foto) .transform(new CropCircleTransformation()).into(imageView);
                }
                return view;
            };
        };
        listact.setAdapter(adapter);
        listact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                HashMap<String,String> map =(HashMap)parent.getItemAtPosition(position);
                String fotonya=map.get(Config.TAG_LOG_foto);

                if(fotonya.equals("")||fotonya==null ) {
                    Picasso.get().load(R.drawable.camera) .transform(new CropCircleTransformation()).into(imageViewFotobesar);
                }else{
                    Picasso.get().load(Config.URL+fotonya).into(imageViewFotobesar);
                }
                customDialog2.show();
            }
        });

    }
    private void getJSONLog(){
        class GetJSON extends AsyncTask<Void,Void,String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
               }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showLog();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String  s = null;
                s = rh.sendGetRequest(Config.getLogTanaman+id_tanaman);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
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

                String  waktu_panen = jo.getString("waktu_panen");
                String  durasi_panen = jo.getString("durasi_panen");
                String  estimasi_panen = jo.getString("estimasi_panen");
                String  estimasi_harga = jo.getString("estimasi_harga");

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
                txtEstimasiPanen.setText(panen);
                txtWaktu.setText(waktu_panen+" Hari");
                txtJml.setText(jml_panen+" Kali");
                txtDurasi.setText(durasi_panen+" Hari");
                txtestimasi_panen.setText(estimasi_panen+" Gr/panen");
                txtHarga.setText("Rp."+estimasi_harga+" Kg");


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
                loading = ProgressDialog.show(DetailPohonkuActivity.this,"Fetching Data","Wait...",false,false);
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

    //////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            bitmap = (Bitmap) data.getExtras().get("data");
            klikfoto.setImageBitmap(bitmap);


        }
    }
    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//         bmp = Bitmap.createScaledBitmap(bmp, 200, 200, true);
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();


        long lengthbmp = imageBytes.length;

        System.out.println("Ukuran 6"+lengthbmp);

        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(DetailPohonkuActivity.this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
            else
            {
                Toast.makeText(DetailPohonkuActivity.this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void TambahLogTanaman() {
        class TambahData extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(DetailPohonkuActivity.this, "Proses Kirim Data...", "Wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
//                            Toast.makeText(MapsActivity.this,id_user+" "+nama_lokasi+" "+longt+" "+latt+" ",Toast.LENGTH_SHORT).show();
                Intent i =new Intent(getApplicationContext(),DetailPohonkuActivity.class);
                startActivity(i);
                finish();
            }

            @Override
            protected String doInBackground(Void... v) {

                HashMap<String, String> params = new HashMap<>();

                params.put(Config.TAG_LOG_id_tanaman, id_tanaman);
                params.put(Config.TAG_LOG_keterangan, spinner.getSelectedItem().toString());
                if(bitmap==null){
                    params.put(Config.TAG_LOG_foto,"");
                }else{
                    params.put(Config.TAG_LOG_foto, getStringImage(bitmap));
                }

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Config.simpanLogTanaman, params);
                return res;
            }
        }
        TambahData ae = new TambahData();
        ae.execute();

    }
}