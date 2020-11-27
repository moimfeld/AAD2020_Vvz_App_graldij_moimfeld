package com.example.aad2020_vvz_app_graldij_moimfeld.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.example.aad2020_vvz_app_graldij_moimfeld.R;
import com.example.aad2020_vvz_app_graldij_moimfeld.Utils.Course;
import com.example.aad2020_vvz_app_graldij_moimfeld.Utils.Parse;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

public class Vvz extends AppCompatActivity {

    //this class hinders the webview from jumping out of the app into a browser app
    private static class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vvz);




        //Webview for VVZ
        final WebView myWebView = findViewById(R.id.webview);
        myWebView.setWebViewClient(new MyWebViewClient());

        //the following lines are needed to initially zoom out the Webview such that it is accessible
        myWebView.getSettings().setUseWideViewPort(true);
        myWebView.setInitialScale(1);

        //enable zoom in webview
        myWebView.getSettings().setBuiltInZoomControls(true);
        myWebView.getSettings().setDisplayZoomControls(false);


        myWebView.loadUrl("http://www.vorlesungsverzeichnis.ethz.ch/Vorlesungsverzeichnis/sucheLehrangebotPre.view?lang=en");



        //Button
        final Button button = findViewById(R.id.saved_lecture);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //parse the currently displayed lecture in the webview
                Parse parse = new Parse();
                Course new_parse = parse.parse(Objects.requireNonNull(myWebView.getUrl()), getApplicationContext());
                //check whether the Lecture fully parsed
                if(new_parse.name != null
                        && (!new_parse.lectures.isEmpty()
                            || !new_parse.lecturesAndExercises.isEmpty()
                            || !new_parse.exercises.isEmpty()
                            || !new_parse.labs.isEmpty())
                        && new_parse.course_code != null
                        && new_parse.ECTS != -1) {
                    //check if the saved_lecture ArrayList is empty
                    if (MainActivity.saved_courses.size() > 0) {
                        //check if the Lecture is already in the ArrayList
                        for (int i = 0; i <= MainActivity.saved_courses.size(); i++) {
                            if (MainActivity.saved_courses.get(i).isEqual(new_parse)) {
                                Toast.makeText(Vvz.this, "lecture has already been saved", Toast.LENGTH_SHORT).show();
                                break;
                            }
                            if (i == MainActivity.saved_courses.size() - 1) {
                                MainActivity.saved_courses.add(new_parse);
                                Toast.makeText(Vvz.this, "lecture saved", Toast.LENGTH_SHORT).show();
                                break;
                            }
                        }
                    //handling the empty saved_lecture ArrayList
                    } else {
                        MainActivity.saved_courses.size();
                        MainActivity.saved_courses.add(new_parse);
                        Toast.makeText(Vvz.this, "lecture saved", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        //Bottom navigation bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.action_Vvz);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_Timetable:
                        //Toast.makeText(Vvz.this, "Recents", Toast.LENGTH_SHORT).show();
                        Intent Timetable = new Intent(Vvz.this, Timetable.class);
                        startActivity(Timetable);
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.action_MainActivity:
                        //Toast.makeText(Vvz.this, "Favorites", Toast.LENGTH_SHORT).show();
                        Intent MainActivity = new Intent(Vvz.this, MainActivity.class);
                        startActivity(MainActivity);
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.action_Vvz:
                        //Toast.makeText(Vvz.this, "Nearby", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });

    }
}