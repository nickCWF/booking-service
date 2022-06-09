package com.example.prototype2.customer;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.prototype2.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class historyFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // TODO:
    DatabaseReference databaseReference;
    FirebaseFirestore fStore;
    RecyclerView recyclerView;
    customerHistoryAdapter historyAdapter;
    private FirebaseUser user;
    private ArrayList<Booking> bookingArrayList;
    private Context bookContext;
    private Activity bookActivity;
    FirebaseAuth auth;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference book = db.collection("Booking");

//    private customerHistoryAdapter adapter = null;

    public historyFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static historyFragment newInstance(String param1, String param2) {
        historyFragment fragment = new historyFragment();
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
        View view = inflater.inflate(R.layout.fragment_customer_history, container, false);

        //Link recycle view to fragment
        recyclerView = view.findViewById(R.id.customerBookingHistory_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        bookActivity = getActivity();
        bookContext = getContext();
        FirebaseApp.initializeApp(getContext());
        recyclerView = view.findViewById(R.id.customerBookingHistory_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setNestedScrollingEnabled(false);
        bookingArrayList = new ArrayList<>();


        historyAdapter = new customerHistoryAdapter(bookContext, bookActivity, (ArrayList<Booking>) bookingArrayList);
        recyclerView.setAdapter(historyAdapter);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String customerid = user.getUid();


        db.collection("Booking").whereEqualTo("customerID", customerid).orderBy("bookDate", Query.Direction.ASCENDING)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                if(!queryDocumentSnapshots.isEmpty()){

                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for(DocumentSnapshot d: list){
                        Booking c = d.toObject(Booking.class);
                        bookingArrayList.add(c);
                    }
                    historyAdapter.notifyDataSetChanged();

                }else {
                    Toast.makeText(getContext(), "No data found in Database", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Something Error!!", Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }


}