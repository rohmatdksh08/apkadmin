package com.jatmika.admin_e_complaintrangkasbitung;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
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
import com.jatmika.admin_e_complaintrangkasbitung.Adapter.RecyclerAdapterKomentar;
import com.jatmika.admin_e_complaintrangkasbitung.Adapter.RecyclerAdapterProses;
import com.jatmika.admin_e_complaintrangkasbitung.Model.Komentar;
import com.jatmika.admin_e_complaintrangkasbitung.Model.MySingleton;
import com.jatmika.admin_e_complaintrangkasbitung.Model.Proses;
import com.jatmika.admin_e_complaintrangkasbitung.Model.Suka;
import com.uncopt.android.widget.text.justify.JustifiedTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.text.TextUtils.isEmpty;

public class DetailInfrastrukturActivity extends AppCompatActivity {

    final private String serverKey = "key=" + "AIzaSyDL3TGOXPGwfM7iIf_pX4zqD3IbTr-I45w";
    final private String contentType = "application/json";
    final String TAG = "NOTIFICATION TAG";

    String FCM_API = "https://fcm.googleapis.com/fcm/send";
    String NOTIFICATION_TITLE;
    String NOTIFICATION_MESSAGE;
    String TOPIC;
    String Penerima;
    String prosesStatus;

    TextView nomorDetailTextView, namaDetailTextView, emailDetailTextView, alamatDetailTextView,
            tanggalDetailTextView, tvBalas, tvJudul, statusDetailtextView;
    JustifiedTextView isiDetailTextView;
    ImageView fotoDetailImageView;
    Button btnTambahKomentar;
    LinearLayout linear2, layout_kerangka;
    RelativeLayout relative1, relativeLayout, btnBack;
    FrameLayout contentFrame;
    FloatingActionButton fab;
    Animation fromright;

    RecyclerView mRecyclerView;
    private RecyclerAdapterKomentar mAdapter;
    private StorageReference mStorageRef;
    FirebaseStorage mStorage;
    DatabaseReference mDatabaseRef, databaseReference;
    private List<Komentar> mKomentar;
    ValueEventListener mDBListener;

    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;

    int satuan;

    private void initializeWidgets(){
        nomorDetailTextView = findViewById(R.id.nomorTextView);
        namaDetailTextView = findViewById(R.id.namaTextView);
        emailDetailTextView = findViewById(R.id.emailTextView);
        alamatDetailTextView = findViewById(R.id.alamatTextView);
        tanggalDetailTextView = findViewById(R.id.tanggalTextView);
        isiDetailTextView = findViewById(R.id.isiTextView);
        statusDetailtextView = findViewById(R.id.statusTextView);
        fotoDetailImageView = findViewById(R.id.fotoDetailImageView);
        contentFrame = findViewById(R.id.content_frame);
        relative1 = findViewById(R.id.relative1);
        btnTambahKomentar = findViewById(R.id.btnTambahKomentar);
        linear2 = findViewById(R.id.linear2);
        layout_kerangka = findViewById(R.id.layout_kerangka);
        tvBalas = findViewById(R.id.tvBalas);
        fab = findViewById(R.id.fab);
        tvJudul = findViewById(R.id.tvJudul);
        btnBack = findViewById(R.id.btnBack);
    }

    String image, berkas, nomor, nik, email, nama, alamat, tanggal, namalokasi, latitude, longitude, isi, kategori, status,
            jml_lihat, jml_suka, jml_balas, getKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_detail_infrastruktur);

        Intent i = this.getIntent();
        image = i.getExtras().getString("IMAGE_KEY");
        berkas = i.getExtras().getString("BERKAS_KEY");
        nomor = i.getExtras().getString("NOMOR_KEY");
        nik = i.getExtras().getString("NIK_KEY");
        email = i.getExtras().getString("EMAIL_KEY");
        nama = i.getExtras().getString("NAMA_KEY");
        alamat = i.getExtras().getString("ALAMAT_KEY");
        tanggal = i.getExtras().getString("TANGGAL_KEY");
        namalokasi = i.getExtras().getString("NAMALOKASI_KEY");
        latitude = i.getExtras().getString("LATITUDE_KEY");
        longitude = i.getExtras().getString("LONGITUDE_KEY");
        isi = i.getExtras().getString("ISI_KEY");
        kategori = i.getExtras().getString("KATEGORI_KEY");
        status = i.getExtras().getString("STATUS_KEY");
        jml_lihat = i.getExtras().getString("JML_LIHAT_KEY");
        jml_suka = i.getExtras().getString("JML_SUKA_KEY");
        jml_balas = i.getExtras().getString("JML_BALAS_KEY");
        getKey = i.getExtras().getString("GETPRIMARY_KEY");

        initializeWidgets();
        displayKomentar();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fromright = AnimationUtils.loadAnimation(this, R.anim.fromright);
        tvJudul.startAnimation(fromright);
        linear2.setVisibility(View.GONE);

        nomorDetailTextView.setText(nomor);
        emailDetailTextView.setText(email);
        namaDetailTextView.setText(nama);
        alamatDetailTextView.setText(alamat);
        tanggalDetailTextView.setText(tanggal);
        isiDetailTextView.setText("''"+isi+"''");
        statusDetailtextView.setText(status);
        Glide.with(this)
                .load(image)
                .into(fotoDetailImageView);

        fotoDetailImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(DetailInfrastrukturActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.show_image, null);
                ImageView imageView = mView.findViewById(R.id.imageView);
                TextView btnClose = mView.findViewById(R.id.btnClose);

                Glide.with(DetailInfrastrukturActivity.this)
                        .load(image)
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

        relative1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status.equals("Menunggu Diproses")){
                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(DetailInfrastrukturActivity.this);
                    View mView = getLayoutInflater().inflate(R.layout.show_menunggu, null);
                    TextView btnClose = mView.findViewById(R.id.btnClose);
                    TextView tvNomor = mView.findViewById(R.id.tvNomor);
                    RelativeLayout relative1 = mView.findViewById(R.id.relative1);

                    mBuilder.setView(mView);
                    final AlertDialog mDialog = mBuilder.create();
                    mDialog.show();

                    tvNomor.setText("No : "+nomor);
                    btnClose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mDialog.dismiss();
                        }
                    });

                    relative1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mDialog.dismiss();

                            CharSequence[] item = {"Dalam Proses", "Selesai"};
                            AlertDialog.Builder request = new AlertDialog.Builder(DetailInfrastrukturActivity.this)
                                    .setTitle("Pilih Status Komplain")
                                    .setItems(item, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int i) {
                                            switch (i){
                                                case 0:
                                                    prosesStatus = "Dalam Proses";
                                                    Intent intent = new Intent(DetailInfrastrukturActivity.this, UbahStatusActivity.class);
                                                    Bundle bundle = new Bundle();
                                                    bundle.putString("prosesStatus", prosesStatus);
                                                    bundle.putString("status", status);
                                                    bundle.putString("getKey", getKey);
                                                    intent.putExtras(bundle);
                                                    startActivity(intent);
                                                    break;
                                                case 1:
                                                    prosesStatus = "Selesai";
                                                    Intent intent2 = new Intent(DetailInfrastrukturActivity.this, UbahStatusActivity.class);
                                                    Bundle bundle2 = new Bundle();
                                                    bundle2.putString("prosesStatus", prosesStatus);
                                                    bundle2.putString("status", status);
                                                    bundle2.putString("getKey", getKey);
                                                    intent2.putExtras(bundle2);
                                                    startActivity(intent2);
                                                    break;
                                            }
                                        }
                                    });
                            request.create();
                            request.show();
                        }
                    });

                } else {
                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(DetailInfrastrukturActivity.this);
                    View mView = getLayoutInflater().inflate(R.layout.show_proses, null);

                    TextView btnClose = mView.findViewById(R.id.btnClose);
                    TextView tvNomor = mView.findViewById(R.id.tvNomor);
                    TextView tvStatus = mView.findViewById(R.id.tvStatus);
                    RelativeLayout relative2 = mView.findViewById(R.id.relative1);
                    RecyclerView mRecyclerView = mView.findViewById(R.id.mRecyclerView);
                    final RecyclerAdapterProses mAdapter;
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("data_komplain").child(getKey).child("status_komplain");
                    final List<Proses> mProsess;

                    mBuilder.setView(mView);
                    final AlertDialog mDialog = mBuilder.create();
                    mDialog.show();

                    mRecyclerView.setHasFixedSize(true);
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(DetailInfrastrukturActivity.this));

                    mProsess = new ArrayList<>();
                    mAdapter = new RecyclerAdapterProses(DetailInfrastrukturActivity.this, mProsess);
                    mRecyclerView.setAdapter(mAdapter);

                    mDBListener = databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            mProsess.clear();
                            for (DataSnapshot kandidatSnapshot : dataSnapshot.getChildren()) {
                                Proses upload = kandidatSnapshot.getValue(Proses.class);
                                upload.setKey(kandidatSnapshot.getKey());
                                mProsess.add(upload);
                            }
                            mAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(DetailInfrastrukturActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                    tvStatus.setText(status);
                    tvNomor.setText("No : "+nomor);
                    btnClose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mDialog.dismiss();
                        }
                    });

                    relative2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mDialog.dismiss();

                            CharSequence[] item = {"Dalam Proses", "Selesai"};
                            AlertDialog.Builder request = new AlertDialog.Builder(DetailInfrastrukturActivity.this)
                                    .setTitle("Pilih Status Komplain")
                                    .setItems(item, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int i) {
                                            switch (i){
                                                case 0:
                                                    prosesStatus = "Dalam Proses";
                                                    Intent intent = new Intent(DetailInfrastrukturActivity.this, UbahStatusActivity.class);
                                                    Bundle bundle = new Bundle();
                                                    bundle.putString("prosesStatus", prosesStatus);
                                                    bundle.putString("status", status);
                                                    bundle.putString("getKey", getKey);
                                                    intent.putExtras(bundle);
                                                    startActivity(intent);
                                                    break;
                                                case 1:
                                                    prosesStatus = "Selesai";
                                                    Intent intent2 = new Intent(DetailInfrastrukturActivity.this, UbahStatusActivity.class);
                                                    Bundle bundle2 = new Bundle();
                                                    bundle2.putString("prosesStatus", prosesStatus);
                                                    bundle2.putString("status", status);
                                                    bundle2.putString("getKey", getKey);
                                                    intent2.putExtras(bundle2);
                                                    startActivity(intent2);
                                                    break;
                                            }
                                        }
                                    });
                            request.create();
                            request.show();
                        }
                    });
                }
            }
        });

        satuan = 1;
        int jml_lihatlama = Integer.parseInt(jml_lihat);
        final int total = satuan + jml_lihatlama;
        FirebaseDatabase.getInstance().getReference("data_komplain").child(getKey).child("jml_lihat").setValue(String.valueOf(total));

        btnTambahKomentar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ScrollView scrollView = findViewById(R.id.scrollView);
                btnTambahKomentar.setVisibility(View.GONE);
                scrollView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                        scrollView.isSmoothScrollingEnabled(); }
                }, 200);
                linear2.setVisibility(View.VISIBLE);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText input = findViewById(R.id.input);
                final ScrollView scrollView = findViewById(R.id.scrollView);

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(DetailInfrastrukturActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.show_loading, null);

                mBuilder.setView(mView);
                mBuilder.setCancelable(false);
                final AlertDialog mDialog = mBuilder.create();
                mDialog.show();

                if (isEmpty(input.getText().toString())) {
                    Toast.makeText(DetailInfrastrukturActivity.this, "Balasan tidak boleh kosong!",
                            Toast.LENGTH_SHORT).show();
                    mDialog.dismiss();

                } else {
                    FirebaseDatabase.getInstance().getReference("data_komplain").child(getKey)
                            .child("balasan").push().setValue(new Komentar(input.getText().toString(),
                            "Admin", "")).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) { FirebaseDatabase.getInstance().getReference("data_komplain").child(getKey).child("balasan").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    long totalBalas;
                                    totalBalas = (dataSnapshot.getChildrenCount());
                                    FirebaseDatabase.getInstance().getReference("data_komplain").child(getKey).child("jml_balas").setValue(String.valueOf(totalBalas))
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Penerima = emailDetailTextView.getText().toString();
                                                    Penerima = Penerima.replaceAll("[@.-]", "");

                                                    TOPIC = "/topics/"+Penerima;
                                                    NOTIFICATION_TITLE = "Balasan Komplain Dari Admin";
                                                    NOTIFICATION_MESSAGE = input.getText().toString();

                                                    JSONObject notification = new JSONObject();
                                                    JSONObject notifcationBody = new JSONObject();
                                                    try {
                                                        notifcationBody.put("title", NOTIFICATION_TITLE);
                                                        notifcationBody.put("message", NOTIFICATION_MESSAGE);

                                                        notification.put("to", TOPIC);
                                                        notification.put("data", notifcationBody);
                                                    } catch (JSONException e) {
                                                        Log.e(TAG, "onCreate: " + e.getMessage() );
                                                    }
                                                    sendNotification(notification);

                                                    linear2.setVisibility(View.GONE);
                                                    input.setText("");
                                                    btnTambahKomentar.setVisibility(View.VISIBLE);
                                                    mDialog.dismiss();

                                                    scrollView.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                                                            scrollView.isSmoothScrollingEnabled();
                                                        }
                                                    }, 200);
                                                }
                                            });
                                } else {
                                    FirebaseDatabase.getInstance().getReference("data_komplain").child(getKey).child("jml_balas").setValue("0")
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Penerima = emailDetailTextView.getText().toString();
                                                    Penerima = Penerima.replaceAll("[@.-]", "");

                                                    TOPIC = "/topics/"+Penerima;
                                                    NOTIFICATION_TITLE = "Balasan Komplain Dari Admin";
                                                    NOTIFICATION_MESSAGE = input.getText().toString();

                                                    JSONObject notification = new JSONObject();
                                                    JSONObject notifcationBody = new JSONObject();
                                                    try {
                                                        notifcationBody.put("title", NOTIFICATION_TITLE);
                                                        notifcationBody.put("message", NOTIFICATION_MESSAGE);

                                                        notification.put("to", TOPIC);
                                                        notification.put("data", notifcationBody);
                                                    } catch (JSONException e) {
                                                        Log.e(TAG, "onCreate: " + e.getMessage() );
                                                    }
                                                    sendNotification(notification);

                                                    linear2.setVisibility(View.GONE);
                                                    input.setText("");
                                                    btnTambahKomentar.setVisibility(View.VISIBLE);
                                                    mDialog.dismiss();

                                                    scrollView.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                                                            scrollView.isSmoothScrollingEnabled();
                                                        }
                                                    }, 200);
                                                }
                                            });
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        }
                    });
                }
            }
        });

        Bundle bundle = new Bundle();
        bundle.putString("namalokasi", String.valueOf(namalokasi));
        bundle.putString("latitude", String.valueOf(latitude));
        bundle.putString("longitude", String.valueOf(longitude));
        bundle.putString("getKey", String.valueOf(getKey));
        FragmentMap fragmentMap = new FragmentMap();
        fragmentMap.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.content_frame, fragmentMap).commit();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void sendNotification(JSONObject notification) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "onResponse: " + response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DetailInfrastrukturActivity.this, "Request error!", Toast.LENGTH_LONG).show();
                        Log.i(TAG, "onErrorResponse: Didn't work");
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
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
        mAdapter = new RecyclerAdapterKomentar (DetailInfrastrukturActivity.this, mKomentar);
        mRecyclerView.setAdapter(mAdapter);

        mStorage = FirebaseStorage.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference("foto_komentar");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("data_komplain").child(getKey).child("balasan");
        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    mKomentar.clear();

                    for (DataSnapshot komentarSnapshot : dataSnapshot.getChildren()) {
                        Komentar upload = komentarSnapshot.getValue(Komentar.class);
                        upload.setKey(komentarSnapshot.getKey());
                        mKomentar.add(upload);
                    }
                    mAdapter.notifyDataSetChanged();
                    tvBalas.setText("Balasan Komplain");
                    layout_kerangka.setVisibility(View.VISIBLE);

                } else{
                    tvBalas.setText("Belum Ada Balasan");
                    layout_kerangka.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(DetailInfrastrukturActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
