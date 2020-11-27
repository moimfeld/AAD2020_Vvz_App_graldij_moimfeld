package com.example.aad2020_vvz_app_graldij_moimfeld.Utils;

import java.util.ArrayList;

public class Course {

    public String name;

    //the new attributes of the class course aren't yet implemented in its methods and constructors!!!!
    public ArrayList<Appointment> lectures; //course_code + V
    public ArrayList<Appointment> lecturesAndExercises; //course_code + G
    public ArrayList<Appointment> exercises; //course_code + U
    public ArrayList<Appointment> labs; //course_code + P

    public String course_code;
    public int ECTS;


    //default constructor needs to be updated for the new strucutre
    public Course(){
        this.name = null;
        this.lectures = new ArrayList<>();
        this.lecturesAndExercises = new ArrayList<>();
        this.exercises = new ArrayList<>();
        this.labs = new ArrayList<>();
        this.course_code = null;
        this.ECTS = -1;
    }

    public Course(String name,
                  ArrayList<Appointment> lectures,
                  ArrayList<Appointment> lecturesAndExercises,
                  ArrayList<Appointment> exercises,
                  ArrayList<Appointment> labs,
                  String course_code,
                  int ECTS){

        this.name = name;
        this.lectures = lectures;
        this.lecturesAndExercises = lecturesAndExercises;
        this.exercises = exercises;
        this.labs = labs;
        this.course_code = course_code;
        this.ECTS = ECTS;
    }

    //compares two Course objects by comparing its unambiguous course codes
    public boolean isEqual(Course compareCourse){
        return this.course_code.equals(compareCourse.course_code);
    }

    //isEmpty() returns true if the lecture is empty.
    public boolean isEmpty(){

        return this.name == null
                || (this.lectures.isEmpty() && this.lecturesAndExercises.isEmpty() && this.exercises.isEmpty() && this.labs.isEmpty())
                || this.course_code == null
                || this.ECTS == -1;
    }
}