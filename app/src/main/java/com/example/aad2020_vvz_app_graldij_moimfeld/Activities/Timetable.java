package com.example.aad2020_vvz_app_graldij_moimfeld.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.alamkanak.weekview.WeekView;
import com.example.aad2020_vvz_app_graldij_moimfeld.R;
import com.example.aad2020_vvz_app_graldij_moimfeld.Utils.Appointment;
import com.example.aad2020_vvz_app_graldij_moimfeld.Utils.Course;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Set;

public class Timetable extends AppCompatActivity {


    ArrayList<String> used_ids = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);


        for(Course course : MainActivity.saved_courses){
            String name=course.name;
            for (Appointment appointment : course.getAllAppointments()){
//                random color
                String day= appointment.day;

                //choose only if the course_unit is selected
                for (int time: appointment.time){
                    String cell = day+"_"+time;
                    if(!used_ids.contains(cell)){
                        used_ids.add(cell);
                        cell="Mon_8";
                        int id = getResources().getIdentifier(cell, "id", getPackageName());
                        TextView text = findViewById(id);
                        text.setText(name);
                        text.setHeight(800);
                        //set background with random color, mixed if more lectures
                    }


                }
            }

        }




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




    }
}