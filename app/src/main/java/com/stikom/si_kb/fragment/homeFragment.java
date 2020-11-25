package com.stikom.si_kb.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.squareup.picasso.Picasso;
import com.stikom.si_kb.Activity_lahan.LokasiActivity;
import com.stikom.si_kb.Activity_planterbag.LokasiplanterActivity;
import com.stikom.si_kb.Config.RequestHandler;
import com.stikom.si_kb.LoginActivity;
import com.stikom.si_kb.Profile_activity.tipsActivity;
import com.stikom.si_kb.Config.Config;
import com.stikom.si_kb.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class homeFragment extends Fragment {

    TextView txtname,txtLahan,txtPlanter,txtJmlTanaman;
    ImageButton btnLahan,btnPlant,btnPanen,btnTips;
    ImageView imageView;
    SharedPreferences sharedPreferences;
    String id_user,nama,telp,alamat,longitude,latitude,foto,status_user,jumlahPlanter,jumlahTanaman,jumlahLahan;
    private ProgressDialog loading;
    private String JSON_STRING;
    View view;

    String tutorial;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

         sharedPreferences = this.getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
         id_user = sharedPreferences.getString(Config.id_user_SHARED_PREF, "null");
         nama = sharedPreferences.getString(Config.nama_SHARED_PREF, "null");
         foto = sharedPreferences.getString(Config.foto_SHARED_PREF, "null");
         tutorial = sharedPreferences.getString(Config.TUTORIAL_SIMPAN, "true");


        txtLahan=(TextView)view.findViewById(R.id.txtLahan);
        txtPlanter=(TextView)view.findViewById(R.id.txtPlanter);
        txtJmlTanaman=(TextView)view.findViewById(R.id.txtJmlTanaman);

        txtname=(TextView)view.findViewById(R.id.txtNama);
        txtname.setText("Hi, "+nama);


        imageView=(ImageView) view.findViewById(R.id.imageView);
        System.out.println("ini foto  "+foto);
        if(foto==null || foto.equals("")){

        }else {
            Picasso.get().load(Config.URL + foto).transform(new CropCircleTransformation()).into(imageView);
        }
        btnLahan=(ImageButton)view.findViewById(R.id.btnLahan);
        btnPlant=(ImageButton)view.findViewById(R.id.btnPlant);
        btnPanen=(ImageButton)view.findViewById(R.id.btnPanen);
        btnTips=(ImageButton)view.findViewById(R.id.btnTips);


        btnLahan.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent( getContext(), LokasiActivity.class);
                startActivity(intent);
            }
        });
        btnPlant.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent( getContext(), LokasiplanterActivity.class);
                startActivity(intent);
//                Toast.makeText(getActivity(), "Belum Tersedia", Toast.LENGTH_LONG).show();
            }
        });
        btnPanen.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Belum Tersedia", Toast.LENGTH_LONG).show();

            }
        });
        btnTips.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent( getContext(), tipsActivity.class);
                startActivity(intent);
            }
        });
        getUser();

        System.out.println("INI TUTORIAL "+ tutorial);
        if(tutorial.equals("true") || tutorial=="true") {
            TapTargetView.showFor(getActivity(),                   // `this` is an Activity
                    TapTarget.forView(view.findViewById(R.id.btnLahan), "Lahanku", "Tekan disini untuk menanam Tanaman pada Lahan")
                            // All options below are optional
                            .outerCircleColor(R.color.lingkaran)      // Specify a color for the outer circle
                            .outerCircleAlpha(0.95f)           // Specify the alpha amount for the outer circle
                            .titleTextSize(20)
                            .targetCircleColor(R.color.lingkarankecil)           // Specify the size (in sp) of the title text
                            .titleTextColor(R.color.putih)      // Specify the color of the title text
                            .descriptionTextSize(16)            // Specify the size (in sp) of the description text
                            .descriptionTextColor(R.color.merah)  // Specify the color of the description text
                            .textColor(R.color.colorAccent)            // Specify a color for both the title and description text
                            .textTypeface(Typeface.SANS_SERIF)  // Specify a typeface for the text
                            .dimColor(R.color.colorPrimaryDark)            // If set, will dim behind the view with 30% opacity of the given color
                            .drawShadow(true)                   // Whether to draw a drop shadow or not
                            .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                            .tintTarget(true)                   // Whether to tint the target view's color
                            .transparentTarget(false)           // Specify whether the target is transparent (displays the content underneath)
                            // Specify a custom drawable to draw as the target
                            .targetRadius(60),                  // Specify the target radius (in dp)
                    new TapTargetView.Listener() {          // The listener can listen for regular clicks, long clicks or cancels
                        @Override
                        public void onTargetClick(TapTargetView view) {
                            super.onTargetClick(view);      // This call is optional
                            nextTarget();
                        }
                    });
        }

        return view;
    }

    void nextTarget(){
        TapTargetView.showFor(getActivity(),                   // `this` is an Activity
                TapTarget.forView(view.findViewById(R.id.btnPlant), "Planter Bag", "Tekan disini untuk menanam Tanaman pada Planterbag")
                        // All options below are optional
                        .outerCircleColor(R.color.lingkaran)      // Specify a color for the outer circle
                        .outerCircleAlpha(0.95f)           // Specify the alpha amount for the outer circle
                        .titleTextSize(20)
                        .targetCircleColor(R.color.lingkarankecil)           // Specify the size (in sp) of the title text
                        .titleTextColor(R.color.putih)      // Specify the color of the title text
                        .descriptionTextSize(16)            // Specify the size (in sp) of the description text
                        .descriptionTextColor(R.color.merah)  // Specify the color of the description text
                        .textColor(R.color.colorAccent)            // Specify a color for both the title and description text
                        .textTypeface(Typeface.SANS_SERIF)  // Specify a typeface for the text
                        .dimColor(R.color.colorPrimaryDark)            // If set, will dim behind the view with 30% opacity of the given color
                        .drawShadow(true)                   // Whether to draw a drop shadow or not
                        .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                        .tintTarget(true)                   // Whether to tint the target view's color
                        .transparentTarget(false)           // Specify whether the target is transparent (displays the content underneath)
                        // Specify a custom drawable to draw as the target
                        .targetRadius(60),                  // Specify the target radius (in dp)
                new TapTargetView.Listener() {          // The listener can listen for regular clicks, long clicks or cancels
                    @Override
                    public void onTargetClick(TapTargetView view) {
                        super.onTargetClick(view);      // This call is optional
                        nextTarget2();
                    }
                });
    }
    void nextTarget2(){
        TapTargetView.showFor(getActivity(),                   // `this` is an Activity
                TapTarget.forView(view.findViewById(R.id.btnPanen), "Panen", "untuk menjual atau membeli tanaman")
                        // All options below are optional
                        .outerCircleColor(R.color.lingkaran)      // Specify a color for the outer circle
                        .outerCircleAlpha(0.95f)           // Specify the alpha amount for the outer circle
                        .titleTextSize(20)
                        .targetCircleColor(R.color.lingkarankecil)           // Specify the size (in sp) of the title text
                        .titleTextColor(R.color.putih)      // Specify the color of the title text
                        .descriptionTextSize(16)            // Specify the size (in sp) of the description text
                        .descriptionTextColor(R.color.merah)  // Specify the color of the description text
                        .textColor(R.color.colorAccent)            // Specify a color for both the title and description text
                        .textTypeface(Typeface.SANS_SERIF)  // Specify a typeface for the text
                        .dimColor(R.color.colorPrimaryDark)            // If set, will dim behind the view with 30% opacity of the given color
                        .drawShadow(true)                   // Whether to draw a drop shadow or not
                        .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                        .tintTarget(true)                   // Whether to tint the target view's color
                        .transparentTarget(false)           // Specify whether the target is transparent (displays the content underneath)
                        // Specify a custom drawable to draw as the target
                        .targetRadius(60),                  // Specify the target radius (in dp)
                new TapTargetView.Listener() {          // The listener can listen for regular clicks, long clicks or cancels
                    @Override
                    public void onTargetClick(TapTargetView view) {
                        super.onTargetClick(view);      // This call is optional
                        nextTarget3();
                    }
                });
    }
    void nextTarget3(){
        TapTargetView.showFor(getActivity(),                   // `this` is an Activity
                TapTarget.forView(view.findViewById(R.id.btnTips), "Tips dan Trik", "Berisikan Tips dan Trik untuk menanam tanaman")
                        // All options below are optional
                        .outerCircleColor(R.color.lingkaran)      // Specify a color for the outer circle
                        .outerCircleAlpha(0.95f)           // Specify the alpha amount for the outer circle
                        .titleTextSize(20)
                        .targetCircleColor(R.color.lingkarankecil)           // Specify the size (in sp) of the title text
                        .titleTextColor(R.color.putih)      // Specify the color of the title text
                        .descriptionTextSize(16)            // Specify the size (in sp) of the description text
                        .descriptionTextColor(R.color.merah)  // Specify the color of the description text
                        .textColor(R.color.colorAccent)            // Specify a color for both the title and description text
                        .textTypeface(Typeface.SANS_SERIF)  // Specify a typeface for the text
                        .dimColor(R.color.colorPrimaryDark)            // If set, will dim behind the view with 30% opacity of the given color
                        .drawShadow(true)                   // Whether to draw a drop shadow or not
                        .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                        .tintTarget(true)                   // Whether to tint the target view's color
                        .transparentTarget(false)           // Specify whether the target is transparent (displays the content underneath)
                        // Specify a custom drawable to draw as the target
                        .targetRadius(60),                  // Specify the target radius (in dp)
                new TapTargetView.Listener() {          // The listener can listen for regular clicks, long clicks or cancels
                    @Override
                    public void onTargetClick(TapTargetView view) {
                        super.onTargetClick(view);      // This call is optional
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(Config.TUTORIAL_SIMPAN, "false");
                        editor.commit();
                    }
                });
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
                status_user = jo.getString("status_user");
                jumlahLahan = jo.getString("jumlahLahan");
                jumlahTanaman = jo.getString("jumlahTanaman");
                jumlahPlanter = jo.getString("jumlahPlanter");

            }
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.putString(Config.nama_SHARED_PREF, nama);
//            editor.putString(Config.telp_SHARED_PREF, telp);
//            editor.putString(Config.alamat_SHARED_PREF, alamat);
//            editor.putString(Config.longitude_SHARED_PREF, longitude);
//            editor.putString(Config.latitude_SHARED_PREF, latitude);
//            editor.putString(Config.foto_SHARED_PREF, foto);
//
//            editor.commit();



            txtLahan.setText("Lahanku : "+jumlahLahan+" Lahan");
            txtPlanter.setText("Planter Bag : "+jumlahPlanter+" Bag");
            txtJmlTanaman.setText("Jumlah Tanaman : "+jumlahTanaman);



            System.out.println("INI STATUS "+status_user);
            if(status_user.equals("0")||status_user== "0"){

                System.out.println("INI STATUSSATAA ");
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, false);
                editor.putString(Config.USERNAME_SHARED_PREF, "");
                editor.putString(Config.password_SHARED_PREF, "");
                editor.commit();

                Intent intent = new Intent( this.getActivity(), LoginActivity.class);
                startActivity(intent);
                this.getActivity().finish();
            }

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
                String s = rh.sendGetRequest(Config.HOMEDETAIL_URL+id_user);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }
}