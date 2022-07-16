package com.jatmika.admin_e_complaintrangkasbitung.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jatmika.admin_e_complaintrangkasbitung.Model.DataUser;
import com.jatmika.admin_e_complaintrangkasbitung.R;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;


public  class RecyclerAdapterDataUser extends RecyclerView.Adapter<RecyclerAdapterDataUser.RecyclerViewHolder>{
    private Context mContext;
    private List<DataUser> dataMahasiswas;
    private OnItemClickListener mListener;

    public RecyclerAdapterDataUser(Context context, List<DataUser> uploads) {
        mContext = context;
        dataMahasiswas = uploads;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_data_user, parent, false);
        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        DataUser currentMahasiswa = dataMahasiswas.get(position);
        holder.tvNama.setText(currentMahasiswa.getNama());
        holder.tvLahir.setText(currentMahasiswa.getTtl());
        holder.tvAlamat.setText(currentMahasiswa.getAlamat());
        Glide.with(mContext)
                .load(currentMahasiswa.getPhoto())
                .into(holder.fotoImageView);
    }

    @Override
    public int getItemCount() {
        return dataMahasiswas.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView tvNama, tvLahir, tvAlamat;
        public ImageView fotoImageView;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.namaTextView);
            tvLahir = itemView.findViewById(R.id.lahirTextView);
            tvAlamat = itemView.findViewById(R.id.alamatTextView);
            fotoImageView = itemView.findViewById(R.id.fotoImageView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mListener.onItemClick(position);
                }
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
}
