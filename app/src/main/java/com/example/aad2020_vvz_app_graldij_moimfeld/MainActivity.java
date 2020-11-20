package com.example.aad2020_vvz_app_graldij_moimfeld;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Test to see if the parse class does its job
        Parse test_parse = new Parse();
        Lecture test = test_parse.parse("http://www.vvz.ethz.ch/Vorlesungsverzeichnis/lerneinheit.view?semkez=2020W&ansicht=KATALOGDATEN&lerneinheitId=140176&lang=de", this);
        String test_string = "Name: " +test.name + ", Day:  " + test.day + ", Start Time: " + test.start_time + ", End Time: " + test.end_time + ", Lecture Code: " + test.lecture_code + ", Credits: " + test.ECTS;
        TextView tv = (TextView)findViewById(R.id.textView3);
        tv.setText(test_string);



        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.action_MainActivity);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_Timetable:
                        //Toast.makeText(MainActivity.this, "Recents", Toast.LENGTH_SHORT).show();
                        Intent Timetable = new Intent(MainActivity.this,Timetable.class);
                        startActivity(Timetable);

                        //This line makes a smooth transition and is used for all transitions in all the activities
                        overridePendingTransition(0, 0);

                        break;
                    case R.id.action_MainActivity:
                        //Toast.makeText(MainActivity.this, "Favorites", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_Vvz:
                        //Toast.makeText(MainActivity.this, "Nearby", Toast.LENGTH_SHORT).show();
                        Intent Vvz = new Intent(MainActivity.this, Vvz.class);
                        startActivity(Vvz);
                        overridePendingTransition(0, 0);
                        break;
                }
                return true;
            }
        });

    }

}
 


