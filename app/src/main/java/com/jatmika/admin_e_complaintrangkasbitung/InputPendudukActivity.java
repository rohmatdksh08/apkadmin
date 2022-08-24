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

    ImageView photo, chooseBtn, btnShow;
    EditText edNik, edEmail, edPass, edNama, edTTL, edAlamat, edNohp;
    RadioButton radioPria, radioWanita;
    Button btnDaftar;

    Uri mImageUri;
    StorageReference mStorageRef;
    DatabaseReference mDatabaseRef;
    StorageTask mUploadTask;
    FirebaseAuth auth;

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
        photo = findViewById(R.id.photo);
        chooseBtn = findViewById(R.id.chooseBtn);
        edNik = findViewById(R.id.edNIK);
        edEmail = findViewById(R.id.edEmail);
        edPass = findViewById(R.id.edPass);
        edNama = findViewById(R.id.edNama);
        edTTL = findViewById(R.id.edTTL);
        radioPria = findViewById(R.id.radioPria);
        radioWanita = findViewById(R.id.radioWanita);
        edAlamat = findViewById(R.id.edAlamat);
        edNohp = findViewById(R.id.edNohp);
        btnShow = findViewById(R.id.ic_toggle);
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

        auth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference("foto_user");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("data_user");

        edPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(show.equals("SHOW")) {
                    show = "HIDE";
                    edPass.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    edPass.setSelection(edPass.length());
                } else {
                    show = "SHOW";
                    edPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    edPass.setSelection(edPass.length());
                }
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

        chooseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        btnDaftar.setOnClickListener(new View.OnClickListener() {
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

    private void updateLabel(){
        String myFormat = "dd MMMM yyyy";
        DateFormat sdf = new SimpleDateFormat(myFormat);
        edTTL.setText(sdf.format(myCalendar.getTime()));
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

            Glide.with(this).load(mImageUri).into(photo);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
        String email = edEmail.getText().toString();
        final String password = edPass.getText().toString();

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(com.jatmika.admin_e_complaintrangkasbitung.InputPendudukActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.show_loading, null);

        mBuilder.setView(mView);
        mBuilder.setCancelable(false);
        final AlertDialog mDialog = mBuilder.create();
        mDialog.show();

        if (TextUtils.isEmpty(email)){
            Toast.makeText(com.jatmika.admin_e_complaintrangkasbitung.InputPendudukActivity.this, "Email harus diisi!", Toast.LENGTH_SHORT).show();
            mDialog.dismiss();

        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(com.jatmika.admin_e_complaintrangkasbitung.InputPendudukActivity.this, "Email tidak valid!", Toast.LENGTH_SHORT).show();
            mDialog.dismiss();

        } else if (TextUtils.isEmpty(password)){
            Toast.makeText(com.jatmika.admin_e_complaintrangkasbitung.InputPendudukActivity.this, "Password harus diisi!", Toast.LENGTH_SHORT).show();
            mDialog.dismiss();

        } else if (password.length() < 6){
            Toast.makeText(com.jatmika.admin_e_complaintrangkasbitung.InputPendudukActivity.this, "Password minimal harus 6 karakter", Toast.LENGTH_SHORT).show();
            mDialog.dismiss();

        } else if (mImageUri == null){
            Toast.makeText(com.jatmika.admin_e_complaintrangkasbitung.InputPendudukActivity.this, "Photo harus ditambahkan!", Toast.LENGTH_SHORT).show();
            mDialog.dismiss();

        } else if (isEmpty(edNama.getText().toString()) || isEmpty(edTTL.getText().toString())
                || isEmpty(edAlamat.getText().toString()) || isEmpty(edNohp.getText().toString())){
            Toast.makeText(this, "Data harus dilengkapi!", Toast.LENGTH_SHORT).show();
            mDialog.dismiss();

        } else if(mImageUri != null || !isEmpty(edNama.getText().toString()) || !isEmpty(edTTL.getText().toString())
                || !isEmpty(edAlamat.getText().toString()) || !isEmpty(edNohp.getText().toString())){
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                                + "." + getFileExtension(mImageUri));

                        mUploadTask = fileReference.putFile(mImageUri)
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                DataUser upload = new DataUser(uri.toString(), edNik.getText().toString(), edEmail.getText().toString(),
                                                        edPass.getText().toString(), edNama.getText().toString(),
                                                        edTTL.getText().toString(), jenkel, edAlamat.getText().toString(),
                                                        edNohp.getText().toString());

                                                String uploadId = mDatabaseRef.push().getKey();
                                                mDatabaseRef.child(uploadId).setValue(upload).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(com.jatmika.admin_e_complaintrangkasbitung.InputPendudukActivity.this, "Berhasil daftar akun, silahkan login!", Toast.LENGTH_LONG).show();
                                                        mDialog.dismiss();
                                                        finish();
                                                    }
                                                });
                                            }
                                        });
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(com.jatmika.admin_e_complaintrangkasbitung.InputPendudukActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                    }
                                });
                    } else {
                        Toast.makeText(com.jatmika.admin_e_complaintrangkasbitung.InputPendudukActivity.this, "Email telah terdaftar!", Toast.LENGTH_LONG).show();
                        mDialog.dismiss();
                    }
                }
            });

        }
    }
}
