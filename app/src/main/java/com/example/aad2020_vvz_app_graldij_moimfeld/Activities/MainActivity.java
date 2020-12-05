package com.example.aad2020_vvz_app_graldij_moimfeld.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aad2020_vvz_app_graldij_moimfeld.R;
import com.example.aad2020_vvz_app_graldij_moimfeld.Utils.Appointment;
import com.example.aad2020_vvz_app_graldij_moimfeld.Utils.AppointmentRecyclerAdapterMainActivity;
import com.example.aad2020_vvz_app_graldij_moimfeld.Utils.AppointmentRecyclerAdapterMainActivityCollisions;
import com.example.aad2020_vvz_app_graldij_moimfeld.Utils.Collision;
import com.example.aad2020_vvz_app_graldij_moimfeld.Utils.Course;
import com.example.aad2020_vvz_app_graldij_moimfeld.Utils.CourseRecyclerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class MainActivity extends AppCompatActivity {

    //lecture array to save the lectures temporarily, there needs to be another saving method involving firebase or some local storing method
    public ArrayList<Course> saved_courses;
    public SharedPreferences sharedPreferences;
    public static ArrayList<Collision> collisions;
    public Button button_collisions;
    public TextView collisionTextView;



    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        overridePendingTransition(0, 0);


        //SharedPreference needs to be created in order to pass it to the recycler adapters
        sharedPreferences= getSharedPreferences("shared_preferences", MODE_PRIVATE);

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


        //Overview at the bottom
        totalCredits.setText("Total Credits: " + Integer.toString(totalCreditsNumber));
        RecyclerView recyclerView = findViewById(R.id.recyclerViewCourses);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        collisions = getCollisions(saved_courses);
        button_collisions = findViewById(R.id.button_collisions);
        collisionTextView = findViewById(R.id.collisions_main_activity);
        if(collisions.size() == 0){
            button_collisions.setVisibility(View.GONE);
            button_collisions.setWidth(0);
            collisionTextView.setText("no collisions found");
            collisionTextView.setWidth(100);
            collisionTextView.setTextColor(Color.parseColor("#4CAF50"));
        }
        else {
            collisionTextView.setText(Integer.toString(collisions.size()) + " collisions found");
            collisionTextView.setTextColor(Color.parseColor("#FF0000"));
            button_collisions.setTextColor(Color.parseColor("#FF0000"));
            button_collisions.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    loadCourses();
                    collisions = MainActivity.getCollisions(saved_courses);
                    ArrayList<Appointment> appointments = MainActivity.getCollisionAppointments(collisions);
                    showPopupWindow(appointments);
                }
            });
        }


        CourseRecyclerAdapter CourseRecyclerAdapter = new CourseRecyclerAdapter(saved_courses, this, totalCredits, actionBar, collisionTextView, button_collisions, sharedPreferences);
        recyclerView.setAdapter(CourseRecyclerAdapter);




        //Bottom navigation bar which can be used to switch from activity to activity. Here the data to be sent to other activities gets built and put into the intent
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.action_MainActivity);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_Timetable:
                        Intent Timetable = new Intent(MainActivity.this,Timetable.class);
                        Timetable.putExtra("saved_courses", CoursesToString(saved_courses));
                        startActivity(Timetable);
                        //This line makes a smooth transition and is used for all transitions in all the activities
                        overridePendingTransition(0, 0);
                        break;

                    case R.id.action_MainActivity:
                        break;

                    case R.id.action_Vvz:
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
    public void loadCourses() {
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



    public static ArrayList<Collision> getCollisions(ArrayList<Course> courses){
        ArrayList<Collision> helper= new ArrayList<>();
        ArrayList<Collision> collisions = new ArrayList<>();
        if (courses.size() != 0) {
            ArrayList<String> days = new ArrayList<>();
            days.add("Mon");
            days.add("Tue");
            days.add("Wed");
            days.add("Thu");
            days.add("Fri");
            days.add("Mon");
            for(String day : days){
                for(int i = 8; i <= 20; i++){
                    ArrayList<Appointment> collidingAppointments = new ArrayList<>();
                    Collision collision = new Collision(day, i, collidingAppointments);
                    helper.add(collision);
                }
            }

            for(Course course : courses){
                for(Collision collision : helper){
                    //Set<String> names = new HashSet<>();
                    for(Appointment appointment : course.getAllAppointments()){
                        for(int time : appointment.time){
                            if(time == collision.time && appointment.day.equals(collision.day) && appointment.selected){// && !names.contains(appointment.courseName)){
                                collision.collidingAppointments.add(appointment);
                                //names.add(appointment.courseName);
                            }
                        }
                    }
                }
            }

            for(Collision c : helper){
                Set<String> names = new HashSet<>();
                for(Appointment a : c.collidingAppointments){
                    names.add(a.courseName);
                }
                if(names.size() > 1){
                    collisions.add(c);
                }
            }
        }

        return collisions;
    }

    public static ArrayList<Appointment> getCollisionAppointments(ArrayList<Collision> collisions){
        ArrayList<Appointment> result = new ArrayList<>();
        for(int i = 0; i < collisions.size(); i++){
            Appointment recyclerTrick = new Appointment("recyclerTrick", collisions.get(i).day, collisions.get(i).time );
            result.add(recyclerTrick);
            result.addAll(collisions.get(i).collidingAppointments);
        }
        return result;
    }


    //this function generates the pop up window, with its recyclerview in it
    @SuppressLint("SetTextI18n")
    public void showPopupWindow(ArrayList<Appointment> appointments) {
        //Create a View object yourself through inflater
        @SuppressLint("InflateParams") View popUpView = LayoutInflater.from(this).inflate(R.layout.popup_main_actitvity, null);

        //Specify the length and width through constants
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;


        //Create a window with our parameters
        final PopupWindow popupWindow = new PopupWindow(popUpView, width, height, true);
        //hardcoded parameter, this is bad
        popupWindow.setHeight(1500);
        //Set the location of the window on the screen
        popupWindow.showAtLocation(popUpView, Gravity.CENTER, 0, 0);


        //Initialize the elements of our window, install the handler
        TextView title = popUpView.findViewById(R.id.textViewMainActivity);
        title.setText("Select your Appointments");


        RecyclerView appointmentRecyclerView = popUpView.findViewById(R.id.recycler_view_appointments_main_activity);

        appointmentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        AppointmentRecyclerAdapterMainActivityCollisions appointmentRecyclerAdapterMainActivityCollisions = new AppointmentRecyclerAdapterMainActivityCollisions(appointments, saved_courses, button_collisions, collisionTextView, sharedPreferences);

        appointmentRecyclerView.setAdapter(appointmentRecyclerAdapterMainActivityCollisions);



        //Handler for clicking on the inactive zone of the window
        popUpView.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                loadCourses();
                //Close the window when clicked
                popupWindow.dismiss();
                return true;
            }
        });
    }
}