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

public class ActivitySearch extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        TextView title = (TextView) findViewById(R.id.activityTitle2);
        title.setText("This is ActivitySearch, please update the searching engine");

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        BottomNavigationViewHelper.removeNavigationShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.ic_home:
                        Intent intent0 = new Intent(ActivitySearch.this, MainActivity.class);
                        startActivity(intent0);
                        break;

                    case R.id.ic_events:
                        Intent intent1 = new Intent(ActivitySearch.this, ActivityEvents.class);
                        startActivity(intent1);
                        break;

                    case R.id.ic_search:

                        break;

                    case R.id.ic_weather:
                        Intent intent3 = new Intent(ActivitySearch.this, ActivityWeather.class);
                        startActivity(intent3);
                        break;

                    case R.id.ic_profile:
                        Intent intent4 = new Intent(ActivitySearch.this, ActivityProfile.class);
                        startActivity(intent4);
                        break;
                }


                return false;
            }
        });
    }

}
