package com.solo11come.utils;

import com.solo11come.models.PlayerStats;

public class PointSystem {
    // T20 Points Configuration
    public static final int RUN = 1;
    public static final int BOUNDARY_BONUS = 1;
    public static final int SIX_BONUS = 2;
    public static final int WICKET = 25;
    public static final int MAIDEN_OVER = 8;
    public static final int CATCH = 8;
    public static final int STUMPING = 12;
    public static final int RUN_OUT = 12;

    public static double calculatePoints(PlayerStats stats, boolean isCaptain, boolean isViceCaptain) {
        double total = 0;
        
        total += stats.getRuns() * RUN;
        total += stats.getBoundaries() * BOUNDARY_BONUS;
        total += stats.getSixes() * SIX_BONUS;
        total += stats.getWickets() * WICKET;
        total += stats.getMaidenOvers() * MAIDEN_OVER;
        total += stats.getCatches() * CATCH;
        total += stats.getStumpings() * STUMPING;

        if (isCaptain) return total * 2.0;
        if (isViceCaptain) return total * 1.5;
        
        return total;
    }
}
