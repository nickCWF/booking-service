package com.example.prototype2.customer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.prototype2.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class customer extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);


        bottomNavigationView = findViewById(R.id.bottomNav);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.book);

    }
    customerProfileFragment customerProfileFragment = new customerProfileFragment();
    customerBookingFragment customerBookingFragment =new customerBookingFragment();
    historyFragment historyFragment =new historyFragment();


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.book:
                getSupportFragmentManager().beginTransaction().replace(R.id.customer_container,customerBookingFragment).commit();
                return true;
            case R.id.history:
                getSupportFragmentManager().beginTransaction().replace(R.id.customer_container,historyFragment).commit();
                return true;
            case R.id.profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.customer_container,customerProfileFragment).commit();
                return true;

            default:
                getSupportFragmentManager().beginTransaction().replace(R.id.customer_container,customerBookingFragment).commit();

        }
        return false;
    }
}