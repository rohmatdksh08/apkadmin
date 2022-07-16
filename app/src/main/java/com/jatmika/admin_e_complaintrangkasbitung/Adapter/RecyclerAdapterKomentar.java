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


public class RecyclerAdapterKomentar extends RecyclerView.Adapter<RecyclerAdapterKomentar.RecyclerViewHolder> {
    private Context mContext;
    private List<Komentar> komentars;
    private String foto, email;

    public RecyclerAdapterKomentar(Context context, List<Komentar> uploads) {
        mContext = context;
        komentars = uploads;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_komentar, parent, false);
        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
        Komentar currentKomentar = komentars.get(position);
        holder.komentarText.setText(currentKomentar.getKomentarText());
        holder.komentarUser.setText(currentKomentar.getKomentarUser());
        email = currentKomentar.getKomentarEmail();
        FirebaseDatabase.getInstance().getReference("data_user").orderByChild("email")
                .equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            for (DataSnapshot dataUser : dataSnapshot.getChildren()) {
                                DataUser data = dataUser.getValue(DataUser.class);
                                foto = (String) dataUser.child("photo").getValue();
                                Glide.with(mContext).load(foto).into(holder.komentarPhoto);
                            }

                        } else {
                            holder.komentarPhoto.setImageResource(R.drawable.anonym);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
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
            komentarPhoto = itemView.findViewById(R.id.komentar_photo);
        }

    }
}

