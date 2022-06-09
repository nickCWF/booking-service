package com.example.prototype2.customer;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.prototype2.R;
import com.example.prototype2.barberData;
import com.example.prototype2.database.barberSchedule;
import com.example.prototype2.owner.barber;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.core.OrderBy;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link bookingBarberFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class bookingBarberFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    // TODO: Rename and change types of parameters
    private static String mParam1;
    private static String mParam2;
    private static String mParam3;

    RecyclerView barberList;
    customerBarberAdapter adapter;
    DatabaseReference checkBook;
    String noIncludeBarber;


    public bookingBarberFragment() {
        // Required empty public constructor
    }

    public static String getDate(){
        return mParam1;
    }

    public static String getHour(){
        return mParam2;
    }

    public static String getMin(){
        return mParam3;
    }

    // TODO: Rename and change types and number of parameters
    public static bookingBarberFragment newInstance(String param1, String param2, String param3) {
        bookingBarberFragment fragment = new bookingBarberFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString("bookingDate");
            mParam2 = getArguments().getString("bookingHour");
            mParam3 = getArguments().getString("bookingMin");
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_booking_barber, container, false);
        ImageButton imgButtonPrevious = (ImageButton) view.findViewById(R.id.imgButtonPrevious);
        ImageButton imgButtonNext = (ImageButton) view.findViewById(R.id.imgButtonNext);

        TextView testData = (TextView) view.findViewById(R.id.TestData);

        if( mParam2 == null && mParam3 == null) {
            testData.setText(mParam1+ " 10" +":00");
            mParam2 = "10";
            mParam3 = "00";
        }

        else if(mParam2 != null && mParam3 == null){
            Date dateCurrent = Calendar.getInstance().getTime();
            DateFormat dateFormat = new SimpleDateFormat("MM");
            String strTime = dateFormat.format(dateCurrent);
            testData.setText(mParam1+ " 10:" + strTime);
            mParam2 = "10";
            mParam3 = strTime;
        }

        else if(mParam2 == null && mParam3 != null){
            Date dateCurrent = Calendar.getInstance().getTime();
            DateFormat dateFormat = new SimpleDateFormat("HH");
            String strTime = dateFormat.format(dateCurrent);
            testData.setText(mParam1+ " "+ strTime +":00");
            mParam2 = strTime;
            mParam3 = "00";
        }

        else {
            testData.setText(mParam1+" "+ mParam2 +":"+mParam3);
        }

        if (Integer.parseInt(mParam3)<10){
            mParam3 = "0"+mParam3;
        }


//        /**Get value from previous fragments**/
//        Bundle args = getArguments();
//        if(args != null){
//            testData.setText(args.getString("bookingDate")+args.getString("bookingHour")+": "+args.getString("bookingMin"));
//        }
//        else {
//            Date currentTime = Calendar.getInstance().getTime();
////            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm");
////            String strDate = dateFormat.format(currentTime);
//            testData.setText(currentTime.toString());
//        }


        imgButtonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.customer_container,new bookingCalanderFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });



        //Link recycle view to fragment
        barberList=(RecyclerView)view.findViewById(R.id.customerBarberService_list);
        barberList.setLayoutManager(new LinearLayoutManager(getContext()));

        // Check time table
//        String checkValidTime = mParam1+mParam2+mParam3;
//        checkBook = FirebaseDatabase.getInstance().getReference().child("checkBook");
//        checkBook.orderByKey().equalTo(checkValidTime).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });


        /**Check Barber Schedule**/
        ArrayList arrayList = new ArrayList();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference checkSchedule = db.collection("/barberSchedule/"+mParam1+"/"+mParam2 +":"+mParam3+"/");

            Query checkQuery = checkSchedule.whereEqualTo("checkTime", mParam2 +":"+mParam3);



            checkQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d("TA", document.getId() + " => " + document.getData());
                            String barberID = document.get("barberID").toString();
                            arrayList.add(barberID);

                            Log.d("TA", barberID);

//                            noIncludeBarber = document.get("barberID");
                        }

                        Log.d("Ta", arrayList.toString());


                    } else {
                        Log.d("TA", "Error getting documents: ", task.getException());
                    }

                    if(!arrayList.isEmpty()){
                        /**Call the recycleview**/
                        CollectionReference barber = db.collection("barber");
                        Query query = barber.whereNotIn("barberUID", arrayList);
//        Query query = barber.orderBy("barberID",Query.Direction.ASCENDING);

                        FirestoreRecyclerOptions<com.example.prototype2.owner.barber> options = new FirestoreRecyclerOptions.Builder<barber>()
                                .setQuery(query, barber.class).build();

                        adapter = new customerBarberAdapter(options);
                        barberList.setHasFixedSize(true);
                        barberList.setAdapter(adapter);
                        adapter.startListening();


                    }
                    else {
                /**Call the recycleview**/
                CollectionReference barber = db.collection("barber");
//                Query query = barber.whereNotIn("barberUID", arrayList);
                Query query = barber.orderBy("barberID",Query.Direction.ASCENDING);

                FirestoreRecyclerOptions<com.example.prototype2.owner.barber> options = new FirestoreRecyclerOptions.Builder<barber>()
                        .setQuery(query, barber.class).build();

                adapter = new customerBarberAdapter(options);
                barberList.setHasFixedSize(true);
                barberList.setAdapter(adapter);
                adapter.startListening();
                    }
                }
            });



        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        Log.d("TAG", "Cached document data: ");


    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
        Log.d("TAG", "Error");

    }
}