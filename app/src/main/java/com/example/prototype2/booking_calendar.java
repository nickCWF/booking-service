package com.example.prototype2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.NumberPicker;

import com.example.prototype2.R;

import java.util.Calendar;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;



public class booking_calendar extends AppCompatActivity {

    private NumberPicker numberPickHour, numberPickMin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_calendar);


        /* starts before 1 month from now */
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -1);

        /* ends after 1 month from now */
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 1);

        // on below line we are setting up our horizontal calendar view and passing id our calendar view to it.
        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(this, R.id.calendarView)
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

        numberPickHour = findViewById(R.id.numPickHour);
        numberPickMin = findViewById(R.id.numPickMin);

        numberPickHour.setMinValue(00);
        numberPickHour.setMaxValue(24);

        numberPickMin.setMinValue(00);
        numberPickMin.setMaxValue(59);
        
        

    }
}