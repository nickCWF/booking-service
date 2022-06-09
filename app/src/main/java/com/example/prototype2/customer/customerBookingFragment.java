package com.example.prototype2.customer;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.prototype2.Login;
import com.example.prototype2.MainActivity;
import com.example.prototype2.R;
import com.example.prototype2.owner.barberService;
import com.example.prototype2.owner.barberServiceAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.util.ArrayList;


public class customerBookingFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    DatabaseReference databaseReference;
    FirebaseFirestore fStore;
    RecyclerView recyclerViewBarberService;
    customerBarberServiceAdapter adapter;
    private ArrayList<customerBarberService> barberServicesList;
    private Context mContext;
    private Activity mActivity;
    private customerBarberServiceAdapter barberServiceAdapter = null;


    public customerBookingFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static customerBookingFragment newInstance(String param1, String param2) {
        customerBookingFragment fragment = new customerBookingFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_customer_booking, container, false);
        ImageButton imgButtonNext = (ImageButton) view.findViewById(R.id.imgButtonNext);


        //goto next schedule process
        imgButtonNext.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                int num =0;
                for(int i = 0; i < customerBarberServiceAdapter.mContentList.size(); i++){
                    if(customerBarberServiceAdapter.mContentList.get(i).isSelected()){
                       num+=1;
                    }
                }
                if(num ==0){
                    Toast.makeText(getActivity(),  " Please make atleast one checkbox", Toast.LENGTH_SHORT).show();
                }
                else {
                    FragmentManager manager = getFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.customer_container,new bookingCalanderFragment())
                            .addToBackStack(null)
                            .commit();
                }
            }
        });

        /**Set Up recycle view**/
        mActivity = getActivity();
        mContext = getContext();
        FirebaseApp.initializeApp(getContext());
        recyclerViewBarberService = view.findViewById(R.id.customerBarberService_list);
        recyclerViewBarberService.setHasFixedSize(true);
        recyclerViewBarberService.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewBarberService.setNestedScrollingEnabled(false);
        barberServicesList = new ArrayList<>();


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference barberSer = db.collection("barberService");

        barberSer.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                barberServicesList.clear();
                if(error!= null){
                    Toast.makeText(getContext(), "Something Error!!", Toast.LENGTH_SHORT).show();
                }
                for(DocumentChange documentChange: value.getDocumentChanges()){

                    barberServicesList.add(documentChange.getDocument().toObject(customerBarberService.class));
                }
                adapter = new customerBarberServiceAdapter(mContext,mActivity, (ArrayList<customerBarberService>) barberServicesList);
                recyclerViewBarberService.setAdapter(adapter);
//                adapter.notifyDataSetChanged();
            }
        });





        return view;
    }

    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onStop() {
        super.onStop();




    }
}