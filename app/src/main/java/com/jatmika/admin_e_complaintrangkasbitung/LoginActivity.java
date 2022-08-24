package com.jatmika.admin_e_complaintrangkasbitung;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jatmika.admin_e_complaintrangkasbitung.API.API;
import com.jatmika.admin_e_complaintrangkasbitung.API.APIUtility;
import com.jatmika.admin_e_complaintrangkasbitung.Model.Admin;
import com.jatmika.admin_e_complaintrangkasbitung.Model.TokenApi;
import com.jatmika.admin_e_complaintrangkasbitung.SharePref.SharePref;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText edUsername, edPass;
    ImageView btnShow;
    Button btnLogin;
    String show = "SHOW";
    String username, password;
    private API apiService;
    private SharePref sharePref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        apiService = APIUtility.getAPI();
        sharePref = new SharePref(this);

        if(sharePref.getStatusLogin() == true) {
            Intent a = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(a);
        }


        edUsername = findViewById(R.id.edUsername);
        edPass = findViewById(R.id.edPass);
        btnShow = findViewById(R.id.ic_toggle);
        btnLogin = findViewById(R.id.btnLogin);

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

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = edUsername.getText().toString();
                password = edPass.getText().toString();

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(LoginActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.show_loading, null);

                mBuilder.setView(mView);
                mBuilder.setCancelable(false);
                final AlertDialog mDialog = mBuilder.create();
                mDialog.show();

                if (TextUtils.isEmpty(username)){
                    Toast.makeText(LoginActivity.this, "Username harus diisi!", Toast.LENGTH_SHORT).show();
                    mDialog.dismiss();
                    return;
                }

                if (TextUtils.isEmpty(password)){
                    Toast.makeText(LoginActivity.this, "Password harus diisi!", Toast.LENGTH_SHORT).show();
                    mDialog.dismiss();
                    return;
                }

                if (password.length() < 6){
                    Toast.makeText(LoginActivity.this, "Password minimal harus 6 karakter!", Toast.LENGTH_SHORT).show();
                    mDialog.dismiss();
                    return;
                }

                apiService.login(username, password, "android").enqueue(new Callback<TokenApi>() {
                    @Override
                    public void onResponse(Call<TokenApi> call, Response<TokenApi> response) {
                        mDialog.dismiss();
                        if(response.body().getCode() == 404){
                            Toast.makeText(LoginActivity.this, "Akun tidak ditemukan", Toast.LENGTH_SHORT).show();

                        }

                        if (response.body().getCode() == 200){
                            Log.i("token", response.body().getAccessToken());
                            sharePref.setStatusLogin(true);
                            sharePref.setTokenApi(response.body().getAccessToken());
                            Intent dashboard = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(dashboard);
                        }
                    }

                    @Override
                    public void onFailure(Call<TokenApi> call, Throwable t) {
                        Log.i("errorResponse", t.toString());
                    }
                });

            }
        });
    }

    public void onBackPressed(){
        startActivity(new Intent(LoginActivity.this, SplashActivity.class));
        moveTaskToBack(true);
    }
}
