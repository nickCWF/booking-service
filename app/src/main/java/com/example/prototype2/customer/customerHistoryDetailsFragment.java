package com.example.prototype2.customer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prototype2.R;
import com.example.prototype2.barber.listApplyLeaveActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link customerHistoryDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class customerHistoryDetailsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String bookID;
    private FirebaseFirestore fStore;
    Button btnReview, btnPayment;

    public customerHistoryDetailsFragment() {
        // Required empty public constructor
    }

    public customerHistoryDetailsFragment(String bookID){
        this.bookID = bookID;
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment customerHistoryDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static customerHistoryDetailsFragment newInstance(String param1, String param2) {
        customerHistoryDetailsFragment fragment = new customerHistoryDetailsFragment();
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
        View view = inflater.inflate(R.layout.fragment_customer_history_details, container, false);
        TextView bookid = view.findViewById(R.id.textViewBookid);
        TextView txtBarberName = view.findViewById(R.id.textViewBarbername);
        TextView txtBarberServicename = view.findViewById(R.id.textViewBarberServicename);
        TextView txtbookDate = view.findViewById(R.id.bookDate);
        TextView txtbookTime = view.findViewById(R.id.bookTime);
        TextView txtStatus = view.findViewById(R.id.bookStatus);
        TextView txtTotalPrice = view.findViewById(R.id.textViewBarberServiceTotalPrice);
        TextView txtTotalDuration = view.findViewById(R.id.bookDuration);

        btnReview = view.findViewById(R.id.btnReview);
        btnReview.setVisibility(view.INVISIBLE);
        btnPayment = view.findViewById(R.id.btnPayment);
        btnPayment.setVisibility(View.INVISIBLE);

        ProgressBar progressBar = view.findViewById(R.id.progressBar);

        ImageView qrCodeImage = view.findViewById(R.id.qrCodeImage);
        bookid.setText(bookID);

        progressBar.setVisibility(view.VISIBLE);
        fStore = FirebaseFirestore.getInstance();
        DocumentReference documentReference = fStore.collection("Booking").document(bookID);
        documentReference.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                       if(task.isSuccessful()){

                           String barberName = task.getResult().getString("barberName");
                           String barberServices = task.getResult().getString("barberServiceName");
                           String bookD = task.getResult().getString("bookDate");
                           String bookT = task.getResult().getString("bookTime");
                           String status = task.getResult().getString("status");
                           String totalPrice = task.getResult().getString("bookTotalPrice");
                           Double totalDuration = task.getResult().getDouble("bookTotalDuration");
                           String barberUID = task.getResult().getString("barberID");
                           String customerID = task.getResult().getString("customerID");

                           txtBarberName.setText(barberName);
                           txtBarberServicename.setText(barberServices);
                           txtbookDate.setText("Date: "+bookD);
                           txtbookTime.setText("Time: "+ bookT);
                           txtTotalPrice.setText(totalPrice);
                           txtTotalDuration.setText(totalDuration.toString());

                           if(status.equals("pending")){
                                txtStatus.setText("Pending");
                               txtStatus.setBackgroundResource(R.drawable.pending_background);
                           }
                           else if(status.equals("cancel"))
                           {
                               txtStatus.setText("Cancel");
                               txtStatus.setBackgroundResource(R.drawable.cancel_background);
                           }
                           else if(status.equals("ongoing"))
                           {
                               txtStatus.setText("Ongoing");
                               txtStatus.setBackgroundResource(R.drawable.ongoing_background);

                           }
                           else if(status.equals("finish"))
                           {
                               txtStatus.setText("Finish");
                               txtStatus.setBackgroundResource(R.drawable.finish_background);
                               btnReview.setVisibility(view.VISIBLE);
                               btnReview.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View view) {
                                       Intent intent = new Intent(getActivity(), ratingServiceActivity.class);
                                       intent.putExtra("barberUID", barberUID);
                                       intent.putExtra("bookID", bookID);
                                       intent.putExtra("customerID", customerID);
                                       startActivity(intent);
                                   }
                               });

                           }
                           else if(status.equals("waiting")){
                               txtStatus.setText("Waiting");
                               txtStatus.setBackgroundResource(R.drawable.waiting_background);
                               btnPayment.setVisibility(View.VISIBLE);
                               btnPayment.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View view) {
                                       //Payment
                                       Intent intentPayment = new Intent(getActivity(), paymentActivity.class);
                                       intentPayment.putExtra("bookID", bookID);
                                       startActivity(intentPayment);
                                   }
                               });
                           }
                           else {
                               txtStatus.setText("");
                           }

                       }
                       else {

                           Toast.makeText(getContext(), "Something Error", Toast.LENGTH_SHORT).show();
                       }
                        progressBar.setVisibility(view.GONE);
                    }
                });
        /**Get the QR code Image**/
        String bookedID = bookID;
        StorageReference mImageRef = FirebaseStorage.getInstance().getReference("book/"+bookedID);
        try {
            File localfile = File.createTempFile("tempfile",".jpeg");
            mImageRef.getFile(localfile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                            qrCodeImage.setImageBitmap(bitmap);
                            progressBar.setVisibility(view.GONE);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressBar.setVisibility(view.GONE);
                    Toast.makeText(getContext(), "QR Code Error", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            progressBar.setVisibility(view.GONE);
        }


        return view;
    }
}