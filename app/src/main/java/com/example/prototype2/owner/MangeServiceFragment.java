package com.example.prototype2.owner;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.prototype2.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class MangeServiceFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    FloatingActionButton btnFloatingAddBarberService;


    FirebaseFirestore fStore;
    RecyclerView barberServiceList;
    barberServiceAdapter adapter;

    public MangeServiceFragment() {

    }


    // TODO: Rename and change types and number of parameters
    public static MangeServiceFragment newInstance(String param1, String param2) {
        MangeServiceFragment fragment = new MangeServiceFragment();
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
        View view = inflater.inflate(R.layout.fragment_mange_service, container, false);
        btnFloatingAddBarberService = view.findViewById(R.id.btnFloatingAddService);
        btnFloatingAddBarberService.setOnClickListener(this);

        barberServiceList=(RecyclerView)view.findViewById(R.id.barberService_list);
        barberServiceList.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseRecyclerOptions<barberService> options =
                new FirebaseRecyclerOptions.Builder<barberService>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("barberService"), barberService.class)
                        .build();

        adapter = new barberServiceAdapter(options);
        barberServiceList.setHasFixedSize(true);
        barberServiceList.setAdapter(adapter);

        return view;
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnFloatingAddService:
                startActivity(new Intent(getActivity(), com.example.prototype2.owner.addBarberService.class));
        }
    }


    @Override
    public void onStart() {
        super.onStart();

            adapter.startListening();
            Log.d("TAG", "Cached document data: ");
            }

    @Override
    public void onStop() {
        super.onStop();

            adapter.stopListening();
        Log.d("TAG", "Error");


    }
}