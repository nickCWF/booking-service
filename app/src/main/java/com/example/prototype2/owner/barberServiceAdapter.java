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
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class barberServiceAdapter extends FirestoreRecyclerAdapter<barberService, barberServiceAdapter.holder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public barberServiceAdapter(@NonNull FirestoreRecyclerOptions<barberService> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull barberServiceAdapter.holder holder, int position, @NonNull barberService model) {
        holder.barberService_name.setText(model.getBarberServiceName());
        holder.barberService_ID.setText(model.getBarberServiceID());

        holder.btnEditBarberService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity=(AppCompatActivity)view.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_main,new editBarberService(model.getBarberServiceID())).addToBackStack(null).commit();
            }
        });
    }

    @NonNull
    @Override
    public barberServiceAdapter.holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_barberservice_single,parent, false);
        return new holder(view);
    }

    public class holder extends RecyclerView.ViewHolder {
        TextView barberService_name, barberService_ID;
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