package com.example.prototype2.owner;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PackageManagerCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prototype2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link editLeaveFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class editLeaveFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String leaveID;
    private String barberID;
    private static int REQUEST_CODE = 100;
    ImageView imageView;
    OutputStream outputStream;
    private String[] resultStatus;
    private String[] resultCondition;

    public editLeaveFragment() {
        // Required empty public constructor
    }

    public editLeaveFragment(String leaveID, String barberID) {
        this.leaveID = leaveID;
        this.barberID = barberID;

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment editLeaveFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static editLeaveFragment newInstance(String param1, String param2) {
        editLeaveFragment fragment = new editLeaveFragment();
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
        View view = inflater.inflate(R.layout.fragment_edit_leave, container, false);

        String[] status = {"Accept", "Reject", "Pending"};
        String[] condition = {"Sick Leave", "Normal Leave", "No Reason"};

        TextView barberName = view.findViewById(R.id.textBarberName);
        TextView leaveDate = view.findViewById(R.id.textDateRange);
        TextView leaveReason = view.findViewById(R.id.textViewLeaveReason);
        TextView currentStatus = view.findViewById(R.id.textViewCurrentStatus);

//        Button btnSaveImage = view.findViewById(R.id.btnDownloadImage);
        Button btnApplyLeave = view.findViewById(R.id.btnApplyLeave);

       imageView = view.findViewById(R.id.imageApplyLeave);

        Spinner listStatus = view.findViewById(R.id.spinnerLeaveStatus);
        Spinner listCondition = view.findViewById(R.id.spinnerLeave);

        ProgressBar progressBar = view.findViewById(R.id.progressBar);

        ArrayAdapter adStatus = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, status);
        listStatus.setAdapter(adStatus);
        ArrayAdapter adCondition = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, condition);
        listCondition.setAdapter(adCondition);


        FirebaseFirestore fStore = FirebaseFirestore.getInstance();
        DocumentReference db = fStore.collection("Leave").document(leaveID);

        db.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        DocumentSnapshot documentSnapshot = task.getResult();
                        progressBar.setVisibility(View.VISIBLE);
                        if(documentSnapshot.exists()){
                            barberName.setText(documentSnapshot.getString("barberName"));
                            Long dateStart = documentSnapshot.getLong("DateStart");
                            Long dateEnd = documentSnapshot.getLong("DateEnd");
                            Date dStart = new Date(dateStart);
                            Date dEnd = new Date(dateEnd);
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                            String formattedStartDate = format.format(dStart);
                            String formattedEndDate  = format.format(dEnd);

                            currentStatus.setText(documentSnapshot.getString("status"));

                            leaveDate.setText(formattedStartDate+" - " +formattedEndDate);
                            leaveReason.setText(documentSnapshot.getString("reason"));
                            progressBar.setVisibility(View.GONE);
                        }else {
                            Toast.makeText(getContext(), "No such document", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }else {
                        Toast.makeText(getContext(), "Failed to get", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
            }
        });


        StorageReference mImageRef = FirebaseStorage.getInstance().getReference("imageApplyLeave/"+barberID+"/"+leaveID);
        try {
            File localfile = File.createTempFile("tempfile",".jpeg");
            mImageRef.getFile(localfile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());

                            imageView.setImageBitmap(bitmap);
                            progressBar.setVisibility(view.GONE);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressBar.setVisibility(view.GONE);
                    Toast.makeText(getContext(), "Image Error", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            progressBar.setVisibility(view.GONE);
        }

        listStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                resultStatus[0] = status[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        listCondition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                resultCondition[0] = condition[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnApplyLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DocumentReference saveLeave = fStore.collection("Leave").document(leaveID);
                Map<String, Object> leave = new HashMap<>();
                leave.put("status", resultStatus[0]);
                leave.put("leaveCondition", resultCondition[0]);
                saveLeave.set(leave).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getContext(), "Success Update", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getContext(), "Something Error, Try again Later", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


        return view;
    }


}