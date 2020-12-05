package com.example.aad2020_vvz_app_graldij_moimfeld.Utils;

import java.util.ArrayList;

public class Appointment {

    public String courseName;
    public String category;
    public String day;
    public int year;
    public ArrayList<Integer> time;
    public String periodicity;
    public ArrayList<String> dates;
    public String place;
    public boolean selected;

    public Appointment(String courseName, String category, String day, int year, ArrayList<Integer> time, String periodicity, ArrayList<String> dates, String place){
        this.courseName = courseName;
        this.category = category;
        this.day = day;
        this.year = year;
        this.time = time;
        this.periodicity = periodicity;
        this.dates = dates;
        this.place = place;
        this.selected = true;
    }

    public int getStartTime(){
        return this.time.get(0);
    }

    public int getEndTime(){
        return this.time.get(this.time.size()-1)+1;
    }



}
