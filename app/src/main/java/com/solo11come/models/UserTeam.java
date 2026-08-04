package com.solo11come.models;

import java.util.List;

public class UserTeam {
    private String matchId;
    private List<String> playerIds;
    private String captainId;
    private String viceCaptainId;
    private String userId;

    // These fields are used for calculations and local display
    private List<Player> selectedPlayers;
    private Player captain;
    private Player viceCaptain;

    public UserTeam(String matchId, List<String> playerIds, String captainId, String viceCaptainId, String userId) {
        this.matchId = matchId;
        this.playerIds = playerIds;
        this.captainId = captainId;
        this.viceCaptainId = viceCaptainId;
        this.userId = userId;
    }

    // Getters for ID-based fields
    public String getMatchId() { return matchId; }
    public List<String> getPlayerIds() { return playerIds; }
    public String getCaptainId() { return captainId; }
    public String getViceCaptainId() { return viceCaptainId; }
    public String getUserId() { return userId; }

    // Getters and Setters for Player object-based fields
    public List<Player> getSelectedPlayers() {
        return selectedPlayers;
    }

    public void setSelectedPlayers(List<Player> selectedPlayers) {
        this.selectedPlayers = selectedPlayers;
    }

    public Player getCaptain() {
        return captain;
    }

    public void setCaptain(Player captain) {
        this.captain = captain;
    }

    public Player getViceCaptain() {
        return viceCaptain;
    }

    public void setViceCaptain(Player viceCaptain) {
        this.viceCaptain = viceCaptain;
    }
}
