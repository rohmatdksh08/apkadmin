package com.jatmika.admin_e_complaintrangkasbitung;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailPendudukActivity extends AppCompatActivity {
    TextView tvNIK, tvEmail, tvNama, tvTTL, tvJenkel, tvAlamat, tvNohp, tvJudul;
    CircleImageView fotoProfile;
    RelativeLayout btnBack;
    Animation fromright;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_detail_data_penduduk);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent i = this.getIntent();
        String image = i.getExtras().getString("IMAGE_KEY");
        String nik = i.getExtras().getString("NIK_KEY");
        String nama = i.getExtras().getString("NAMA_KEY");
        String ttl = i.getExtras().getString("TTL_KEY");
        String jenkel = i.getExtras().getString("JENKEL_KEY");
        String alamat = i.getExtras().getString("ALAMAT_KEY");
        String getKey = i.getExtras().getString("GETPRIMARY_KEY");

        tvNIK = findViewById(R.id.tvNik);
        tvNama = findViewById(R.id.tvNama);
        tvTTL = findViewById(R.id.tvTTL);
        tvJenkel = findViewById(R.id.tvJenkel);
        tvAlamat = findViewById(R.id.tvAlamat);
        fotoProfile = findViewById(R.id.imageProfile);
        tvJudul = findViewById(R.id.tvJudul);
        btnBack = findViewById(R.id.btnBack);

        fromright = AnimationUtils.loadAnimation(this, R.anim.fromright);
        tvJudul.startAnimation(fromright);

        tvNIK.setText(nik);
        tvNama.setText(nama);
        tvTTL.setText(ttl);
        tvJenkel.setText(jenkel);
        tvAlamat.setText(alamat);
        Glide.with(this)
                .load(R.mipmap.ic_launcher)
                .into(fotoProfile);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
