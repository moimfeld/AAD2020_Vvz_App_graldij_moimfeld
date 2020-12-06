package com.example.aad2020_vvz_app_graldij_moimfeld.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
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


    //helper function returning true if the celltofind is already stored in the ArrayList
    public boolean containsCell(ArrayList<DisplayLecture> used_ids, String celltofind){
        for (DisplayLecture time_slot : used_ids){
            if (time_slot.cell.equals(celltofind)){
//                Toast.makeText(this, "true", Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return false;
    }

    //helper function returning the index of the ArrayList where the celltofind string is stored. If
    //no cell is present returns 0. Should always be used only if the above function containsCell returned true.
    public int cellInArrayListIndex(ArrayList<DisplayLecture> used_ids, String celltofind){
        for (DisplayLecture time_slot : used_ids){
            if (time_slot.cell.equals(celltofind)){
                return used_ids.indexOf(time_slot);
            }
        }
        return 0;
    }

    //helper function converting ArrayList of Integers to an array of int. Resulting array of int is returned
    public int[] convertArrayListToArray(ArrayList<Integer> arrayList){
        int size=arrayList.size();
        int[] array = new int[size];
        for(int i=0; i<size; i++){
            array[i] = arrayList.get(i);
        }
        return array;
    }
    //helper function to convert ArrayList to a Set (in order to exploit sets' features). Used here to
    // avoid same exercises displayed twice, together with the function convertSetToString
    public Set<String> convertArrayListToSet(ArrayList<String> arrayList){
        return new HashSet<>(arrayList);
    }

    //helper function converting a set of strings to a string, appending all elements separated with " &\n".
    //See also function convertArrayListToSet
    public String convertSetToString(Set<String> set){
        String outputstring = "";
        Iterator<String> itr = set.iterator();
        while(itr.hasNext()){

            outputstring += itr.next();
            if(itr.hasNext()){ //check if not the last element in the Arraylist. If not: append " &"
                outputstring+=" & ";
            }
        }
        return outputstring;
    }


    //create an ArrayList where the saved courses can be stored
    private ArrayList<Course> saved_courses = new ArrayList<>();

    //ArrayList with objects DisplayLecture that are being selected
    public ArrayList<DisplayLecture> used_ids = new ArrayList<>();

    //ArrayList with objects DisplayLecture that are being selected
    public ArrayList<DisplayLecture> not_used_ids = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);

        used_ids.clear();
        not_used_ids.clear();




        //here the saved_courses ArrayList gets rebuilt from the MainActivity Intent
        Type type = new TypeToken<ArrayList<Course>>(){}.getType();
        Gson gson = new Gson();
        saved_courses = gson.fromJson(getIntent().getStringExtra("saved_courses"), type);
        if(saved_courses == null){
            saved_courses = new ArrayList<>();
        }



        getWindow().setStatusBarColor(Color.parseColor("#1F407A"));

        //Stack of Strings where the 14 colors for the timetable are stored.
        Stack<String> color_palette = new Stack<>();
        color_palette.addAll(Arrays.asList(
                "#6f9fd8", //"little boy blue" (last color displayed)
                "#343148", //eclipse
                "#f5489e", //pink
                "#EFC050", //mimosa
                "#385C9B", //ETH 1 blue
                "#72791C", //ETH 4 green
                "#FF6F61", //coral
                "#956013", //ETH 9 brown
                "#6F6F6F", //ETH 6 grey
                "#007A96", //ETH 8 green-blue
                "#d9193c", //ETH 7 red - modified
                "#91056A", //ETH 5 pink
                "#1269B0", //ETH 3 blue
                "#485A2C"  //ETH 2 green (first color displayed)
        ));
        //iterate over courses
        for(Course course : saved_courses){

            //pop a color from the stack and assign it to a course unit (only if stack is not empty)
            String current_color;
            if(!color_palette.empty()){
                current_color = color_palette.pop();
            }
            else{
                Toast.makeText(this, "Too many lectures chosen. Max: 14", Toast.LENGTH_SHORT).show();
                current_color = "#000000";
            }

            //iterate over appointments
            for (Appointment appointment : course.getAllAppointments()){
                //stored the name of the course unit
                String name;
                //store the course unit with the letter representing the category of the course
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

                //stored the days of the appointments
                String day= appointment.day;

                //if course is not during the semester, should not be displayed in the timetable
                if(day.equals("not during the semester")) {
                    continue;
                }

                //iterate over times of the appointmen
                for (Integer time: appointment.time){
                    //create cell id linked to the .xml file
                    String cell = day+"_"+time.toString();
                    //if lecture not selected
                    if(!appointment.selected){
                        //check if cell_id already saved in the ArrayList of the not-chosen. Accordingly save or add the information
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
                    //if lecture is selected
                    else {
                        //convert from cell string to id int.
                        int id = getResources().getIdentifier(cell, "id", getPackageName());
                        //if new cell
                        if (!containsCell(used_ids, cell)) {
                            //create new DisplayLecture and add it to the used_ids
                            DisplayLecture new_cell_slot = new DisplayLecture(cell, Color.parseColor(current_color), name, appointment);
                            used_ids.add(new_cell_slot);
                            //find right cell with id, and display name with background color
                            TextView text = findViewById(id);
                            text.setText(name);
                            text.setBackgroundColor(Color.parseColor(current_color));
                        }
                        //if cell already occupied
                        else {
                            //save the current color in the right position
                            used_ids.get(cellInArrayListIndex(used_ids, cell)).addColor(Color.parseColor(current_color));

                            TextView text = findViewById(id);
                            //create the int array with the colors present in that cell. Will be used for the gradient
                            int[] colors = convertArrayListToArray(used_ids.get(cellInArrayListIndex(used_ids, cell)).colors_for_cell);
                            //with the int array of the colors, create the drawable with faded color (gradient), with fading from left to right
                            Drawable gradient = new GradientDrawable(GradientDrawable.Orientation.valueOf("LEFT_RIGHT"), colors);
                            //set the drawable as a background
                            text.setBackground(gradient);
                            //add name to to the used_ids ArrayList for that specific cell of the table
                            used_ids.get(cellInArrayListIndex(used_ids, cell)).addName(name);
                            //set the text of the cell, using the names saved present in that cell.
                            //In order to avoid having twice the same name, use the helper functions
                            //to convert ArrayList to Set (eliminating the duplicates), and then convert
                            //the Set to the String in order to set it as a text
                            text.setText(convertSetToString((convertArrayListToSet(used_ids.get(cellInArrayListIndex(used_ids, cell)).names_for_cell))));
                            //in case of multiple appointments at the same cell, save also the appointment in the
                            //used_ids, in order to use it later for the click&popup
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
    //function called if table cell is clicked
    public void tryClick(View view) {
        //cast view to textview
        TextView clicked_textview = (TextView) view;
        //get int id of clicked textview
        int clicked_id_int = clicked_textview.getId();
        //convert int id to string id
        String clicked_int_string = getResources().getResourceEntryName(clicked_id_int);
        //check if clicked cell has already a displayed lecture
        if(containsCell(used_ids , clicked_int_string)){
            //get index of the clicked cell in the used_ids
            int index_of_clicked_element_selected = cellInArrayListIndex(used_ids, clicked_int_string);
            //get the element of the clicked cell in the used_ids, i.e. all lectures selected that take place
            //during that cell
            DisplayLecture infos_clicked_element_selected = used_ids.get(index_of_clicked_element_selected);
            //check if some non-selected lectures also would take place in that cell
            if(containsCell(not_used_ids , clicked_int_string)){
                //if yes, find index of that cell in the not_used_ids
                int index_of_clicked_element_NOT_selected = cellInArrayListIndex(not_used_ids, clicked_int_string);
                //get the element of the clicked cell in the not_used_ids, i.e. all lectures NOT selected
                //that would take place during that cell
                DisplayLecture infos_clicked_element_NOT_selected = not_used_ids.get(index_of_clicked_element_NOT_selected);
                //append the appointments of the not-selected courses taking place at that time to the
                //selected appointments also taking place at that time
                infos_clicked_element_selected.appointments_for_cell.addAll(infos_clicked_element_NOT_selected.appointments_for_cell);
            }
            //show a popup window with all the courses taking place at the clicked time&day (both selected
            //and non-selected courses)
            showPopupWindow(infos_clicked_element_selected.appointments_for_cell, this);
        }
    }


    //"this function generates the pop up window, with its recyclerview in it
    @SuppressLint("SetTextI18n")
    public void showPopupWindow(ArrayList<Appointment> appointments, final Context context) {

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



        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
//                Toast.makeText(context, "this code got executed", Toast.LENGTH_SHORT).show();
                Intent Timetable = new Intent(Timetable.this,Timetable.class);
                Gson gson = new Gson();
                String json = gson.toJson(saved_courses);
                Timetable.putExtra("saved_courses", json);
                startActivity(Timetable);
                overridePendingTransition(0, 0);
            }
        });

        //Handler for clicking on the inactive zone of the window
        popUpView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                //Close the window when clicked
                popupWindow.dismiss();
                return true;
            }
        });
    }



}