package com.example.prototype2.barber;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.prototype2.barber.barberWorkingData;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prototype2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class barberServiceWorkingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // creating variables for our ArrayList and context
    private ArrayList<barberWorkingData> barberWorkingDataArrayList;
    public static ArrayList<barberWorkingData> mContentList;
    private Context mContext;
    private Activity mActivity;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    public barberServiceWorkingAdapter(Context mContext, Activity mActivity, ArrayList<barberWorkingData> mContentList){
        this.mContext = mContext;
        this.mActivity = mActivity;
        this.mContentList = mContentList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_barber_work_details, parent, false);
        return new ViewHolder(view, viewType);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView customerName, workTime, workRange, workService, textViewbarberID;
        Button workStatus;

        public ViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);
            customerName = itemView.findViewById(R.id.textViewWorkCustomerName);
            workTime = itemView.findViewById(R.id.textViewTime);
//                workRange = itemView.findViewById(R.id.textViewRangeTime);
            workService = itemView.findViewById(R.id.textViewWorkService);
            workStatus = itemView.findViewById(R.id.textViewWorkStatus);
            textViewbarberID = itemView.findViewById(R.id.textViewbarberID);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder mainHolder = (ViewHolder) holder;
        final int pos = position;
        final barberWorkingData model = mContentList.get(pos);
        mainHolder.customerName.setText(model.getCustomerName());
        mainHolder.workTime.setText(model.getBookTime());

        mainHolder.workStatus.setText(model.getStatus());
        mainHolder.workService.setText(model.getBarberServiceName());

        mainHolder.textViewbarberID.setText(model.getBookID());

        String customerName = mainHolder.customerName.getText().toString();
        Log.d("TAG", customerName);


        String bID = mainHolder.textViewbarberID.getText().toString();
        Log.d("BID", bID);

        ArrayList<String> arrayList = mContentList.get(position).getBarberServiceNameList();

        String visibleString = null;
        for(int i = 0; i <arrayList.size();i++){
            if(i == 0) {
                visibleString = "" + arrayList.get(i).toString();
            } else {
                visibleString +=  "\n" +arrayList.get(i).toString();
            }
        }
        mainHolder.workService.setText(visibleString);

        mainHolder.workStatus.setOnClickListener(new View.OnClickListener() {
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
                                    mainHolder.workStatus.setText(resultStatus[0]);

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

    @Override
    public int getItemCount() {
        return mContentList.size();
    }
}
