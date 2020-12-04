package com.example.aad2020_vvz_app_graldij_moimfeld.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aad2020_vvz_app_graldij_moimfeld.R;
import com.example.aad2020_vvz_app_graldij_moimfeld.Utils.Course;
import com.example.aad2020_vvz_app_graldij_moimfeld.Utils.CourseRecyclerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    //lecture array to save the lectures temporarily, there needs to be another saving method involving firebase or some local storing method
    public ArrayList<Course> saved_courses;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        overridePendingTransition(0, 0);

        //SharedPreference needs to be created in order to pass it to the recycler adapters
        SharedPreferences sharedPreferences= getSharedPreferences("shared_preferences", MODE_PRIVATE);

        //loadCourses loads whatever was saved last in the sharedPreferences with key "saved_courses
        loadCourses();

        //Set the status bar color to the ETH color
        getWindow().setStatusBarColor(Color.parseColor("#1F407A"));


        TextView actionBar = findViewById(R.id.action_bar_mainactivity);
        if(saved_courses.size() != 0){
            actionBar.setText("Course Drawer");
        }
        else{
            actionBar.setText("Course Drawer is empty");
        }
        TextView totalCredits = findViewById(R.id.main_activity_total_credits);
        int totalCreditsNumber = 0;
        for(Course c : saved_courses){
            totalCreditsNumber += c.ECTS;
        }

        totalCredits.setText(Integer.toString(totalCreditsNumber));
        RecyclerView recyclerView = findViewById(R.id.recyclerViewCourses);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        CourseRecyclerAdapter CourseRecyclerAdapter = new CourseRecyclerAdapter(saved_courses, this, totalCredits, actionBar, sharedPreferences);
        recyclerView.setAdapter(CourseRecyclerAdapter);





        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.action_MainActivity);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_Timetable:
                        //Toast.makeText(MainActivity.this, "Recents", Toast.LENGTH_SHORT).show();
                        Intent Timetable = new Intent(MainActivity.this,Timetable.class);
                        Timetable.putExtra("saved_courses", CoursesToString(saved_courses));
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
                        Vvz.putExtra("saved_courses", CoursesToString(saved_courses));
                        startActivity(Vvz);
                        overridePendingTransition(0, 0);
                        break;
                }
                return true;
            }
        });


    }


    //this method loads the saved_courses ArrayList, if there is one saved (if there is none, it creates a new ArrayList)
    private void loadCourses() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared_preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("saved_courses", null);
        Type type = new TypeToken<ArrayList<Course>>(){}.getType();
        saved_courses = gson.fromJson(json, type);
        if(saved_courses == null){
            saved_courses = new ArrayList<>();
        }
    }

    private String CoursesToString(ArrayList<Course> saved_courses){
        Gson gson = new Gson();
        return gson.toJson(saved_courses);
    }
}