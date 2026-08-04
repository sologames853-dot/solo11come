package com.solo11come.models;

public class Contest {
    private String id;
    private String prizePool;
    private String entryFee;
    private String spots;
    private String category;

    public Contest(String prizePool, String entryFee, String spots, String category) {
        this.prizePool = prizePool;
        this.entryFee = entryFee;
        this.spots = spots;
        this.category = category;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getPrizePool() { return prizePool; }
    public String getEntryFee() { return entryFee; }
    public String getSpots() { return spots; }
    public String getCategory() { return category; }
}
