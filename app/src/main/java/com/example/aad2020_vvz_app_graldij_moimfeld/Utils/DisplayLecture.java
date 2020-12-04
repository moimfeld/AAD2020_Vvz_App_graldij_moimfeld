package com.example.aad2020_vvz_app_graldij_moimfeld.Utils;

import java.util.ArrayList;

public class DisplayLecture {
    public String cell = new String();
    public ArrayList<Integer> colors_for_cell;
    public ArrayList<String> names_for_cell;

    public DisplayLecture (String input_cell, int input_color, String input_name){
        this.cell = input_cell;
        colors_for_cell = new ArrayList<>();
        colors_for_cell.add(input_color);
        names_for_cell = new ArrayList<>();
        names_for_cell.add(input_name);
    }

    public void addColor(int input_color){
        colors_for_cell.add(input_color);
    }
    public void addName(String input_name){
        names_for_cell.add(input_name);
    }


}