package com.stikom.si_kb.Profile_activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.stikom.si_kb.Config.Config;
import com.stikom.si_kb.Config.RequestHandler;
import com.stikom.si_kb.LoginActivity;
import com.stikom.si_kb.R;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public class gantiPassActivity extends AppCompatActivity {
    Button btnEdit,btnKembali;
    SharedPreferences sharedPreferences;
    String USERNAME_SIMPAN,id_user,pass_user;

    EditText editOldPass, editNewPass, editNewPass2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ganti_pass);
        sharedPreferences = gantiPassActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        USERNAME_SIMPAN = sharedPreferences.getString(Config.USERNAME_SHARED_PREF, "null");
        id_user = sharedPreferences.getString(Config.id_user_SHARED_PREF, "null");
        pass_user = sharedPreferences.getString(Config.password_SHARED_PREF, "null");


        editOldPass=(EditText)findViewById(R.id.editOldPass);
        editNewPass=(EditText)findViewById(R.id.editNewPass);
        editNewPass2=(EditText)findViewById(R.id.editNewPass2);


        btnKembali=(Button)findViewById(R.id.btnKembali);
        btnKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateProfile();
//                finish();
            }
        });

    }


    private void UpdateProfile() {

        System.out.println(pass_user+" "+editOldPass.getText().toString());
        if (!pass_user.equals(editOldPass.getText().toString())) {
            Toast.makeText(gantiPassActivity.this, "Sandi Salah", Toast.LENGTH_LONG).show();
        }else if(editNewPass.getText().toString().equals("") || editNewPass.getText().toString().length()< 6 ) {
            Toast.makeText(gantiPassActivity.this, "Sandi Kurang dari 6 digit", Toast.LENGTH_LONG).show();
        }else if(!editNewPass.getText().toString().equals(editNewPass2.getText().toString()) ) {
            Toast.makeText(gantiPassActivity.this, "Sandi baru tidak sama", Toast.LENGTH_LONG).show();
        }else{
            class TambahData extends AsyncTask<Void, Void, String> {
                ProgressDialog loading;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    loading = ProgressDialog.show(gantiPassActivity.this, "Proses Kirim Data...", "Wait...", false, false);
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    loading.dismiss();
                    SharedPreferences sharedPreferences = gantiPassActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(Config.password_SHARED_PREF, editNewPass.getText().toString());
                    editor.commit();

                    finish();
                }

                @Override
                protected String doInBackground(Void... v) {

                    HashMap<String, String> params = new HashMap<>();

                    params.put(Config.KEY_EMP_id_user, id_user);
                    params.put(Config.KEY_EMP_password, editNewPass.getText().toString());

                    RequestHandler rh = new RequestHandler();
                    String res = rh.sendPostRequest(Config.EDIT_PASS_URL, params);
                    return res;
                }
            }
            TambahData ae = new TambahData();
            ae.execute();

        }
    }
}