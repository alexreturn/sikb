package com.stikom.si_kb.Activity_planterbag;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.stikom.si_kb.Activity_lahan.LokasiActivity;
import com.stikom.si_kb.Activity_lahan.MapsActivity;
import com.stikom.si_kb.Config.Config;
import com.stikom.si_kb.Config.RequestHandler;
import com.stikom.si_kb.R;

import java.util.HashMap;

public class MapsPlanterActivity  extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    TextView textView4;
    Button btnKembali,btnSimpan;
    private Dialog customDialog;
    private EditText txtInputName,txtluas_lahan;
    private Button btnInsertName,btnCancel;
    String id_user,nama_lokasi,luas_lahan;
    Double longt,latt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_planter);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        SharedPreferences sharedPreferences = MapsPlanterActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        id_user = sharedPreferences.getString(Config.id_user_SHARED_PREF, "null");

        textView4=(TextView)findViewById(R.id.textView4);
        btnKembali=(Button)findViewById(R.id.btnKembali);
        btnSimpan=(Button)findViewById(R.id.btnSimpan);
        btnKembali.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnSimpan.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                customDialog.show();
            }
        });

        initCustomDialog();
        // GET CURRENT LOCATION
        FusedLocationProviderClient mFusedLocation = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapsPlanterActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
            return;
        }
        mFusedLocation.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    LatLng sydney = new LatLng(location.getLatitude(),  location.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(sydney).title("Your Location")  .draggable(true));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,16));
                    textView4.setText("Koordinat : "+location.getLatitude()+", "+ location.getLongitude());
                    longt=location.getLongitude();
                    latt=location.getLatitude();
                    mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                        @Override
                        public void onMarkerDragStart(Marker arg0) {
                            // TODO Auto-generated method stub
                            Log.d("System out", "onMarkerDragStart..."+arg0.getPosition().latitude+"..."+arg0.getPosition().longitude);
                        }

                        @SuppressWarnings("unchecked")
                        @Override
                        public void onMarkerDragEnd(Marker arg0) {
                            // TODO Auto-generated method stub
                            Log.d("System out", "onMarkerDragEnd..."+arg0.getPosition().latitude+"..."+arg0.getPosition().longitude);
                            textView4.setText("Koordinat : "+arg0.getPosition().latitude+", "+ arg0.getPosition().longitude);
                            longt= arg0.getPosition().longitude;
                            latt= arg0.getPosition().latitude;
                            mMap.animateCamera(CameraUpdateFactory.newLatLng(arg0.getPosition()));
                        }

                        @Override
                        public void onMarkerDrag(Marker arg0) {
                            // TODO Auto-generated method stub
                            Log.i("System out", "onMarkerDrag...");
                        }
                    });
                }
            }
        });
    }
    private void initCustomDialog(){
        customDialog = new Dialog(MapsPlanterActivity.this);
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customDialog.setContentView(R.layout.popup_koordinat_planter);
        customDialog.setCancelable(true);

        txtInputName = customDialog.findViewById(R.id.userName);
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
                String name = txtInputName.getText().toString();

                nama_lokasi=name;
//                txtName.setText(name);
                customDialog.dismiss();
                TambahData(name);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(MapsPlanterActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

    }

    private void TambahData(String nama) {
        if (nama.equals("") ) {
            Toast.makeText(MapsPlanterActivity.this,"Nama Lokasi Tidak Boleh Kosong",Toast.LENGTH_SHORT).show();
        }else{
            class TambahData extends AsyncTask<Void, Void, String> {
                ProgressDialog loading;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    loading = ProgressDialog.show(MapsPlanterActivity.this, "Proses Kirim Data...", "Wait...", false, false);
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    loading.dismiss();
//                            Toast.makeText(MapsActivity.this,id_user+" "+nama_lokasi+" "+longt+" "+latt+" ",Toast.LENGTH_SHORT).show();
                    Intent i =new Intent(getApplicationContext(),LokasiplanterActivity.class);
                    startActivity(i);
                    finish();
                }

                @Override
                protected String doInBackground(Void... v) {

                    HashMap<String, String> params = new HashMap<>();
                    params.put(Config.KEY_LOKASI_id_user,id_user);
                    params.put(Config.KEY_LOKASI_nama_lokasi,nama_lokasi);
                    params.put(Config.KEY_LOKASI_ukuran_planter,"");
                    params.put(Config.KEY_LOKASI_longitude,longt+"");
                    params.put(Config.KEY_LOKASI_latitude,latt+"");
                    params.put(Config.KEY_LOKASI_kategori,"PLANTER");

                    RequestHandler rh = new RequestHandler();
                    String res = rh.sendPostRequest(Config.LOKASI_SIMPAN_URL, params);
                    return res;
                }
            }
            TambahData ae = new TambahData();
            ae.execute();
        }
    }
}