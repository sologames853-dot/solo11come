package com.solo11come.models;

import java.io.Serializable;

public class Player implements Serializable {
    private String id;
    private String name;
    private String role;
    private double credits;
    private String team;
    private String image;
    private boolean isPlaying = false;

    public Player(String id, String name, String role, double credits, String team, String image) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.credits = credits;
        this.team = team;
        this.image = image;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getRole() { return role; }
    public double getCredits() { return credits; }
    public String getTeam() { return team; }
    public String getImage() { return image; }
    public boolean isPlaying() { return isPlaying; }
    public void setPlaying(boolean playing) { isPlaying = playing; }
}
