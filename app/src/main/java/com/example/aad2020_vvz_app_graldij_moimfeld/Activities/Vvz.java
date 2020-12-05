package com.example.aad2020_vvz_app_graldij_moimfeld.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Objects;

public class Vvz extends AppCompatActivity {

    //Variables needed throughout the whole activity
    public ArrayList<Course> saved_courses;
    private WebView myWebView;
    @SuppressLint("StaticFieldLeak")
    static Context context;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vvz);
        context = this;

        //here the saved_courses ArrayList gets rebuilt from the MainActivity Intent.putExtra
        Type type = new TypeToken<ArrayList<Course>>(){}.getType();
        Gson gson = new Gson();
        saved_courses = gson.fromJson(getIntent().getStringExtra("saved_courses"), type);
        if(saved_courses == null){
            saved_courses = new ArrayList<>();
        }

        //Set the status bar color to the ETH color
        getWindow().setStatusBarColor(Color.parseColor("#1F407A"));


        //Webview for VVZ
        myWebView  = findViewById(R.id.webview);
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
                if(!new_parse.isEmpty()) {
                    //check if the saved_lecture ArrayList is empty
                    if (saved_courses.size() > 0) {
                        //check if the Lecture is already in the ArrayList
                        for (int i = 0; i <= saved_courses.size(); i++) {
                            if (saved_courses.get(i).isEqual(new_parse)) {
                                Toast.makeText(Vvz.this, "course has already been saved", Toast.LENGTH_SHORT).show();
                                break;
                            }
                            if (i == saved_courses.size() - 1) {
                                saved_courses.add(new_parse);
                                Toast.makeText(Vvz.this, "course saved", Toast.LENGTH_SHORT).show();
                                saveCourses();
                                break;
                            }
                        }
                    //handling the empty saved_lecture ArrayList
                    } else {
                        saved_courses.size();
                        saved_courses.add(new_parse);
                        Toast.makeText(Vvz.this, "course saved", Toast.LENGTH_SHORT).show();
                        saveCourses();
                    }
                }
            }
        });

        //Bottom navigation bar which can be used to switch from activity to activity. Here the data to be sent to other activities gets built and put into the intent
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.action_Vvz);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_Timetable:
                        Intent Timetable = new Intent(Vvz.this, Timetable.class);
                        Gson gson = new Gson();
                        Timetable.putExtra("saved_courses", gson.toJson(saved_courses));
                        startActivity(Timetable);
                        overridePendingTransition(0, 0);


                    case R.id.action_MainActivity:
                        Intent MainActivity = new Intent(Vvz.this, MainActivity.class);
                        startActivity(MainActivity);
                        overridePendingTransition(0, 0);
                        break;

                    case R.id.action_Vvz:
                        break;
                }
                return true;
            }
        });

    }

    //this class hinders the webview from jumping out of the app into a browser app and prevents the user from getting out of the VVZ web pages
    private static class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            if(!StringUtils.contains(request.getUrl().toString(), "Vorlesungsverzeichnis")){
                Toast.makeText(context, "you cannot leave the course catalogue", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (myWebView.canGoBack()) {
                    myWebView.goBack();
                } else {
                    finish();
                }
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void saveCourses() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared_preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(saved_courses);
        editor.putString("saved_courses", json);
        editor.apply();
    }

}