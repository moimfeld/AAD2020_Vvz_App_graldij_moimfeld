package com.example.aad2020_vvz_app_graldij_moimfeld;

public class Lecture {
    String name;
    String day;
    String start_time;
    String end_time;
    String lecture_code;
    int ECTS;

    public Lecture(String name, String day, String start_time, String end_time, String lecture_code, int ECTS) {
        this.name = name;
        this.day = day;
        this.start_time = start_time;
        this.end_time = end_time;
        this.lecture_code = lecture_code;
        this.ECTS = ECTS;
    }

}



