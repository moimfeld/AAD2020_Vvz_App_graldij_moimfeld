package com.example.aad2020_vvz_app_graldij_moimfeld.Utils;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aad2020_vvz_app_graldij_moimfeld.Activities.MainActivity;
import com.example.aad2020_vvz_app_graldij_moimfeld.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;



public class CourseRecyclerAdapter extends RecyclerView.Adapter<CourseRecyclerAdapter.CourseItemHolder> {

    //Attributes
    private ArrayList<Course> courses;
    private final Context context;
    //totalCredits is needed such that the amount of credits can be can be changed when a lecture gets deleted
    private final TextView totalCredits;
    private final TextView actionBar;
    private final TextView collision;
    private final Button button;
    private final  SharedPreferences sharedPreferences;

    //Constructor
    public CourseRecyclerAdapter(ArrayList<Course> courses, Context context, TextView totalCredits, TextView actionBar, TextView collision, Button button, SharedPreferences sharedPreferences) {
        this.courses = courses;
        this.context = context;
        this.totalCredits = totalCredits;
        this.collision = collision;
        this.button = button;
        this.actionBar = actionBar;
        this.sharedPreferences = sharedPreferences;
    }


    @NonNull
    @Override
    public CourseItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycler_row_courses, parent, false);
        return new CourseItemHolder(view);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final CourseItemHolder holder, final int position) {
        // We set the texts and the image of our MenuItemHolder object
        holder.name.setText(courses.get(position).name);
        holder.credits.setText(courses.get(position).ECTS + " credits");
        if (courses.get(position).getAllAppointments().size() != 0) {
            holder.button_appointments.setVisibility(View.VISIBLE);
            holder.button_appointments.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    showPopupWindow(position);
                }

            });
        }
        else{
            holder.button_appointments.setVisibility(View.GONE);
        }
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            public void onClick(View v) {

                courses.remove(position);
                //this line is to update the recyclerview. without it the recyclerview crashes
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,courses.size());

                //here the courses array gets saved, to the sharedPreference with the key "saved_courses"
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Gson gson = new Gson();
                String json = gson.toJson(courses);
                editor.putString("saved_courses", json);
                editor.apply();

                int totalCreditsNumber = 0;
                for (Course c : courses) {
                    totalCreditsNumber += c.ECTS;
                }
                totalCredits.setText("Total Credits: " + Integer.toString(totalCreditsNumber));

                //here i set the Action bar to the empty state, when the last lecture got deleted
                if (courses.size() != 0) {
                    actionBar.setText("Course Drawer");
                } else {
                    actionBar.setText("Course Drawer is empty");
                }
                MainActivity.collisions = MainActivity.getCollisions(courses);
                if(MainActivity.collisions.size() == 0){
                    button.setVisibility(View.GONE);
                    button.setWidth(0);
                    collision.setText("no conflicts found");
                    collision.setWidth(100);
                    collision.setTextColor(Color.parseColor("#4CAF50"));
                }

                else {
                    button.setVisibility(View.VISIBLE);
                    collision.setText(Integer.toString(MainActivity.collisions.size()) + " conflicts found");
                    collision.setTextColor(Color.parseColor("#FF0000"));
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return courses.size();
    }


    static class CourseItemHolder extends RecyclerView.ViewHolder {
        TextView name, credits;
        LinearLayout linearLayoutCourse1;
        Button button_appointments, delete;

        public CourseItemHolder(@NonNull View itemView) {
            super(itemView);
            // links the attributes with the recycler_row items
            name = itemView.findViewById(R.id.recycler_row_Course_name);
            credits = itemView.findViewById(R.id.recycler_row_credits);
            linearLayoutCourse1 = itemView.findViewById(R.id.linearLayoutCourse1);
            button_appointments = itemView.findViewById(R.id.button_appointments);
            delete = itemView.findViewById(R.id.button_delete);
        }
    }



    //this function generates the pop up window, with its recyclerview in it
    @SuppressLint("SetTextI18n")
    public void showPopupWindow(int position) {
        Gson gson = new Gson();
        String json = sharedPreferences.getString("saved_courses", null);
        Type type = new TypeToken<ArrayList<Course>>(){}.getType();
        courses = new ArrayList<>();
        courses = gson.fromJson(json, type);
        if(courses == null){
            courses = new ArrayList<>();
        }

        //Create a View object yourself through inflater
        @SuppressLint("InflateParams") View popUpView = LayoutInflater.from(context).inflate(R.layout.popup_main_actitvity, null);

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

        appointmentRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        AppointmentRecyclerAdapterMainActivity appointmentRecyclerApdapterMainActivity = new AppointmentRecyclerAdapterMainActivity(courses.get(position).getAllAppointments(), courses, button, collision, sharedPreferences);

        appointmentRecyclerView.setAdapter(appointmentRecyclerApdapterMainActivity);

        //in order for the "show collisions" button to work one the MainActivity gets restarted as soon as the popup gets dismissed.
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                Intent Timetable = new Intent(context,MainActivity.class);
                Gson gson = new Gson();
                String json = gson.toJson(courses);
                Timetable.putExtra("saved_courses", json);
                context.startActivity(Timetable);
            }
        });


        //Handler for clicking on the inactive zone of the window
        popUpView.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                //Close the window when clicked
                popupWindow.dismiss();
                return true;
            }
        });
    }



}

