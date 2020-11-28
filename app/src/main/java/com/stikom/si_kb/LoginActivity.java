package com.stikom.si_kb;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.stikom.si_kb.Config.Config;
import com.stikom.si_kb.Config.RequestHandler;
import com.stikom.si_kb.Pohonku.DetailPohonkuActivity;
import com.stikom.si_kb.Profile_activity.tipsActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    Button buttonLogin, btnTlp,btnWA;
    Boolean loggedIn= false;
    EditText txtUsername,txtPassword;
    private ProgressDialog loading;
    int success;
    private String JSON_STRING;
    String id_user,USERNAME_SIMPAN,nama,telp,alamat,longitude,latitude,foto;
    TextView txtLupaSandi;

    private Dialog customDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        txtUsername = (EditText) findViewById(R.id.txtusername);
        txtPassword = (EditText) findViewById(R.id.txtpassword);

        txtLupaSandi=(TextView)findViewById(R.id.txtLupaSandi);
        txtLupaSandi.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                customDialog.show();
            }
        });

        buttonLogin=(Button)findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(this);


        initCustomDialog();
    }
    private void initCustomDialog(){
        customDialog = new Dialog(LoginActivity.this);
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customDialog.setContentView(R.layout.popup_lupasandi);
        customDialog.setCancelable(true);

        btnWA=customDialog.findViewById(R.id.btnWA);
        btnWA.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View view) {
                String url = "https://api.whatsapp.com/send?phone=+6281339341632";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        btnTlp=customDialog.findViewById(R.id.btnTlp);
        btnTlp.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", "+6281339341632",null));
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onResume(){
        super.onResume();

        SharedPreferences sharedPreferences=getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        loggedIn=sharedPreferences.getBoolean(Config.LOGGEDIN_SHARED_PREF, false);


        if (loggedIn==true){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }

    }
    @Override
    public void onClick(View v) {
//        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//        startActivity(intent);
        login();
    }


    private void login() {
        final String USERNAME = txtUsername.getText().toString().trim();
        final String pass = txtPassword.getText().toString().trim();
        if(USERNAME.equals("") || pass.equals("")  ) {
            Toast.makeText(LoginActivity.this, "NIM/Password tidak boleh kosong", Toast.LENGTH_LONG).show();
        }else{
            loading = ProgressDialog.show(LoginActivity.this, "Memeriksa Akun", "Tunggu Yah...", false, false);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.LOGIN_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if(response.equals("success")){

                                SharedPreferences sharedPreferences = LoginActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();

                                editor.putString(Config.USERNAME_SHARED_PREF, USERNAME);
                                editor.putString(Config.password_SHARED_PREF, pass);
                                editor.commit();

                                getUser();
                            }else{
                                loading.dismiss();
                                Toast.makeText(LoginActivity.this, "Username/Password Salah", Toast.LENGTH_LONG).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            String json = null;
                            NetworkResponse response = error.networkResponse;
                            if (response != null && response.data != null) {
                                switch (response.statusCode) {
                                    case 400:
                                        json = new String(response.data);
                                        json = trimMessage(json, "message");
                                        if (json != null) displayMessage(json);
                                        break;
                                }
                            }
                        }
                        public String trimMessage(String json, String key) {
                            String trimmedString = null;
                            try {
                                JSONObject obj = new JSONObject(json);
                                trimmedString = obj.getString(key);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                return null;
                            }
                            return trimmedString;
                        }
                        public void displayMessage(String toastString) {
                            Toast.makeText(LoginActivity.this, toastString, Toast.LENGTH_LONG).show();
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put(Config.KEY_EMP_username, USERNAME);
                    params.put(Config.KEY_EMP_password, pass);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }
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
                nama = jo.getString("nama");
                telp = jo.getString("telp");
                alamat = jo.getString("alamat");
                longitude = jo.getString("longitude");
                latitude = jo.getString("latitude");
                foto = jo.getString("foto");
                USERNAME_SIMPAN = jo.getString("username");
            }
            SharedPreferences sharedPreferences = LoginActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, true);
            editor.putString(Config.id_user_SHARED_PREF, id_user);
            editor.putString(Config.nama_SHARED_PREF, nama);
            editor.putString(Config.telp_SHARED_PREF, telp);
            editor.putString(Config.alamat_SHARED_PREF, alamat);
            editor.putString(Config.longitude_SHARED_PREF, longitude);
            editor.putString(Config.latitude_SHARED_PREF, latitude);
            editor.putString(Config.foto_SHARED_PREF, foto);
//            editor.putString(Config.jurusan_SHARED_PREF, jurusan);
            editor.commit();

            loading.dismiss();

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);


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
                String s = rh.sendGetRequest(Config.USER_URL+txtUsername.getText().toString());
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }
}