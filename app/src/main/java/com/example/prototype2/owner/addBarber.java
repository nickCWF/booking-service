package com.example.prototype2.owner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prototype2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class addBarber extends AppCompatActivity implements View.OnClickListener {

    private TextView registerBarber;
    private EditText editTextBarberName, editTextBarberEmail, editTextBarberPassword, editTextBarberContactNumber, editTextSalary, editTextBarberID;
    private ProgressBar progressBar;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_barber);

        registerBarber = (Button) findViewById(R.id.btnRegisterBarber);
        registerBarber.setOnClickListener(this);

        editTextBarberName = (EditText) findViewById(R.id.editTextBarberName);
        editTextBarberContactNumber = (EditText) findViewById(R.id.editTextBarberContactNumber);
        editTextBarberPassword = (EditText) findViewById(R.id.editTextBarberPassword);
        editTextSalary = (EditText) findViewById(R.id.editTextBarberSalary);
        editTextBarberEmail = (EditText) findViewById(R.id.editTextBarberEmail);
        editTextBarberID = (EditText) findViewById(R.id.editTextBarberID);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnRegisterBarber:
                registerBarber();
                break;
        }
    }

        private void registerBarber() {
            String barbername = editTextBarberName.getText().toString().trim();
            String barberemail = editTextBarberEmail.getText().toString().trim();
            String barberContactNumber = editTextBarberContactNumber.getText().toString().trim();
            String barberPassword = editTextBarberPassword.getText().toString().trim();
            String barberSalary = editTextSalary.getText().toString().trim();
            String barberID = editTextBarberID.getText().toString().trim();

            if (barbername.isEmpty()) {
                editTextBarberName.setError("Name is required!");
                editTextBarberName.requestFocus();
                return;
            }
            if (barberemail.isEmpty()) {
                editTextBarberEmail.setError("Email is required!");
                editTextBarberEmail.requestFocus();
                return;
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(barberemail).matches()) {
                editTextBarberEmail.setError("Please provide valid email");
                editTextBarberEmail.requestFocus();
                return;
            }
            if (barberContactNumber.isEmpty()) {
                editTextBarberContactNumber.setError("Contact number is required!");
                editTextBarberContactNumber.requestFocus();
                return;
            }
            if (barberContactNumber.length() < 10) {
                editTextBarberContactNumber.setError("Please provide valid contact number!");
                editTextBarberContactNumber.requestFocus();
                return;
            }
            if (!Patterns.PHONE.matcher(barberContactNumber).matches()) {
                editTextBarberContactNumber.setError("Please provide valid contact number!");
                editTextBarberContactNumber.requestFocus();
                return;
            }
            if (barberPassword.isEmpty()) {
                editTextBarberPassword.setError("Password is required!");
                editTextBarberPassword.requestFocus();
                return;
            }
            if (barberPassword.length() < 6) {
                editTextBarberPassword.setError("Min password length should be 6 characters!");
                editTextBarberPassword.requestFocus();
                return;
            }
            if (barberID.isEmpty()) {
                editTextBarberID.setError("ID is require!");
                editTextBarberID.requestFocus();
                return;
            }

            progressBar.setVisibility(View.VISIBLE);
            fAuth.createUserWithEmailAndPassword(barberemail, barberPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)  {


                    saveUser(barbername, barberemail, barberContactNumber, barberSalary, barberID);
                    saveDocument(barbername, barberemail, barberContactNumber, barberSalary, barberID);
                    saveRealDocument(barbername, barberemail,  barberID);
                    progressBar.setVisibility(View.GONE);
                    startActivity(new Intent(getApplicationContext(), owner.class));
                }
            });

        }

        public void saveUser(String barbername, String barberemail, String barberContactNumber, String barberSalary, String ID){
            FirebaseUser user = fAuth.getCurrentUser();
            Toast.makeText(addBarber.this, "Account Create", Toast.LENGTH_SHORT).show();
            DocumentReference df = fStore.collection("Users").document(user.getUid());
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("barberName", barbername);
            userInfo.put("barberEmail", barberemail);

            //access level
            userInfo.put("isBarber", "1");
            df.set(userInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(addBarber.this, "Account Create", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(addBarber.this, "Something Wrong!", Toast.LENGTH_SHORT).show();
                }
            });
        }

        public void saveRealDocument(String barbername, String barberemail, String ID){
            Map<String, Object> barber = new HashMap<>();
            barber.put("barberName", barbername);
            barber.put("barberEmail", barberemail);
            barber.put("barberID", ID);

            FirebaseDatabase.getInstance().getReference("barber").child(ID).setValue(barber);

        }

    public void saveDocument(String barbername, String barberemail, String barberContactNumber, String barberSalary, String ID){

        Toast.makeText(addBarber.this, "Account Create", Toast.LENGTH_SHORT).show();
        DocumentReference df = fStore.collection("barber").document(ID);
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("barberID", ID);
        userInfo.put("barberName", barbername);
        userInfo.put("barberEmail", barberemail);
        userInfo.put("barberContactNumber", barberContactNumber);
        userInfo.put("barberSalary", barberSalary);

        df.set(userInfo);
    }


    }

