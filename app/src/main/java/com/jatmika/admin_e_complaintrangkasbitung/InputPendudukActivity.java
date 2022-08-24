package com.jatmika.admin_e_complaintrangkasbitung;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.jatmika.admin_e_complaintrangkasbitung.Model.DataUser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.text.TextUtils.isEmpty;

public class InputPendudukActivity extends AppCompatActivity {

    RelativeLayout btnBack;
    TextView tvJudul;
    Animation fromright;

    EditText edNik,edNama,edTlahir, edTTL, edAlamat;
    RadioButton radioPria, radioWanita;
    Button btnDaftar;

    private static final int PICK_IMAGE_REQUEST = 1;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;

    String jenkel;
    String show = "SHOW";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_input_penduduk);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnBack = findViewById(R.id.btnBack);
        tvJudul = findViewById(R.id.tvJudul);
        edNik = findViewById(R.id.edNIK);
        edNama = findViewById(R.id.edNama);
        edTlahir = findViewById(R.id.edTlahir);
        edTTL = findViewById(R.id.edTTL);
        radioPria = findViewById(R.id.radioPria);
        radioWanita = findViewById(R.id.radioWanita);
        edAlamat = findViewById(R.id.edAlamat);

        btnDaftar = findViewById(R.id.btnDaftar);

        fromright = AnimationUtils.loadAnimation(this, R.anim.fromright);
        tvJudul.startAnimation(fromright);

        radioPria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioWanita.setChecked(false);
                jenkel = "Pria";
            }
        });

        radioWanita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioPria.setChecked(false);
                jenkel = "Wanita";
            }
        });


        myCalendar = Calendar.getInstance();
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        edTTL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(com.jatmika.admin_e_complaintrangkasbitung.InputPendudukActivity.this, date,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });



        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void updateLabel(){
        String myFormat = "dd MMMM yyyy";
        DateFormat sdf = new SimpleDateFormat(myFormat);
        edTTL.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}
