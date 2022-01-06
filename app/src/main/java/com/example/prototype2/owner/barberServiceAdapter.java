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
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class barberServiceAdapter extends FirebaseRecyclerAdapter<barberService,barberServiceAdapter.holder> {

    public barberServiceAdapter(@NonNull FirebaseRecyclerOptions<barberService> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull holder holder, int i, @NonNull barberService barberService) {
        holder.barberService_name.setText(barberService.getBarberServiceName());
        holder.barberService_ID.setText(barberService.getBarberServiceID());

        holder.btnEditBarberService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity=(AppCompatActivity)view.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_main,new editBarberService(barberService.getBarberServiceID())).addToBackStack(null).commit();
            }
        });
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_barberservice_single,parent, false);
        return new holder(view);
    }

    public class holder extends RecyclerView.ViewHolder{
        TextView barberService_name, barberService_price, barberService_ID;
        Button btnEditBarberService, btnDeleteBarberService;
        public holder(@NonNull View itemView) {
            super(itemView);

            barberService_name = itemView.findViewById(R.id.barberService_name);
            barberService_ID = itemView.findViewById(R.id.barberService_ID);
            btnEditBarberService = itemView.findViewById(R.id.btnEditBarberService);
            btnDeleteBarberService = itemView.findViewById(R.id.btnDeleteBarberService);
        }
    }
}
