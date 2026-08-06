package com.solo11come.models;

public class Match {
    private String id;
    private String team1;
    private String team2;
    private String matchTime;
    private String matchType;
    private String team1Logo;
    private String team2Logo;
    private String name;
    private String status;
    private String score;
    private boolean matchStarted;
    private boolean matchEnded;
    private boolean hasSquad;
    private boolean fantasyEnabled;

    public Match() {}

    public boolean isHasSquad() { return hasSquad; }
    public void setHasSquad(boolean hasSquad) { this.hasSquad = hasSquad; }

    public boolean isFantasyEnabled() { return fantasyEnabled; }
    public void setFantasyEnabled(boolean fantasyEnabled) { this.fantasyEnabled = fantasyEnabled; }

    public boolean isMatchStarted() { return matchStarted; }
    public void setMatchStarted(boolean matchStarted) { this.matchStarted = matchStarted; }

    public boolean isMatchEnded() { return matchEnded; }
    public void setMatchEnded(boolean matchEnded) { this.matchEnded = matchEnded; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getScore() { return score; }
    public void setScore(String score) { this.score = score; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTeam1() { return team1; }
    public void setTeam1(String team1) { this.team1 = team1; }

    public String getTeam2() { return team2; }
    public void setTeam2(String team2) { this.team2 = team2; }

    public String getMatchTime() { return matchTime; }
    public void setMatchTime(String matchTime) { this.matchTime = matchTime; }

    public String getMatchType() { return matchType; }
    public void setMatchType(String matchType) { this.matchType = matchType; }

    public String getTeam1Logo() { return team1Logo; }
    public void setTeam1Logo(String team1Logo) { this.team1Logo = team1Logo; }

    public String getTeam2Logo() { return team2Logo; }
    public void setTeam2Logo(String team2Logo) { this.team2Logo = team2Logo; }
}
