package com.example.aad2020_vvz_app_graldij_moimfeld.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.aad2020_vvz_app_graldij_moimfeld.R;
import com.example.aad2020_vvz_app_graldij_moimfeld.Utils.Appointment;
import com.example.aad2020_vvz_app_graldij_moimfeld.Utils.Course;
import com.example.aad2020_vvz_app_graldij_moimfeld.Utils.DisplayLecture;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;


public class Timetable extends AppCompatActivity {

    //create an ArrayList where the saved courses can be stored
    private ArrayList<Course> saved_courses = new ArrayList<>();


    public boolean containsCell(ArrayList<DisplayLecture> used_ids, String celltofind){
        for (DisplayLecture time_slot : used_ids){
            if (time_slot.cell.equals(celltofind)){
                Toast.makeText(this, "true", Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return false;
    }
    public int cellInArrayListIndex(ArrayList<DisplayLecture> used_ids, String celltofind){
        for (DisplayLecture time_slot : used_ids){
            if (time_slot.cell==celltofind){
                return used_ids.indexOf(time_slot);
            }
        }
        return 0;
    }
    public int[] convertArrayListToArray(ArrayList<Integer> arrayList){
        int size=arrayList.size();
        int[] array = new int[size];
        for(int i=0; i<size; i++){
            array[i] = arrayList.get(i).intValue();
        }
        return array;
    }

    public String convertArrayListToString(ArrayList<String> arrayList){
        String outputstring = new String();
        for(String string_element : arrayList){
            outputstring +=string_element;
                    if(!(arrayList.indexOf(string_element) == (arrayList.size() -1))){ //check if not the last element in the Arraylist. If not: append " &"
                        outputstring+=" &";
                    }
        }
        return outputstring;
    }

    ArrayList<DisplayLecture> used_ids = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);

        //here the saved_courses ArrayList gets rebuilt from the MainActivity Intent
        Type type = new TypeToken<ArrayList<Course>>(){}.getType();
        Gson gson = new Gson();
        saved_courses = gson.fromJson(getIntent().getStringExtra("saved_courses"), type);
        if(saved_courses == null){
            saved_courses = new ArrayList<>();
        }



        getWindow().setStatusBarColor(Color.parseColor("#1F407A"));

        Stack<String> color_palette = new Stack<>();
        color_palette.addAll(Arrays.asList(
                "#B63131", "#EA80FC", "#8C9EFF", "#80D8FF",
                "#A7FFEB", "#CCFF90", "#FFFF8D", "#FFD180",
                "#1E88E5", "#FF8A80", "#43A047",  "#FF0000"));

        for(Course course : saved_courses){
            String name=course.name;
            String current_color;
            if(!color_palette.empty()){
                current_color = color_palette.pop();
            }
            else{
                Toast.makeText(this, "Too many lectures chosen. Max: 12", Toast.LENGTH_SHORT).show();
                current_color = "#dcdcdc";
            }

            for (Appointment appointment : course.getAllAppointments()){
                String day= appointment.day;

                if(appointment.selected==false)
                    continue;
                for (Integer time: appointment.time){
                    //directly get the string instead of the integer?
                    String cell = day+"_"+time.toString();
//                   cell="Mon_8";
                    int id = getResources().getIdentifier(cell, "id", getPackageName());

                     if(!containsCell(used_ids,cell)){
                            DisplayLecture new_cell_slot = new DisplayLecture(cell, Color.parseColor(current_color), name);
                            used_ids.add(new_cell_slot);

                            TextView text = findViewById(id);
                            text.setText(name);
                            text.setBackgroundColor(Color.parseColor(current_color));

                            Toast.makeText(this, "1"+name+cell+"--"+new_cell_slot.cell, Toast.LENGTH_SHORT).show();

                     }
                    else {
                         Toast.makeText(this, "2"+name+cell, Toast.LENGTH_SHORT).show();
                        used_ids.get(cellInArrayListIndex(used_ids, cell)).addColor(Color.parseColor(current_color));

                        TextView text = findViewById(id);

                         int[] colors = convertArrayListToArray(used_ids.get(cellInArrayListIndex(used_ids, cell)).colors_for_cell);
//                         GradientDrawable.Orientation orientation = new GradientDrawable.Orientation();
//                         orientation.valueOf("LEFT_RIGHT");
                         Drawable gradient= new GradientDrawable(GradientDrawable.Orientation.valueOf("LEFT_RIGHT"), colors);
                         text.setBackground(gradient);

                         used_ids.get(cellInArrayListIndex(used_ids, cell)).addName(name);

                        text.setText(convertArrayListToString(used_ids.get(cellInArrayListIndex(used_ids, cell)).names_for_cell));
//                         Toast.makeText(this, "2"+name+cell, Toast.LENGTH_SHORT).show();
                    }


                }
            }

        }



        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.action_Timetable);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_Timetable:
                        //Toast.makeText(Timetable.this, "Recents", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_MainActivity:
                        //Toast.makeText(Timetable.this, "Favorites", Toast.LENGTH_SHORT).show();
                        Intent MainActivity = new Intent(Timetable.this, MainActivity.class);
                        startActivity(MainActivity);
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.action_Vvz:
                        //Toast.makeText(Timetable.this, "Nearby", Toast.LENGTH_SHORT).show();
                        Intent Vvz = new Intent(Timetable.this, Vvz.class);
                        startActivity(Vvz);
                        overridePendingTransition(0, 0);
                        break;
                }
                return true;
            }


        });




    }

    public void tryClick(View view) {
        String id = view.getResources().getResourceName(view.getId());
        Toast.makeText(this, id, Toast.LENGTH_SHORT).show();
        //add banner with lecture informations. I think to use the used_ids, but there the references to the
        //lectures are missing. Maybe better to add those references to the DisplayLecture class in order to
        //have it in the used_ids? Or change approach and save all these information in the courses
        //and appointments (i.e. also colors, cell, ...)??
    }


}