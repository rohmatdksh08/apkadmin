package com.jatmika.admin_e_complaintrangkasbitung;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
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
import com.jatmika.admin_e_complaintrangkasbitung.API.API;
import com.jatmika.admin_e_complaintrangkasbitung.API.APIUtility;
import com.jatmika.admin_e_complaintrangkasbitung.Model.DataUser;
import com.jatmika.admin_e_complaintrangkasbitung.SharePref.SharePref;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.text.TextUtils.isEmpty;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    API apiService;
    SharePref sharePref;

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
                jenkel = "Laki-laki";
            }
        });

        radioWanita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioPria.setChecked(false);
                jenkel = "Perempuan";
            }
        });

        apiService = APIUtility.getAPI();
        sharePref = new SharePref(this);


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
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(InputPendudukActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.show_loading, null);

                mBuilder.setView(mView);
                mBuilder.setCancelable(false);
                final AlertDialog mDialog = mBuilder.create();
                mDialog.show();
                String myFormat = "YYYY-MM-dd";
                DateFormat sdf = new SimpleDateFormat(myFormat);
                String tanggalLahir = sdf.format(myCalendar.getTime());
                Log.i("dateDate", tanggalLahir);
                if (TextUtils.isEmpty(edNik.getText().toString()) || TextUtils.isEmpty(edNama.getText().toString()) || TextUtils.isEmpty(edAlamat.getText().toString()) || TextUtils.isEmpty(edTlahir.getText().toString()) || TextUtils.isEmpty(edTTL.getText().toString()) || TextUtils.isEmpty(jenkel)){
                    Toast.makeText(InputPendudukActivity.this, "Data tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                    mDialog.dismiss();
                    return;
                }else{
                    apiService.addPenduduk("Bearer "+sharePref.getTokenApi(), edNik.getText().toString(), edNama.getText().toString(), jenkel, edTlahir.getText().toString(), tanggalLahir, edAlamat.getText().toString()).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if(response.code() == 200){
                                Toast.makeText(InputPendudukActivity.this, "Data Berhasil ditambah", Toast.LENGTH_SHORT).show();
                                mDialog.dismiss();
                                Intent intent = new Intent(InputPendudukActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });
                }


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
