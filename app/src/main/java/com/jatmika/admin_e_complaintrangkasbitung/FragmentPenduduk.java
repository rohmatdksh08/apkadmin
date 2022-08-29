package com.jatmika.admin_e_complaintrangkasbitung;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jatmika.admin_e_complaintrangkasbitung.API.API;
import com.jatmika.admin_e_complaintrangkasbitung.API.APIUtility;
import com.jatmika.admin_e_complaintrangkasbitung.Adapter.PendudukAdapter;
import com.jatmika.admin_e_complaintrangkasbitung.Adapter.RecyclerAdapterKomplain;
import com.jatmika.admin_e_complaintrangkasbitung.Model.DataUser;
import com.jatmika.admin_e_complaintrangkasbitung.Model.Komplain;
import com.jatmika.admin_e_complaintrangkasbitung.Model.Penduduk;
import com.jatmika.admin_e_complaintrangkasbitung.SharePref.SharePref;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentPenduduk extends Fragment implements PendudukAdapter.OnItemClickListener {
    private PendudukAdapter mAdapter;
    private List<Penduduk> penduduks;
    private TextView tvNoData;
    private Context context;
    private SharePref sharePref;
    private API apiService;
    FloatingActionButton fab;

    public FragmentPenduduk(){

    }

    private void openDetailDataUser(String[] data){
        Intent intent = new Intent(getActivity(), DetailPendudukActivity.class);
        intent.putExtra("IMAGE_KEY",data[0]);
        intent.putExtra("NIK_KEY",data[1]);
        intent.putExtra("NAMA_KEY",data[2]);
        intent.putExtra("TTL_KEY",data[3]);
        intent.putExtra("JENKEL_KEY",data[4]);
        intent.putExtra("ALAMAT_KEY",data[5]);
        intent.putExtra("GETPRIMARY_KEY",data[6]);
        startActivity(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.data_penduduk, container, false);
        context = getActivity().getApplicationContext();
        apiService = APIUtility.getAPI();
        sharePref = new SharePref(context);
        fab = view.findViewById(R.id.fab_data_penduduk);
        Log.i("aaaaa", "success");

        tvNoData = view.findViewById(R.id.tvNoData);
        RecyclerView mRecyclerView = view.findViewById(R.id.mRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        penduduks = new ArrayList<>();
        mAdapter = new PendudukAdapter(getActivity(), penduduks);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        apiService.getPenduduk("Bearer "+sharePref.getTokenApi()).enqueue(new Callback<List<Penduduk>>() {
            @Override
            public void onResponse(Call<List<Penduduk>> call, Response<List<Penduduk>> response) {
                penduduks.clear();
                for(Penduduk penduduk: response.body()){
                    Log.i("dataPenduduk", penduduk.getNama_penduduk());
                    penduduks.add(penduduk);
                }
                mAdapter.notifyDataSetChanged();
                tvNoData.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<Penduduk>> call, Throwable t) {

            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), InputPendudukActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
   
    @Override
    public void onItemClick(int position) {
        Penduduk clickPenduduk = penduduks.get(position);
        LocalDate ld = LocalDate.parse(clickPenduduk.getTanggal_lahir());
        String ttl = clickPenduduk.getTempat_lahir()+", "+ld.getDayOfMonth()+" "+ld.getMonth()+" "+ld.getYear();
        String[] mahasiswaData = {"", clickPenduduk.getNik(), clickPenduduk.getNama_penduduk(), ttl, clickPenduduk.getJenis_kelamin(), clickPenduduk.getAlamat(), clickPenduduk.getId_penduduk()};

        openDetailDataUser(mahasiswaData);
    }
}
