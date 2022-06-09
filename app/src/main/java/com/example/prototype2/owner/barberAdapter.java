package com.example.prototype2.owner;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prototype2.R;
import com.example.prototype2.barberData;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class barberAdapter extends FirestoreRecyclerAdapter<barber, barberAdapter.holder> {

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public barberAdapter(@NonNull FirestoreRecyclerOptions<barber> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull barberAdapter.holder holder, int position, @NonNull barber model) {
        holder.barberName.setText(model.getBarberName());
        holder.barberID.setText(model.getBarberID());
        holder.barberEmail.setText(model.getBarberEmail());
        holder.btnEditBarber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity=(AppCompatActivity)view.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_main,new editBarber(model.getBarberID())).addToBackStack(null).commit();
            }
        });
        holder.btnDeleteBarber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence options[]=new CharSequence[]{
                        // select any from the value
                        "Delete",
                        "Cancel",
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                builder.setTitle("Delete Barber");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i ==0){
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            CollectionReference barber = db.collection("barber");
                            barber.document(model.getBarberID()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        FirebaseFirestore user = FirebaseFirestore.getInstance();
                                        CollectionReference users = user.collection("Users");
                                        users.document(model.getBarberUID()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                               if(task.isSuccessful()){

                                                   Toast.makeText(view.getContext(), "Delete Success", Toast.LENGTH_SHORT).show();
                                               }
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    }
                });
                builder.show();
            }
        });
    }

    @NonNull
    @Override
    public barberAdapter.holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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

    private void delete(String barberID){

    }
}
