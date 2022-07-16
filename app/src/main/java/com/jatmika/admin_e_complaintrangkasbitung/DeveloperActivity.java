package com.jatmika.admin_e_complaintrangkasbitung;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class DeveloperActivity extends AppCompatActivity {

    TableRow tblFb, tblIg;
    TableLayout tabel1;
    RelativeLayout btnBack;
    TextView tvJudul;

    Animation fromright, frombottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_developer);

        tblFb = findViewById(R.id.row_fb);
        tblIg = findViewById(R.id.row_ig);
        tabel1 = findViewById(R.id.tabel1);
        btnBack = findViewById(R.id.btnBack);
        tvJudul = findViewById(R.id.tvJudul);
        Toolbar toolbar = findViewById(R.id.toolbar);

        fromright = AnimationUtils.loadAnimation(this, R.anim.fromright);
        frombottom = AnimationUtils.loadAnimation(this, R.anim.frombottom);

        tvJudul.startAnimation(fromright);
        tabel1.startAnimation(frombottom);
        setSupportActionBar(toolbar);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tblFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://www.facebook.com/adoel.ccputrasulunghsj"));
                startActivity(intent);
            }
        });

        tblIg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://www.instagram.com/abdul_dongkers/"));
                startActivity(intent);
            }
        });
    }
}
