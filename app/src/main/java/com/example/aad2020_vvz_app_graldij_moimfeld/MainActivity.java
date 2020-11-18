package com.example.aad2020_vvz_app_graldij_moimfeld;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity {
//    Parse new_parse = new Parse();
//    Document doc = new_parse.doInBackground("http://www.vvz.ethz.ch/Vorlesungsverzeichnis/lerneinheit.view?lang=en&semkez=2020W&ansicht=ALLE&lerneinheitId=140984&");

    public Lecture parse(Document doc){
        Lecture result;
        result= new Lecture("ciao_name", "ciao_day", "ciao_start", "ciao_end", "ciao_code", 14);

        return result;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


         AsyncTask<String, Void, Document> task = new Parse().execute("http://www.vvz.ethz.ch/Vorlesungsverzeichnis/lerneinheit.view?lang=en&semkez=2020W&ansicht=ALLE&lerneinheitId=140984&");
        try {
            Document doc = task.get();
            Element content = doc.getElementById("contentTop");
            Toast.makeText(MainActivity.this, content.text(), Toast.LENGTH_SHORT).show();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }





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



