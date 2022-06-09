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
import com.example.prototype2.barber.leaveApplyData;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ownerLeaveApplyAdapter extends FirestoreRecyclerAdapter<leaveApplyData,ownerLeaveApplyAdapter.holder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ownerLeaveApplyAdapter(@NonNull FirestoreRecyclerOptions<leaveApplyData> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull holder holder, int position, @NonNull leaveApplyData model) {
        Long dateStart = model.getDateStart();
        Long dateEnd = model.getDateEnd();
        Date dStart = new Date(dateStart);
        Date dEnd = new Date(dateEnd);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String formattedStartDate = format.format(dStart);
        String formattedEndDate  = format.format(dEnd);
        holder.textViewBarberID.setText(model.getBarberID());
        holder.textViewBarberName.setText(model.getBarberName());
        holder.textViewStatus.setText(model.getStatus());
        holder.textViewDateRange.setText(formattedStartDate+" - " +formattedEndDate);

        holder.btnApplyStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity=(AppCompatActivity)view.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_main,new editLeaveFragment(model.getLeaveID(), model.getBarberID())).addToBackStack(null).commit();
            }
        });

    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_owner_apply_list,parent,false);
        return new holder(view);
    }

    public class holder extends RecyclerView.ViewHolder {
        TextView textViewDateRange, textViewBarberID, textViewBarberName, textViewStatus;
        Button btnApplyStatus;
        public holder(@NonNull View itemView) {
            super(itemView);
            textViewStatus = itemView.findViewById(R.id.textViewApplyStatus);
            textViewDateRange = itemView.findViewById(R.id.textViewDateRange);
            btnApplyStatus = itemView.findViewById(R.id.btnApplyStatus);
            textViewBarberID = itemView.findViewById(R.id.textViewLeaveID);
            textViewBarberName = itemView.findViewById(R.id.textViewBarberName);
        }
    }
}
