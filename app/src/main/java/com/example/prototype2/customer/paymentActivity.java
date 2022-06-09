package com.example.prototype2.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.prototype2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class paymentActivity extends AppCompatActivity {

    private FirebaseFirestore fStore;
    private String bookTotalPrice, customerID, bookDate, barberID, barberName;
    private ArrayList<Double> arrayList;
    private ArrayList serviceList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        TextView textView13 = findViewById(R.id.textView13);
        Spinner spinner = findViewById(R.id.cardType);

        EditText editTextCardNumber = findViewById(R.id.editTextCardNumber);
        EditText editTextMonth = findViewById(R.id.editTextMonth);
        EditText editTextYear = findViewById(R.id.editTextYear);
        EditText editTextCSV = findViewById(R.id.editTextCSV);

        Button btnPay = findViewById(R.id.btnPay);

        String[] status = {"VISA", "MASTER"};
        ArrayAdapter aa = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, status);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(aa);

        Intent intent = getIntent();
        String bookID = intent.getStringExtra("bookID");
        final Double[] totalPrice = {0.00};


        fStore = FirebaseFirestore.getInstance();
        DocumentReference db = fStore.collection("Booking").document(bookID);


        db.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                   task.getResult();
                   arrayList = (ArrayList<Double>) task.getResult().get("bookPriceList");
                   serviceList = (ArrayList) task.getResult().get("barberServiceNameList");
                   bookTotalPrice = task.getResult().getString("bookTotalPrice");
                   customerID = task.getResult().getString("customerID");
                   bookDate = task.getResult().getString("bookDate");
                   barberID = task.getResult().getString("barberID");
                   barberName = task.getResult().getString("barberName");
                    Log.d("TAG", arrayList.toString());
                    textView13.setText(bookTotalPrice);


                }
                else {

                }
            }
        });




        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference payment = db.collection("Payment").document();
                Map<String, Object> pay = new HashMap<>();
                String key = payment.getId();
                pay.put("payID", key);
                pay.put("bookTotalPrice", bookTotalPrice);
                pay.put("customerID", customerID);

                payment.set(pay);

                DocumentReference sales = db.collection("Sales").document().collection(bookDate).document();
                String keySale = sales.getId();
                Map<String, Object> sale = new HashMap<>();

                sale.put("saleID", keySale);
                sale.put("bookPriceList", arrayList);
                sale.put("barberServiceNameList", serviceList);

                sales.set(sale);

                DocumentReference performances = db.collection("Performances").document(bookDate).collection(barberID).document();
                DocumentReference perform = db.collection("Performances").document().collection(bookDate).document();
                String path = perform.getPath();
                Log.d("TAG", path);
                String performKey = performances.getId();
                Map<String, Object> performance = new HashMap<>();
                performance.put("performID", performKey);
                performance.put("bookID", bookID);
                performance.put("barberID", barberID);
                performance.put("barberName", barberName);

                performances.set(performance);

            }
        });

//        Log.d("TAG", totalPrice[0].toString());

    }
}