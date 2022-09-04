package com.jatmika.admin_e_complaintrangkasbitung.Adapter;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jatmika.admin_e_complaintrangkasbitung.Model.Komplain;
import com.jatmika.admin_e_complaintrangkasbitung.R;
import com.uncopt.android.widget.text.justify.JustifiedTextView;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;


public class RecyclerAdapterKomplain extends RecyclerView.Adapter<RecyclerAdapterKomplain.RecyclerViewHolder> {
    private Context mContext;
    private List<Komplain> komplains;
    private OnItemClickListener mListener;

    public RecyclerAdapterKomplain(Context context, List<Komplain> uploads) {
        mContext = context;
        komplains = uploads;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_komplain, parent, false);
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.frombottom);
        v.startAnimation(animation);
        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        Komplain currentKomplain = komplains.get(position);
        holder.namaTextView.setText(currentKomplain.getNama());
        holder.isiTextView.setText("''" +currentKomplain.getIsi()+ "''");
        String status = holder.setText(currentKomplain.getStatus());
        String urlImage = "https://api-rohmat.kosanbahari.xyz/uploads/"+currentKomplain.getFoto();
        if (status.equals("Menunggu Diproses")){
            holder.imgStatus.setImageResource(R.drawable.status_menunggu);
            holder.statusTextView.setTextColor(mContext.getResources().getColor(R.color.greenColor));
            holder.statusTextView.setText(currentKomplain.getStatus());
        } else if (status.equals("Dalam Proses")){
            holder.imgStatus.setImageResource(R.drawable.status_proses);
            holder.statusTextView.setTextColor(mContext.getResources().getColor(R.color.yellowColor));
            holder.statusTextView.setText(currentKomplain.getStatus());
        } else {
            holder.imgStatus.setImageResource(R.drawable.status_selesai);
            holder.statusTextView.setTextColor(mContext.getResources().getColor(R.color.redColor));
            holder.statusTextView.setText(currentKomplain.getStatus());
        }
        holder.tanggalTextView.setText(currentKomplain.getTanggal());
        holder.lihatTextView.setText(currentKomplain.getJml_lihat());
        holder.sukaTextView.setText(currentKomplain.getJml_suka());
        String balasan = holder.setText(currentKomplain.getJml_balas());
        if (balasan.equals("0")){
            holder.balasTextView.setText("Belum ada balasan");
        } else {
            holder.balasTextView.setText(currentKomplain.getJml_balas()+" Balasan");
        }
        String foto = holder.setText(currentKomplain.getFoto());
        if (foto.equals("")){
            holder.fotoImageView.setImageResource(R.drawable.placeholder3);
        } else {
            Glide.with(mContext)
                    .load(urlImage)
                    .into(holder.fotoImageView);
        }
    }

    @Override
    public int getItemCount() {
        return komplains.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener,
            MenuItem.OnMenuItemClickListener, View.OnClickListener{

        public TextView namaTextView, statusTextView, tanggalTextView, lihatTextView, sukaTextView, balasTextView;
        public JustifiedTextView isiTextView;
        public ImageView fotoImageView, imgStatus;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            namaTextView = itemView.findViewById(R.id.tvNama);
            isiTextView = itemView.findViewById(R.id.tvIsi);
            statusTextView = itemView.findViewById(R.id.tvStatus);
            tanggalTextView = itemView.findViewById(R.id.tvTanggal);
            lihatTextView = itemView.findViewById(R.id.tvLihat);
            sukaTextView = itemView.findViewById(R.id.tvSuka);
            balasTextView = itemView.findViewById(R.id.tvBalas);
            fotoImageView = itemView.findViewById(R.id.fotoImageView);
            imgStatus = itemView.findViewById(R.id.imgStatus);

            itemView.setOnCreateContextMenuListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

            MenuItem deleteItem = menu.add(Menu.NONE, 1, 1, "Hapus");
            deleteItem.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {

                    switch (item.getItemId()) {
                        case 1:
                            mListener.onDeleteItemClick(position);
                            return true;
                    }
                }
            }
            return false;
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

        public String setText(String jml_balas) {
            return jml_balas;
        }
    }

    public interface OnItemClickListener {
        void onDeleteItemClick(int position);
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
}

