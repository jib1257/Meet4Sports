package com.example.bingchen.meet4sports;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.bingchen.meet4sports.Entity.Event;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.regex.Pattern;

public class  ActivityCreateEvent extends AppCompatActivity {
    private TextInputLayout event_name_input, date_input, time_input, location_input, sport_input, numParticipants_input;
    private Button confirmCreation, cancelCreation;
    String event_name, date, time, location, sport, organizerEmail, organizerID, organizerName;
    int numParticipants;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        event_name_input = findViewById(R.id.event_name_input);
        date_input = findViewById(R.id.date_input);
        time_input = findViewById(R.id.time_input);
        location_input = findViewById(R.id.location_input);
        sport_input = findViewById(R.id.sport_input);
        numParticipants_input = findViewById(R.id.numParticipants_input);

        confirmCreation = findViewById(R.id.confirm_create_event);
        cancelCreation = findViewById(R.id.cancel_create_event);

        cancelCreation.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onBackPressed();
            }
        });
        confirmCreation.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                 event_name = event_name_input.getEditText().getText().toString();
                 date = date_input.getEditText().getText().toString();
                 time = time_input.getEditText().getText().toString();
                 location = location_input.getEditText().getText().toString();
                 sport = sport_input.getEditText().getText().toString();
                 numParticipants = Integer.parseInt(numParticipants_input.getEditText().getText().toString());

                 FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                 organizerID = currentUser.getUid();
                 organizerEmail = currentUser.getEmail();
                 organizerName = currentUser.getDisplayName();
                 createEvent(event_name, date, time, location, sport, numParticipants, organizerEmail, organizerID, organizerName);

             }
        });
    }

    private void createEvent(String event_name_entered, String date_entered, String time_entered, String location_entered, String sport_entered, int numParticipants_entered, String organizerEmail_entered, final String organizerID_entered, String organizerName_entered){
        if(event_name_entered.equals("") || date_entered.equals("") || time_entered.equals("") || location_entered.equals("") || sport_entered.equals("")){
            Toast.makeText(ActivityCreateEvent.this, "need to fill in all the field", Toast.LENGTH_LONG).show();
            return;
        }

        if(numParticipants_entered < 2){
            Toast.makeText(ActivityCreateEvent.this, "At least 2 participants are required", Toast.LENGTH_LONG).show();
            return;
        }

        if(Pattern.matches("^\\d\\d:\\d\\d$", time_entered) == false){
            Toast.makeText(ActivityCreateEvent.this, "Wrong format for time", Toast.LENGTH_LONG).show();
            return;
        }

        if(Pattern.matches("^\\d{4}-\\d{2}-\\d{2}$",date_entered) == false){
            Toast.makeText(ActivityCreateEvent.this, "Wrong format for date", Toast.LENGTH_LONG).show();
            return;
        }

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("events");
        final String eventID = ref.push().getKey();
        ref = ref.child(eventID);

        Event e = new Event(eventID, event_name_entered, sport_entered, location_entered,  date_entered, time_entered, numParticipants_entered, organizerID_entered, organizerEmail_entered, organizerName_entered);

        ref.setValue(e).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    DatabaseReference organizeRef = FirebaseDatabase.getInstance().getReference();
                    organizeRef = organizeRef.child("organize").child(organizerID_entered).child(eventID);
                    HashMap<String, String> hm = new HashMap<>();
                    hm.put("event_id", eventID);
                    organizeRef.setValue(hm);
                    Toast.makeText(ActivityCreateEvent.this, "Event created successfully!", Toast.LENGTH_LONG).show();
                    onBackPressed();
                }
                else{
                    Toast.makeText(ActivityCreateEvent.this, "Failed to create event.", Toast.LENGTH_LONG).show();
                    onBackPressed();
                }
            }
        });
    }
}
