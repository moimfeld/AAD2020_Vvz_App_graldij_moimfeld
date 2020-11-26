package com.example.aad2020_vvz_app_graldij_moimfeld.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.aad2020_vvz_app_graldij_moimfeld.R;
import com.example.aad2020_vvz_app_graldij_moimfeld.Utils.Appointment;
import com.example.aad2020_vvz_app_graldij_moimfeld.Utils.Course;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {


    //lecture array to save the lectures temporarily, there needs to be another saving method involving firebase or some local storing method
    public static ArrayList<Course> saved_courses = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Test to see if the parse class does its job
        StringBuilder test_string = new StringBuilder();
        if(saved_courses.size() != 0) {
            for(int i = 0; i < saved_courses.size(); i++) {
                Course test = saved_courses.get(i);
                test_string.append("Course ").append(i + 1).append("\n");
                test_string.append("Name: ").append(test.name).append("\n");
                test_string.append("Lecture Code: ").append(test.course_code).append("\n");
                test_string.append("Credits: ").append(test.ECTS).append("\n").append("\n");

//                test_string.append(test.lectures.get(0).dates.size());
                int j = 0;
                    while(j < test.lectures.size()){
                        Appointment current = test.lectures.get(j);
                        test_string.append("Lecture Appointment: ").append(j+1).append(": \n");
                        test_string.append("Day: ").append(current.day).append("\n");
                        test_string.append("Start: ").append(current.getStartTime()).append("\n");
                        test_string.append("End: ").append(current.getEndTime()).append("\n");
                        test_string.append("Dates: ").append(current.dates.get(0)).append("\n");
                        j++;
                    }
                    j = 0;
                    test_string.append("\n");
                    while(j < test.lecturesAndExercises.size()){
                        Appointment current = test.lecturesAndExercises.get(j);
                        test_string.append("Lecture And Exercise Appointment: ").append(j+1).append(": \n");
                        test_string.append("Day: ").append(current.day).append("\n");
                        test_string.append("Start: ").append(current.getStartTime()).append("\n");
                        test_string.append("End: ").append(current.getEndTime()).append("\n");
                        test_string.append("Dates: ").append(current.dates.get(0)).append("\n");
                        j++;
                }
                    j = 0;
                    test_string.append("\n");
                    while(j < test.exercises.size()){
                        Appointment current = test.exercises.get(j);
                        test_string.append("Exercise Appointment: ").append(j+1).append(": \n");
                        test_string.append("Day: ").append(current.day).append("\n");
                        test_string.append("Start: ").append(current.getStartTime()).append("\n");
                        test_string.append("End: ").append(current.getEndTime()).append("\n");
                        test_string.append("Dates: ").append(current.dates.get(0)).append("\n");
                        j++;
                }
                    j = 0;
                    test_string.append("\n");
                    while(j < test.labs.size()){
                        Appointment current = test.labs.get(j);
                        test_string.append("Lab Appointment: ").append(j+1).append(": \n");
                        test_string.append("Day: ").append(current.day).append("\n");
                        test_string.append("Start: ").append(current.getStartTime()).append("\n");
                        test_string.append("End: ").append(current.getEndTime()).append("\n");
                        test_string.append("Dates: ").append(current.dates.get(0)).append("\n");
                        j++;
                }
                TextView tv = findViewById(R.id.textView3);
                tv.setText(test_string);
            }
        }


        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.action_MainActivity);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_Timetable:
                        //Toast.makeText(MainActivity.this, "Recents", Toast.LENGTH_SHORT).show();
                        Intent Timetable = new Intent(MainActivity.this,Timetable.class);
                        startActivity(Timetable);

                        //This line makes a smooth transition and is used for all transitions in all the activities
                        overridePendingTransition(0, 0);

                        break;
                    case R.id.action_MainActivity:
                        //Toast.makeText(MainActivity.this, "Favorites", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_Vvz:
                        //Toast.makeText(MainActivity.this, "Nearby", Toast.LENGTH_SHORT).show();
                        Intent Vvz = new Intent(MainActivity.this, Vvz.class);
                        startActivity(Vvz);
                        overridePendingTransition(0, 0);
                        break;
                }
                return true;
            }
        });

    }

}
 


