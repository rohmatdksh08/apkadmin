package com.jatmika.admin_e_complaintrangkasbitung;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import okhttp3.ResponseBody;
import pl.aprilapps.easyphotopicker.EasyImage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.jatmika.admin_e_complaintrangkasbitung.API.API;
import com.jatmika.admin_e_complaintrangkasbitung.API.APIUtility;
import com.jatmika.admin_e_complaintrangkasbitung.Model.Komentar;
import com.jatmika.admin_e_complaintrangkasbitung.Model.Proses;
import com.jatmika.admin_e_complaintrangkasbitung.SharePref.SharePref;

import java.io.ByteArrayOutputStream;
import java.io.File;

import static android.text.TextUtils.isEmpty;

public class UbahStatusActivity extends AppCompatActivity {

    TextView tvJudul;
    EditText edPesan, edOleh;
    Button chooseBtn, sendbtn;
    ImageView imageView;
    RelativeLayout btnBack;
    Animation fromright;

    private static final int PICK_IMAGE_CAMERA = 001;
    private static final int PICK_IMAGE_GALLERY = 002;
    private SharePref sharePref;
    private API apiService;

    private Uri mImageUri;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef, databaseReference;
    private StorageTask mUploadTask;
    private FirebaseStorage mStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_status);
        sharePref = new SharePref(this);
        apiService = APIUtility.getAPI();

        final String proseStatus = getIntent().getExtras().getString("prosesStatus");
        final String status = getIntent().getExtras().getString("status");
        final String getKey = getIntent().getExtras().getString("getKey");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tvJudul = findViewById(R.id.tvJudul);
        btnBack = findViewById(R.id.btnBack);
        edPesan = findViewById(R.id.edPesan);
        edOleh = findViewById(R.id.edOleh);
        sendbtn = findViewById(R.id.sendBtn);

        if (proseStatus.equals("Dalam Proses")){
            tvJudul.setText("Status Komplain (Dalam Proses)");
        } else {
            tvJudul.setText("Status Komplain (Selesai)");
        }

        fromright = AnimationUtils.loadAnimation(this, R.anim.fromright);
        tvJudul.startAnimation(fromright);

        mStorage = FirebaseStorage.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference("foto_status");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("data_komplain").child(getKey).child("status_komplain");


        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmpty(edPesan.getText().toString()) || isEmpty(edOleh.getText().toString())) {
                    Toast.makeText(UbahStatusActivity.this, "Data tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                }else{
                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(UbahStatusActivity.this);
                    View mView = getLayoutInflater().inflate(R.layout.show_loading, null);

                    mBuilder.setView(mView);
                    mBuilder.setCancelable(false);
                    final AlertDialog mDialog = mBuilder.create();
                    mDialog.show();
                    apiService.updateStatusComplain("Bearer "+sharePref.getTokenApi(), getKey, edOleh.getText().toString(), edPesan.getText().toString(), proseStatus).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.code() == 200){
                                Toast.makeText(UbahStatusActivity.this, "Status komplain berhasil diubah!", Toast.LENGTH_LONG).show();
                                mDialog.dismiss();
                                finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.i("responseErrpr", t.toString());

                        }
                    });
                }


            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new EasyImage.Callbacks() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {

            }

            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
                switch (type){
                    case PICK_IMAGE_CAMERA:
                        Glide.with(UbahStatusActivity.this)
                                .asBitmap()
                                .load(imageFile)
                                .into(imageView);
                        break;
                    case PICK_IMAGE_GALLERY:
                        Glide.with(UbahStatusActivity.this)
                                .load(imageFile)
                                .into(imageView);
                        break;
                }
            }

            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {

            }
        });
    }

    private void uploadFile() {
        final String getKey = getIntent().getExtras().getString("getKey");
        final String proseStatus = getIntent().getExtras().getString("prosesStatus");

        if (isEmpty(edPesan.getText().toString()) || isEmpty(edOleh.getText().toString())) {
            Toast.makeText(this, "Data tidak boleh kosong!", Toast.LENGTH_SHORT).show();

        } else {
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(UbahStatusActivity.this);
            View mView = getLayoutInflater().inflate(R.layout.show_loading, null);

            mBuilder.setView(mView);
            mBuilder.setCancelable(false);
            final AlertDialog mDialog = mBuilder.create();
            mDialog.show();

            mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.exists()){
                        if (proseStatus.equals("Dalam Proses")) {
                            FirebaseDatabase.getInstance().getReference("data_komplain").child(getKey).child("status").setValue("Dalam Proses")
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            imageView.setDrawingCacheEnabled(true);
                                            imageView.buildDrawingCache();
                                            Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                            byte[] data = baos.toByteArray();

                                            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                                                    + "." + getFileExtension(data));

                                            UploadTask uploadTask = fileReference.putBytes(data);
                                            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                        @Override
                                                        public void onSuccess(Uri uri) {
                                                            Proses upload = new Proses(uri.toString(), edPesan.getText().toString(), "Dalam proses "+edOleh.getText().toString());

                                                            String uploadId = mDatabaseRef.push().getKey();
                                                            mDatabaseRef.child(uploadId).setValue(upload).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    Toast.makeText(UbahStatusActivity.this, "Status komplain berhasil diubah!", Toast.LENGTH_LONG).show();
                                                                    mDialog.dismiss();
                                                                    finish();
                                                                }
                                                            });
                                                        }
                                                    });
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(UbahStatusActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                                }
                                            });
                                        }
                                    });

                        } else {
                            FirebaseDatabase.getInstance().getReference("data_komplain").child(getKey).child("status").setValue("Selesai")
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            imageView.setDrawingCacheEnabled(true);
                                            imageView.buildDrawingCache();
                                            Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                            byte[] data = baos.toByteArray();

                                            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                                                    + "." + getFileExtension(data));

                                            UploadTask uploadTask = fileReference.putBytes(data);
                                            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                        @Override
                                                        public void onSuccess(Uri uri) {
                                                            Proses upload = new Proses(uri.toString(), edPesan.getText().toString(), "Selesai diproses oleh "+edOleh.getText().toString());

                                                            String uploadId = mDatabaseRef.push().getKey();
                                                            mDatabaseRef.child(uploadId).setValue(upload).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    Toast.makeText(UbahStatusActivity.this, "Status komplain berhasil diubah!", Toast.LENGTH_LONG).show();
                                                                    mDialog.dismiss();
                                                                    finish();
                                                                }
                                                            });
                                                        }
                                                    });
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(UbahStatusActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                                }
                                            });
                                        }
                                    });
                        }

                    } else {
                        mDatabaseRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                if (proseStatus.equals("Dalam Proses")) {
                                    FirebaseDatabase.getInstance().getReference("data_komplain").child(getKey).child("status")
                                            .setValue("Dalam Proses").addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            imageView.setDrawingCacheEnabled(true);
                                            imageView.buildDrawingCache();
                                            Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                            byte[] data = baos.toByteArray();

                                            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                                                    + "." + getFileExtension(data));

                                            UploadTask uploadTask = fileReference.putBytes(data);
                                            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                        @Override
                                                        public void onSuccess(Uri uri) {
                                                            Proses upload = new Proses(uri.toString(), edPesan.getText().toString(), "Dalam proses "+edOleh.getText().toString());

                                                            String uploadId = mDatabaseRef.push().getKey();
                                                            mDatabaseRef.child(uploadId).setValue(upload).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    Toast.makeText(UbahStatusActivity.this, "Status komplain berhasil diubah!", Toast.LENGTH_LONG).show();
                                                                    mDialog.dismiss();
                                                                    finish();
                                                                }
                                                            });
                                                        }
                                                    });
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(UbahStatusActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                                }
                                            });
                                        }
                                    });

                                } else {
                                    FirebaseDatabase.getInstance().getReference("data_komplain").child(getKey).child("status")
                                            .setValue("Selesai").addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            imageView.setDrawingCacheEnabled(true);
                                            imageView.buildDrawingCache();
                                            Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                                            byte[] data = baos.toByteArray();

                                            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                                                    + "." + getFileExtension(data));

                                            UploadTask uploadTask = fileReference.putBytes(data);
                                            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                        @Override
                                                        public void onSuccess(Uri uri) {
                                                            Proses upload = new Proses(uri.toString(), edPesan.getText().toString(), "Selesai diproses oleh "+edOleh.getText().toString());

                                                            String uploadId = mDatabaseRef.push().getKey();
                                                            mDatabaseRef.child(uploadId).setValue(upload).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    Toast.makeText(UbahStatusActivity.this, "Status komplain berhasil diubah!", Toast.LENGTH_LONG).show();
                                                                    mDialog.dismiss();
                                                                    finish();
                                                                }
                                                            });
                                                        }
                                                    });
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(UbahStatusActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                                }
                                            });
                                        }
                                    });
                                }
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private byte[] getFileExtension(byte[] data) {
        return data;
    }
}
