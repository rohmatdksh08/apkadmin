package com.jatmika.admin_e_complaintrangkasbitung;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

public class KecamatanActivity extends AppCompatActivity {

    RelativeLayout btnBack;
    TextView tvJudul;
    Animation fromright;

    CarouselView carouselView;
//    int[] sampleImages = {R.drawable.kecamatan1, R.drawable.kecamatan2, R.drawable.kecamatan3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_kecamatan);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnBack = findViewById(R.id.btnBack);
        tvJudul = findViewById(R.id.tvJudul);

        fromright = AnimationUtils.loadAnimation(this, R.anim.fromright);
        tvJudul.startAnimation(fromright);

        carouselView = findViewById(R.id.carouselView);
//        carouselView.setPageCount(sampleImages.length);
        carouselView.setImageListener(imageListener);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
//            imageView.setImageResource(sampleImages[position]);
        }
    };
}
