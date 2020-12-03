package com.example.aad2020_vvz_app_graldij_moimfeld.Utils;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aad2020_vvz_app_graldij_moimfeld.Activities.MainActivity;
import com.example.aad2020_vvz_app_graldij_moimfeld.R;
import java.util.ArrayList;



public class CourseRecyclerAdapter extends RecyclerView.Adapter<CourseRecyclerAdapter.CourseItemHolder> {

    //Attributes
    private final ArrayList<Course> courses;
    private final Context context;
    //totalCredits is needed such that the amount of credits can be can be changed when a lecture gets deleted
    private final TextView totalCredits;
    private final TextView actionBar;

    //Constructor
    public CourseRecyclerAdapter(ArrayList<Course> courses, Context context, TextView totalCredits, TextView actionBar) {
        this.courses = courses;
        this.context = context;
        this.totalCredits = totalCredits;
        this.actionBar = actionBar;
    }


    @NonNull
    @Override
    public CourseItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycler_row_courses, parent, false);
        return new CourseItemHolder(view);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final CourseItemHolder holder, final int position) {
        // We set the texts and the image of our MenuItemHolder object
        holder.name.setText(courses.get(position).name);
        holder.credits.setText(courses.get(position).ECTS + " credits");
        holder.button_appointments.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
              showPopupWindow(position);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            public void onClick(View v) {
                MainActivity.saved_courses.remove(position);
                //this line is to update the recyclerview. without it the recyclerview crashes
                notifyItemRemoved(position);
                int totalCreditsNumber = 0;
                for(Course c : MainActivity.saved_courses){
                    totalCreditsNumber += c.ECTS;
                }
                totalCredits.setText(Integer.toString(totalCreditsNumber));

                //here i set the Action bar to the empty state, when the last lecture got deleted
                if(MainActivity.saved_courses.size() != 0){
                    actionBar.setText("Course Drawer");
                }
                else{
                    actionBar.setText("Course Drawer is empty");
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return courses.size();
    }


    static class CourseItemHolder extends RecyclerView.ViewHolder {
        TextView name, credits;
        LinearLayout linearLayoutCourse1;
        Button button_appointments, delete;

        public CourseItemHolder(@NonNull View itemView) {
            super(itemView);
            // links the attributes with the recycler_row items
            name = itemView.findViewById(R.id.recycler_row_Course_name);
            credits = itemView.findViewById(R.id.recycler_row_credits);
            linearLayoutCourse1 = itemView.findViewById(R.id.linearLayoutCourse1);
            button_appointments = itemView.findViewById(R.id.button_appointments);
            delete = itemView.findViewById(R.id.button_delete);
        }

    }



    //this function generates the pop up window, with its recyclerview in it
    @SuppressLint("SetTextI18n")
    public void showPopupWindow(int position) {

        //Create a View object yourself through inflater
        @SuppressLint("InflateParams") View popUpView = LayoutInflater.from(context).inflate(R.layout.popup, null);

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
        TextView title = popUpView.findViewById(R.id.textView);
        title.setText("Select your Appointments");


        RecyclerView appointmentRecyclerView = popUpView.findViewById(R.id.recycler_view_appointments);

        appointmentRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        AppointmentRecyclerApdapter appointmentRecyclerApdapter = new AppointmentRecyclerApdapter(courses.get(position).getAllAppointments());

        appointmentRecyclerView.setAdapter(appointmentRecyclerApdapter);



        //Handler for clicking on the inactive zone of the window
        popUpView.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                //Close the window when clicked
                popupWindow.dismiss();
                return true;
            }
        });
    }



}

