package com.jatmika.admin_e_complaintrangkasbitung.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jatmika.admin_e_complaintrangkasbitung.Model.DataUser;
import com.jatmika.admin_e_complaintrangkasbitung.Model.Penduduk;
import com.jatmika.admin_e_complaintrangkasbitung.R;

import java.time.LocalDate;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;


public  class PendudukAdapter extends RecyclerView.Adapter<PendudukAdapter.RecyclerViewHolder>{
    private Context mContext;
    private List<Penduduk> dataPenduduks;
    private OnItemClickListener mListener;

    public PendudukAdapter(Context context, List<Penduduk> uploads) {
        mContext = context;
        dataPenduduks = uploads;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_data_user, parent, false);
        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        Penduduk currentPenduduk = dataPenduduks.get(position);
        holder.tvNama.setText(currentPenduduk.getNama_penduduk());
        LocalDate ld = LocalDate.parse(currentPenduduk.getTanggal_lahir());
        String ttl = currentPenduduk.getTempat_lahir()+", "+ld.getDayOfMonth()+" "+ld.getMonth()+" "+ld.getYear();
        holder.tvLahir.setText(ttl);
        holder.tvAlamat.setText(currentPenduduk.getAlamat());
        Glide.with(mContext)
                .load(R.mipmap.ic_launcher)
                .into(holder.fotoImageView);
    }

    @Override
    public int getItemCount() {
        return dataPenduduks.size();
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
