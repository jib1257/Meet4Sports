package com.example.bingchen.meet4sports;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * Created by User on 4/15/2017.
 */

public class ActivityProfile extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        TextView title = (TextView) findViewById(R.id.activityTitle4);
        title.setText("This is ActivityProfile, please update methods of loading the contents");

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        BottomNavigationViewHelper.removeNavigationShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(4);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.ic_arrow:
                        Intent intent0 = new Intent(ActivityProfile.this, MainActivity.class);
                        startActivity(intent0);
                        break;

                    case R.id.ic_events:
                        Intent intent1 = new Intent(ActivityProfile.this, ActivityEvents.class);
                        startActivity(intent1);
                        break;

                    case R.id.ic_search:
                        Intent intent2 = new Intent(ActivityProfile.this, ActivitySearch.class);
                        startActivity(intent2);
                        break;

                    case R.id.ic_weather:
                        Intent intent3 = new Intent(ActivityProfile.this, ActivityWeather.class);
                        startActivity(intent3);
                        break;

                    case R.id.ic_profile:
                        break;
                }


                return false;
            }
        });
    }
}
