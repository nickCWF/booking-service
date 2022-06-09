package com.example.prototype2.barber;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.prototype2.R;
import com.example.prototype2.customer.customer;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class listApplyLeaveActivity extends AppCompatActivity {


    RecyclerView recyclerViewLeaveApply;
    private ArrayList<leaveApplyData> applyLeaveList;
    private Context mContext;
    private Activity mActivity;
    barberApplyLeaveAdapter adapter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_apply_leave);


        recyclerViewLeaveApply = findViewById(R.id.applyLeaveView);
        progressBar = findViewById(R.id.progressBar);
        recyclerViewLeaveApply.setNestedScrollingEnabled(false);

        progressBar.setVisibility(View.VISIBLE);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference barberLeave = db.collection("Leave");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String barberID = user.getUid();
        Log.d("UserID",barberID );

        Query query = db.collection("Leave").whereEqualTo("barberID", barberID);
//        Query query = barberLeave.whereEqualTo("barberID", barberID);

        FirestoreRecyclerOptions<leaveApplyData> options = new FirestoreRecyclerOptions.Builder<leaveApplyData>()
                .setQuery(query, leaveApplyData.class).build();

        adapter = new barberApplyLeaveAdapter(options);
        recyclerViewLeaveApply.setHasFixedSize(true);
        recyclerViewLeaveApply.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewLeaveApply.setAdapter(adapter);
        progressBar.setVisibility(View.GONE);
        ImageButton btnAddApplyLeave = (ImageButton) findViewById(R.id.btnAddApplyLeave);
        btnAddApplyLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), barberApplyLeaveActivity.class));
            }
        });


    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
}