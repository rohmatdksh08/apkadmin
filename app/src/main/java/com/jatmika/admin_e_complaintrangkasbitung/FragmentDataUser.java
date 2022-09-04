package com.jatmika.admin_e_complaintrangkasbitung;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.jatmika.admin_e_complaintrangkasbitung.API.API;
import com.jatmika.admin_e_complaintrangkasbitung.API.APIUtility;
import com.jatmika.admin_e_complaintrangkasbitung.Adapter.RecyclerAdapterDataUser;
import com.jatmika.admin_e_complaintrangkasbitung.Model.DataUser;
import com.jatmika.admin_e_complaintrangkasbitung.Model.Penduduk;
import com.jatmika.admin_e_complaintrangkasbitung.SharePref.SharePref;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentDataUser extends Fragment implements RecyclerAdapterDataUser.OnItemClickListener {

    RecyclerView mRecyclerView;
    RecyclerAdapterDataUser mAdapter;
    FirebaseAuth mAuth;
    FirebaseStorage mStorage;
    DatabaseReference mDatabaseRef;
    ValueEventListener mDBListener;
    List<DataUser> mDatas;
    TextView tvNoData;
    API apiService;
    SharePref sharePref;

    private void openDetailDataUser(String[] data){
        Intent intent = new Intent(getActivity(), DetailDataUserActivity.class);
        intent.putExtra("IMAGE_KEY",data[0]);
        intent.putExtra("NIK_KEY",data[1]);
        intent.putExtra("EMAIL_KEY",data[2]);
        intent.putExtra("PASSWORD_KEY",data[3]);
        intent.putExtra("NAMA_KEY",data[4]);
        intent.putExtra("TTL_KEY",data[5]);
        intent.putExtra("JENKEL_KEY",data[6]);
        intent.putExtra("ALAMAT_KEY",data[7]);
        intent.putExtra("NOHP_KEY",data[8]);
        intent.putExtra("GETPRIMARY_KEY",data[9]);
        startActivity(intent);
    }

    public FragmentDataUser() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_data_user, container, false);

        tvNoData = view.findViewById(R.id.tvNoData);

        mRecyclerView = view.findViewById(R.id.mRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mDatas = new ArrayList<>();
        mAdapter = new RecyclerAdapterDataUser (getActivity(), mDatas);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);

        apiService = APIUtility.getAPI();
        sharePref = new SharePref(getActivity().getApplicationContext());

        apiService.getPengguna("Bearer "+sharePref.getTokenApi()).enqueue(new Callback<List<DataUser>>() {
            @Override
            public void onResponse(Call<List<DataUser>> call, Response<List<DataUser>> response) {
                Log.i("error", response.toString());
                for (DataUser dataUser : response.body()){
                    mDatas.add(dataUser);
                }
                mAdapter.notifyDataSetChanged();
                tvNoData.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<DataUser>> call, Throwable t) {
                Log.i("errorUser", t.toString());
            }
        });


        return view;
    }

    @Override
    public void onItemClick(int position) {
//        DataUser clickedMahasiswa = mDatas.get(position);
//        String[] mahasiswaData = {clickedMahasiswa.getNik(), clickedMahasiswa.getEmail(),
//              clickedMahasiswa.getNama(), clickedMahasiswa.getNohp(), clickedMahasiswa.getKey()};
//
//        openDetailDataUser(mahasiswaData);
    }
}
