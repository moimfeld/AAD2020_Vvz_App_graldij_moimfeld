package com.example.aad2020_vvz_app_graldij_moimfeld;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Test to see if the parse class does its job
        Parse test_parse = new Parse();
        Lecture test = test_parse.parse("http://www.vvz.ethz.ch/Vorlesungsverzeichnis/lerneinheit.view?lang=en&semkez=2020W&ansicht=ALLE&lerneinheitId=140984&", this);






//        Document doc = new_parse.doInBackground("http://www.vvz.ethz.ch/Vorlesungsverzeichnis/lerneinheit.view?lang=en&semkez=2020W&ansicht=ALLE&lerneinheitId=140984&");


//        Toast.makeText(MainActivity.this, parsed_lecture.name, Toast.LENGTH_SHORT).show();
//        Toast.makeText(MainActivity.this, parsed_lecture.start_time, Toast.LENGTH_SHORT).show();
//        Toast.makeText(MainActivity.this, parsed_lecture.end_time, Toast.LENGTH_SHORT).show();
//        Toast.makeText(MainActivity.this, String.valueOf(parsed_lecture.ECTS), Toast.LENGTH_SHORT).show();
//        Toast.makeText(MainActivity.this, parsed_lecture.day, Toast.LENGTH_SHORT).show();
//        Toast.makeText(MainActivity.this, parsed_lecture.lecture_code, Toast.LENGTH_SHORT).show();

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



