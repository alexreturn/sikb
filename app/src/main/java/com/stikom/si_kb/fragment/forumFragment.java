package com.stikom.si_kb.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.stikom.si_kb.Activity.LokasiActivity;
import com.stikom.si_kb.Activity.tipsActivity;
import com.stikom.si_kb.Config.Config;
import com.stikom.si_kb.R;


public class forumFragment extends Fragment {


    LinearLayout layWA,layCall,layWEB;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forum, container, false);

        layWA=(LinearLayout)view.findViewById(R.id.layWA);
        layCall=(LinearLayout)view.findViewById(R.id.layCall);
        layWEB=(LinearLayout)view.findViewById(R.id.layWEB);

        layWA.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String url = "https://api.whatsapp.com/send?phone=+6281339341632";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        layCall.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", "+6281339341632",null));
                startActivity(intent);
            }
        });
        layWEB.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
                startActivity(browserIntent);
            }
        });



        return view;
    }
}