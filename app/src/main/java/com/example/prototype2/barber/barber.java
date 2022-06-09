package com.example.prototype2.barber;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.prototype2.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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