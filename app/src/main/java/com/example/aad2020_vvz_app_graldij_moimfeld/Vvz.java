package com.example.aad2020_vvz_app_graldij_moimfeld;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Vvz extends AppCompatActivity {

    //this class hinders the webview from jumping out of the app into a browser app
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vvz);


        //Webview for VVZ
        WebView myWebView = (WebView) findViewById(R.id.webview);
        myWebView.setWebViewClient(new MyWebViewClient());

        //the following lines are needed to initially zoom out the Webview such that it is accessible
        myWebView.getSettings().setUseWideViewPort(true);
        myWebView.setInitialScale(1);

        //enable zoom in webview
        myWebView.getSettings().setBuiltInZoomControls(true);
        myWebView.getSettings().setDisplayZoomControls(false);


        myWebView.loadUrl("http://www.vorlesungsverzeichnis.ethz.ch/Vorlesungsverzeichnis/sucheLehrangebotPre.view?lang=en");



        //Bottom navigation bar
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
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