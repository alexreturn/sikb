package com.stikom.si_kb.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.stikom.si_kb.Activity.LokasiActivity;
import com.stikom.si_kb.Activity.tipsActivity;
import com.stikom.si_kb.Config.Config;
import com.stikom.si_kb.LoginActivity;
import com.stikom.si_kb.R;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class homeFragment extends Fragment {

    TextView txtname,txtnim;
    ImageButton btnLahan,btnPlant,btnPanen,btnTips;
    ImageView imageView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String nama = sharedPreferences.getString(Config.nama_SHARED_PREF, "null");
        String foto = sharedPreferences.getString(Config.foto_SHARED_PREF, "null");
//        String nim = sharedPreferences.getString(Config.NIM_SHARED_PREF, "null");
//        String alamat = sharedPreferences.getString(Config.alamat_SHARED_PREF, "null");
//        String jurusan = sharedPreferences.getString(Config.jurusan_SHARED_PREF, "null");
//
        txtname=(TextView)view.findViewById(R.id.txtNama);
//        txtnim=(TextView)view.findViewById(R.id.txtnim);
//
        txtname.setText("Hi, "+nama);
//        txtnim.setText(nim+" - "+jurusan);

//        Transformation transformation = new RoundedTransformationBuilder()
//                .borderColor(Color.BLACK)
//                .borderWidthDp(3)
//                .cornerRadiusDp(30)
//                .oval(false)
//                .build();
        imageView=(ImageView) view.findViewById(R.id.imageView);
        Picasso.get().load(Config.URL+foto) .transform(new CropCircleTransformation()).into(imageView);

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
                Intent intent = new Intent( getContext(), LokasiActivity.class);
                startActivity(intent);
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

        return view;
    }
}