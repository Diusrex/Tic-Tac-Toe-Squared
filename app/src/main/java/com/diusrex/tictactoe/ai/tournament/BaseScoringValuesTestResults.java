package com.diusrex.tictactoe.ai.tournament;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import com.diusrex.tictactoe.ai.AIPlayer;
import com.diusrex.tictactoe.ai.scoring_calculations.ScoringFunction;
import com.diusrex.tictactoe.ai.scoring_calculations.ScoringValues;


public abstract class BaseScoringValuesTestResults implements Comparable<BaseScoringValuesTestResults> {
    private final ScoringValues ownValue;
    private final AIPlayer player;

    private int won, drew, lost;
    private List<Long> times;

    public BaseScoringValuesTestResults(ScoringValues ownValue, AIPlayer player) {
        this.ownValue = ownValue;
        this.player = player;

        won = drew = lost = 0;
        times = new ArrayList<>();
    }

    @Override
    public int compareTo(BaseScoringValuesTestResults other) {
        if (won != other.won)
            return won - other.won;

        else if (drew != other.drew)
            return drew - other.drew;

        return 0;
    }

    public AIPlayer getPlayer() {
        return player;
    }
    
    public synchronized void addTime(long timeTaken) {
        times.add(timeTaken);
    }

    public synchronized void increaseWon() {
        ++won;
    }

    public synchronized void increaseLost() {
        ++lost;
    }

    public synchronized void increaseDraw() {
        ++drew;
    }

    public List<Long> getAllTimes() {
        return times;
    }
    
    public long getTotalTime() {
        return TimeInfo.getTotalTime(times);
    }
    
    public double getAverageTime() {
        return TimeInfo.getAverageTime(times);
    }
    
    public double getTimeStdDev() {
        return TimeInfo.getTimeStdDev(times);
    }

    public void printOut(PrintStream printStream) {
        //printStream.println("wins: " + won + " draw: " + drew + " loss: " + lost);

        //printAdditionalInfo(printStream);
        //printStream.println("Took " + getAverageTime() + "ms, with std dev: " + getTimeStdDev());

        ScoringFunction cF = ownValue.getMainScoring();
        printStream.print(cF.getCannotWinPointScore() + " " + cF.getOwnsOnlyTakenInLine() + " "
                + cF.getOwnsBothOnlyTakenInLine() + " " + cF.blockedPlayerInLine() + " ");

        cF = ownValue.getSectionScoring();
        printStream.println(cF.getCannotWinPointScore() + " " + cF.getOwnsOnlyTakenInLine() + " "
                + cF.getOwnsBothOnlyTakenInLine() + " " + cF.blockedPlayerInLine());
    }

    protected abstract void printAdditionalInfo(PrintStream printStream);

    public void reset() {
        won = lost = drew = 0;
        times.clear();
    }

}
