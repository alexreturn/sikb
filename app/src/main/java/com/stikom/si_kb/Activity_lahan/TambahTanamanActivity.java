package com.stikom.si_kb.Activity_lahan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.stikom.si_kb.Config.Config;
import com.stikom.si_kb.Config.ModelDataTanaman;
import com.stikom.si_kb.Config.RequestHandler;
import com.stikom.si_kb.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class TambahTanamanActivity extends AppCompatActivity {


    JSONArray result;
    private String JSON_STRING;
    private ProgressDialog loading;
    Spinner SpinTanaman;
    Button btnKembali,btnTambah;
    TextView txtInfo,txtWaktu,txtJml;
    private Dialog customDialog;
    ImageView imageView2;
    private EditText txtInputKeterangan;
    private TextView txtInputTanggal;
    private Button btnInsertName,btnCancel;

    List<String> valueId = new ArrayList<String>();
    List<String> valueNama = new ArrayList<String>();
    List<String> valueJenis = new ArrayList<String>();
    List<String> valueInfo = new ArrayList<String>();
    List<String> valueMusim = new ArrayList<String>();
    List<String> valueJml_planterbag = new ArrayList<String>();
    List<String> valueWaktu_panen = new ArrayList<String>();
    List<String> valueJml_panen = new ArrayList<String>();
    List<String> valueKeterangan = new ArrayList<String>();
    List<String> valueStatus = new ArrayList<String>();
    List<String> valueTimestamp = new ArrayList<String>();

     String id_lokasi,id_user,id_pohon,nama,tanggal_tanam,panen,jml_panen,status,keterangan;
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    int PICK_IMAGE_REQUEST = 1;
    Bitmap bitmap,storebitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_tanaman);


        SharedPreferences sharedPreferences = TambahTanamanActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        id_lokasi = sharedPreferences.getString(Config.LOKASI_SIMPAN_LOKASI, "null");
        id_user = sharedPreferences.getString(Config.id_user_SHARED_PREF, "null");



        imageView2=(ImageView) findViewById(R.id.imageView2);
        BitmapDrawable drawable = (BitmapDrawable) imageView2.getDrawable();
        storebitmap = drawable.getBitmap();
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if (TambahTanamanActivity.this.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                    } else {
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            }
        });
        SpinTanaman=(Spinner)findViewById(R.id.SpinTanaman);

        btnKembali=(Button) findViewById(R.id.btnKembali);
        btnKembali.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent( TambahTanamanActivity.this, TanamankuActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnTambah=(Button) findViewById(R.id.btnTambah);
        btnTambah.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                customDialog.show();
            }
        });
        initCustomDialog();

        txtInfo=(TextView) findViewById(R.id.txtInfo);
        txtWaktu=(TextView) findViewById(R.id.txtWaktu);
        txtJml=(TextView) findViewById(R.id.txtJml);

        getJSONLokasi();
    }
    private void initCustomDialog(){
        customDialog = new Dialog(TambahTanamanActivity.this);
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customDialog.setContentView(R.layout.popup_tambah_tanaman);
        customDialog.setCancelable(true);

        txtInputTanggal = customDialog.findViewById(R.id.txtTanggal);
        txtInputKeterangan = customDialog.findViewById(R.id.txtKeterangan);

        btnInsertName = customDialog.findViewById(R.id.btnsimpan);
        btnCancel = customDialog.findViewById(R.id.btncancel);

        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        tanggal_tanam=currentDate;

        txtInputTanggal.setText(currentDate);
        txtInputTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar newCalendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(TambahTanamanActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                        txtInputTanggal.setText(""+dateFormatter.format(newDate.getTime()));
                        tanggal_tanam=""+dateFormatter.format(newDate.getTime());
                    }
                },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });



        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                customDialog.dismiss();
            }
        });
        btnInsertName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog.dismiss();
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TambahTanamanActivity.this);
                alertDialogBuilder.setTitle("Tambah Tanaman?");
                alertDialogBuilder
                        .setMessage("Periksa Data Sebelum Di simpan!")
                        .setCancelable(false)
                        .setPositiveButton("Simpan",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                TambahDataTanaman(txtInputKeterangan.getText().toString());

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
    }

    private void showLokasi(){
        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject(JSON_STRING);
            result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            ArrayList<ModelDataTanaman> listDataTanaman = new ArrayList<ModelDataTanaman>();
            listDataTanaman.clear();
            for(int i = 0; i<result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);

                ModelDataTanaman modelTanaman = new ModelDataTanaman();
                modelTanaman.setId(jo.getString("id"));
                modelTanaman.setNama(jo.getString("nama"));
                modelTanaman.setJenis(jo.getString("jenis"));
                modelTanaman.setInfo(jo.getString("info"));
                modelTanaman.setMusim(jo.getString("musim"));
                modelTanaman.setJml_planterbag(jo.getString("jml_planterbag"));
                modelTanaman.setWaktu_panen(jo.getString("waktu_panen"));
                modelTanaman.setJml_panen(jo.getString("jml_panen"));
//                modelTanaman.setKeterangan(jo.getString("keterangan"));
//                modelTanaman.setStatus(jo.getString("status"));
                modelTanaman.setTimestamp(jo.getString("timestamp"));


//                String  id = jo.getString(Config.KEY_DATA_id);
//                String  nama = jo.getString(Config.KEY_DATA_nama);
//                String  jenis = jo.getString(Config.KEY_DATA_jenis);
//                String  info = jo.getString(Config.KEY_DATA_info);
//                String  musim = jo.getString(Config.KEY_DATA_musim);
//                String  jml_planterbag = jo.getString(Config.KEY_DATA_jml_planterbag);
//                String  waktu_panen = jo.getString(Config.KEY_DATA_waktu_panen);
//                String  timestamp = jo.getString(Config.KEY_DATA_timestamp);
//
//                HashMap<String, String> employees = new HashMap<>();
//                employees.put(Config.KEY_DATA_id, id);
//                employees.put(Config.KEY_DATA_nama, nama);
//                employees.put(Config.KEY_DATA_jenis, jenis);
//                employees.put(Config.KEY_DATA_info, info);
//                employees.put(Config.KEY_DATA_musim, musim);
//                employees.put(Config.KEY_DATA_jml_planterbag, jml_planterbag);
//                employees.put(Config.KEY_DATA_waktu_panen, waktu_panen);
//                employees.put(Config.KEY_DATA_timestamp, timestamp);

                listDataTanaman.add(modelTanaman);
            }
            for (int i = 0; i < listDataTanaman.size(); i++) {
                valueId.add(listDataTanaman.get(i).getId());
                valueNama.add(listDataTanaman.get(i).getNama());
                valueJenis.add(listDataTanaman.get(i).getJenis());
                valueInfo.add(listDataTanaman.get(i).getInfo());
                valueMusim.add(listDataTanaman.get(i).getMusim());
                valueJml_planterbag.add(listDataTanaman.get(i).getJml_planterbag());
                valueWaktu_panen.add(listDataTanaman.get(i).getWaktu_panen());
                valueJml_panen.add(listDataTanaman.get(i).getJml_panen());
                valueTimestamp.add(listDataTanaman.get(i).getTimestamp());

            }

            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(TambahTanamanActivity.this,
                    android.R.layout.simple_spinner_item, valueNama);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            SpinTanaman.setAdapter(spinnerAdapter);
            SpinTanaman.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    String idTanaman = valueId.get(position);
                    String namaTanaman = valueNama.get(position);
                    String jenisTanaman = valueJenis.get(position);
                    String infoTanaman = valueInfo.get(position);
                    String musimTanaman = valueMusim.get(position);
                    String jml_planterbagTanaman = valueJml_planterbag.get(position);
                    String waktuPanenTanaman = valueWaktu_panen.get(position);
                    String Jml_panenTanaman = valueJml_panen.get(position);
                    String timestampTanaman = valueTimestamp.get(position);

                    nama=namaTanaman;
                    id_pohon=idTanaman;
                    txtInfo.setText(infoTanaman);
                    txtWaktu.setText(waktuPanenTanaman+" Hari");
                    txtJml.setText(Jml_panenTanaman+" Kali");


                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                    Calendar c = Calendar.getInstance();
                    try {
                        c.setTime(sdf.parse(tanggal_tanam));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    c.add(Calendar.DATE, Integer.parseInt(waktuPanenTanaman));  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
                    SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
                    String output = sdf1.format(c.getTime());
                    panen=output;
                    jml_panen=Jml_panenTanaman;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }

            });


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    private void getJSONLokasi(){
        class GetJSON extends AsyncTask<Void,Void,String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(TambahTanamanActivity.this,"Fetching Data","Wait...",false,false);
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
                s = rh.sendGetRequest(Config.DATA_TANAMAN);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }


    private void TambahDataTanaman(final String keterangannya) {
            class TambahData extends AsyncTask<Void, Void, String> {
                ProgressDialog loading;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    loading = ProgressDialog.show(TambahTanamanActivity.this, "Proses Kirim Data...", "Wait...", false, false);
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    loading.dismiss();
//                            Toast.makeText(MapsActivity.this,id_user+" "+nama_lokasi+" "+longt+" "+latt+" ",Toast.LENGTH_SHORT).show();
                    Intent i =new Intent(getApplicationContext(),LokasiActivity.class);
                    startActivity(i);
                    finish();
                }

                @Override
                protected String doInBackground(Void... v) {

                    HashMap<String, String> params = new HashMap<>();

//                    System.out.println("ININI "+id_lokasi+" "+id_user+" "+id_pohon+" "+nama+" "+tanggal_tanam+" "+panen+" "+jml_panen);
                    params.put(Config.TAG_TANAMANKU_id_lokasi, id_lokasi);
                    params.put(Config.TAG_TANAMANKU_id_user, id_user);
                    params.put(Config.TAG_TANAMANKU_id_pohon, id_pohon);
                    params.put(Config.TAG_TANAMANKU_nama, nama);
                    params.put(Config.TAG_TANAMANKU_tanggal_tanam, tanggal_tanam);
                    params.put(Config.TAG_TANAMANKU_panen, panen);
                    params.put(Config.TAG_TANAMANKU_jumlah_panen, jml_panen);
                    params.put(Config.TAG_TANAMANKU_keterangan, txtInputKeterangan.getText().toString());
                    if(bitmap==null){
                        params.put(Config.TAG_TANAMANKU_foto_tanaman, getStringImage(storebitmap));
                    }else{
                        params.put(Config.TAG_TANAMANKU_foto_tanaman, getStringImage(bitmap));
                    }

                    RequestHandler rh = new RequestHandler();
                    String res = rh.sendPostRequest(Config.TANAMAN_SIMPAN_URL, params);
                    return res;
                }
            }
            TambahData ae = new TambahData();
            ae.execute();

    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            bitmap = (Bitmap) data.getExtras().get("data");
            imageView2.setImageBitmap(bitmap);


        }
    }
    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // bmp = Bitmap.createScaledBitmap(bmp, 200, 200, true);
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
                Toast.makeText(TambahTanamanActivity.this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
            else
            {
                Toast.makeText(TambahTanamanActivity.this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }
}