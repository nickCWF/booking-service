package com.example.prototype2.owner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.prototype2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class addBarberService extends AppCompatActivity implements View.OnClickListener {

    EditText editTextBarberServiceName, editTextBarberServicePrice, editTextBarberServiceCommission, editTextBarberServiceDuration;
    Button btnAddBarberService;

    FirebaseFirestore fStore;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_barber_service);


        editTextBarberServiceName = (EditText) findViewById(R.id.editTextBarberServiceName);
        editTextBarberServicePrice = (EditText) findViewById(R.id.editTextBarberServicePrice);
        editTextBarberServiceCommission = (EditText) findViewById(R.id.editTextBarberServiceCommission);
        editTextBarberServiceDuration = (EditText) findViewById(R.id.editTextBarberServiceDuration);

        btnAddBarberService = (Button) findViewById(R.id.btnAddBarberService);
        btnAddBarberService.setOnClickListener(this);

        fStore = FirebaseFirestore.getInstance();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnAddBarberService:
                addBarberService();
                break;
        }
    }
    public void addBarberService(){

        String barberServiceName = editTextBarberServiceName.getText().toString().trim();
        String barberServicePrice = editTextBarberServicePrice.getText().toString().trim();
        String barberServiceCommission = editTextBarberServiceCommission.getText().toString().trim();
        Double barberServiceDuration = Double.parseDouble(editTextBarberServiceDuration.getText().toString());

        if(barberServiceName.isEmpty()){
            editTextBarberServiceName.setError("Name is required!");
            editTextBarberServiceName.requestFocus();
            return;
        }
        if(barberServicePrice.isEmpty()){
            editTextBarberServicePrice.setError("Name is required!");
            editTextBarberServicePrice.requestFocus();
            return;
        }
        if(barberServiceCommission.isEmpty()){
            editTextBarberServiceCommission.setError("Name is required!");
            editTextBarberServiceCommission.requestFocus();
            return;
        }
        if(barberServiceDuration.toString().isEmpty()){
            editTextBarberServiceDuration.setError("Duration is required!");
            editTextBarberServiceDuration.requestFocus();
            return;
        }

        DocumentReference df = fStore.collection("barberService").document();
        df.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        Toast.makeText(addBarberService.this, "The ID is not valid", Toast.LENGTH_SHORT).show();
                    }else {
                        addDataBarberService(df,barberServiceName,barberServicePrice,barberServiceCommission, barberServiceDuration);
//                        addListDataBarberService(barberServiceID, barberServiceName,barberServicePrice);
                    }
                }else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });

    }

    public void addDataBarberService(DocumentReference df, String barberServiceName, String barberServicePrice, String barberServiceCommission, Double barberServiceDuration){
        String id = df.getId();
        Map<String, Object> barberServiceInfo = new HashMap<>();
        barberServiceInfo.put("barberServiceID", id);
        barberServiceInfo.put("barberServiceName", barberServiceName);
        barberServiceInfo.put("barberServicePrice", barberServicePrice);
        barberServiceInfo.put("barberServiceCommission", barberServiceCommission);
        barberServiceInfo.put("barberServiceDuration", barberServiceDuration);


        df.set(barberServiceInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(addBarberService.this, "Service Create", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(addBarberService.this, "Not success", Toast.LENGTH_SHORT).show();
            }
        });
        MangeServiceFragment fragment = new MangeServiceFragment();


        startActivity(new Intent(getApplicationContext(),owner.class));
    }

//    public void addListDataBarberService( String barberServiceName, String barberServicePrice){
//        Map<String, Object> barberService = new HashMap<>();
//        barberService.put("barberServiceID", barberServiceID);
//        barberService.put("barberServiceName", barberServiceName);
//        barberService.put("barberServicePrice",barberServicePrice);
//
//        FirebaseDatabase.getInstance().getReference("barberService").child().setValue(barberService);
//    }
}