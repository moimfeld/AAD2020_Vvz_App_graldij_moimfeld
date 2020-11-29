package com.example.aad2020_vvz_app_graldij_moimfeld.Utils;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
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
    public void onBindViewHolder(@NonNull AppointmentRecyclerApdapter.AppointmentItemHolder holder, int position) {
        // We set the texts and the image of our MenuItemHolder object
        holder.day.setText("Day: " +appointments.get(position).day);
        holder.start.setText("Start of the Lecture: " + Integer.toString(appointments.get(position).getStartTime()));
        holder.end.setText("End of the Lecture: " + Integer.toString(appointments.get(position).getEndTime()));
        holder.place.setText("Place: " + appointments.get(position).place);
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }


    static class AppointmentItemHolder extends RecyclerView.ViewHolder {
        TextView day, start, end, place;
        LinearLayout linearLayoutAppointment;

        public AppointmentItemHolder(@NonNull View itemView) {
            super(itemView);
            // links the attributes with the recycler_row items
            day = itemView.findViewById(R.id.recycler_row_appointment_day);
            start = itemView.findViewById(R.id.recycler_row_appointment_start);
            end = itemView.findViewById(R.id.recycler_row_appointment_end);
            place = itemView.findViewById(R.id.recycler_row_appointments_place);
            linearLayoutAppointment = itemView.findViewById(R.id.recycler_row_appointment_linearlayout);



        }

    }
}
