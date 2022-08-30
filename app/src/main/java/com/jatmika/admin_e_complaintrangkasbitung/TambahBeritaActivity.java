package com.jatmika.admin_e_complaintrangkasbitung;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.jatmika.admin_e_complaintrangkasbitung.API.API;
import com.jatmika.admin_e_complaintrangkasbitung.API.APIUtility;
import com.jatmika.admin_e_complaintrangkasbitung.Model.DataBerita;
import com.jatmika.admin_e_complaintrangkasbitung.SharePref.SharePref;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.text.TextUtils.isEmpty;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahBeritaActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    Button chooseBtn, sendBtn;
    private EditText JudulBerita, TanggalPosting, tv_isiberita;
    private ImageView imageView;
    TextView tvJudul;
    RelativeLayout btnBack;

    Animation fromright;
    Calendar myCalendar;
    SharePref sharePref;
    API apiService;

    private Uri mImageUri;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private StorageTask mUploadTask;

    String Penulis = "Admin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_tambah_berita);

        chooseBtn = findViewById(R.id.chooseBtn);
        sendBtn = findViewById(R.id.sendBtn);
        JudulBerita = findViewById(R.id.tv_judulberita);
        TanggalPosting = findViewById(R.id.tv_tanggalposting);
        tv_isiberita = findViewById(R.id.tv_isiberita);
        imageView = findViewById(R.id.imageView);
        btnBack = findViewById(R.id.btnBack);
        tvJudul = findViewById(R.id.tvJudul);
        Toolbar toolbar = findViewById(R.id.toolbar);

        fromright = AnimationUtils.loadAnimation(this, R.anim.fromright);
        tvJudul.startAnimation(fromright);
        setSupportActionBar(toolbar);

        myCalendar = Calendar.getInstance();
        sharePref = new SharePref(this);
        apiService = APIUtility.getAPI();

        String myFormat = "dd MMM yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        TanggalPosting.setText(sdf.format(myCalendar.getTime()));

        mStorageRef = FirebaseStorage.getInstance().getReference("foto_berita");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("data_berita");

        chooseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFile();
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

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();

            Glide.with(this).load(mImageUri).into(imageView);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
        if (mImageUri == null || isEmpty(JudulBerita.getText().toString())
                || isEmpty(TanggalPosting.getText().toString()) || isEmpty(tv_isiberita.getText().toString())) {
            Toast.makeText(this, "Data tidak boleh kosong!", Toast.LENGTH_SHORT).show();

        } else {
            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));

            AlertDialog.Builder mBuilder = new AlertDialog.Builder(TambahBeritaActivity.this);
            View mView = getLayoutInflater().inflate(R.layout.show_loading, null);
            imageView.setDrawingCacheEnabled(true);
            imageView.buildDrawingCache();
            Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
            byte[] data = baos.toByteArray();

            mBuilder.setView(mView);
            mBuilder.setCancelable(false);
            final AlertDialog mDialog = mBuilder.create();
            mDialog.show();
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"),data);
            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("foto", System.currentTimeMillis()
                            + "." + data, requestFile);
            RequestBody isi = RequestBody.create(MediaType.parse("multipart/form-data"), tv_isiberita.getText().toString());
            RequestBody judul = RequestBody.create(MediaType.parse("multipart/form-data"), JudulBerita.getText().toString());

            apiService.addBerita("Bearer "+sharePref.getTokenApi(), isi, judul, body).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if(response.code() == 200){
                        mDialog.dismiss();
                        Toast.makeText(TambahBeritaActivity.this, "Berita berhasil dikirim!", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
//            mUploadTask = fileReference.putFile(mImageUri)
//                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//
//                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                @Override
//                                public void onSuccess(Uri uri) {
//                                    DataBerita upload = new DataBerita(JudulBerita.getText().toString().trim(),
//                                            uri.toString(), TanggalPosting.getText().toString(),
//                                            Penulis, tv_isiberita.getText().toString());
//
//                                    String uploadId = mDatabaseRef.push().getKey();
//                                    mDatabaseRef.child(uploadId).setValue(upload).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                        @Override
//                                        public void onSuccess(Void aVoid) {
//                                            mDialog.dismiss();
//                                            Toast.makeText(TambahBeritaActivity.this, "Berita berhasil dikirim!", Toast.LENGTH_LONG).show();
//                                            finish();
//                                        }
//                                    });
//                                }
//                            });
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(TambahBeritaActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    })
//                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
//                        }
//                    });
        }
    }
}
