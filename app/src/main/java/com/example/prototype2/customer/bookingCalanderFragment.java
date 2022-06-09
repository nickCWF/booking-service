package com.example.prototype2.customer;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.example.prototype2.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

public class bookingCalanderFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private NumberPicker numberPickHour, numberPickMin;
    public String bookingData;
    public String bookingHour, bookingMin;

    public bookingCalanderFragment() {

    }


    public static bookingCalanderFragment newInstance(String param1, String param2) {
        bookingCalanderFragment fragment = new bookingCalanderFragment();
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
        View view = inflater.inflate(R.layout.fragment_booking_calander, container, false);

        TextView tv =(TextView) view.findViewById(R.id.listBarberServiceText);

        final Calendar defaultDate = Calendar.getInstance();

        /* starts before 1 month from now */
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, 0);

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
                //
                .defaultSelectedDate(startDate)
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
//                bookingData = date.toString();
            }
        });


        numberPickHour = view.findViewById(R.id.numPickHour);
        numberPickMin = view.findViewById(R.id.numPickMin);

        numberPickHour.setMinValue(10);
        numberPickHour.setMaxValue(20);
        numberPickHour.setValue(10);

        numberPickMin.setMinValue(00);
        numberPickMin.setMaxValue(59);
        numberPickMin.setValue(0);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = df.format(System.currentTimeMillis());

        bookingData = currentDate;

//        bookingHour = String.valueOf(numberPickHour.getValue());
//        bookingMin = String.valueOf(numberPickMin.getValue());

        numberPickHour.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                bookingHour = String.valueOf(i1);
            }
        });

        numberPickMin.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                bookingMin = String.valueOf(i1);
            }
        });

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                Date d = date.getTime();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String strDate = dateFormat.format(d);
                bookingData = strDate;

//                bookingData = String.valueOf(date.getTime());
            }
        });

        ImageButton imgButtonPrevious = (ImageButton) view.findViewById(R.id.imgButtonPrevious);
        ImageButton imgButtonNext = (ImageButton) view.findViewById(R.id.imgButtonNext);

        imgButtonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.customer_container,new customerBookingFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        imgButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putString("bookingDate", bookingData);
                bundle.putString("bookingMin", bookingMin);
                bundle.putString("bookingHour", bookingHour);


                bookingBarberFragment fragment = new bookingBarberFragment();
                fragment.setArguments(bundle);

                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.customer_container,fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });



        /**Get the information checkbox from barber services**/
//        for(int i = 0; i < customerBarberServiceAdapter.mContentList.size(); i++){
//            if(customerBarberServiceAdapter.mContentList.get(i).isSelected()){
//                tv.setText(tv.getText()+": "+customerBarberServiceAdapter.mContentList.get(i).getBarberServiceName());
//            }
//        }


        return view;
    }


}