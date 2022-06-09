package com.example.prototype2.customer;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prototype2.Login;
import com.example.prototype2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.concurrent.Executor;


public class customerProfileFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Button btnLogoutCustomer;
    ImageButton updateUsername, updatePassword, updateContactNumber, updateEmail;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference reference;
    private FirebaseFirestore fStore;
    String Email;
    int PICK_IMAGE_REQUEST = 111;
    Uri imageUri;
    ImageView imageView;
    StorageReference storageReference;
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    TextView viewTextCustomerName;
    TextView viewTextCustomerEmail;
    TextView viewTextCustomerContactNumber;
    TextView textViewCustomerPassword;


    String editTextUpdateUsername;

    ProgressDialog pd;

    private String userID;

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

        updateUsername = (ImageButton) view.findViewById(R.id.btnImgEditCustomerName);
        updateEmail = (ImageButton) view.findViewById(R.id.btnImgEditCustomerEmail);
        updateContactNumber = (ImageButton) view.findViewById(R.id.btnImgEditCustomerContactNumber);
        updatePassword = (ImageButton) view.findViewById(R.id.btnImgEditCustomerPassword);
        Button btnProfile = (Button) view.findViewById(R.id.btnProfile);

        btnProfile.setOnClickListener(this);

        pd = new ProgressDialog(getContext());


        btnLogoutCustomer = view.findViewById(R.id.btnLogoutCustomer);
        btnLogoutCustomer.setOnClickListener(this);
        updatePassword.setOnClickListener(this);
        updateContactNumber.setOnClickListener(this);
        updateEmail.setOnClickListener(this);
        updateUsername.setOnClickListener(this);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        fStore = FirebaseFirestore.getInstance();
        userID = user.getUid();

         viewTextCustomerName = (TextView) view.findViewById(R.id.viewTextCustomerName);
         viewTextCustomerEmail = (TextView) view.findViewById(R.id.viewTextCustomerEmail);
         viewTextCustomerContactNumber = (TextView) view.findViewById(R.id.viewTextCustomerContactNumber);
         textViewCustomerPassword = (TextView) view.findViewById(R.id.textViewCustomerPassword);

         imageView = (ImageView) view.findViewById(R.id.barberProfileImage);


        DocumentReference documentReference = fStore.collection("Users").document(userID);
        documentReference.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.getResult().exists()){

                            String fName = task.getResult().getString("Username");
                            Email = task.getResult().getString("Email");
                            String ContactNumber = task.getResult().getString("ContactNumber");

                            viewTextCustomerName.setText(fName);
                            viewTextCustomerEmail.setText(Email);
                            viewTextCustomerContactNumber.setText(ContactNumber);

                        }
                        else {
                            Intent intent = new Intent(getActivity(), Login.class);
                            startActivity(intent);
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
                Log.d("TAG", e.toString());
            }
        });

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
            case R.id.btnImgEditCustomerName:
                showNameChangeDialog();
                break;
            case R.id.btnImgEditCustomerEmail:
                showEmailChangeDialog();
                break;
            case R.id.btnImgEditCustomerContactNumber:
                showContactNumberChangeDialog();
                break;
            case R.id.btnImgEditCustomerPassword:
                showPasswordChangeDialog();
//                Dialog dialog = new Dialog(getActivity());
//                dialog.setContentView(R.layout.dialog_update_password);
//                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                dialog.setCancelable(true);
//                dialog.show();
                break;
            case  R.id.btnProfile:
                selectImage();
                break;

        }
    }

    private void showPasswordChangeDialog() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_update_password, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        TextView txtOldPassword =  view.findViewById(R.id.oldpasslog);
        TextView txtNewPassword = view.findViewById(R.id.newpasslog);
        TextView txtConPassword = view.findViewById(R.id.conformpasslog);
        Button btnUpdatePass = view.findViewById(R.id.updatepass);

        user = FirebaseAuth.getInstance().getCurrentUser();

        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.show();

        btnUpdatePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldPassword = txtOldPassword.getText().toString().trim();
                String newPassword = txtNewPassword.getText().toString().trim();
                String conPassword = txtConPassword.getText().toString().trim();
                if(!oldPassword.isEmpty() && !newPassword.isEmpty() && !conPassword.isEmpty()){
                    if(newPassword.equals(conPassword)){
                        AuthCredential credential = EmailAuthProvider
                                .getCredential(Email, oldPassword);

                        // Prompt the user to re-provide their sign-in credentials
                        user.reauthenticate(credential)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            // updatePassword
                                            user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        Toast.makeText(view.getContext(), "Success Update", Toast.LENGTH_SHORT).show();
                                                        dialog.cancel();
                                                    }
                                                }
                                            });
                                        }else {
                                            Toast.makeText(view.getContext(), "Old password not same", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }else {
                        Toast.makeText(view.getContext(), "Not Update", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(view.getContext(), "Please fill in all the information", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showNameChangeDialog(){
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle("Update Name");
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(10, 10, 10, 10);
        EditText editText = new EditText(getContext());
        editText.setHint("Enter Name");
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        layout.addView(editText);
        alert.setView(layout);
        alert.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String editTextName = editText.getText().toString();
                DocumentReference salaryRef = fStore.collection("Users").document(user.getUid());
                salaryRef.update("Username", editTextName).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getContext(), "Success Update Name", Toast.LENGTH_SHORT).show();
                            viewTextCustomerName.setText(editTextName);
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
    }

    private void showEmailChangeDialog(){
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle("Update Email");
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(10, 10, 10, 10);
        EditText editText = new EditText(getContext());
        editText.setHint("Enter Email");
        editText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        layout.addView(editText);
        alert.setView(layout);
        alert.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String editTextName = editText.getText().toString();
                DocumentReference salaryRef = fStore.collection("Users").document(user.getUid());
                salaryRef.update("Emai", editTextName).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getContext(), "Success Update Email", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(getContext(), "Something Error, Try again later", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                dialogInterface.cancel();
            }
        });
        alert.show();
    }

    private void showContactNumberChangeDialog(){
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle("Update Contact Number");
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(10, 10, 10, 10);
        EditText editText = new EditText(getContext());
        editText.setHint("Enter Contact Number");
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        layout.addView(editText);
        alert.setView(layout);
        alert.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String editTextContactNumber = editText.getText().toString();
                if (editTextContactNumber.length()<10){
                    editText.setError("Please provide valid contact number!");
                    editText.requestFocus();
                }
                DocumentReference salaryRef = fStore.collection("Users").document(user.getUid());
                salaryRef.update("ContactNumber", editTextContactNumber).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getContext(), "Success Update Contact Number", Toast.LENGTH_SHORT).show();
                            viewTextCustomerContactNumber.setText(editTextContactNumber);
                        }
                        else {
                            Toast.makeText(getContext(), "Something Error, Try again later", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                dialogInterface.cancel();
            }
        });
        alert.show();
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
                Toast.makeText(getActivity(),"Failed.", Toast.LENGTH_LONG).show();
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
