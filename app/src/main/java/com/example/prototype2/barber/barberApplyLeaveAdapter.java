package com.example.prototype2.barber;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.example.prototype2.barber.leaveApplyData;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prototype2.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.example.prototype2.barber.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class barberApplyLeaveAdapter extends FirestoreRecyclerAdapter<leaveApplyData,barberApplyLeaveAdapter.holder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public barberApplyLeaveAdapter(@NonNull FirestoreRecyclerOptions<leaveApplyData> options) {
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
        holder.textViewDateRange.setText(formattedStartDate+" to "+formattedEndDate);
        String leaveStatus = model.getStatus();
        if (leaveStatus.equals("pending")){
            holder.btnApplyStatus.setText("Pending");
            holder.btnApplyStatus.setBackgroundColor(Color.parseColor("#FFA500"));
        }
        else if(leaveStatus.equals("accept")){
            holder.btnApplyStatus.setText("Accept");
            holder.btnApplyStatus.setBackgroundColor(Color.parseColor("#00FF00"));
        }
        else if(leaveStatus.equals("reject")){
            holder.btnApplyStatus.setText("Reject");
            holder.btnApplyStatus.setBackgroundColor(Color.parseColor("FF0000"));
        }
        holder.textViewBarberID.setText(model.getLeaveID());


    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_barber_apply_leave,parent,false);
        return new holder(view);
    }

    public class holder extends RecyclerView.ViewHolder {
        TextView textViewDateRange, textViewBarberID;
        Button btnApplyStatus;
        public holder(@NonNull View itemView) {
            super(itemView);
            textViewDateRange = itemView.findViewById(R.id.textViewDateRange);
            btnApplyStatus = itemView.findViewById(R.id.btnApplyStatus);
            textViewBarberID = itemView.findViewById(R.id.textViewLeaveID);

        }
    }
}
