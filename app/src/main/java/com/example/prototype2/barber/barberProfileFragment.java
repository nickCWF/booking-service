package com.example.prototype2.barber;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prototype2.Login;
import com.example.prototype2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


public class barberProfileFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Button btnlogoutBarber;
    private FirebaseUser user;
    private DatabaseReference reference;
    private FirebaseFirestore fStore;
    private String userID;
    int PICK_IMAGE_REQUEST = 111;
    Uri imageUri;
    ImageView imageView;
    StorageReference storageReference;
    FirebaseAuth fAuth = FirebaseAuth.getInstance();



    public barberProfileFragment() {

    }


    public static barberProfileFragment newInstance(String param1, String param2) {
        barberProfileFragment fragment = new barberProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_barber_profile, container, false);

        btnlogoutBarber = view.findViewById(R.id.btnlogoutBarber);
        btnlogoutBarber.setOnClickListener(this);



        Button btnApplyLeave = view.findViewById(R.id.btnApplyLeave);
        btnApplyLeave.setOnClickListener(this);

        Button btnChangeProfile = (Button) view.findViewById(R.id.btnChangeProfile);
        btnChangeProfile.setOnClickListener(this);

        imageView = view.findViewById(R.id.imageView);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        fStore = FirebaseFirestore.getInstance();
        userID = user.getUid();

        final TextView viewTextBarberID = (TextView) view.findViewById(R.id.viewTextBarberID);
        final TextView viewTextBarberName = (TextView) view.findViewById(R.id.viewTextBarberName);
        final TextView viewTextBarberEmail = (TextView) view.findViewById(R.id.viewTextBarberEmail);
        final TextView viewTextBarberContactNumber = (TextView) view.findViewById(R.id.viewTextBarberContactNumber);
        final TextView viewTextBarberSalary = (TextView) view.findViewById(R.id.viewTextBarberSalary);



        CollectionReference baberRef = fStore.collection("barber");
        Query query = baberRef.whereEqualTo("barberUID", userID);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                 for (QueryDocumentSnapshot document : task.getResult()){
                     String bName = document.getString("barberName");

                     String Email = document.getString("barberEmail");
                     String ContactNumber = document.getString("barberContactNumber");
                     String salary = document.getString("barberSalary");

                     viewTextBarberID.setText("");
                     viewTextBarberName.setText(bName);
                     viewTextBarberEmail.setText(Email);
                     viewTextBarberContactNumber.setText(ContactNumber);
                     viewTextBarberSalary.setText(salary);
                 }

                }
                else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });


        StorageReference profileRef = FirebaseStorage.getInstance().getReference("users/"+fAuth.getCurrentUser().getUid()+"/profile");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(imageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(),"No profile picture", Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnlogoutBarber:
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(getContext(), "Success Logout", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), Login.class));
                break;
            case R.id.btnApplyLeave:
                startActivity(new Intent(getActivity(), listApplyLeaveActivity.class));
                break;
            case R.id.btnChangeProfile:
                selectImage();

                break;
        }
    }

    private void uploadImage() {
        final StorageReference fileRef = FirebaseStorage.getInstance().getReference("users/"+fAuth.getCurrentUser().getUid()+"/profile");
        fileRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getActivity(),"Success Uploaded", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG", e.toString());
//                Toast.makeText(getActivity(),"Failed.", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void selectImage(){
        Intent intent = new Intent();
        intent.setType("image/");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && data!= null && data.getData() != null){
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
            uploadImage();
        }
    }



}