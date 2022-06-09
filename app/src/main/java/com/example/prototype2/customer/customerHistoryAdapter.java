package com.example.prototype2.customer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prototype2.R;
import com.example.prototype2.owner.editBarberService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class customerHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context bookContext;
    private Activity bookActivity;
    public static ArrayList<Booking> bookingList;
    ArrayList<Booking> Booking = new ArrayList<>();

    String totalBaberServiceName = null;

    FirebaseFirestore firestore;

    public customerHistoryAdapter(Context bookContext, Activity bookActivity, ArrayList<Booking> bookList){
        this.bookContext = bookContext;
        this.bookActivity = bookActivity;
        bookingList = bookList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_customer_history, parent, false);
        return new ViewHolder(view, viewType);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView barberService_name, barber_name, bookTime, bookDate, bookID, status;
        Button btnViewBookingDetails, btnCancelBooking;

        public ViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);
            barberService_name = itemView.findViewById(R.id.textViewBarberService);
            barber_name = itemView.findViewById(R.id.textViewBarberName);
            bookTime = itemView.findViewById(R.id.textViewTime);
            bookDate = itemView.findViewById(R.id.textViewDate);
            btnCancelBooking = itemView.findViewById(R.id.btnCancelBooking);
            btnViewBookingDetails = itemView.findViewById(R.id.btnViewBookingHistory);
            bookID = itemView.findViewById(R.id.textViewBookID);
            status = itemView.findViewById(R.id.textViewStatus);


        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            ViewHolder viewHolder = (ViewHolder) holder;
            final int pos = position;
            final Booking model = bookingList.get(position);
            viewHolder.barberService_name.setText(model.getBarberServiceName());
            viewHolder.barber_name.setText("Barber Name: "+model.getBarberName());
            viewHolder.bookDate.setText("Date: "+model.getBookDate());
            viewHolder.bookTime.setText("Time: "+model.getBookTime());
            viewHolder.bookID.setText(model.getBookID());
            viewHolder.status.setText(model.getStatus());
            String latestStatus = viewHolder.status.getText().toString();

            Log.d("TAG", latestStatus);

            if (latestStatus.equals("cancel")){
                viewHolder.btnCancelBooking.setVisibility(View.GONE);
            }
            else {
                viewHolder.btnCancelBooking.setVisibility(View.VISIBLE);
            }



//            String visibleString = null;
//            for(int i = 0; i <arrayList.size();i++){
//                if(i == 0) {
//                    visibleString = "" + arrayList.get(i).toString();
//                } else {
//                    visibleString +=  ", " +arrayList.get(i).toString();
//                }
//            }
//        viewHolder.barberService_name.setText(visibleString);

            viewHolder.btnViewBookingDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppCompatActivity activity=(AppCompatActivity)view.getContext();
                    activity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.customer_container,new customerHistoryDetailsFragment(model.getBookID()))
                            .addToBackStack(null).commit();
                }
            });


            viewHolder.btnCancelBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cancelBookID = viewHolder.bookID.getText().toString().trim();
                AlertDialog.Builder builder
                        = new AlertDialog
                        .Builder(view.getContext());

                builder.setMessage("Do you want to cancel booking?");

                // Set Alert Title
                builder.setTitle("Cancel Book !");

                // Set Cancelable false
                // for when the user clicks on the outside
                // the Dialog Box then it will remain show
                builder.setCancelable(false);

                // Set the positive button with yes name
                // OnClickListener method is use of
                // DialogInterface interface.

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                            DatabaseReference databaseReference;
                            databaseReference = FirebaseDatabase.getInstance().getReference().child("Booking");
                            databaseReference.child(cancelBookID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                   if (task.isSuccessful()){
                                       Toast.makeText(view.getContext(), "Successfully canceled", Toast.LENGTH_SHORT).show();
                                   }
                                   else {
                                       Toast.makeText(view.getContext(), "Something Error!! Try Again Later!", Toast.LENGTH_SHORT).show();
                                   }
                               }
                            });
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    static void showAlertDialog(String bookID, View view){
        AlertDialog.Builder builder
                = new AlertDialog
                .Builder(view.getContext());

        builder.setMessage("Do you want to cancel booking?");

        // Set Alert Title
        builder.setTitle("Cancel Book !");

        // Set Cancelable false
        // for when the user clicks on the outside
        // the Dialog Box then it will remain show
        builder.setCancelable(false);

        // Set the positive button with yes name
        // OnClickListener method is use of
        // DialogInterface interface.

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
