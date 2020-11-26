package com.example.aad2020_vvz_app_graldij_moimfeld.Utils;

import java.util.ArrayList;

public class Appointment {

    public String day;
    public ArrayList<Integer> time;
    public String periodicity;
    public ArrayList<String> dates;

    public Appointment(String day, ArrayList<Integer> time, String periodicity, ArrayList<String> dates){
        this.day = day;
        this.time = time;
        this.periodicity = periodicity;
        this.dates = dates;
    }

    public boolean isEmpty(){
        return this.day == null && this.time.isEmpty() && this.dates.isEmpty();
    }

    public int getStartTime(){
        return this.time.get(0);
    }

    public int getEndTime(){
        return this.time.get(time.size()-1);
    }
}
