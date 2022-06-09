package com.example.prototype2.barber;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.example.prototype2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.Result;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link barberScanFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class barberScanFragment extends Fragment {
    private static final int CAMERA_REQUEST_CODE =101;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private CodeScanner codeScanner;
    FirebaseFirestore fStore;

    public barberScanFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment barberScanFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static barberScanFragment newInstance(String param1, String param2) {
        barberScanFragment fragment = new barberScanFragment();
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
        View view = inflater.inflate(R.layout.fragment_barber_scan, container, false);
        CodeScannerView scannerView = view.findViewById(R.id.scanner_view);
        codeScanner = new CodeScanner(getActivity(), scannerView);
        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String qrCodeID = result.getText();
//                        Toast.makeText(getActivity(), qrCodeID, Toast.LENGTH_SHORT).show();
                        if(!qrCodeID.isEmpty()){
//                            Toast.makeText(getActivity(), qrCodeID, Toast.LENGTH_SHORT).show();
                            fStore = FirebaseFirestore.getInstance();
                            DocumentReference codeRef = fStore.collection("Booking").document(qrCodeID);
                            codeRef.update("status", "ongoing").addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(getContext(), "Success Update", Toast.LENGTH_LONG).show();
                                    }
                                    else {
                                        Toast.makeText(getContext(), "QR code Error", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                        else {
                            Toast.makeText(getContext(), "Something Error, please try again!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                codeScanner.startPreview();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        codeScanner.startPreview();
    }

    @Override
    public void onPause() {
        codeScanner.releaseResources();
        super.onPause();
    }
}