package com.example.aad2020_vvz_app_graldij_moimfeld;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.alamkanak.weekview.WeekView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Timetable extends AppCompatActivity {





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);



        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.action_Timetable);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_Timetable:
                        //Toast.makeText(Timetable.this, "Recents", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_MainActivity:
                        //Toast.makeText(Timetable.this, "Favorites", Toast.LENGTH_SHORT).show();
                        Intent MainActivity = new Intent(Timetable.this, MainActivity.class);
                        startActivity(MainActivity);
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.action_Vvz:
                        //Toast.makeText(Timetable.this, "Nearby", Toast.LENGTH_SHORT).show();
                        Intent Vvz = new Intent(Timetable.this, Vvz.class);
                        startActivity(Vvz);
                        overridePendingTransition(0, 0);
                        break;
                }
                return true;
            }
        });

        // Get a reference for the week view in the layout.
//        WeekView myWeekView = (WeekView) findViewById(R.id.weekView);

        // Set an action when any event is clicked.
//        myWeekView.setOnEventClickListener(mEventClickListener);

        // The week view has infinite scrolling horizontally. We have to provide the events of a
        // month every time the month changes on the week view.
//        myWeekView.setMonthChangeListener(mMonthChangeListener);

        // Set long press listener for events.
//        myWeekView.setEventLongPressListener(mEventLongPressListener);


    }
}