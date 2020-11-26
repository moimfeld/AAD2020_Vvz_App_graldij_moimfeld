package com.example.aad2020_vvz_app_graldij_moimfeld.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.aad2020_vvz_app_graldij_moimfeld.R;
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
        String test_string;
        if(saved_courses.size() != 0) {
            test_string = "";
            for(int i = 0; i < saved_courses.size(); i++) {
                Course test = saved_courses.get(i);
                test_string = test_string
                        + "Size of the lecture Arraylist: " + test.lectures.size()
                        + ", Name: " + test.name
                        + ", Day:  " + test.lectures.get(test.lectures.size()-1).day
                        + ", Periodicity:  " + test.lectures.get(test.lectures.size()-1).periodicity
                        + ", Dates: " + test.lectures.get(test.lectures.size()-1).dates.get(test.lectures.get(test.lectures.size()-1).dates.size()-1)
                        + ", start_time:  " + test.lectures.get(test.lectures.size()-1).getStartTime()
                        + ", end_time:  " + test.lectures.get(test.lectures.size()-1).getEndTime()
                        + ", Lecture Code: " + test.course_code
                        + ", Credits: " + test.ECTS + "\n";
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
 


