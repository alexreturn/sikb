package com.stikom.si_kb.Profile_activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.stikom.si_kb.Config.Config;
import com.stikom.si_kb.Config.RequestHandler;
import com.stikom.si_kb.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class LihatProfileActivity extends AppCompatActivity {

    private String JSON_STRING;
    String id_user,USERNAME_SIMPAN,namas,telp,alamat,longitude,latitude,foto;
    SharedPreferences sharedPreferences;
    TextView txtUsername;
    EditText editNama,editAlamat,editNumber;
    Button btnEdit,btnKembali;
    String USERNAME_SHARED_PREF;

    String Status="EDIT";
    ImageView imageViewFoto;
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    int PICK_IMAGE_REQUEST = 1;
    Bitmap bitmap,storebitmap;
    String foto_store;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lihat_profile);

        sharedPreferences = LihatProfileActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        USERNAME_SHARED_PREF = sharedPreferences.getString(Config.USERNAME_SHARED_PREF, "null");
        foto_store = sharedPreferences.getString(Config.foto_SHARED_PREF, "null");

        imageViewFoto=(ImageView)findViewById(R.id.imageViewFoto);
        Picasso.get().load(Config.URL+foto_store) .transform(new CropCircleTransformation()).into(imageViewFoto);

        try {
            BitmapDrawable drawable = (BitmapDrawable) imageViewFoto.getDrawable();
            storebitmap = drawable.getBitmap();
        }catch (Exception sdsd){

        }

        imageViewFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("ini "+ bitmap);
                if(Status.equals("SIMPAN")) {
                    if (LihatProfileActivity.this.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                    } else {
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST);
                    }
                }
            }
        });

         txtUsername=(TextView)findViewById(R.id.txtUsername);
         txtUsername.setText(USERNAME_SHARED_PREF);

         editNama=(EditText)findViewById(R.id.editNama);
         editAlamat=(EditText)findViewById(R.id.editAlamat);
         editNumber=(EditText)findViewById(R.id.editNumber);

         btnEdit=(Button)findViewById(R.id.btnEdit);
         btnKembali=(Button)findViewById(R.id.btnKembali);

        editNama.setEnabled(false);
        editAlamat.setEnabled(false);
        editNumber.setEnabled(false);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Status.equals("EDIT")){
                    editNama.setEnabled(true);
                    editAlamat.setEnabled(true);
                    editNumber.setEnabled(true);
                    Status="SIMPAN";
                    btnEdit.setText("SIMPAN");
                    getUser();
                }else{
                    getUser();
                    btnEdit.setText("SUNTING PROFILE");
                    editNama.setEnabled(false);
                    editAlamat.setEnabled(false);
                    editNumber.setEnabled(false);
                    UpdateProfile();
                }
            }
        });
        btnKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        getUser();
    }

    private void showUser() {

        JSONObject jsonObject = null;
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);

            for (int i = 0; i < result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);
                id_user = jo.getString("id_user");
                namas = jo.getString("nama");
                telp = jo.getString("telp");
                alamat = jo.getString("alamat");
                longitude = jo.getString("longitude");
                latitude = jo.getString("latitude");
                foto = jo.getString("foto");
                USERNAME_SIMPAN = jo.getString("username");


            }

            editNama.setText(namas);
            editAlamat.setText(alamat);
            editNumber.setText(telp);

            System.out.println("adsasdnama  "+ namas);
            System.out.println("adsasdnama  "+alamat);
            System.out.println("adsasdnama  "+telp);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(Config.nama_SHARED_PREF, namas);
            editor.putString(Config.telp_SHARED_PREF, telp);
            editor.putString(Config.alamat_SHARED_PREF, alamat);
            editor.putString(Config.longitude_SHARED_PREF, longitude);
            editor.putString(Config.latitude_SHARED_PREF, latitude);
            editor.putString(Config.foto_SHARED_PREF, foto);
            editor.commit();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getUser(){

        class GetJSON extends AsyncTask<Void,Void,String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                JSON_STRING = s;
                showUser();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(Config.USER_URL+USERNAME_SHARED_PREF);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }



    private void UpdateProfile() {
        class TambahData extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(LihatProfileActivity.this, "Proses Kirim Data...", "Wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Status="EDIT";
                loading.dismiss();
////                            Toast.makeText(MapsActivity.this,id_user+" "+nama_lokasi+" "+longt+" "+latt+" ",Toast.LENGTH_SHORT).show();
//                Intent i =new Intent(getApplicationContext(), LihatProfileActivity.class);
//                startActivity(i);
                finish();
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
            protected String doInBackground(Void... v) {

                HashMap<String, String> params = new HashMap<>();

                params.put(Config.KEY_EMP_id_user, id_user);
                params.put(Config.KEY_EMP_nama, editNama.getText().toString());
                params.put(Config.KEY_EMP_alamat, editAlamat.getText().toString());
                params.put(Config.KEY_EMP_telp, editNumber.getText().toString());
                if(bitmap==null){
                    params.put(Config.KEY_EMP_foto, getStringImage(storebitmap));
                }else{
                    params.put(Config.KEY_EMP_foto, getStringImage(bitmap));
                }

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Config.EDIT_USER_URL, params);
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
            imageViewFoto.setImageBitmap(bitmap);
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
                Toast.makeText(LihatProfileActivity.this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
            else
            {
                Toast.makeText(LihatProfileActivity.this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }
}