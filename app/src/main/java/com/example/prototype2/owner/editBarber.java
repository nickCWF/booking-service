package com.example.prototype2.owner;

import android.app.ProgressDialog;
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

public class editBarber extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String barberID, editTextUpdateSalary;
    FirebaseFirestore fStore;
    ProgressDialog pd;
    ImageButton btnImgEditSalary;

    public editBarber() {
        // Required empty public constructor
    }

    public editBarber(String barberID){
        this.barberID = barberID;
    }


    // TODO: Rename and change types and number of parameters
    public static editBarber newInstance(String param1, String param2) {
        editBarber fragment = new editBarber();
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
        View view = inflater.inflate(R.layout.fragment_edit_barber, container, false);
        fStore = FirebaseFirestore.getInstance();
        pd = new ProgressDialog(getActivity());
        pd.setCanceledOnTouchOutside(false);

        btnImgEditSalary = view.findViewById(R.id.btnImgEditSalary);
       btnImgEditSalary.setOnClickListener(this);

        TextView barberIDTextView = view.findViewById(R.id.viewTextBarberID);
        TextView barberNameTextView = view.findViewById(R.id.viewTextBarberName);
        TextView barberSalaryTextView = view.findViewById(R.id.viewTextBarberSalary);
        TextView barberContactTextView = view.findViewById(R.id.viewTextBarberContactNumber);
        TextView barberEmailTextView = view.findViewById(R.id.viewTextBarberEmail);

        DocumentReference db = fStore.collection("barber").document(barberID);

        db.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(getContext(), "Something error, Try again later!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (value.exists()){
                    barberIDTextView.setText(value.getString("barberID"));
                    barberNameTextView.setText(value.getString("barberName"));
                    barberSalaryTextView.setText(value.getString("barberSalary"));
                    barberContactTextView .setText(value.getString("barberContactNumber"));
                    barberEmailTextView.setText(value.getString("barberEmail"));
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
            case R.id.btnImgEditSalary:
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setTitle("Update Salary");
                LinearLayout layout = new LinearLayout(getContext());
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setPadding(10, 10, 10, 10);
                EditText editText = new EditText(getContext());

                editText.setHint("Enter Salary");
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                layout.addView(editText);
                alert.setView(layout);
                alert.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        editTextUpdateSalary = editText.getText().toString();
                        DocumentReference salaryRef = fStore.collection("barber").document(barberID);
                        salaryRef.update("barberSalary", editTextUpdateSalary).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(getContext(), "Success Update Salary", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(getContext(), "Something Error try again later", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                        dialogInterface.cancel();
                    }
                });
                alert.show();
                break;
        }
    }

    public void showSalaryChangeDialog(final String key){

    }
}