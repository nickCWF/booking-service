package com.example.prototype2.barber;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prototype2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link barberScheduleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class barberScheduleFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String bookingDate;

    RecyclerView recyclerView;
    barberServiceWorkingAdapter workAdapter;
    private Context workContext;
    private Activity workActivity;
    private ArrayList<barberWorkingData> workArrayList;



    public barberScheduleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment barberScheduleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static barberScheduleFragment newInstance(String param1, String param2) {
        barberScheduleFragment fragment = new barberScheduleFragment();
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
        View view = inflater.inflate(R.layout.fragment_barber_schedule, container, false);

        TextView txtTodayDate = (TextView) view.findViewById(R.id.textViewTodayDate);
        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        /**Set Up recycle view**/
        workActivity = getActivity();
        workContext = getContext();
        FirebaseApp.initializeApp(getContext());
        recyclerView = view.findViewById(R.id.barberWorkDetails_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setNestedScrollingEnabled(false);
        workArrayList = new ArrayList<barberWorkingData>();



        /* starts before 1 month from now */
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -1);

        /* ends after 1 month from now */
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 1);

        // on below line we are setting up our horizontal calendar view and passing id our calendar view to it.
        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(view, R.id.calendarView)
                // on below line we are adding a range
                // as start date and end date to our calendar.
                .range(startDate, endDate)
                // on below line we are providing a number of dates
                // which will be visible on the screen at a time.
                .datesNumberOnScreen(5)
                // at last we are calling a build method
                // to build our horizontal recycler view.
                .build();
        // on below line we are setting calendar listener to our calendar view.
        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                // on below line we are printing date
                // in the logcat which is selected.
                Log.e("TAG", "CURRENT DATE IS " + date);
            }
        });

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = df.format(System.currentTimeMillis());

        bookingDate = currentDate;


        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {

                Date d = date.getTime();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String strDate = dateFormat.format(d);
                bookingDate = strDate;
                txtTodayDate.setText(bookingDate);


                progressBar.setVisibility(view.VISIBLE);

                /**Get the barber Working Information**/
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String barberid = user.getUid();
                db.collection("Booking")
                        .whereEqualTo("barberID", barberid).whereEqualTo("bookDate", strDate).whereGreaterThanOrEqualTo("schedule", 0).orderBy("schedule")
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        workArrayList.clear();
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                /**barberName, String barberServiceName,String bookTime, ArrayList barberServiceNameList,String status**/
                                Log.d("TA", document.getId() + " => " + document.getData());
                                barberWorkingData workingData = new barberWorkingData(
                                        document.getData().get("barberName").toString(),
                                        document.getData().get("barberServiceName").toString(),
                                        document.getData().get("bookTime").toString(),
                                        (ArrayList<String>) document.get("barberServiceNameList"),
                                        document.getData().get("status").toString(), document.getData().get("customerName").toString(), document.getData().get("bookID").toString());
                                workArrayList.add(workingData);
                            }
                            workAdapter = new barberServiceWorkingAdapter(workContext, workActivity, (ArrayList<barberWorkingData>) workArrayList);
                            recyclerView.setAdapter(workAdapter);
                            workAdapter.notifyDataSetChanged();
                            progressBar.setVisibility(view.GONE);
                        }else {
                            progressBar.setVisibility(view.GONE);
                        }
                    }
                });



            }
        });


        return view;
    }

    public void getBarberWorkingDetails(String getDate){


    }
}