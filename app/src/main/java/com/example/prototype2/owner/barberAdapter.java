package com.example.prototype2.owner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prototype2.R;
import com.example.prototype2.barberData;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class barberAdapter extends FirebaseRecyclerAdapter<barberData, barberAdapter.holder> {

    public barberAdapter(@NonNull FirebaseRecyclerOptions<barberData> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull holder holder, int i, @NonNull barberData barberData) {
        holder.barberName.setText(barberData.getBarberName());
        holder.barberID.setText(barberData.getBarberID());
        holder.barberEmail.setText(barberData.getBarberEmail());

        holder.btnEditBarber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity=(AppCompatActivity)view.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_main,new editBarber(barberData.getBarberID())).addToBackStack(null).commit();
            }
        });
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_barber_single,parent, false);
        return new barberAdapter.holder(view);
    }

    public class holder extends RecyclerView.ViewHolder {
        TextView barberName, barberID, barberEmail;
        Button btnEditBarber, btnDeleteBarber;

        public holder(@NonNull View itemView) {
            super(itemView);

            barberName = itemView.findViewById(R.id.textViewBarberName);
            barberID = itemView.findViewById(R.id.textViewBarberID);
            barberEmail = itemView.findViewById(R.id.textViewbarberEmail);

            btnEditBarber = itemView.findViewById(R.id.btnEditBarber);
            btnDeleteBarber = itemView.findViewById(R.id.btnDeleteBarber);

        }
    }
}
