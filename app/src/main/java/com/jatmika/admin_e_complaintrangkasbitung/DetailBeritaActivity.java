package com.jatmika.admin_e_complaintrangkasbitung;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jatmika.admin_e_complaintrangkasbitung.API.API;
import com.jatmika.admin_e_complaintrangkasbitung.API.APIUtility;
import com.jatmika.admin_e_complaintrangkasbitung.Adapter.RecyclerAdapterKomentar;
import com.jatmika.admin_e_complaintrangkasbitung.Adapter.RecyclerAdapterKomentarBerita;
import com.jatmika.admin_e_complaintrangkasbitung.Model.Komentar;
import com.jatmika.admin_e_complaintrangkasbitung.SharePref.SharePref;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import static android.text.TextUtils.isEmpty;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailBeritaActivity extends AppCompatActivity {

    TextView judulDetailTextView, tanggalDetailTextView, penulisDetailTextView, beritaDetailTextView, tvJudul;
    ImageView fotoDetailImageView;
    Button btnTambahKomentar;
    LinearLayout linear2;
    RelativeLayout btnBack;
    FloatingActionButton fab;
    Animation fromright;

    RecyclerView mRecyclerView;
    private RecyclerAdapterKomentarBerita mAdapter;
    DatabaseReference mDatabaseRef;
    private List<Komentar> mKomentar;
    ValueEventListener mDBListener;
    SharePref sharePref;
    API apiService;

    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;

    private void initializeWidgets(){
        judulDetailTextView = findViewById(R.id.judulDetailTextView);
        tanggalDetailTextView = findViewById(R.id.tanggalDetailTextView);
        penulisDetailTextView = findViewById(R.id.penulisDetailTextView);
        beritaDetailTextView = findViewById(R.id.beritaDetailTextView);
        fotoDetailImageView = findViewById(R.id.fotoDetailImageView);
        btnTambahKomentar = findViewById(R.id.btnTambahKomentar);
        linear2 = findViewById(R.id.linear2);
        fab = findViewById(R.id.fab);
        btnBack = findViewById(R.id.btnBack);
        tvJudul = findViewById(R.id.tvJudul);
    }

    String judul, tanggal, penulis, berita, image, getKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_detail_berita);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent i = this.getIntent();
        judul = i.getExtras().getString("JUDUL_KEY");
        tanggal = i.getExtras().getString("TANGGAL_KEY");
        penulis = i.getExtras().getString("PENULIS_KEY");
        berita = i.getExtras().getString("BERITA_KEY");
        image = i.getExtras().getString("IMAGE_KEY");
        getKey = i.getExtras().getString("GETPRIMARY_KEY");
        sharePref = new SharePref(this);
        apiService = APIUtility.getAPI();

        initializeWidgets();
        displayKomentar();

        fromright = AnimationUtils.loadAnimation(this, R.anim.fromright);
        tvJudul.startAnimation(fromright);
        linear2.setVisibility(View.GONE);

        judulDetailTextView.setText(judul);
        tanggalDetailTextView.setText(tanggal);
        penulisDetailTextView.setText(penulis);
        beritaDetailTextView.setText(berita);
        String urlImage = "https://api-rohmat.kosanbahari.xyz/uploads/"+image;
        Glide.with(this)
                .load(urlImage)
                .into(fotoDetailImageView);

        fotoDetailImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(DetailBeritaActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.show_image, null);
                ImageView imageView = mView.findViewById(R.id.imageView);
                TextView btnClose = mView.findViewById(R.id.btnClose);
                String urlImage = "https://api-rohmat.kosanbahari.xyz/uploads/"+image;

                Glide.with(DetailBeritaActivity.this)
                        .load(urlImage)
                        .into(imageView);

                mBuilder.setView(mView);
                final AlertDialog mDialog = mBuilder.create();
                mDialog.show();

                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialog.dismiss();
                    }
                });
            }
        });

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        btnTambahKomentar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ScrollView scrollView = findViewById(R.id.scrollView);
                btnTambahKomentar.setVisibility(View.GONE);
                scrollView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                        scrollView.isSmoothScrollingEnabled();
                    }}, 200);
                linear2.setVisibility(View.VISIBLE);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText input = findViewById(R.id.input);
                final ScrollView scrollView = findViewById(R.id.scrollView);

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(DetailBeritaActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.show_loading, null);

                mBuilder.setView(mView);
                mBuilder.setCancelable(false);
                final AlertDialog mDialog = mBuilder.create();
                mDialog.show();

                if (isEmpty(input.getText().toString())) {
                    Toast.makeText(DetailBeritaActivity.this, "Komentar tidak boleh kosong!",
                            Toast.LENGTH_SHORT).show();
                    mDialog.dismiss();

                } else {
                    apiService.addComentarBerita("Bearer "+sharePref.getTokenApi(), getKey, input.getText().toString()).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if(response.code() == 200){
                                linear2.setVisibility(View.GONE);
                                input.setText("");
                                btnTambahKomentar.setVisibility(View.VISIBLE);
                                mDialog.dismiss();
                                displayKomentar();

                                scrollView.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                                        scrollView.isSmoothScrollingEnabled();
                                    }
                                }, 200);
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });
//                    FirebaseDatabase.getInstance().getReference("data_berita").child(getKey)
//                            .child("komentar").push().setValue(new Komentar(input.getText().toString(), "Admin",
//                            "")).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                @Override
//                                public void onSuccess(Void aVoid) {
//                                    linear2.setVisibility(View.GONE);
//                                    input.setText("");
//                                    btnTambahKomentar.setVisibility(View.VISIBLE);
//                                    mDialog.dismiss();
//
//                                    scrollView.postDelayed(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            scrollView.fullScroll(ScrollView.FOCUS_DOWN);
//                                            scrollView.isSmoothScrollingEnabled();
//                                        }
//                                    }, 200);
//                                }
//                            });
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

    @Override
    public void onBackPressed() {
        finish();
    }

    private void displayKomentar() {
        mRecyclerView = findViewById(R.id.list_of_komentar);
        RecyclerView.LayoutManager layoutManager = new FlexboxLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        mKomentar = new ArrayList<>();
        mAdapter = new RecyclerAdapterKomentarBerita (DetailBeritaActivity.this, mKomentar);
        mRecyclerView.setAdapter(mAdapter);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("data_berita").child(getKey).child("komentar");
        apiService.getComentarBerita("Bearer "+sharePref.getTokenApi(), getKey).enqueue(new Callback<List<Komentar>>() {
            @Override
            public void onResponse(Call<List<Komentar>> call, Response<List<Komentar>> response) {
                mKomentar.clear();
                if(response.code() == 200){
                    for (Komentar komentar : response.body()){
                        mKomentar.add(komentar);
                    }
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Komentar>> call, Throwable t) {

            }
        });
//        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                mKomentar.clear();
//
//                for (DataSnapshot komentarSnapshot : dataSnapshot.getChildren()) {
//                    Komentar upload = komentarSnapshot.getValue(Komentar.class);
//                    upload.setKey(komentarSnapshot.getKey());
//                    mKomentar.add(upload);
//                }
//                mAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Toast.makeText(DetailBeritaActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
    }
}
