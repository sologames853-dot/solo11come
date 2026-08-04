package com.solo11come.models;

public class PlayerStats {
    private String playerId;
    private int runs;
    private int wickets;
    private int catches;
    private int stumpings;
    private int boundaries;
    private int sixes;
    private int maidenOvers;

    public PlayerStats(String playerId) {
        this.playerId = playerId;
    }

    // Getters and Setters
    public String getPlayerId() { return playerId; }
    public int getRuns() { return runs; }
    public void setRuns(int runs) { this.runs = runs; }
    public int getWickets() { return wickets; }
    public void setWickets(int wickets) { this.wickets = wickets; }
    public int getCatches() { return catches; }
    public void setCatches(int catches) { this.catches = catches; }
    public int getStumpings() { return stumpings; }
    public void setStumpings(int stumpings) { this.stumpings = stumpings; }
    public int getBoundaries() { return boundaries; }
    public void setBoundaries(int boundaries) { this.boundaries = boundaries; }
    public int getSixes() { return sixes; }
    public void setSixes(int sixes) { this.sixes = sixes; }
    public int getMaidenOvers() { return maidenOvers; }
    public void setMaidenOvers(int maidenOvers) { this.maidenOvers = maidenOvers; }
}
