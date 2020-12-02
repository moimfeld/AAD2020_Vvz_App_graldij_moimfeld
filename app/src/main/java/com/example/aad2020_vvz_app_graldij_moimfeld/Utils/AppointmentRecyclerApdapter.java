package com.example.aad2020_vvz_app_graldij_moimfeld.Utils;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aad2020_vvz_app_graldij_moimfeld.Activities.MainActivity;
import com.example.aad2020_vvz_app_graldij_moimfeld.R;
import java.util.ArrayList;


public class AppointmentRecyclerApdapter extends RecyclerView.Adapter<AppointmentRecyclerApdapter.AppointmentItemHolder>{
    //Attributes
    private ArrayList<Appointment> appointments;
    //Constructor
    public AppointmentRecyclerApdapter(ArrayList<Appointment> appointments) {
        this.appointments = appointments;
    }


    @NonNull
    @Override
    public AppointmentRecyclerApdapter.AppointmentItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycler_row_appointments, parent, false);
        return new AppointmentItemHolder(view);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull AppointmentRecyclerApdapter.AppointmentItemHolder holder, final int position) {
        // We set the texts and the image of our MenuItemHolder object
        holder.category.setText(appointments.get(position).category);
        holder.day.setText("Day: " + appointments.get(position).day);
        holder.time.setText("Time: " + Integer.toString(appointments.get(position).getStartTime()) + " - " + Integer.toString(appointments.get(position).getEndTime()));
        holder.place.setText("Place: " + appointments.get(position).place);
        holder.aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    appointments.get(position).selected = true;
                } else {
                    appointments.get(position).selected = false;
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
        Switch aSwitch;
        LinearLayout linearLayoutAppointment;

        public AppointmentItemHolder(@NonNull View itemView) {
            super(itemView);
            // links the attributes with the recycler_row items
            category = itemView.findViewById(R.id.recycler_row_appointment_category);
            day = itemView.findViewById(R.id.recycler_row_appointment_day);
            time = itemView.findViewById(R.id.recycler_row_appointment_time);
            place = itemView.findViewById(R.id.recycler_row_appointments_place);
            aSwitch = itemView.findViewById(R.id.switch1);
            linearLayoutAppointment = itemView.findViewById(R.id.recycler_row_appointment_linearlayout);



        }

    }
}
