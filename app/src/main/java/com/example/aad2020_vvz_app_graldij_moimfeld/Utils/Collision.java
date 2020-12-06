package com.example.aad2020_vvz_app_graldij_moimfeld.Utils;

import java.util.ArrayList;

public class Collision {

    public String day;
    public String daylong;
    public int time;
    public ArrayList<Appointment> collidingAppointments;

    public Collision(String day, int time, ArrayList<Appointment> collidingAppointments){
        this.day = day;
        this.time = time;
        this.collidingAppointments = collidingAppointments;
        switch(day){
            case("Mon"):
                this.daylong = "Monday";
                break;
            case("Tue"):
                this.daylong = "Tuesday";
                break;
            case("Wed"):
                this.daylong = "Wednesday";
                break;
            case("Thu"):
                this.daylong = "Thursday";
                break;
            case("Fri"):
                this.daylong = "Friday";
                break;
            default:
                this.daylong ="no day";
                break;
        }
    }
}
