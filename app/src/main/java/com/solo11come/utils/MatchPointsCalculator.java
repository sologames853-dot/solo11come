package com.solo11come.utils;

import com.solo11come.models.Player;
import com.solo11come.models.PlayerStats;
import com.solo11come.models.UserTeam;
import java.util.Map;

public class MatchPointsCalculator {
    
    public static double calculateTeamTotal(UserTeam userTeam, Map<String, PlayerStats> liveStatsMap) {
        double teamTotal = 0;

        for (Player player : userTeam.getSelectedPlayers()) {
            // In a real app, we would use a unique Player ID
            // Here we use player name as a dummy key
            PlayerStats stats = liveStatsMap.get(player.getName());
            
            if (stats != null) {
                boolean isC = (player.getName().equals(userTeam.getCaptain().getName()));
                boolean isVC = (player.getName().equals(userTeam.getViceCaptain().getName()));
                
                teamTotal += PointSystem.calculatePoints(stats, isC, isVC);
            }
        }
        
        return teamTotal;
    }
}
