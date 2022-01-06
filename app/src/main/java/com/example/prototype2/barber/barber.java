package com.example.prototype2.barber;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;

import com.example.prototype2.R;
import com.example.prototype2.customer.customerBookingFragment;
import com.example.prototype2.customer.customerProfileFragment;
import com.example.prototype2.customer.historyFragment;
import com.example.prototype2.owner.barberService;
import com.example.prototype2.owner.barberServiceAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class barber extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barber);


        bottomNavigationView = findViewById(R.id.barberbottomNav);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.barberSchedule);

    }
    barberProfileFragment barberProfileFragment = new barberProfileFragment();
    barberScanFragment barberScanFragment = new barberScanFragment();
    barberScheduleFragment barberScheduleFragment = new barberScheduleFragment();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.barberSchedule:
                getSupportFragmentManager().beginTransaction().replace(R.id.barber_container,barberScheduleFragment).commit();
                return true;
            case R.id.scan:
                getSupportFragmentManager().beginTransaction().replace(R.id.barber_container,barberScanFragment ).commit();
                return true;
            case R.id.barberProfile:
                getSupportFragmentManager().beginTransaction().replace(R.id.barber_container,barberProfileFragment).commit();
                return true;
        }

        return false;
    }
}