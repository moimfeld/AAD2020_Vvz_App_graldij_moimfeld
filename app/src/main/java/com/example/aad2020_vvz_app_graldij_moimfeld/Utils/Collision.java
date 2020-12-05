package com.example.aad2020_vvz_app_graldij_moimfeld.Utils;

import java.util.ArrayList;

public class Collision {

    public String day;
    public int time;
    public ArrayList<Appointment> collidingAppointments;

    public Collision(String day, int time, ArrayList<Appointment> collidingAppointments){
        this.day = day;
        this.time = time;
        this.collidingAppointments = collidingAppointments;
    }
}
