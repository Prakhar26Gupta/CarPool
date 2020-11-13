package com.example.carpool02;

import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

public class navigation_bottom extends AppCompatActivity {
    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_bottom);
        bottomNav  = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(nav_listener);


        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new home_fragment()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener nav_listener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;

                    switch(menuItem.getItemId())
                    {
                        case R.id.navigation_home:
                            selectedFragment = new home_fragment();
                            break;

                        case R.id.navigation_search:
                            selectedFragment = new search_fragment();
                            break;
                        case R.id.navigation_chat:
                            selectedFragment = new chat_fragment();
                            break;
                        case R.id.navigation_profile:
                            selectedFragment = new profile();
                            break;

                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();
                    return true;
                }
            };


}
