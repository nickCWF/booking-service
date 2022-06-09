package com.example.prototype2;

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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity implements View.OnClickListener{

    private TextView banner, registerUser;
    private EditText editTextUsername, editTextEmail, editTextPassword, editTextContactNumber, editTextConformPassword;
    private ProgressBar progressBar;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        banner = (TextView) findViewById(R.id.banner);
        banner.setOnClickListener(this);

        registerUser = (Button) findViewById(R.id.btnRegister);
        registerUser.setOnClickListener(this);

        editTextUsername = (EditText) findViewById(R.id.txtUsername);

        editTextEmail = (EditText) findViewById(R.id.txtEmailAddress);
        editTextContactNumber = (EditText) findViewById(R.id.txtContactNumber);
        editTextPassword = (EditText) findViewById(R.id.txtPassword);
        editTextConformPassword = (EditText) findViewById(R.id.txtConformPassword);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.banner:
                startActivity(new Intent(this, Login.class));
                break;
            case R.id.btnRegister:
                registerUser();
                break;
        }
    }

    private void registerUser() {
        String username = editTextUsername.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String contactNumber = editTextContactNumber.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String conformPassword = editTextConformPassword.getText().toString().trim();

        if(username.isEmpty()){
            editTextUsername.setError("Username is required!");
            editTextUsername.requestFocus();
            return;
        }

        if(email.isEmpty()){
            editTextEmail.setError("Email is required!");
            editTextEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Please provide valid email");
            editTextEmail.requestFocus();
            return;
        }
        if(contactNumber.isEmpty()){
            editTextContactNumber.setError("Contact number is required!");
            editTextContactNumber.requestFocus();
            return;
        }
        if (contactNumber.length()<10){
            editTextContactNumber.setError("Please provide valid contact number!");
            editTextContactNumber.requestFocus();
            return;
        }
        if(!Patterns.PHONE.matcher(contactNumber).matches()){
            editTextEmail.setError("Please provide valid contact number!");
            editTextEmail.requestFocus();
            return;
        }
        if(password.isEmpty()){
            editTextPassword.setError("Password is required!");
            editTextPassword.requestFocus();
            return;
        }
        if(password.length() < 6 ){
            editTextPassword.setError("Min password length should be 6 characters!");
            editTextPassword.requestFocus();
            return;
        }
        if(conformPassword.isEmpty()){
            editTextConformPassword.setError("Conform Password is required!");
            editTextConformPassword.requestFocus();
            return;
        }
        if(!password.equals(conformPassword)){
            editTextConformPassword.setError("Password incorrect!");
            editTextConformPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        fAuth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                FirebaseUser user = fAuth.getCurrentUser();
                Toast.makeText(Register.this, "Account Create", Toast.LENGTH_SHORT).show();
                DocumentReference df = fStore.collection("Users").document(user.getUid());
                Map<String, Object> userInfo = new HashMap<>();
                userInfo.put("Username", username);
                userInfo.put("Email",email);
                userInfo.put("ContactNumber", contactNumber);


                //access level
                userInfo.put("isClient","1");

                df.set(userInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(Register.this, "Account Create", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Register.this, "Something Wrong!", Toast.LENGTH_SHORT).show();
                    }
                });
                progressBar.setVisibility(View.GONE);
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Register.this, "Failed to create account!", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}