package com.example.bingchen.meet4sports;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.bingchen.meet4sports.Entity.Event;
import com.example.bingchen.meet4sports.ViewHolder.EventViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private RecyclerView eventList;
    private DatabaseReference dbRef;
    private FirebaseRecyclerAdapter<Event, EventViewHolder> adapter;
    private Button create_event;
    private LinearLayoutManager mLayoutManager;
    private String eventID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbRef = FirebaseDatabase.getInstance().getReference().child("events");
        dbRef.keepSynced(true);

        create_event = (Button) findViewById(R.id.create_event_btn);
        eventList = (RecyclerView) findViewById(R.id.event_recycle_view);
        eventList.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        eventList.setLayoutManager(mLayoutManager);

        // Set up the ViewPager with the sections adapter.

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        BottomNavigationViewHelper.removeNavigationShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);
        fetch();

        create_event.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startActivity(new Intent(MainActivity.this, ActivityCreateEvent.class));
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.ic_home:

                        break;

                    case R.id.ic_message:
                        Intent intent1 = new Intent(MainActivity.this, ActivityMessage.class);
                        startActivity(intent1);
                        break;

                    case R.id.ic_search:
                        Intent intent2 = new Intent(MainActivity.this, ActivitySearch.class);
                        startActivity(intent2);
                        break;

                    case R.id.ic_weather:
                        Intent intent3 = new Intent(MainActivity.this, ActivityWeather.class);
                        startActivity(intent3);
                        break;

                    case R.id.ic_profile:
                        Intent intent4 = new Intent(MainActivity.this, ActivityProfile.class);
                        startActivity(intent4);
                        break;
                }

                return false;
            }
        });
    }

    private void fetch() {
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("events").orderByKey();

        FirebaseRecyclerOptions<Event> options =
                new FirebaseRecyclerOptions.Builder<Event>()
                        .setQuery(query, new SnapshotParser<Event>() {
                            @NonNull
                            @Override
                            public Event parseSnapshot(@NonNull DataSnapshot snapshot) {
                                return new Event(snapshot.child("eventID").getValue().toString(),
                                        snapshot.child("eventName").getValue().toString(),
                                        snapshot.child("sport").getValue().toString(),
                                        snapshot.child("location").getValue().toString(),
                                        snapshot.child("date").getValue().toString(),
                                        snapshot.child("time").getValue().toString(),
                                        Integer.parseInt(snapshot.child("maxNumParticipant").getValue().toString()),
                                        snapshot.child("organizerID").getValue().toString(),
                                        snapshot.child("organizerEmail").getValue().toString(),
                                        snapshot.child("organizerName").getValue().toString());
                            }
                        })
                        .build();

        adapter = new FirebaseRecyclerAdapter<Event, EventViewHolder>(options) {
            @Override
            public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.event_row, parent, false);

                return new EventViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(EventViewHolder holder, final int position, Event e) {
                holder.setTitle(e.getEventName());
                holder.setDate(e.getDate());
                holder.setTime(e.getTime());
                holder.setOrganizer(e.getOrganizerName());
                holder.setSport(e.getSport());
                final String eventID = e.getEventID();
                holder.root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent viewDetails = new Intent(MainActivity.this, ActivityJoinEvents.class);
                        viewDetails.putExtra("event_id", eventID);
                        startActivity(viewDetails);
                    }
                });
            }

        };
        eventList.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}
