package com.example.aad2020_vvz_app_graldij_moimfeld.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.ColorUtils;
import com.example.aad2020_vvz_app_graldij_moimfeld.R;
import com.example.aad2020_vvz_app_graldij_moimfeld.Utils.Appointment;
import com.example.aad2020_vvz_app_graldij_moimfeld.Utils.Course;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;


public class Timetable extends AppCompatActivity {


    ArrayList<String> used_ids = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);

        getWindow().setStatusBarColor(Color.parseColor("#1F407A"));

        Stack<String> color_palette = new Stack<>();
        color_palette.addAll(Arrays.asList(
                "#FF8A80", "#EA80FC", "#8C9EFF", "#80D8FF",
                "#A7FFEB", "#CCFF90", "#FFFF8D", "#FFD180",
                "#1E88E5", "#B63131", "#43A047",  "#FF0000"));

        for(Course course : MainActivity.saved_courses){
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
                     if(!used_ids.contains(cell)){
                            used_ids.add(cell);
                            TextView text = findViewById(id);
                            text.setText(name);
                            text.setBackgroundColor(Color.parseColor(current_color));
                            Toast.makeText(this, "1"+name+cell, Toast.LENGTH_SHORT).show();
                        //set background with random color, mixed if more lectures
                        }
                    else {
                        TextView text = findViewById(id);
                        int old_color;
                        Drawable background = text.getBackground();
                            old_color= ((ColorDrawable) background).getColor();
                            String old_name=text.getText().toString();

                        int new_color = Color.parseColor(current_color);
//                        int blendedColor = ColorUtils.blendARGB(old_color, new_color, 0.5F);
//                        text.setBackgroundColor(blendedColor);
                         int[] colors = new int[]{old_color, new_color};
//                         GradientDrawable.Orientation orientation = new GradientDrawable.Orientation();
//                         orientation.valueOf("LEFT_RIGHT");
                         Drawable gradient= new GradientDrawable(GradientDrawable.Orientation.valueOf("LEFT_RIGHT"), colors);
                         text.setBackground(gradient);
                        text.setText(old_name+"&"+name);
                         Toast.makeText(this, "2"+name+cell, Toast.LENGTH_SHORT).show();
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
}