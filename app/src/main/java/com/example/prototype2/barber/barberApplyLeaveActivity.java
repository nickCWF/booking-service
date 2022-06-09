package com.example.prototype2.barber;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import com.example.prototype2.R;

import com.example.prototype2.customer.customerData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class barberApplyLeaveActivity extends AppCompatActivity {

    TextView dateRangeText;
    Button calender, applyLeave, selectPicture;
    ImageView imageApplyLeave;
    EditText textLeaveReason;
    int PICK_IMAGE_REQUEST = 111;
    NumberPicker leaveDate;
    ProgressBar progressBar;
    Uri imageUri;

    StorageReference storageReference;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String barberID = user.getUid();
    String formattedstrDate;
    String formattedendDate;
    String leaveID;

    Long startDate;
    Long endDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barber_apply_leave);

        dateRangeText = findViewById(R.id.textDateRange);
        calender = findViewById(R.id.btnApplyCalenderLeave);
        progressBar = findViewById(R.id.progressBar);
        applyLeave = findViewById(R.id.btnApplyLeave);
        selectPicture = findViewById(R.id.btnSelectPicture);
        imageApplyLeave = findViewById(R.id.imageApplyLeave);
        textLeaveReason = findViewById(R.id.editTextLeaveReason);



        selectPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });



        /**Show calender Selection**/
        CalendarConstraints.Builder constraintBuilder =new CalendarConstraints.Builder();
        constraintBuilder.setValidator(DateValidatorPointForward.now());

        MaterialDatePicker.Builder<Pair<Long, Long>> materialDateBuilder = MaterialDatePicker.Builder.dateRangePicker();

        materialDateBuilder.setTitleText("SELECT A DATE");
        materialDateBuilder.setCalendarConstraints(constraintBuilder.build());

        final MaterialDatePicker materialDatePicker = materialDateBuilder.build();

        calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                materialDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
                progressBar.setVisibility(View.GONE);
                materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
                    @Override
                    public void onPositiveButtonClick(Pair<Long,Long> selection) {
                        startDate = selection.first;
                        endDate = selection.second;
                        String dateRange = selection.toString();
                        Calendar calendarStr = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                        Calendar calendarEnd = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                        calendarStr.setTimeInMillis(startDate);
                        calendarEnd.setTimeInMillis(endDate);
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        Log.d("TAG", dateRange);
                        formattedstrDate  = format.format(calendarStr.getTime());
                        formattedendDate  = format.format(calendarEnd.getTime());
                        dateRangeText.setVisibility(View.VISIBLE);
                        dateRangeText.setText(formattedstrDate+" to "+ formattedendDate);
                    }
                });
            }
        });



        applyLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dataDate = dateRangeText.getText().toString().trim();
                String dataReason = textLeaveReason.getText().toString().trim();
                if (imageUri == null || dataDate.isEmpty() || dataReason.isEmpty())
                {
                    Toast.makeText(getApplication(),"Please Fill up all the information", Toast.LENGTH_LONG).show();
                }
                else {
                    uploadData();
                    uploadImage();
                    startActivity(new Intent(getApplicationContext(), listApplyLeaveActivity.class));
                }
            }
        });


    }

    private void uploadImage() {
        storageReference = FirebaseStorage.getInstance().getReference("imageApplyLeave/"+barberID+"/"+leaveID);
        storageReference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getApplication(),"Success Uploaded", Toast.LENGTH_LONG).show();
                    }
                });

    }

    private void uploadData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userID = user.getUid();
        DocumentReference applyLeave = db.collection("Leave").document();

        leaveID = applyLeave.getId();
        String barberName = customerData.getBarberName();
        String leaveReason = textLeaveReason.getText().toString().trim();
        Map<String, Object> data = new HashMap<>();
        data.put("barberName", barberName);
        data.put("barberID", userID);
        data.put("reason", leaveReason);
        data.put("leaveID", leaveID);
        data.put("status", "pending");
        data.put("DateStart", startDate);
        data.put("DateEnd", endDate);

        applyLeave.set(data)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Success apply leave", Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Not Success please try again", Toast.LENGTH_LONG).show();
                        }
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && data!= null && data.getData() != null){
            imageUri = data.getData();
            imageApplyLeave.setImageURI(imageUri);
        }
    }

    @Override
    public void onBackPressed(){
        startActivity(new Intent(getApplicationContext(), listApplyLeaveActivity.class));
        return;
    }
}