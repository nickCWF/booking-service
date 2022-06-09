package com.example.prototype2.customer;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.lights.LightState;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prototype2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.WriterException;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link customerBookingDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class customerBookingDetailsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String date, hour, min, barberName, barberID ;
    private static final DecimalFormat df = new DecimalFormat("0.00");


    DatabaseReference ref, checkBook;
    Booking book;
    long maxID = 0;
    private FirebaseUser user;

    String[] barberServiceID;
    List<String> mylist;


    public customerBookingDetailsFragment() {
        // Required empty public constructor
    }

    public customerBookingDetailsFragment(String date, String hour, String min, String barberName, String barberID ){
        this.date = date;
        this.hour = hour;
        this.min = min;
        this.barberName = barberName;
        this.barberID = barberID;

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment customerBookingDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static customerBookingDetailsFragment newInstance(String param1, String param2) {
        customerBookingDetailsFragment fragment = new customerBookingDetailsFragment();
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
            mParam1 = getArguments().getString("bookingDate");
            mParam2 = getArguments().getString("barberID");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_customer_booking_details, container, false);
        TextView txtBarberServices = (TextView) view.findViewById(R.id.textViewBarberServices);
        TextView txtBarber = (TextView) view.findViewById(R.id.textViewBarberSelected);
        TextView txtDate = (TextView) view.findViewById(R.id.textViewDate);
        TextView txtTime = (TextView) view.findViewById(R.id.textViewTime);
        TextView txtBarberServiceID = (TextView) view.findViewById(R.id.textViewBarberServiceID);
        TextView txtServiceTotalPrice = (TextView) view.findViewById(R.id.textViewTotalPrice);
        TextView txtServiceTotalCommission = (TextView) view.findViewById(R.id.textViewTotalCommission);
        TextView txtServiceTotalDuration = (TextView) view.findViewById(R.id.textViewTotalDuration);
        TextView displayServiceTotalDuration = (TextView) view.findViewById(R.id.textDisplayTotalDuration);
        ImageView qrImage = (ImageView) view.findViewById(R.id.imageView);

        ProgressBar progressBar = view.findViewById(R.id.progressBar);

        Button btnBook = (Button) view.findViewById(R.id.btnBook);


//        DecimalFormat hourFormat = new DecimalFormat("00");
//        String formatHour = hourFormat.format(hour);
//        DecimalFormat minFormat = new DecimalFormat("00");
//        String formatMin = minFormat.format(min);

        String fullDate = date;
        int newHour = Integer.parseInt(hour);
        int newMin = Integer.parseInt(min);

        //Check our time
        if(newMin <10){
            String fullTime = newHour + ":" + String.format("%02d",newMin);
            txtTime.setText(fullTime);
        }
        else {
            String fullTime =  hour +":"+min;
            txtTime.setText(fullTime);
        }


        txtDate.setText(fullDate);
        txtBarber.setText(barberName);
        Double totalPrice = 0.00;
        Double totalCommission= 0.00;
        Double totalDuration = 0.00;

        ArrayList arrayList = new ArrayList();
        ArrayList priceList = new ArrayList();

        /**Get the information checkbox from barber services**/
        for(int i = 0; i < customerBarberServiceAdapter.mContentList.size(); i++){
            if(customerBarberServiceAdapter.mContentList.get(i).isSelected()){

//                txtBarberServices.setText(customerBarberServiceAdapter.mContentList.get(i).getBarberServiceName()+", "+txtBarberServices.getText());
//                txtBarberServiceID.setText(customerBarberServiceAdapter.mContentList.get(i).getBarberServiceID()+", "+txtBarberServiceID.getText());
               if (txtBarberServices.getText().toString().isEmpty()||txtBarberServices.getText().toString()==null){
                   txtBarberServices.setText(customerBarberServiceAdapter.mContentList.get(i).getBarberServiceName());
                   txtBarberServiceID.setText(customerBarberServiceAdapter.mContentList.get(i).getBarberServiceID());
               }
               else {
                   txtBarberServices.setText(txtBarberServices.getText()+", "+customerBarberServiceAdapter.mContentList.get(i).getBarberServiceName());
                   txtBarberServiceID.setText(txtBarberServiceID.getText()+", "+customerBarberServiceAdapter.mContentList.get(i).getBarberServiceID());
               }

                String barberServicePrice = customerBarberServiceAdapter.mContentList.get(i).getBarberServicePrice();
                String barberServiceCommission = customerBarberServiceAdapter.mContentList.get(i).getBarberServiceCommission();
                Double barberServiceP = Double.parseDouble(barberServicePrice);
                totalPrice = totalPrice + barberServiceP;

                priceList.add(customerBarberServiceAdapter.mContentList.get(i).getBarberServicePrice());
                arrayList.add(customerBarberServiceAdapter.mContentList.get(i).getBarberServiceName());

                Double barberServiceC = Double.parseDouble(barberServiceCommission);
                totalCommission = totalCommission + barberServiceC;

                Double duration = customerBarberServiceAdapter.mContentList.get(i).getBarberServiceDuration();
                totalDuration = totalDuration + duration;
            }
        }

       for (int i = 0; i < arrayList.size();i++){
           Log.d("exist", priceList.get(i).toString());
           Log.d("exist", arrayList.get(i).toString());
       }

        String displayTotalPrice = "RM "+ df.format(totalPrice) ;
                txtServiceTotalPrice.setText(displayTotalPrice);

        String displayTotalCommission = totalCommission.toString().trim();
        txtServiceTotalCommission.setText(displayTotalCommission);

        String storeTotalDuration = String.format("%.2f", totalDuration);
        txtServiceTotalDuration.setText(storeTotalDuration);



        if(totalDuration<1){
            Double newTotalDuration = totalDuration * 100;
            String displayTotalDuration = String.format("%.2f", newTotalDuration);
            displayServiceTotalDuration.setText(displayTotalDuration+" min");
        }
        else {
            Double newTotalDuration = totalDuration;
            String displayTotalDuration = String.format("%.2f", newTotalDuration);
            displayServiceTotalDuration.setText(displayTotalDuration+" ");
        }

        /**Get all data information**/
        String bookTime, bookDate, barberService, barberServiceName, customerName;

        customerName = customerData.getUsername();
        barberServiceName = txtBarberServices.getText().toString().trim();
        bookTime = txtTime.getText().toString().trim();
        bookDate = txtDate.getText().toString().trim();
        barberService = txtBarberServiceID.getText().toString().trim();
//        txtBarber.setText(customerName);

        ref = FirebaseDatabase.getInstance().getReference().child("Booking");
        checkBook = FirebaseDatabase.getInstance().getReference().child("checkBook");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    maxID=(snapshot.getChildrenCount());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        String customerID;
        user = FirebaseAuth.getInstance().getCurrentUser();
        customerID = user.getUid();


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference barber = db.collection("Booking").document();



        String bookTotalPrice = txtServiceTotalPrice.getText().toString().trim();
        String bookTotalCommission = txtServiceTotalCommission.getText().toString().trim();
        Double bookTotalDuration = Double.parseDouble(txtServiceTotalDuration.getText().toString());

        String scheduleTime =  hour +"."+min;
        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                /**Booking customer Schedule**/
                String key = barber.getId();
                Double schedule = Double.parseDouble(scheduleTime);
                Map<String, Object> book = new HashMap<>();
                book.put("bookID", key);
                book.put("barberServiceId", barberService);
                book.put("barberServiceName", barberServiceName);
                book.put("barberServiceNameList", arrayList);
                book.put("barberName", barberName);
                book.put("barberID", barberID);
                book.put("bookTime", bookTime);
                book.put("bookDate", bookDate);
                book.put("customerID", customerID);
                book.put("customerName", customerName);
                book.put("schedule", schedule);
                book.put("bookTotalPrice", bookTotalPrice);
                book.put("bookPriceList", priceList);
                book.put("bookTotalCommission", bookTotalCommission);
                book.put("bookTotalDuration", bookTotalDuration);
                book.put("status","pending");

                barber.set(book).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getActivity(),"Success make appointment", Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(getActivity(),"Not Success please try again later", Toast.LENGTH_LONG).show();
                        }
                    }
                });


                /**Save Check Book**/
//                DocumentReference checkSchedule = db.collection("barberSchedule").document(barberID).collection(bookDate).document(key);
                DocumentReference checkSchedule = db.collection("barberSchedule").document(bookDate).collection(bookTime).document();
                Map<String, Object> checkBooklist = new HashMap<>();
                checkBooklist.put("checkTime", bookTime);
                checkBooklist.put("barberID", barberID);
                checkSchedule.set(checkBooklist).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
//                            Toast.makeText(getActivity(),"Success shcedule", Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(getActivity(),"Not Success schedule", Toast.LENGTH_LONG).show();
                        }
                    }
                });

                /**Get Check Book**/
                db.collection("barberSchedule/"+barberID+"/"+bookDate)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Log.d("exist", document.getId() + " => " + document.getData());
                                    }
                                }
                                else {
                                    Log.d("not exist", "No data");
                                }
                            }
                        });


                /**Generate QR code**/
                if(key.isEmpty()){
                    Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
                }
                else {
                    QRGEncoder qrgEncoder = new QRGEncoder(key, null , QRGContents.Type.TEXT, 500);
                    try {
                        Bitmap qrBits = qrgEncoder.encodeAsBitmap();
                        qrImage.setImageBitmap(qrBits);
                        qrImage.setDrawingCacheEnabled(true);
                        qrImage.buildDrawingCache();


                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        StorageReference storageRef = storage.getReference();
                        StorageReference mountainsRef = storageRef.child("book/"+ key);

                        Bitmap saveQRImage = ((BitmapDrawable) qrImage.getDrawable()).getBitmap();

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        saveQRImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] data = baos.toByteArray();

                        UploadTask uploadTask = mountainsRef.putBytes(data);
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                                Toast.makeText(getContext(), "Success Upload", Toast.LENGTH_SHORT).show();
//                                Uri downloadURL = taskSnapshot.getDownloadUrl();
                            }
                        });

                    }catch (WriterException e){
                        e.printStackTrace();
                    }
                }
                progressBar.setVisibility(View.GONE);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        FragmentManager manager = getFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();
                        transaction.replace(R.id.customer_container,new customerBookingFragment())
                                .addToBackStack(null)
                                .commit();
                    }
                }, 5000);

            }
        });


        return view;
    }
}