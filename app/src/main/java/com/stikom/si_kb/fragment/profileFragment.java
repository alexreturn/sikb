package com.stikom.si_kb.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.stikom.si_kb.Config.Config;
import com.stikom.si_kb.LoginActivity;
import com.stikom.si_kb.Profile_activity.AboutActivity;
import com.stikom.si_kb.Profile_activity.LihatProfileActivity;
import com.stikom.si_kb.Profile_activity.gantiPassActivity;
import com.stikom.si_kb.R;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class profileFragment extends Fragment {
    TextView lihatprofile,textView,txtAbout,gantiPassword;
    Button btnlogout;
    ImageView imageView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String nama = sharedPreferences.getString(Config.nama_SHARED_PREF, "null");
        String foto = sharedPreferences.getString(Config.foto_SHARED_PREF, "null");

        imageView=(ImageView)view.findViewById(R.id.imageView);
        Picasso.get().load(Config.URL+foto) .transform(new CropCircleTransformation()).into(imageView);

        textView=(TextView)view.findViewById(R.id.textView);
        textView.setText(nama);

        txtAbout=(TextView)view.findViewById(R.id.txtAbout);

        lihatprofile=(TextView)view.findViewById(R.id.lihatprofile);
        lihatprofile.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent( getContext(), LihatProfileActivity.class);
                startActivity(intent);
            }
        });
        gantiPassword=(TextView)view.findViewById(R.id.gantiPassword);
        gantiPassword.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent( getContext(), gantiPassActivity.class);
                startActivity(intent);
            }
        });
        txtAbout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent( getContext(), AboutActivity.class);
                startActivity(intent);
            }
        });

        btnlogout=(Button)view.findViewById(R.id.btnlogout);
        btnlogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                logout();
            }
        });

        return view;
    }

    public void logout(){
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, false);
        editor.putString(Config.USERNAME_SHARED_PREF, "");
        editor.putString(Config.password_SHARED_PREF, "");
        editor.commit();

        Intent intent = new Intent( this.getActivity(), LoginActivity.class);
        startActivity(intent);
        this.getActivity().finish();
    }
}