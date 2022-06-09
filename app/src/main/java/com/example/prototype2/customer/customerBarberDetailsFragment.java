package com.example.prototype2.customer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prototype2.Login;
import com.example.prototype2.R;
import com.example.prototype2.owner.barber;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link customerBarberDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class customerBarberDetailsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String barberName, barberID;
    private FirebaseFirestore fStore;
    customerBarberDetailsAdapter adapter;
    Uri imageUri;
    ImageView imageView;

    public customerBarberDetailsFragment() {
        // Required empty public constructor
    }

    public customerBarberDetailsFragment(String barberName, String barberID) {
        this.barberName = barberName;
        this.barberID = barberID;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment customerBarberDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static customerBarberDetailsFragment newInstance(String param1, String param2) {
        customerBarberDetailsFragment fragment = new customerBarberDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_customer_barber_details, container, false);
        TextView textViewBarberName = view.findViewById(R.id.viewTextBarberName);
        TextView textViewBarberEmail = view.findViewById(R.id.viewTextBarberEmail);
        TextView textViewBarberContactNumber = view.findViewById(R.id.viewTextBarberContactNumber);

        ProgressBar progressBar = view.findViewById(R.id.progressBar);

        RecyclerView commentList = view.findViewById(R.id.recyclerViewReview);

        imageView = view.findViewById(R.id.imageView);

        progressBar.setVisibility(View.VISIBLE);
        fStore = FirebaseFirestore.getInstance();
        DocumentReference documentReference = fStore.collection("barber").document(barberID);
        documentReference.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.getResult().exists()){

                            String fName = task.getResult().getString("barberName");
                            String Email = task.getResult().getString("barberEmail");
                            String ContactNumber = task.getResult().getString("barberContactNumber");

                            textViewBarberName.setText(fName);
                            textViewBarberEmail.setText(Email);
                            textViewBarberContactNumber.setText(ContactNumber);

                        }

                    }
                });

        StorageReference profileRef = FirebaseStorage.getInstance().getReference("users/"+barberID+"/profile");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                Picasso.get().load(uri).into(imageView);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG", e.toString());
//                Toast.makeText(getActivity(),"No profile picture", Toast.LENGTH_LONG).show();

            }
        });


        /**Adapter**/
        commentList.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference rate = db.collection("Rating");
        Query query = rate.whereEqualTo("barberID", barberID).orderBy("rate", Query.Direction.DESCENDING);


        FirestoreRecyclerOptions<ratingData> options = new FirestoreRecyclerOptions.Builder<ratingData>()
                .setQuery(query, ratingData.class).build();

        adapter = new customerBarberDetailsAdapter(options);
        commentList.setHasFixedSize(true);
        commentList.setAdapter(adapter);
        adapter.startListening();

        progressBar.setVisibility(View.GONE);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        Log.d("TAG", "Cached document data: ");


    }

    @Override
    public void onStop() {
        super.onStop();
//        adapter.stopListening();
        Log.d("TAG", "Error");

    }


}