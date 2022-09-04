package com.jatmika.admin_e_complaintrangkasbitung;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jatmika.admin_e_complaintrangkasbitung.API.API;
import com.jatmika.admin_e_complaintrangkasbitung.API.APIUtility;
import com.jatmika.admin_e_complaintrangkasbitung.Model.PercentFormatter;
import com.jatmika.admin_e_complaintrangkasbitung.Model.PersentaseKomplain;
import com.jatmika.admin_e_complaintrangkasbitung.SharePref.SharePref;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersentaseKomplainActivity extends AppCompatActivity {

    private PieChart mPieChart;
    TextView tvJudul;
    RelativeLayout btnBack;
    Animation fromright;

    API apiService;
    SharePref sharePref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_persentase);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        apiService = APIUtility.getAPI();
        sharePref = new SharePref(this);

        tvJudul = findViewById(R.id.tvJudul);
        btnBack = findViewById(R.id.btnBack);

        fromright = AnimationUtils.loadAnimation(this, R.anim.fromright);
        tvJudul.startAnimation(fromright);

        mPieChart = findViewById(R.id.pieChart);
//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("data_persentase_komplain");

        final List<PieEntry> pieEntries = new ArrayList<>();

        apiService.getPersentase("Bearer "+sharePref.getTokenApi()).enqueue(new Callback<List<PersentaseKomplain>>() {
            @Override
            public void onResponse(Call<List<PersentaseKomplain>> call, Response<List<PersentaseKomplain>> response) {
                for (PersentaseKomplain persentaseKomplain : response.body()){
                    pieEntries.add(new PieEntry(Float.parseFloat(persentaseKomplain.getPersen()), persentaseKomplain.getKategori()));
                    mPieChart.setVisibility(View.VISIBLE);
                    mPieChart.animateXY(3000, 3000);

                    PieDataSet pieDataSet = new PieDataSet(pieEntries, "E-Complaint");
                    pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                    pieDataSet.setValueTextColor(Color.WHITE);
                    pieDataSet.setValueTextSize(12);
                    pieDataSet.setValueFormatter(new PercentFormatter());

                    PieData pieData = new PieData(pieDataSet);
                    mPieChart.setData(pieData);

                    Description description = new Description();
                    description.setText("Persentase Pengiriman Komplain Masyarakat Desa Rancabango");
                    mPieChart.setDescription(description);
                    mPieChart.invalidate();
                }
            }

            @Override
            public void onFailure(Call<List<PersentaseKomplain>> call, Throwable t) {

            }
        });

//        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                for (DataSnapshot s : dataSnapshot.getChildren()) {
//                    PersentaseKomplain persentaseKomplain = s.getValue(PersentaseKomplain.class);
//                    pieEntries.add(new PieEntry(Float.parseFloat(persentaseKomplain.getPersen()), persentaseKomplain.getKategori()));
//                }
//
//                mPieChart.setVisibility(View.VISIBLE);
//                mPieChart.animateXY(3000, 3000);
//
//                PieDataSet pieDataSet = new PieDataSet(pieEntries, "E-Complaint");
//                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
//                pieDataSet.setValueTextColor(Color.WHITE);
//                pieDataSet.setValueTextSize(12);
//                pieDataSet.setValueFormatter(new PercentFormatter());
//
//                PieData pieData = new PieData(pieDataSet);
//                mPieChart.setData(pieData);
//
//                Description description = new Description();
//                description.setText("Persentase Pengiriman Komplain Masyarakat Desa Rancabango");
//                mPieChart.setDescription(description);
//                mPieChart.invalidate();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
