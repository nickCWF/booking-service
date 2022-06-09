package com.example.prototype2.customer;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prototype2.R;
import com.example.prototype2.owner.barber;
import com.example.prototype2.barberData;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class customerBarberAdapter extends FirestoreRecyclerAdapter<barber,customerBarberAdapter.holder> {


    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public customerBarberAdapter(@NonNull FirestoreRecyclerOptions<barber> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull customerBarberAdapter.holder holder, int position, @NonNull barber model) {
        holder.barber_name.setText(model.getBarberName());
        holder.barber_email.setText(model.getBarberEmail());
        String barberID = model.getBarberUID();

        StorageReference profileRef = FirebaseStorage.getInstance().getReference("users/"+barberID+"/profile");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(holder.imageBarber);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG", "Error");
            }
        });

        holder.booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String bookDate = bookingBarberFragment.getDate();
                String bookHour = bookingBarberFragment.getHour();
                String bookMin = bookingBarberFragment.getMin();


                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                customerBookingDetailsFragment fragment = new customerBookingDetailsFragment(bookDate, bookHour, bookMin, model.getBarberName(), model.getBarberUID());

                activity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.customer_container, fragment)
                        .addToBackStack(null).commit();

            }
        });

        holder.barberDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                customerBarberDetailsFragment fragment = new customerBarberDetailsFragment(model.getBarberName(), model.getBarberUID());

                activity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.customer_container, fragment)
                        .addToBackStack(null).commit();
            }
        });

            }

    @NonNull
    @Override
    public customerBarberAdapter.holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_customer_barber,parent,false);
        return new holder(view);
    }

    public class holder extends RecyclerView.ViewHolder {
        TextView barber_name, barber_email, date, hour, min;
        Button booking, barberDetails, barberReview;
        ShapeableImageView imageBarber;
        Uri imageUri;

        public holder(@NonNull View itemView) {
            super(itemView);

            barber_name = itemView.findViewById(R.id.customerBarberName);
            barber_email = itemView.findViewById(R.id.customerBarberEmail);
            booking = itemView.findViewById(R.id.buttonBook);
            barberDetails = itemView.findViewById(R.id.buttonDetails);
            imageBarber = itemView.findViewById(R.id.barberImage);


        }
    }
}
