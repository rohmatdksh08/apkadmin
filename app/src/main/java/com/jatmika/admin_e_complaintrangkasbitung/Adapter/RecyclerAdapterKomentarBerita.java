package com.jatmika.admin_e_complaintrangkasbitung.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jatmika.admin_e_complaintrangkasbitung.Model.DataUser;
import com.jatmika.admin_e_complaintrangkasbitung.Model.Komentar;
import com.jatmika.admin_e_complaintrangkasbitung.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;


public class RecyclerAdapterKomentarBerita extends RecyclerView.Adapter<RecyclerAdapterKomentarBerita.RecyclerViewHolder> {
    private Context mContext;
    private List<Komentar> komentars;

    public RecyclerAdapterKomentarBerita(Context context, List<Komentar> uploads) {
        mContext = context;
        komentars = uploads;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_komentar_berita, parent, false);
        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
        Komentar currentKomentar = komentars.get(position);
        holder.komentarText.setText(currentKomentar.getKomentarText());
        holder.komentarUser.setText(currentKomentar.getKomentarUser());
    }

    @Override
    public int getItemCount() {
        return komentars.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView komentarText, komentarUser;
        CircleImageView komentarPhoto;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            komentarText = itemView.findViewById(R.id.komentar_text);
            komentarUser = itemView.findViewById(R.id.komentar_user);
        }

    }
}

