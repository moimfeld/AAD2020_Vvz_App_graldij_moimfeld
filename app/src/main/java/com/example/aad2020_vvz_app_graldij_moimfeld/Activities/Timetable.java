package com.example.aad2020_vvz_app_graldij_moimfeld.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aad2020_vvz_app_graldij_moimfeld.R;
import com.example.aad2020_vvz_app_graldij_moimfeld.Utils.Appointment;
import com.example.aad2020_vvz_app_graldij_moimfeld.Utils.AppointmentRecyclerAdapterTimetable;
import com.example.aad2020_vvz_app_graldij_moimfeld.Utils.Course;
import com.example.aad2020_vvz_app_graldij_moimfeld.Utils.DisplayLecture;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;


public class Timetable extends AppCompatActivity {

    //create an ArrayList where the saved courses can be stored
    private ArrayList<Course> saved_courses = new ArrayList<>();

    public boolean containsCell(ArrayList<DisplayLecture> used_ids, String celltofind){
        for (DisplayLecture time_slot : used_ids){
            if (time_slot.cell.equals(celltofind)){
//                Toast.makeText(this, "true", Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return false;
    }


    public int cellInArrayListIndex(ArrayList<DisplayLecture> used_ids, String celltofind){
        for (DisplayLecture time_slot : used_ids){
            if (time_slot.cell.equals(celltofind)){
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

    public String convertSetToString(Set<String> set){
        String outputstring = new String();
        Iterator<String> itr = set.iterator();
        while(itr.hasNext()){

            outputstring += itr.next();
                    if(itr.hasNext()){ //check if not the last element in the Arraylist. If not: append " &"
                        outputstring+="&\n";
                    }
        }
        return outputstring;
    }

    public Set<String> convertArrayListToSet(ArrayList<String> arrayList){
        Set<String> result = new HashSet<String>();
        for(String string_element : arrayList){
            result.add(string_element);
        }
        return result;
    }

    ArrayList<DisplayLecture> used_ids = new ArrayList<>();
    ArrayList<DisplayLecture> not_used_ids = new ArrayList<>();
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
//        color_palette.addAll(Arrays.asList(
//                "#43A047",  "#FF0000", "#8C9EFF", "#80D8FF",
//                "#A7FFEB", "#CCFF90", "#FFFF8D", "#FFD180",
//                "#1E88E5", "#FF8A80", "#B63131", "#EA80FC"));

        color_palette.addAll(Arrays.asList(
                "#72791C", //ETH 4 green
                "#956013", //ETH 9 brown
                "#007A96", //ETH 8 green-blue
                "#A8322D", //ETH 7 red
                "#6F6F6F", //ETH 6 grey
                "#91056A", //ETH 5 pink
                "#1269B0", //ETH 3 blue
                "#485A2C"  //ETH 2 green
        ));

        for(Course course : saved_courses){
            String current_color;
            if(!color_palette.empty()){
                current_color = color_palette.pop();
            }
            else{
                Toast.makeText(this, "Too many lectures chosen. Max: 12", Toast.LENGTH_SHORT).show();
                current_color = "#dcdcdc";
            }

            for (Appointment appointment : course.getAllAppointments()){
                String name;

                switch(appointment.category){
                    case "Lecture":
                        name="V - "+appointment.courseName;
                        break;

                    case "Lecture with Exercise":
                        name="G - "+appointment.courseName;
                        break;

                    case "Exercise":
                        name="U - "+appointment.courseName;
                        break;

                    case "Lab":
                        name="P - "+appointment.courseName;
                        break;

                    default:
                        name=appointment.courseName;
                        break;
                }





                String day= appointment.day;

                if(day.equals("not during the semester")) {
                    continue;
                }
                for (Integer time: appointment.time){
                    String cell = day+"_"+time.toString();

                    if(appointment.selected==false){
                        if(!containsCell(not_used_ids,cell)){
                            DisplayLecture new_cell_slot = new DisplayLecture(cell, Color.parseColor(current_color), name, appointment);
                            not_used_ids.add(new_cell_slot);
                        }
                        else{
                            not_used_ids.get(cellInArrayListIndex(not_used_ids, cell)).addColor(Color.parseColor(current_color));
                            not_used_ids.get(cellInArrayListIndex(not_used_ids, cell)).addName(name);
                            not_used_ids.get(cellInArrayListIndex(not_used_ids, cell)).addAppointment(appointment);
                        }
                    }
                    else {
                        int id = getResources().getIdentifier(cell, "id", getPackageName());

                        if (!containsCell(used_ids, cell)) {
                            DisplayLecture new_cell_slot = new DisplayLecture(cell, Color.parseColor(current_color), name, appointment);
                            used_ids.add(new_cell_slot);

                            TextView text = findViewById(id);
                            text.setText(name);
                            text.setBackgroundColor(Color.parseColor(current_color));

                            //                            Toast.makeText(this, "1"+name+cell+"--"+new_cell_slot.cell, Toast.LENGTH_SHORT).show();
                        } else {
                            //                         Toast.makeText(this, "2"+name+cell, Toast.LENGTH_SHORT).show();
                            used_ids.get(cellInArrayListIndex(used_ids, cell)).addColor(Color.parseColor(current_color));

                            TextView text = findViewById(id);

                            int[] colors = convertArrayListToArray(used_ids.get(cellInArrayListIndex(used_ids, cell)).colors_for_cell);
                            //                         GradientDrawable.Orientation orientation = new GradientDrawable.Orientation();
                            //                         orientation.valueOf("LEFT_RIGHT");
                            Drawable gradient = new GradientDrawable(GradientDrawable.Orientation.valueOf("LEFT_RIGHT"), colors);
                            text.setBackground(gradient);

                            used_ids.get(cellInArrayListIndex(used_ids, cell)).addName(name);

                            text.setText(convertSetToString((convertArrayListToSet(used_ids.get(cellInArrayListIndex(used_ids, cell)).names_for_cell))));

                            //                         Toast.makeText(this, "2"+name+cell, Toast.LENGTH_SHORT).show();


                            used_ids.get(cellInArrayListIndex(used_ids, cell)).addAppointment(appointment);
                        }
                    }

                }
            }

        }


        //Bottom navigation bar which can be used to switch from activity to activity. Here the data to be sent to other activities gets built and put into the intent
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.action_Timetable);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_Timetable:
                        break;

                    case R.id.action_MainActivity:
                        Intent MainActivity = new Intent(Timetable.this, MainActivity.class);
                        startActivity(MainActivity);
                        overridePendingTransition(0, 0);
                        break;

                    case R.id.action_Vvz:
                        Intent Vvz = new Intent(Timetable.this, Vvz.class);
                        Gson gson = new Gson();
                        Vvz.putExtra("saved_courses", gson.toJson(saved_courses));
                        startActivity(Vvz);
                        overridePendingTransition(0, 0);
                        break;
                }
                return true;
            }


        });




    }

    public void tryClick(View view) {
        TextView clicked_textview = (TextView) view;
        int clicked_id_int = clicked_textview.getId();
        String clicked_int_string = getResources().getResourceEntryName(clicked_id_int);
//        Toast.makeText(this, clicked_int_string, Toast.LENGTH_SHORT).show();
        if(containsCell(used_ids , clicked_int_string)){
//            Toast.makeText(this, "contained", Toast.LENGTH_SHORT).show();
            int index_of_clicked_element_selected = cellInArrayListIndex(used_ids, clicked_int_string);
            DisplayLecture infos_clicked_element_selected = used_ids.get(index_of_clicked_element_selected);

            if(containsCell(not_used_ids , clicked_int_string)){
                int index_of_clicked_element_NOT_selected = cellInArrayListIndex(not_used_ids, clicked_int_string);
                DisplayLecture infos_clicked_element_NOT_selected = not_used_ids.get(index_of_clicked_element_NOT_selected);
                infos_clicked_element_selected.appointments_for_cell.addAll(infos_clicked_element_NOT_selected.appointments_for_cell);
            }

//            Toast.makeText(this, Integer.toString(infos_clicked_element.appointments_for_cell.size()), Toast.LENGTH_SHORT).show();
            showPopupWindow(infos_clicked_element_selected.appointments_for_cell);
        }

//        Toast.makeText(this, "NOT contained", Toast.LENGTH_SHORT).show();
        //add banner with lecture information. I think to use the used_ids, but there the references to the
        //lectures are missing. Maybe better to add those references to the DisplayLecture class in order to
        //have it in the used_ids? Or change approach and save all these information in the courses
        //and appointments (i.e. also colors, cell, ...)??
    }


    //"this function generates the pop up window, with its recyclerview in it
    @SuppressLint("SetTextI18n")
    public void showPopupWindow(ArrayList<Appointment> appointments) {

        //initialize the sharedPreferences to save the changes
        SharedPreferences sharedPreferences = getSharedPreferences("shared_preferences", MODE_PRIVATE);

        //Create a View object yourself through inflater
        @SuppressLint("InflateParams") View popUpView = LayoutInflater.from(this).inflate(R.layout.popup_timetable, null);

        //Specify the length and width through constants
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;

        //Make Inactive Items Outside Of PopupWindow
        boolean focusable = true;

        //Create a window with our parameters
        final PopupWindow popupWindow = new PopupWindow(popUpView, width, height, focusable);
        //hardcoded parameter, this is bad
        popupWindow.setHeight(1500);
        //Set the location of the window on the screen
        popupWindow.showAtLocation(popUpView, Gravity.CENTER, 0, 0);


        //Initialize the elements of our window, install the handler
        TextView title = popUpView.findViewById(R.id.textViewTimetable);
        title.setText("Select your Appointments");


        RecyclerView appointmentRecyclerView = popUpView.findViewById(R.id.recycler_view_appointments_timetable);

        appointmentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        AppointmentRecyclerAdapterTimetable appointmentRecyclerAdapterTimetable = new AppointmentRecyclerAdapterTimetable(appointments, saved_courses, sharedPreferences);

        appointmentRecyclerView.setAdapter(appointmentRecyclerAdapterTimetable);



        //Handler for clicking on the inactive zone of the window
        popUpView.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Intent Timetable = new Intent(Timetable.this,Timetable.class);
                Gson gson = new Gson();
                String json = gson.toJson(saved_courses);
                Timetable.putExtra("saved_courses", json);
                startActivity(Timetable);
                overridePendingTransition(0, 0);

                //Close the window when clicked
                popupWindow.dismiss();
                return true;
            }
        });
    }



}