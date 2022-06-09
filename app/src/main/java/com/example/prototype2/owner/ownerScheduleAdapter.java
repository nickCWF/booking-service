package com.example.prototype2.owner;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prototype2.R;
import com.example.prototype2.barber.barber;
import com.example.prototype2.barber.barberWorkingData;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;

public class ownerScheduleAdapter extends FirestoreRecyclerAdapter<barberWorkingData, ownerScheduleAdapter.holder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ownerScheduleAdapter(@NonNull FirestoreRecyclerOptions<barberWorkingData> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull holder holder, int position, @NonNull barberWorkingData model) {
        holder.editTextbookID.setText(model.getBookID());
        holder.customerName.setText(model.getCustomerName());
        holder.workTime.setText(model.getBookTime());
        holder.barberName.setText(model.getBarberName());
        holder.btnUpdateStatus.setText(model.getStatus());
        holder.workService.setText(model.getBarberServiceName());
        String bID = model.getBookID();


        ArrayList<String> arrayList = model.getBarberServiceNameList();

        String visibleString = null;
        for(int i = 0; i <arrayList.size();i++){
            if(i == 0) {
                visibleString = "" + arrayList.get(i);
            } else {
                visibleString +=  "\n" +arrayList.get(i);
            }
        }

        holder.workService.setText(visibleString);

        holder.btnUpdateStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore fStore = FirebaseFirestore.getInstance();
                String[] status = {"pending", "cancel", "ongoing", "waiting", "finish"};
                final String[] resultStatus = new String[1];
                final int pos;
                Context context = view.getContext();
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Update Status");
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setPaddingRelative(10, 10, 10, 10);
                Spinner spinner = new Spinner(context);

                spinner.setPaddingRelative(50, 20, 50, 10);
                ArrayAdapter aa = new ArrayAdapter(context, android.R.layout.simple_spinner_item, status);
                aa.setDropDownViewResource(android.R.layout.simple_spinner_item);
                spinner.setAdapter(aa);
                layout.addView(spinner);
                alert.setView(layout);

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                        resultStatus[0] = status[pos];

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                alert.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        DocumentReference salaryRef = fStore.collection("Booking").document(bID);
                        salaryRef.update("status", resultStatus[0]).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(context, "Success Update Status", Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(context, "Something Error try again later", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                        dialogInterface.cancel();
                    }
                });

                alert.show();

            }
        });
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_owner_schedule, parent, false);
        return new ownerScheduleAdapter.holder(view);
    }

    public class holder extends RecyclerView.ViewHolder {
        TextView customerName, workTime, workRange, workService, editTextbookID, barberName;
        Button btnUpdateStatus;
        public holder(@NonNull View itemView) {
            super(itemView);
            barberName = itemView.findViewById(R.id.textViewScheduleBarberName);
            customerName = itemView.findViewById(R.id.textViewWorkCustomerName);
            workTime = itemView.findViewById(R.id.textViewTime);
            editTextbookID = itemView.findViewById(R.id.textViewBookID);
            workService = itemView.findViewById(R.id.textViewWorkService);
            btnUpdateStatus = itemView.findViewById(R.id.btnWorkStatus);
        }
    }
}