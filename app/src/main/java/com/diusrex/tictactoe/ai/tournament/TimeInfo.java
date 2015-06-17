package com.diusrex.tictactoe.ai.tournament;

import java.util.List;

public class TimeInfo {
    public static long getTotalTime(List<Long> times) {
        long total = 0;
        for (Long time : times) {
            total += time;
        }
        
        return total;
    }
    
    public static double getAverageTime(List<Long> times) {
        if (times.size() != 0)
            return getTotalTime(times)/ times.size();
        else
            return 0.0;
    }
    
    public static double getTimeStdDev(List<Long> times) {
        if (times.size() == 0)
            return 0.0;
        
        double averageTime = getAverageTime(times);
        
        double differences = 0;
        for (long time : times) {
            differences += (time - averageTime) * (time - averageTime);
        }
        
        return Math.sqrt(differences / times.size());
    }
    
    public static class GameTimeInfo {
        private final long times[];
        
        public GameTimeInfo() {
            times = new long[2];
            times[0] = times[1] = 0;
        }
        
        public void addTime(long time, int player) {
            times[player] += time;
        }
        
        public long getPlayerOneTime() {
            return times[0];
        }
        
        public long getPlayerTwoTime() {
            return times[1];
        }
    }
}
