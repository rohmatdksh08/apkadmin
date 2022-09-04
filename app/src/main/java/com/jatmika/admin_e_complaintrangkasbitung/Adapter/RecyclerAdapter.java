package com.jatmika.admin_e_complaintrangkasbitung.Adapter;

import android.content.Context;
import android.util.Log;
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
import com.jatmika.admin_e_complaintrangkasbitung.Model.DataBerita;
import com.jatmika.admin_e_complaintrangkasbitung.R;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;


public  class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder>{
    private Context mContext;
    private List<DataBerita> beritas;
    private OnItemClickListener mListener;

    public RecyclerAdapter(Context context, List<DataBerita> uploads) {
        mContext = context;
        beritas = uploads;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_berita, parent, false);
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.frombottom);
        v.startAnimation(animation);
        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        DataBerita currentBerita = beritas.get(position);
        holder.judulTextView.setText(currentBerita.getjudul());
        holder.tanggalTextView.setText(currentBerita.getcreated_at());
        holder.penulisTextView.setText(currentBerita.getnama());
        holder.isiTextView.setText(currentBerita.getisi());
        String urlImage = "https://api-rohmat.kosanbahari.xyz/uploads/"+currentBerita.getFoto();
        Glide.with(mContext)
                .load(urlImage)
                .into(holder.fotoImageView);
    }

    @Override
    public int getItemCount() {
        return beritas.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener,
            MenuItem.OnMenuItemClickListener, View.OnClickListener{

        private TextView judulTextView, tanggalTextView, penulisTextView, isiTextView;
        private ImageView fotoImageView;

        private RecyclerViewHolder(View itemView) {
            super(itemView);
            judulTextView = itemView.findViewById ( R.id.judulTextView );
            tanggalTextView = itemView.findViewById(R.id.tanggalTextView);
            penulisTextView = itemView.findViewById(R.id.penulisTextView);
            isiTextView = itemView.findViewById(R.id.isiTextView);
            fotoImageView = itemView.findViewById(R.id.fotoImageView);

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
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onDeleteItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
}
