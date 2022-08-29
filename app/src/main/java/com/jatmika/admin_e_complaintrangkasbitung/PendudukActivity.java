package com.jatmika.admin_e_complaintrangkasbitung;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.jatmika.admin_e_complaintrangkasbitung.API.API;
import com.jatmika.admin_e_complaintrangkasbitung.API.APIUtility;
import com.jatmika.admin_e_complaintrangkasbitung.Adapter.PendudukAdapter;
import com.jatmika.admin_e_complaintrangkasbitung.Model.Penduduk;
import com.jatmika.admin_e_complaintrangkasbitung.SharePref.SharePref;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PendudukActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    RelativeLayout btnBack;
    FloatingActionButton fab;
    TextView namaPenduduk, alamatPenduduk, lahirPenduduk;
    PendudukAdapter adapter;
    RecyclerView recyclerView;
    private List<Penduduk> penduduks;
    View.OnClickListener listiner;

    SharePref sharePref;
    API apiService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_data_penduduk);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PendudukActivity.this, InputPendudukActivity.class);
                startActivity(intent);

            }
        });

            btnBack = findViewById(R.id.btnBack);

                btnBack.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    finish();
                }

            });
        }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}
