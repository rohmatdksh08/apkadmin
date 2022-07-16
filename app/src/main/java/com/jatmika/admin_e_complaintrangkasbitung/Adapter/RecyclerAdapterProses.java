package com.jatmika.admin_e_complaintrangkasbitung.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jatmika.admin_e_complaintrangkasbitung.Model.Proses;
import com.jatmika.admin_e_complaintrangkasbitung.R;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;


public class RecyclerAdapterProses extends RecyclerView.Adapter<RecyclerAdapterProses.RecyclerViewHolder> {
    private Context mContext;
    private List<Proses> komentars;

    public RecyclerAdapterProses(Context context, List<Proses> uploads) {
        mContext = context;
        komentars = uploads;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_proses, parent, false);
        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        Proses currentKomentar = komentars.get(position);
        holder.pesanText.setText(currentKomentar.getPesan());
        holder.olehText.setText(currentKomentar.getOleh());
        Glide.with(mContext)
                .load(currentKomentar.getPhoto())
                .into(holder.Photo);
    }

    @Override
    public int getItemCount() {
        return komentars.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        public TextView pesanText, olehText;
        public ImageView Photo;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            pesanText = itemView.findViewById(R.id.tvPesan);
            olehText = itemView.findViewById(R.id.tvOleh);
            Photo = itemView.findViewById(R.id.fotoImageView);
        }
    }
}

