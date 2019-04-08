package com.example.bingchen.meet4sports.model.model;

/**
 * Created by sYUYx on 5/11/17.
 */

public class GamePlayer {
    private String userId;
    private String email;
    private Boolean isHost;

    public GamePlayer() {
        userId = "null";
        email = "null";
        isHost = false;
    }

    public GamePlayer(String id, String email) {
        this.userId = id;
        this.email = email;
        isHost = false;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userid) {
        this.userId = userid;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setIsHost() {
        this.isHost = true;
    }
}
