package com.example.prototype2.owner;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.prototype2.R;
import com.example.prototype2.barberData;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ManageBarberFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    FloatingActionButton addBarber;
    RecyclerView barberview;

    barberAdapter adapter;

    FirebaseFirestore fStore;

    public ManageBarberFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static ManageBarberFragment newInstance(String param1, String param2) {
        ManageBarberFragment fragment = new ManageBarberFragment();
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
        View view = inflater.inflate(R.layout.fragment_manage_barber, container, false);
        addBarber = view.findViewById(R.id.btnAddBarber);
        addBarber.setOnClickListener(this);

        barberview=(RecyclerView)view.findViewById(R.id.barberview);
        barberview.setLayoutManager(new LinearLayoutManager(getContext()));

        /**call the database**/
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference barber = db.collection("barber");
        Query query = barber.orderBy("barberID",Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<barber> options = new FirestoreRecyclerOptions.Builder<barber>()
                .setQuery(query, barber.class).build();

        adapter = new barberAdapter(options);
        barberview.setHasFixedSize(true);
        barberview.setAdapter(adapter);

//        FirebaseRecyclerOptions<barberData> options =
//                new FirebaseRecyclerOptions.Builder<barberData>()
//                        .setQuery(FirebaseDatabase.getInstance().getReference().child("barber"), barberData.class)
//                        .build();
//
//        adapter = new barberAdapter(options);
//        barberview.setHasFixedSize(true);
//        barberview.setAdapter(adapter);
        return view;
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnAddBarber:
                startActivity(new Intent(getActivity(), com.example.prototype2.owner.addBarber.class));
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }
}