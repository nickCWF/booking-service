package com.example.prototype2.customer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.prototype2.Login;
import com.example.prototype2.R;
import com.google.firebase.auth.FirebaseAuth;


public class customerProfileFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Button btnLogoutCustomer;

    public customerProfileFragment() {
        // Required empty public constructor
    }


    public static customerProfileFragment newInstance(String param1, String param2) {
        customerProfileFragment fragment = new customerProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_customer_profile, container, false);

        btnLogoutCustomer = view.findViewById(R.id.btnLogoutCustomer);
        btnLogoutCustomer.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnLogoutCustomer:
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(getContext(), "Success Logout", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), Login.class));
                break;
        }
    }
}