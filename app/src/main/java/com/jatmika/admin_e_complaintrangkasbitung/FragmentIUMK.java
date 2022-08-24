package com.jatmika.admin_e_complaintrangkasbitung;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.jatmika.admin_e_complaintrangkasbitung.API.API;
import com.jatmika.admin_e_complaintrangkasbitung.API.APIUtility;
import com.jatmika.admin_e_complaintrangkasbitung.Adapter.RecyclerAdapterKomplain;
import com.jatmika.admin_e_complaintrangkasbitung.Model.Komplain;
import com.jatmika.admin_e_complaintrangkasbitung.Model.PersentaseKomplain;
import com.jatmika.admin_e_complaintrangkasbitung.SharePref.SharePref;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentIUMK extends Fragment implements RecyclerAdapterKomplain.OnItemClickListener {

    private RecyclerAdapterKomplain mAdapter;
    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;
    private List<Komplain> mPengaduans;
    private TextView tvNoData;
    private Context context;
    private SharePref sharePref;
    private API apiService;

    private void openDetailKomplainIUMK(String[] data){
        Intent intent = new Intent(getActivity(), DetailIUMKActivity.class);
        intent.putExtra("IMAGE_KEY",data[0]);
        intent.putExtra("BERKAS_KEY",data[1]);
        intent.putExtra("NOMOR_KEY",data[2]);
        intent.putExtra("NIK_KEY",data[3]);
        intent.putExtra("EMAIL_KEY",data[4]);
        intent.putExtra("NAMA_KEY",data[5]);
        intent.putExtra("ALAMAT_KEY",data[6]);
        intent.putExtra("TANGGAL_KEY",data[7]);
        intent.putExtra("NAMALOKASI_KEY",data[8]);
        intent.putExtra("LATITUDE_KEY",data[9]);
        intent.putExtra("LONGITUDE_KEY",data[10]);
        intent.putExtra("ISI_KEY",data[11]);
        intent.putExtra("KATEGORI_KEY",data[12]);
        intent.putExtra("STATUS_KEY",data[13]);
        intent.putExtra("JML_LIHAT_KEY",data[14]);
        intent.putExtra("JML_SUKA_KEY",data[15]);
        intent.putExtra("JML_BALAS_KEY",data[16]);
        intent.putExtra("GETPRIMARY_KEY",data[17]);
        startActivity(intent);
    }

    private String jumlahKomplainIUMK, jumlahKomplainKependudukan, jumlahKomplainKTP,
            jumlahKomplainNikah, jumlahKomplainSPPT, jumlahKomplainTutupJalan;

    public FragmentIUMK() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_iumk, container, false);

        context = getActivity().getApplicationContext();
        sharePref = new SharePref(context);
        apiService = APIUtility.getAPI();

        tvNoData = view.findViewById(R.id.tvNoData);
        RecyclerView mRecyclerView = view.findViewById(R.id.mRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mPengaduans = new ArrayList<>();
        mAdapter = new RecyclerAdapterKomplain(getActivity(), mPengaduans);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);

        mStorage = FirebaseStorage.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("data_komplain");
        apiService.getComplain("Bearer "+sharePref.getTokenApi(), "iumk").enqueue(new Callback<List<Komplain>>() {
            @Override
            public void onResponse(Call<List<Komplain>> call, Response<List<Komplain>> response) {
                Log.i("response", response.body().toString());
                for (Komplain komplain : response.body()){
                    mPengaduans.add(komplain);
                }
                mAdapter.notifyDataSetChanged();
                tvNoData.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<Komplain>> call, Throwable t) {
                Log.i("responseError", t.toString());
            }
        });

        return view;
    }

    @Override
    public void onDeleteItemClick(int position) {
        showJumlahKomplain();

        Komplain selectedItem = mPengaduans.get(position);
        final String selectedKey = selectedItem.getKey();

        String url = selectedItem.getBerkas();

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        View mView = getLayoutInflater().inflate(R.layout.show_loading, null);

        mBuilder.setView(mView);
        mBuilder.setCancelable(false);
        final AlertDialog mDialog = mBuilder.create();
        mDialog.show();

        if (url.equals("")){
            mDatabaseRef.child(selectedKey).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    FirebaseDatabase.getInstance().getReference("data_komplain").orderByChild("kategori").equalTo("Komplain IUMK").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()){
                                long totalData = dataSnapshot.getChildrenCount();
                                FirebaseDatabase.getInstance().getReference("data_persentase_komplain").child("Komplain IUMK").child("jumlah_komplain").setValue(String.valueOf(totalData));
                                showJumlahKomplain();

                                double komplainIUMK = Double.parseDouble(String.valueOf(totalData));
                                double komplainKependudukan = Double.parseDouble(jumlahKomplainKependudukan);
                                double komplainKTP = Double.parseDouble(jumlahKomplainKTP);
                                double komplainNikah = Double.parseDouble(jumlahKomplainNikah);
                                double komplainSPPT = Double.parseDouble(jumlahKomplainSPPT);
                                double komplainTutupJalan = Double.parseDouble(jumlahKomplainTutupJalan);
                                double TotalPersen2 = (komplainIUMK / ( komplainIUMK + komplainKependudukan + komplainKTP + komplainNikah + komplainSPPT + komplainTutupJalan)) * 100;
                                double TotalPersen3 = (komplainKependudukan / ( komplainIUMK + komplainKependudukan + komplainKTP + komplainNikah + komplainSPPT + komplainTutupJalan)) * 100;
                                double TotalPersen4 = (komplainKTP / ( komplainIUMK + komplainKependudukan + komplainKTP + komplainNikah + komplainSPPT + komplainTutupJalan)) * 100;
                                double TotalPersen5 = (komplainNikah / ( komplainIUMK + komplainKependudukan + komplainKTP + komplainNikah + komplainSPPT + komplainTutupJalan)) * 100;
                                double TotalPersen6 = (komplainSPPT / ( komplainIUMK + komplainKependudukan + komplainKTP + komplainNikah + komplainSPPT + komplainTutupJalan)) * 100;
                                double TotalPersen7 = (komplainTutupJalan / ( komplainIUMK + komplainKependudukan + komplainKTP + komplainNikah + komplainSPPT + komplainTutupJalan)) * 100;

                                FirebaseDatabase.getInstance().getReference("data_persentase_komplain").child("Komplain IUMK").child("persen").setValue(String.valueOf(TotalPersen2));
                                FirebaseDatabase.getInstance().getReference("data_persentase_komplain").child("Komplain Kependudukan").child("persen").setValue(String.valueOf(TotalPersen3));
                                FirebaseDatabase.getInstance().getReference("data_persentase_komplain").child("Komplain KTP").child("persen").setValue(String.valueOf(TotalPersen4));
                                FirebaseDatabase.getInstance().getReference("data_persentase_komplain").child("Komplain Nikah").child("persen").setValue(String.valueOf(TotalPersen5));
                                FirebaseDatabase.getInstance().getReference("data_persentase_komplain").child("Komplain SPPT").child("persen").setValue(String.valueOf(TotalPersen6));
                                FirebaseDatabase.getInstance().getReference("data_persentase_komplain").child("Komplain Tutup Jalan").child("persen").setValue(String.valueOf(TotalPersen7));

                                mDialog.dismiss();
                                Toast.makeText(FragmentIUMK.this.context, "Komplain telah dihapus!", Toast.LENGTH_SHORT).show();

                            } else {
                                FirebaseDatabase.getInstance().getReference("data_persentase_komplain").child("Komplain IUMK").child("jumlah_komplain").setValue("0")
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                showJumlahKomplain();

                                                double komplainIUMK = 0;
                                                double komplainKependudukan = Double.parseDouble(jumlahKomplainKependudukan);
                                                double komplainKTP = Double.parseDouble(jumlahKomplainKTP);
                                                double komplainNikah = Double.parseDouble(jumlahKomplainNikah);
                                                double komplainSPPT = Double.parseDouble(jumlahKomplainSPPT);
                                                double komplainTutupJalan = Double.parseDouble(jumlahKomplainTutupJalan);

                                                if (komplainKependudukan == 0 && komplainKTP == 0 && komplainNikah == 0
                                                        && komplainSPPT == 0 && komplainTutupJalan == 0){
                                                    FirebaseDatabase.getInstance().getReference("data_persentase_komplain").child("Komplain IUMK").child("persen").setValue("0.0");
                                                    mDialog.dismiss();
                                                    Toast.makeText(FragmentIUMK.this.context, "Komplain telah dihapus!", Toast.LENGTH_SHORT).show();

                                                } else {
                                                    double TotalPersen1 = ( ( komplainIUMK + komplainKependudukan + komplainKTP + komplainNikah + komplainSPPT + komplainTutupJalan)) * 100;
                                                    double TotalPersen2 = (komplainIUMK / ( komplainIUMK + komplainKependudukan + komplainKTP + komplainNikah + komplainSPPT + komplainTutupJalan)) * 100;
                                                    double TotalPersen3 = (komplainKependudukan / ( komplainIUMK + komplainKependudukan + komplainKTP + komplainNikah + komplainSPPT + komplainTutupJalan)) * 100;
                                                    double TotalPersen4 = (komplainKTP / ( komplainIUMK + komplainKependudukan + komplainKTP + komplainNikah + komplainSPPT + komplainTutupJalan)) * 100;
                                                    double TotalPersen5 = (komplainNikah / ( komplainIUMK + komplainKependudukan + komplainKTP + komplainNikah + komplainSPPT + komplainTutupJalan)) * 100;
                                                    double TotalPersen6 = (komplainSPPT / ( komplainIUMK + komplainKependudukan + komplainKTP + komplainNikah + komplainSPPT + komplainTutupJalan)) * 100;
                                                    double TotalPersen7 = (komplainTutupJalan / ( komplainIUMK + komplainKependudukan + komplainKTP + komplainNikah + komplainSPPT + komplainTutupJalan)) * 100;

                                                    FirebaseDatabase.getInstance().getReference("data_persentase_komplain").child("Komplain IUMK").child("persen").setValue(String.valueOf(TotalPersen2));
                                                    FirebaseDatabase.getInstance().getReference("data_persentase_komplain").child("Komplain Kependudukan").child("persen").setValue(String.valueOf(TotalPersen3));
                                                    FirebaseDatabase.getInstance().getReference("data_persentase_komplain").child("Komplain KTP").child("persen").setValue(String.valueOf(TotalPersen4));
                                                    FirebaseDatabase.getInstance().getReference("data_persentase_komplain").child("Komplain Nikah").child("persen").setValue(String.valueOf(TotalPersen5));
                                                    FirebaseDatabase.getInstance().getReference("data_persentase_komplain").child("Komplain SPPT").child("persen").setValue(String.valueOf(TotalPersen6));
                                                    FirebaseDatabase.getInstance().getReference("data_persentase_komplain").child("Komplain Tutup Jalan").child("persen").setValue(String.valueOf(TotalPersen7));

                                                    mDialog.dismiss();
                                                    Toast.makeText(FragmentIUMK.this.context, "Komplain telah dihapus!", Toast.LENGTH_SHORT).show();
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
            });

        } else {
            StorageReference imageRef = mStorage.getReferenceFromUrl(selectedItem.getBerkas());
            imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    mDatabaseRef.child(selectedKey).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            FirebaseDatabase.getInstance().getReference("data_komplain").orderByChild("kategori").equalTo("Komplain IUMK").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()){
                                        long totalData = dataSnapshot.getChildrenCount();
                                        FirebaseDatabase.getInstance().getReference("data_persentase_komplain").child("Komplain IUMK").child("jumlah_komplain").setValue(String.valueOf(totalData));
                                        showJumlahKomplain();

                                        double komplainIUMK = Double.parseDouble(String.valueOf(totalData));
                                        double komplainKependudukan = Double.parseDouble(jumlahKomplainKependudukan);
                                        double komplainKTP = Double.parseDouble(jumlahKomplainKTP);
                                        double komplainNikah = Double.parseDouble(jumlahKomplainNikah);
                                        double komplainSPPT = Double.parseDouble(jumlahKomplainSPPT);
                                        double komplainTutupJalan = Double.parseDouble(jumlahKomplainTutupJalan);
                                        double TotalPersen1 = ( ( komplainIUMK + komplainKependudukan + komplainKTP + komplainNikah + komplainSPPT + komplainTutupJalan)) * 100;
                                        double TotalPersen2 = (komplainIUMK / ( komplainIUMK + komplainKependudukan + komplainKTP + komplainNikah + komplainSPPT + komplainTutupJalan)) * 100;
                                        double TotalPersen3 = (komplainKependudukan / ( komplainIUMK + komplainKependudukan + komplainKTP + komplainNikah + komplainSPPT + komplainTutupJalan)) * 100;
                                        double TotalPersen4 = (komplainKTP / ( komplainIUMK + komplainKependudukan + komplainKTP + komplainNikah + komplainSPPT + komplainTutupJalan)) * 100;
                                        double TotalPersen5 = (komplainNikah / ( komplainIUMK + komplainKependudukan + komplainKTP + komplainNikah + komplainSPPT + komplainTutupJalan)) * 100;
                                        double TotalPersen6 = (komplainSPPT / ( komplainIUMK + komplainKependudukan + komplainKTP + komplainNikah + komplainSPPT + komplainTutupJalan)) * 100;
                                        double TotalPersen7 = (komplainTutupJalan / ( komplainIUMK + komplainKependudukan + komplainKTP + komplainNikah + komplainSPPT + komplainTutupJalan)) * 100;

                                        FirebaseDatabase.getInstance().getReference("data_persentase_komplain").child("Komplain Infrastruktur").child("persen").setValue(String.valueOf(TotalPersen1));
                                        FirebaseDatabase.getInstance().getReference("data_persentase_komplain").child("Komplain IUMK").child("persen").setValue(String.valueOf(TotalPersen2));
                                        FirebaseDatabase.getInstance().getReference("data_persentase_komplain").child("Komplain Kependudukan").child("persen").setValue(String.valueOf(TotalPersen3));
                                        FirebaseDatabase.getInstance().getReference("data_persentase_komplain").child("Komplain KTP").child("persen").setValue(String.valueOf(TotalPersen4));
                                        FirebaseDatabase.getInstance().getReference("data_persentase_komplain").child("Komplain Nikah").child("persen").setValue(String.valueOf(TotalPersen5));
                                        FirebaseDatabase.getInstance().getReference("data_persentase_komplain").child("Komplain SPPT").child("persen").setValue(String.valueOf(TotalPersen6));
                                        FirebaseDatabase.getInstance().getReference("data_persentase_komplain").child("Komplain Tutup Jalan").child("persen").setValue(String.valueOf(TotalPersen7));

                                        mDialog.dismiss();
                                        Toast.makeText(FragmentIUMK.this.context, "Komplain telah dihapus!", Toast.LENGTH_SHORT).show();

                                    } else {
                                        FirebaseDatabase.getInstance().getReference("data_persentase_komplain").child("Komplain IUMK").child("jumlah_komplain").setValue("0")
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        showJumlahKomplain();

                                                        double komplainIUMK = 0;
                                                        double komplainKependudukan = Double.parseDouble(jumlahKomplainKependudukan);
                                                        double komplainKTP = Double.parseDouble(jumlahKomplainKTP);
                                                        double komplainNikah = Double.parseDouble(jumlahKomplainNikah);
                                                        double komplainSPPT = Double.parseDouble(jumlahKomplainSPPT);
                                                        double komplainTutupJalan = Double.parseDouble(jumlahKomplainTutupJalan);

                                                        if (komplainKependudukan == 0 && komplainKTP == 0 && komplainNikah == 0
                                                                && komplainSPPT == 0 && komplainTutupJalan == 0){
                                                            FirebaseDatabase.getInstance().getReference("data_persentase_komplain").child("Komplain IUMK").child("persen").setValue("0.0");
                                                            mDialog.dismiss();
                                                            Toast.makeText(FragmentIUMK.this.context, "Komplain telah dihapus!", Toast.LENGTH_SHORT).show();

                                                        } else {
                                                            double TotalPersen2 = (komplainIUMK / ( komplainIUMK + komplainKependudukan + komplainKTP + komplainNikah + komplainSPPT + komplainTutupJalan)) * 100;
                                                            double TotalPersen3 = (komplainKependudukan / ( komplainIUMK + komplainKependudukan + komplainKTP + komplainNikah + komplainSPPT + komplainTutupJalan)) * 100;
                                                            double TotalPersen4 = (komplainKTP / ( komplainIUMK + komplainKependudukan + komplainKTP + komplainNikah + komplainSPPT + komplainTutupJalan)) * 100;
                                                            double TotalPersen5 = (komplainNikah / ( komplainIUMK + komplainKependudukan + komplainKTP + komplainNikah + komplainSPPT + komplainTutupJalan)) * 100;
                                                            double TotalPersen6 = (komplainSPPT / ( komplainIUMK + komplainKependudukan + komplainKTP + komplainNikah + komplainSPPT + komplainTutupJalan)) * 100;
                                                            double TotalPersen7 = (komplainTutupJalan / ( komplainIUMK + komplainKependudukan + komplainKTP + komplainNikah + komplainSPPT + komplainTutupJalan)) * 100;

                                                            FirebaseDatabase.getInstance().getReference("data_persentase_komplain").child("Komplain IUMK").child("persen").setValue(String.valueOf(TotalPersen2));
                                                            FirebaseDatabase.getInstance().getReference("data_persentase_komplain").child("Komplain Kependudukan").child("persen").setValue(String.valueOf(TotalPersen3));
                                                            FirebaseDatabase.getInstance().getReference("data_persentase_komplain").child("Komplain KTP").child("persen").setValue(String.valueOf(TotalPersen4));
                                                            FirebaseDatabase.getInstance().getReference("data_persentase_komplain").child("Komplain Nikah").child("persen").setValue(String.valueOf(TotalPersen5));
                                                            FirebaseDatabase.getInstance().getReference("data_persentase_komplain").child("Komplain SPPT").child("persen").setValue(String.valueOf(TotalPersen6));
                                                            FirebaseDatabase.getInstance().getReference("data_persentase_komplain").child("Komplain Tutup Jalan").child("persen").setValue(String.valueOf(TotalPersen7));

                                                            mDialog.dismiss();
                                                            Toast.makeText(FragmentIUMK.this.context, "Komplain telah dihapus!", Toast.LENGTH_SHORT).show();
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
                    });
                }
            });
        }
    }

    @Override
    public void onItemClick(int position) {
        Komplain clickedKomplain = mPengaduans.get(position);
        String[] komplainData = {clickedKomplain.getFoto(), clickedKomplain.getBerkas(), clickedKomplain.getNomor(),
                clickedKomplain.getNik(), clickedKomplain.getEmail(), clickedKomplain.getNama(), clickedKomplain.getAlamat(),
                clickedKomplain.getTanggal(), clickedKomplain.getNamalokasi(), String.valueOf(clickedKomplain.getLatitude()),
                String.valueOf(clickedKomplain.getLongitude()), clickedKomplain.getIsi(), clickedKomplain.getKategori(),
                clickedKomplain.getStatus(), clickedKomplain.getJml_lihat(), clickedKomplain.getJml_suka(),
                clickedKomplain.getJml_balas(), clickedKomplain.getKey()};

        openDetailKomplainIUMK(komplainData);
    }

    private void showJumlahKomplain(){
        FirebaseDatabase.getInstance().getReference("data_persentase_komplain").orderByChild("kategori").equalTo("Komplain IUMK")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            for (DataSnapshot dataPersentase : dataSnapshot.getChildren()) {
                                PersentaseKomplain data = dataPersentase.getValue(PersentaseKomplain.class);
                                jumlahKomplainIUMK = (String) dataPersentase.child("jumlah_komplain").getValue();
                            }
                        } else {
                            jumlahKomplainIUMK = "0";
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        FirebaseDatabase.getInstance().getReference("data_persentase_komplain").orderByChild("kategori").equalTo("Komplain Kependudukan")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            for (DataSnapshot dataPersentase : dataSnapshot.getChildren()) {
                                PersentaseKomplain data = dataPersentase.getValue(PersentaseKomplain.class);
                                jumlahKomplainKependudukan = (String) dataPersentase.child("jumlah_komplain").getValue();
                            }
                        } else {
                            jumlahKomplainKependudukan = "0";
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        FirebaseDatabase.getInstance().getReference("data_persentase_komplain").orderByChild("kategori").equalTo("Komplain KTP")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            for (DataSnapshot dataPersentase : dataSnapshot.getChildren()) {
                                PersentaseKomplain data = dataPersentase.getValue(PersentaseKomplain.class);
                                jumlahKomplainKTP = (String) dataPersentase.child("jumlah_komplain").getValue();
                            }
                        } else {
                            jumlahKomplainKTP = "0";
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        FirebaseDatabase.getInstance().getReference("data_persentase_komplain").orderByChild("kategori").equalTo("Komplain Nikah")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            for (DataSnapshot dataPersentase : dataSnapshot.getChildren()) {
                                PersentaseKomplain data = dataPersentase.getValue(PersentaseKomplain.class);
                                jumlahKomplainNikah = (String) dataPersentase.child("jumlah_komplain").getValue();
                            }
                        } else {
                            jumlahKomplainNikah = "0";
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        FirebaseDatabase.getInstance().getReference("data_persentase_komplain").orderByChild("kategori").equalTo("Komplain SPPT")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            for (DataSnapshot dataPersentase : dataSnapshot.getChildren()) {
                                PersentaseKomplain data = dataPersentase.getValue(PersentaseKomplain.class);
                                jumlahKomplainSPPT = (String) dataPersentase.child("jumlah_komplain").getValue();
                            }
                        } else {
                            jumlahKomplainSPPT = "0";
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        FirebaseDatabase.getInstance().getReference("data_persentase_komplain").orderByChild("kategori").equalTo("Komplain Tutup Jalan")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            for (DataSnapshot dataPersentase : dataSnapshot.getChildren()) {
                                PersentaseKomplain data = dataPersentase.getValue(PersentaseKomplain.class);
                                jumlahKomplainTutupJalan = (String) dataPersentase.child("jumlah_komplain").getValue();
                            }
                        } else {
                            jumlahKomplainTutupJalan = "0";
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}
