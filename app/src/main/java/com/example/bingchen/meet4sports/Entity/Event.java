package com.example.bingchen.meet4sports.Entity;

public class Event {
    private String name, sport, location;
    private String date;
    private String time;
    private int maxNumParticipant;
    private String organizerName;
    private String organizerEmail;
    private String organizerID;
    private String eventID;
    private int currentNumParticipant = 0;

    public Event(){
    }

    public Event(String eventID, String name, String sport, String location, String date, String time, int maxNumParticipant, String organizerID, String organizerEmail, String organizerName){
        this.eventID = eventID;
        this.name = name;
        this.sport = sport;
        this.location = location;
        this.date = date;
        this.time = time;
        this.maxNumParticipant = maxNumParticipant;
        this.organizerID = organizerID;
        this.organizerName = organizerName;
        this.organizerEmail = organizerEmail;
        incParticipants();
    }

    public void incParticipants(){
        this.currentNumParticipant++;
    }

    public String getEventID(){return this.eventID;}

    public void setEventID(String i){this.eventID = i;}

    public String getEventName(){
        return this.name;
    }

    public void setEventName(String newName){
        this.name = newName;
    }

    public String getSport(){
        return sport;
    }

    public void setSport(String newSport){
        this.sport = newSport;
    }

    public String getLocation(){
        return this.location;
    }

    public void setLocation(String loc){this.location = loc;}

    public String getDate(){
        return this.date;
    }

    public void setDate(String d){this.date = d;}

    public String getTime(){
        return this.time;
    }

    public void setTime(String t){this.time = t;}

    public int getMaxNumParticipant(){
        return this.maxNumParticipant;
    }

    public void setMaxNumParticipant(int m){this.maxNumParticipant = m;}

    public String getOrganizerName(){
        return this.organizerName;
    }

    public void setOrganizerName(String n){this.organizerName = n;}

    public String getOrganizerEmail(){
        return this.organizerEmail;
    }

    public void setOrganizerEmail(String e){this.organizerEmail = e;}

    public String getOrganizerID(){return this.organizerID;}

    public void setOrganizerID(String i){this.organizerID = i;}
}
