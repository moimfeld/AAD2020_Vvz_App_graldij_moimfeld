package com.example.aad2020_vvz_app_graldij_moimfeld.Utils;

import java.util.ArrayList;

public class Course {

    public String name;

    //the new attributes of the class course aren't yet implemented in its methods and constructors!!!!
    public ArrayList<Appointment> lectures; //course_code + V and one lecture Appointment contains information about weekday, time and all dates of this appointment
    public ArrayList<Appointment> exercises; //course_code + U and one exercise Appointment contains information about weekday, time and all dates of this appointment
    public ArrayList<Appointment> labs; //course_code + P and one lab Appointment contains information about weekday, time and all dates of this appointment

    //the following three attributes will become obsolete when the lectures, exercises and labs arrays are implemented!!! (but before that they have to stay or else the code breaks
    public String day;
    public String start_time;
    public String end_time;

    public String course_code;
    public int ECTS;


    //default constructor needs to be updated for the new strucutre
    public Course(){
        this.name = null;
        this.day = null;
        this.start_time = null;
        this.end_time = null;
        this.course_code = null;
        this.ECTS = -1;
    }
    public Course(String name, ArrayList<Appointment> lectures, ArrayList<Appointment> exercises, ArrayList<Appointment> labs, String day, String start_time, String end_time, String course_code, int ECTS) {
        this.name = name;
        this.lectures = lectures;
        this.exercises = exercises;
        this.labs = labs;
        this.day = day;
        this.start_time = start_time;
        this.end_time = end_time;
        this.course_code = course_code;
        this.ECTS = ECTS;
    }

    //isEqual method needs to be updated for the new structure
    //is it really necessary to compare all attributes, since the lecture code should be unambiguous
    public boolean isEqual(Course compareCourse){
        return this.name.equals(compareCourse.name) && this.day.equals(compareCourse.day) && this.start_time.equals(compareCourse.start_time) && this.end_time.equals(compareCourse.end_time) && this.course_code.equals(compareCourse.course_code) && this.ECTS == compareCourse.ECTS;
    }

    //isEmpty method needs to be updated for the new structure
//    isEmpty() returns true if the lecture is empty.
    public boolean isEmpty(){
        //Toast messages to give a feedback if the lecture couldn't fully get parsed
        return this.name == null || this.day == null || this.start_time == null || this.end_time == null || this.course_code == null || this.ECTS == -1;
    }
}