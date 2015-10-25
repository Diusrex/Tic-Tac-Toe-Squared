package com.diusrex.tictactoe.ai.tournament.test_results;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class PlayerTimeResults {
    private final List<Long> times;

    public PlayerTimeResults() {
        times = new ArrayList<>();
    }

    public void add(long time) {
        times.add(time);
    }

    public void copyDataFrom(PlayerTimeResults other) {
        times.addAll(other.times);
    }

    public void clear() {
        times.clear();
    }

    public long getTotalTime() {
        long total = 0;
        for (Long time : times) {
            total += time;
        }

        return total;
    }

    public double getAverageTime() {
        if (times.size() != 0)
            return getTotalTime() / times.size();
        else
            return 0.0;
    }

    public double getTimeStdDev() {
        if (times.size() == 0)
            return 0.0;

        double averageTime = getAverageTime();

        double differences = 0;
        for (long time : times) {
            differences += (time - averageTime) * (time - averageTime);
        }

        return Math.sqrt(differences / times.size());
    }

    public void printTimeBreakdown(PrintStream printStream) {
        printStream.println(getAverageTime() + ", std-dev " + getTimeStdDev());
    }
}
