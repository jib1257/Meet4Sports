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

public class ActivityEvents extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        TextView title = (TextView) findViewById(R.id.activityTitle1);
        title.setText("This is ActivityEvents");

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        BottomNavigationViewHelper.removeNavigationShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.ic_home:
                        Intent intent0 = new Intent(ActivityEvents.this, MainActivity.class);
                        startActivity(intent0);
                        break;

                    case R.id.ic_events:

                        break;

                    case R.id.ic_search:
                        Intent intent2 = new Intent(ActivityEvents.this, ActivitySearch.class);
                        startActivity(intent2);
                        break;

                    case R.id.ic_weather:
                        Intent intent3 = new Intent(ActivityEvents.this, ActivitySearch.class);
                        startActivity(intent3);
                        break;

                    case R.id.ic_profile:
                        Intent intent4 = new Intent(ActivityEvents.this, ActivitySearch.class);
                        startActivity(intent4);
                        break;
                }


                return false;
            }
        });
    }
}
