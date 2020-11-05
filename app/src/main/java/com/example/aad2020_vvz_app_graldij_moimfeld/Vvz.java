package com.example.aad2020_vvz_app_graldij_moimfeld;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Vvz extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vvz);


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