package com.example.bingchen.meet4sports;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bingchen.meet4sports.Entity.Event;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

/**
 * Created by User on 4/15/2017.
 */

public class ActivityJoinEvents extends AppCompatActivity {
    private TextView event_name, time, date, location, sport, orgName, orgEmail, curPart, maxPart;
    private Button back, join;
    private DatabaseReference dbRef;
    private String eventID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_events);

        event_name = (TextView) findViewById(R.id.event_table_name_input);
        time = (TextView) findViewById(R.id.event_table_time_input);
        date = (TextView) findViewById(R.id.event_table_date_input);
        location = (TextView) findViewById(R.id.event_table_location_input);
        sport = (TextView) findViewById(R.id.event_table_sport_input);
        orgName = (TextView) findViewById(R.id.event_table_organizer_name_input);
        orgEmail = (TextView) findViewById(R.id.event_table_organizer_email_input);
        curPart = (TextView) findViewById(R.id.event_table_cur_part_input);
        maxPart = (TextView) findViewById(R.id.event_table_max_part_input);
        back = (Button) findViewById(R.id.event_table_back_btn);
        join = (Button) findViewById(R.id.event_table_join_btn);
        eventID = getIntent().getStringExtra("event_id");
        dbRef = FirebaseDatabase.getInstance().getReference().child("events").child(eventID);

        // configure event info
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Event e = dataSnapshot.getValue(Event.class);
                event_name.setText(e.getEventName());
                location.setText(e.getLocation());
                date.setText(e.getDate());
                time.setText(e.getTime());
                sport.setText(e.getSport());
                orgName.setText(e.getOrganizerName());
                orgEmail.setText(e.getOrganizerEmail());
                curPart.setText("1");
                maxPart.setText(Integer.toString(e.getMaxNumParticipant()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Failed to join!", Toast.LENGTH_SHORT).show();
                return;
            }
        });

        // configure join button
        join.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("participate").child(currentUser.getUid());
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        GenericTypeIndicator<HashMap<String, String>> dataHM = new GenericTypeIndicator<HashMap<String, String>>(){};
                        for(DataSnapshot data: dataSnapshot.getChildren()){
                            if(data.getValue(dataHM).get("event_id").equals(eventID)){
                                Toast.makeText(getApplicationContext(), "You have already joined this event!", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                HashMap<String, String> hm = new HashMap<>();
                hm.put("event_id", eventID);
                ref.child(eventID).setValue(hm).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "Joined Successfully", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Failed to join!", Toast.LENGTH_SHORT).show();
                            return;

                        }
                    }
                });
            }
        });


        // configure back button
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        // botton navigation bar
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
                        Intent intent0 = new Intent(ActivityJoinEvents.this, MainActivity.class);
                        startActivity(intent0);
                        break;

                    case R.id.ic_message:

                        break;

                    case R.id.ic_search:
                        Intent intent2 = new Intent(ActivityJoinEvents.this, ActivitySearch.class);
                        startActivity(intent2);
                        break;

                    case R.id.ic_weather:
                        Intent intent3 = new Intent(ActivityJoinEvents.this, ActivitySearch.class);
                        startActivity(intent3);
                        break;

                    case R.id.ic_profile:
                        Intent intent4 = new Intent(ActivityJoinEvents.this, ActivitySearch.class);
                        startActivity(intent4);
                        break;
                }


                return false;
            }
        });
    }
}
