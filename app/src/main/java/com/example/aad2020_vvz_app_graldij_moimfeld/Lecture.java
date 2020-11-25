package com.example.aad2020_vvz_app_graldij_moimfeld;

import android.widget.Toast;

public class Lecture {
    public String name;
    public String day;
    public String start_time;
    public String end_time;
    public String lecture_code;
    public int ECTS;

    public Lecture (){
        this.name = null;
        this.day = null;
        this.start_time = null;
        this.end_time = null;
        this.lecture_code = null;
        this.ECTS = -1;
    }
    public Lecture(String name, String day, String start_time, String end_time, String lecture_code, int ECTS) {
        this.name = name;
        this.day = day;
        this.start_time = start_time;
        this.end_time = end_time;
        this.lecture_code = lecture_code;
        this.ECTS = ECTS;
    }

    public boolean isEqual(Lecture compareLecture){
        return this.name.equals(compareLecture.name) && this.day.equals(compareLecture.day) && this.start_time.equals(compareLecture.start_time) && this.end_time.equals(compareLecture.end_time) && this.lecture_code.equals(compareLecture.lecture_code) && this.ECTS == compareLecture.ECTS;
    }

//    isEmpty() returns true if the lecture is empty.
    public boolean isEmpty(){
        //Toast messages to give a feedback if the lecture couldn't fully get parsed
        return this.name == null || this.day == null || this.start_time == null || this.end_time == null || this.lecture_code == null || this.ECTS == -1;
    }
}



