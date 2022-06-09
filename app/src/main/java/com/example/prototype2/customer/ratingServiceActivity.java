package com.example.prototype2.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.prototype2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class ratingServiceActivity extends AppCompatActivity {

    private FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_service);

        fStore = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        String barberID = intent.getStringExtra("barberUID");
        String bookID = intent.getStringExtra("bookID");
        String customerID = intent.getStringExtra("customerID");

        Button btnSubmitReview = findViewById(R.id.btnSubmitReview);
        EditText editTextTextComment = findViewById(R.id.editTextTextMultiLine);
        RatingBar ratingBar = findViewById(R.id.ratingBar);
        ProgressBar progressBar = findViewById(R.id.progressBar);



        final Float[] rating = new Float[1];
        rating[0]= null;

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                    rating[0] = v;
                Log.d("tag", String.valueOf(v));
            }
        });

        fStore = FirebaseFirestore.getInstance();
        DocumentReference db = fStore.collection("Rating").document();
        CollectionReference ratingRef = fStore.collection("Rating");

        Query query = ratingRef.whereEqualTo("bookID",bookID);
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()){
                        for (QueryDocumentSnapshot documentSnapshot: task.getResult()){
                            btnSubmitReview.setVisibility(View.GONE);
                            Float latestRating = documentSnapshot.getDouble("rate").floatValue();

                            ratingBar.setRating(latestRating);
                            editTextTextComment.setText(documentSnapshot.getString("comment"));
                            editTextTextComment.setEnabled(false);
                        }
                    }else {

                    }
                }
            });


        btnSubmitReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String comment = editTextTextComment.getText().toString().trim();
                Log.d("TAG", comment);
                if (rating[0]==null){
                    Toast.makeText(getApplication(),"Please make rating", Toast.LENGTH_LONG).show();
                    return;
                }
                if (comment.isEmpty()){
                    Toast.makeText(getApplication(),"Please leave a comment", Toast.LENGTH_LONG).show();
                    return;
                }

                else {
                    progressBar.setVisibility(View.VISIBLE);
                    fStore = FirebaseFirestore.getInstance();
                    DocumentReference db = fStore.collection("Rating").document();
                    String rateKey = db.getId();
                    Map<String, Object> rate = new HashMap<>();
                    rate.put("barberID", barberID);
                    rate.put("customerID", customerID);
                    rate.put("bookID", bookID);
                    rate.put("rateID", rateKey);
                    rate.put("rate", rating[0]);
                    rate.put("comment", comment);

                    db.set(rate).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(getApplication(),"Success make comment", Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                                finish();
                                overridePendingTransition(0,250);
                                startActivity(getIntent());
                                overridePendingTransition(0,250);
                            }
                            else {
                                Toast.makeText(getApplication(),"Not Success please try again later", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }



            }
        });

        Log.d("tag", barberID + ", " + bookID + ", "+ customerID);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}