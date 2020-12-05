package com.example.aad2020_vvz_app_graldij_moimfeld.Utils;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aad2020_vvz_app_graldij_moimfeld.R;
import com.google.gson.Gson;

import java.util.ArrayList;


public class AppointmentRecyclerAdapterMainActivity extends RecyclerView.Adapter<AppointmentRecyclerAdapterMainActivity.AppointmentItemHolder>{
    //Attributes
    private ArrayList<Appointment> appointments;
    private ArrayList<Course> courses;
    private SharedPreferences sharedPreferences;
    //Constructor
    public AppointmentRecyclerAdapterMainActivity(ArrayList<Appointment> appointments, ArrayList<Course> courses, SharedPreferences sharedPreferences) {
        this.appointments = appointments;
        this.courses = courses;
        this.sharedPreferences = sharedPreferences;
    }


    @NonNull
    @Override
    public AppointmentRecyclerAdapterMainActivity.AppointmentItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycler_row_appointments_main_activity, parent, false);
        return new AppointmentItemHolder(view);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull AppointmentRecyclerAdapterMainActivity.AppointmentItemHolder holder, final int position) {
        // We set the texts and the image of our MenuItemHolder object
        holder.category.setText(appointments.get(position).category);
        holder.day.setText("Day: " + appointments.get(position).day);
        holder.time.setText("Time: " + appointments.get(position).getStartTime() + " - " + appointments.get(position).getEndTime());
        holder.place.setText("Place: " + appointments.get(position).place);
        holder.aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    appointments.get(position).selected = true;

                    //here the courses array gets saved, to the sharedPreference with the key "saved_courses"
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(courses);
                    editor.putString("saved_courses", json);
                    editor.apply();
                } else {
                    appointments.get(position).selected = false;
                    //here the courses array gets saved, to the sharedPreference with the key "saved_courses"
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(courses);
                    editor.putString("saved_courses", json);
                    editor.apply();
                }
            }
        });
        //important that this next line gets executed after the holder.aSwitch.setOnCheckedChangeListener.... or else the switched dont behave, and sometimes toggle back to the on state when scrolling fast
        holder.aSwitch.setChecked(appointments.get(position).selected);
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }


    static class AppointmentItemHolder extends RecyclerView.ViewHolder {
        TextView category, day, time, place;
        @SuppressLint("UseSwitchCompatOrMaterialCode")
        Switch aSwitch;
        LinearLayout linearLayoutAppointment;

        public AppointmentItemHolder(@NonNull View itemView) {
            super(itemView);
            // links the attributes with the recycler_row items
            category = itemView.findViewById(R.id.recycler_row_appointment_category_main_activity);
            day = itemView.findViewById(R.id.recycler_row_appointment_day_main_activity);
            time = itemView.findViewById(R.id.recycler_row_appointment_time_main_activity);
            place = itemView.findViewById(R.id.recycler_row_appointments_place_main_activity);
            aSwitch = itemView.findViewById(R.id.switch1_main_activity);
            linearLayoutAppointment = itemView.findViewById(R.id.recycler_row_appointment_linearlayout_main_activity);


        }

    }
}
