package com.example.prototype2.owner;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prototype2.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;



public class ReportFragment extends Fragment {
    String formattedstrDate;
    String formattedendDate;

    Long startDate;
    Long endDate;

    private RadioGroup radioGroup;

    /**Graph setting**/
    private static final int MAX_X_VALUE = 7;
    private static final int MAX_Y_VALUE = 50;
    private static final int MIN_Y_VALUE = 5;
    private static final String SET_LABEL = "App Downloads";
    private static final String[] DAYS = { "SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT" };
    ArrayList arrayList;

    private BarChart chart;

    public ReportFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_report, container, false);

        Button btnCalenderReport = view.findViewById(R.id.btnCalenderReport);
        ProgressBar progressBar = view.findViewById(R.id.progressBar);
        TextView dateRangeText = view.findViewById(R.id.textViewDateRange);
        /**Graph**/
        chart = view.findViewById(R.id.barChart);
        /**Radio button**/
        radioGroup = view.findViewById(R.id.radioGroup);

        /**Show calender Selection**/
        CalendarConstraints.Builder constraintBuilder =new CalendarConstraints.Builder();
        constraintBuilder.setValidator(DateValidatorPointForward.now());

        MaterialDatePicker.Builder<Pair<Long, Long>> materialDateBuilder = MaterialDatePicker.Builder.dateRangePicker();

        materialDateBuilder.setTitleText("SELECT A DATE");
        materialDateBuilder.setCalendarConstraints(constraintBuilder.build());

        final MaterialDatePicker materialDatePicker = materialDateBuilder.build();

        btnCalenderReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                materialDatePicker.show(getFragmentManager(), "MATERIAL_DATE_PICKER");
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

        /**Listen to Radio button**/

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.radioButtonSales:
                        Toast.makeText(getActivity(),"Please Fill up all the information", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.radioButtonPerformance:
                        Toast.makeText(getActivity(),"Pl", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });


        /**Get Date**/
        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        DocumentReference performRef = db.collection("Performances").document("2022-06-09");
        CollectionReference performRef = db.collection("Performances/2022-06-09/QWbzWdPdqxS5TqLczZVBQ15rsOv1");

//        performRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    if(document.exists()){
//                        Log.d("TAG", "DocumentSnapshot data: " + document.getData());
//                    }
//
//                }
//            }
//        });

        performRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("TA", document.getId() + " => " + document.getData());}
                }else {

                }
            }
        });

        /**Graph**/

        getData();
        BarDataSet barDataSet = new BarDataSet(arrayList, "Days");
        BarData barData = new BarData(barDataSet);

        chart.setData(barData);

        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        barDataSet.setValueTextColor(Color.BLACK);

        barDataSet.setValueTextSize(16f);

        chart.getDescription().setEnabled(false);

        XAxis xAxis = chart.getXAxis();

        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return DAYS[(int)value];
            }
        });

        return view;
    }

    private void getData(){
        arrayList = new ArrayList();
        arrayList.add(new BarEntry(2f,10));
        arrayList.add(new BarEntry(3f,20));
        arrayList.add(new BarEntry(4f,30));
        arrayList.add(new BarEntry(5f,40));
        arrayList.add(new BarEntry(6f,50));
    }

}