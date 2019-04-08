package com.example.bingchen.meet4sports.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class EventViewHolder extends RecyclerView.ViewHolder{
    public View mView;
    public RelativeLayout root;

    public EventViewHolder(View itemView){
        super(itemView);
        mView = itemView;
        root = itemView.findViewById(com.example.bingchen.meet4sports.R.id.list_root);

    }

    public void setTitle(String title){
        TextView event_title = (TextView) mView.findViewById(com.example.bingchen.meet4sports.R.id.event_card_name);
        event_title.setText(title);
    }

    public void setDate(String date){
        TextView event_text = (TextView) mView.findViewById(com.example.bingchen.meet4sports.R.id.event_card_date);
        event_text.setText(date);
    }

    public void setTime(String time){
        TextView event_time = (TextView) mView.findViewById(com.example.bingchen.meet4sports.R.id.event_card_time);
        event_time.setText(time);
    }

    public void setOrganizer(String org){
        TextView organizer = (TextView) mView.findViewById(com.example.bingchen.meet4sports.R.id.event_card_organizer);
        organizer.setText(org);
    }

    public void setSport(String s){
        TextView sport = (TextView) mView.findViewById(com.example.bingchen.meet4sports.R.id.event_card_sport);
        sport.setText(s);
    }
}