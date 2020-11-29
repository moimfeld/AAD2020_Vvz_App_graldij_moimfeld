package com.example.aad2020_vvz_app_graldij_moimfeld.Utils;


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
import com.example.aad2020_vvz_app_graldij_moimfeld.R;
import java.util.ArrayList;



public class CourseRecyclerAdapter extends RecyclerView.Adapter<CourseRecyclerAdapter.CourseItemHolder> {

    //Attributes
    private ArrayList<Course> courses;
    private Context context;

    //Constructor
    public CourseRecyclerAdapter(ArrayList<Course> courses, Context context) {
        this.courses = courses;
        this.context = context;
    }


    @NonNull
    @Override
    public CourseItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycler_row_courses, parent, false);
        return new CourseItemHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final CourseItemHolder holder, final int position) {
        // We set the texts and the image of our MenuItemHolder object
        holder.name.setText(courses.get(position).name);
        holder.credits.setText(courses.get(position).ECTS + " credits");
        holder.button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
              showPopupWindow(position);
            }
        });
    }


    @Override
    public int getItemCount() {
        return courses.size();
    }


    class CourseItemHolder extends RecyclerView.ViewHolder {
        TextView name, credits;
        LinearLayout linearLayoutCourse1;
        Button button;

        public CourseItemHolder(@NonNull View itemView) {
            super(itemView);
            // links the attributes with the recycler_row items
            name = itemView.findViewById(R.id.recycler_row_Course_name);
            credits = itemView.findViewById(R.id.recycler_row_credits);
            linearLayoutCourse1 = itemView.findViewById(R.id.linearLayoutCourse1);
            button = itemView.findViewById(R.id.button_appointments);
        }

    }



    //this function generates the pop up window, with its recyclerview in it
    public void showPopupWindow(int position) {

        //Create a View object yourself through inflater
        View popUpView = LayoutInflater.from(context).inflate(R.layout.popup, null);

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


        //example of how to use buttons in popupWindow!!!
//        Button buttonEdit = popupView.findViewById(R.id.messageButton);
//        buttonEdit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                //As an example, display the message
//                Toast.makeText(view.getContext(), "Wow, popup action button", Toast.LENGTH_SHORT).show();
//
//            }
//        });



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

