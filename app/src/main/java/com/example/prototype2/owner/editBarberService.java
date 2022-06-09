package com.example.prototype2.owner;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prototype2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;


public class editBarberService extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String barberServiceID, editTextUpdateServiceName, editTextUpdateServicePrice, editTextUpdateServiceCommission, editTextUpdateServiceDuration;
    ImageButton btnImgEditServiceName, btnImgEditServicePrice, btnImgEditServiceCommission, btnImgEditServiceDuration;


    FirebaseFirestore fStore;

    public editBarberService() {
        // Required empty public constructor
    }

   public editBarberService(String barberServiceID)
   {
        this.barberServiceID = barberServiceID;

   }

    // TODO: Rename and change types and number of parameters
    public static editBarberService newInstance(String param1, String param2) {
        editBarberService fragment = new editBarberService();
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
        View view = inflater.inflate(R.layout.fragment_edit_barber_service, container, false);
        fStore = FirebaseFirestore.getInstance();
        TextView barberServiceIDTextView =  view.findViewById(R.id.barberServiceIDHolder);
        TextView barberServiceNameTextView =  view.findViewById(R.id.barberServiceNameHolder);
        TextView barberServiceCommissionTextView =  view.findViewById(R.id.barberServiceCommissionHolder);
        TextView barberServicePriceTextView =  view.findViewById(R.id.barberServicePriceHolder);
        TextView barberServiceDurationTextView =  view.findViewById(R.id.barberServiceDurationHolder);


        btnImgEditServiceName = view.findViewById(R.id.btnImgbarberServiceName);
        btnImgEditServiceName.setOnClickListener(this);
        btnImgEditServicePrice = view.findViewById(R.id.btnImgbarberServicePrice);
        btnImgEditServicePrice.setOnClickListener(this);
        btnImgEditServiceCommission = view.findViewById(R.id.btnImgbarberServiceCommission);
        btnImgEditServiceCommission.setOnClickListener(this);
        btnImgEditServiceDuration = view.findViewById(R.id.btnImgbarberServiceDuration);
        btnImgEditServiceDuration.setOnClickListener(this);

        DocumentReference db = fStore.collection("barberService").document(barberServiceID);

        db.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(getContext(), "Something error, Try again later!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (value.exists()){
                   barberServiceIDTextView.setText(value.getString("barberServiceID"));
                   barberServiceNameTextView.setText(value.getString("barberServiceName"));
                   barberServiceCommissionTextView.setText(value.getString("barberServiceCommission"));
                   barberServicePriceTextView.setText(value.getString("barberServicePrice"));
                   barberServiceDurationTextView.setText(String.valueOf(value.getDouble("barberServiceDuration")));
               }else {
                    Toast.makeText(getContext(), "No such Data!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        return view;

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.btnImgbarberServiceName:
                AlertDialog.Builder alertServiceName = new AlertDialog.Builder(getContext());
                alertServiceName.setTitle("Update Service Name");
                LinearLayout layoutServiceName = new LinearLayout(getContext());
                layoutServiceName.setOrientation(LinearLayout.VERTICAL);
                layoutServiceName.setPadding(10, 10, 10, 10);
                EditText editText = new EditText(getContext());

                editText.setHint("Enter Service Name");
                editText.setInputType(InputType.TYPE_CLASS_TEXT);
                layoutServiceName.addView(editText);
                alertServiceName.setView(layoutServiceName);
                alertServiceName.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        editTextUpdateServiceName = editText.getText().toString();
                        DocumentReference nameRef = fStore.collection("barberService").document(barberServiceID);
                        nameRef.update("barberServiceName", editTextUpdateServiceName).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(getContext(), "Success Update Name", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getContext(), "Something Error try again later", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                        dialogInterface.cancel();
                    }
                });
                alertServiceName.show();
                break;
            case R.id.btnImgbarberServicePrice:
                AlertDialog.Builder alertPrice = new AlertDialog.Builder(getContext());
                alertPrice.setTitle("Update Service Price");
                LinearLayout layoutPrice = new LinearLayout(getContext());
                layoutPrice.setOrientation(LinearLayout.VERTICAL);
                layoutPrice.setPadding(10, 10, 10, 10);
                EditText editTextPrice = new EditText(getContext());

                editTextPrice.setHint("Enter Service Price");
                editTextPrice.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
                layoutPrice.addView(editTextPrice);
                alertPrice.setView(layoutPrice);
                alertPrice.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        editTextUpdateServicePrice = editTextPrice.getText().toString();
                        DocumentReference priceRef = fStore.collection("barberService").document(barberServiceID);
                        priceRef.update("barberServicePrice", editTextUpdateServicePrice).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(getContext(), "Success Update Price", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getContext(), "Something Error try again later", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                        dialogInterface.cancel();
                    }
                });
                alertPrice.show();
                break;
            case R.id.btnImgbarberServiceCommission:
                AlertDialog.Builder alertCommission = new AlertDialog.Builder(getContext());
                alertCommission.setTitle("Update Service Commission");
                LinearLayout layoutCommission = new LinearLayout(getContext());
                layoutCommission.setOrientation(LinearLayout.VERTICAL);
                layoutCommission.setPadding(10, 10, 10, 10);
                EditText editTextCommission = new EditText(getContext());

                editTextCommission.setHint("Enter Service Commission");
                editTextCommission.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
                layoutCommission.addView(editTextCommission);
                alertCommission.setView(layoutCommission);
                alertCommission.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        editTextUpdateServiceCommission = editTextCommission.getText().toString();
                        DocumentReference commissionRef = fStore.collection("barberService").document(barberServiceID);
                        commissionRef.update("barberServiceCommission", editTextUpdateServiceCommission).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(getContext(), "Success Update Commission", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getContext(), "Something Error try again later", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                        dialogInterface.cancel();
                    }
                });
                alertCommission.show();
                break;
            case R.id.btnImgbarberServiceDuration:
                AlertDialog.Builder alertDuration = new AlertDialog.Builder(getContext());
                alertDuration.setTitle("Update Service Duration");
                LinearLayout layoutDuration = new LinearLayout(getContext());
                layoutDuration.setOrientation(LinearLayout.VERTICAL);
                layoutDuration.setPadding(10, 10, 10, 10);
                EditText editTextDuration = new EditText(getContext());

                editTextDuration.setHint("Enter Service Duration");
                editTextDuration.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
                layoutDuration.addView(editTextDuration);
                alertDuration.setView(layoutDuration);
                alertDuration.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        editTextUpdateServiceDuration = editTextDuration.getText().toString();
                        Double duration = Double.parseDouble(editTextUpdateServiceDuration);
                        DocumentReference commissionRef = fStore.collection("barberService").document(barberServiceID);
                        commissionRef.update("barberServiceDuration", duration).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(getContext(), "Success Update Duration", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getContext(), "Something Error try again later", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                        dialogInterface.cancel();
                    }
                });
                alertDuration.show();
                break;
        }
    }
}