package com.diusrex.tictactoe.ai.tournament.test_results;

import com.diusrex.tictactoe.ai.AIPlayer;
import com.diusrex.tictactoe.ai.scoring_calculations.ScoringFunction;
import com.diusrex.tictactoe.ai.scoring_calculations.ScoringValues;
import com.diusrex.tictactoe.logic.TicTacToeEngine;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseScoringValuesTestResults implements Comparable<BaseScoringValuesTestResults> {
    private final ScoringValues ownValue;
    private final AIPlayer player;

    private int won, drew, lost;
    private List<Long> times;

    private AISpecificData specificData;

    private int[] winDepths;
    private int[] lossDepths;
    private int[] winAsFirstDepth;
    private int[] winAsSecondDepth;

    public BaseScoringValuesTestResults(ScoringValues ownValue, AIPlayer player) {
        this.ownValue = ownValue;
        this.player = player;

        winDepths = new int[TicTacToeEngine.MAX_NUM_MOVES + 1];
        lossDepths = new int[TicTacToeEngine.MAX_NUM_MOVES + 1];
        winAsFirstDepth = new int[TicTacToeEngine.MAX_NUM_MOVES + 1];
        winAsSecondDepth = new int[TicTacToeEngine.MAX_NUM_MOVES + 1];

        won = drew = lost = 0;
        times = new ArrayList<Long>();
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

    public synchronized void increaseWon(int depth, boolean first) {
        ++won;
        ++winDepths[depth];

        if (first)
            ++winAsFirstDepth[depth];
        else
            ++winAsSecondDepth[depth];
    }

    public synchronized void increaseLost(int depth) {
        ++lost;
        ++lossDepths[depth];
    }

    public synchronized void increaseDraw() {
        ++drew;
    }

    public List<Long> getAllTimes() {
        return times;
    }

    public long getTotalTime() {
        return TimingResults.getTotalTime(times);
    }

    public double getAverageTime() {
        return TimingResults.getAverageTime(times);
    }

    public double getTimeStdDev() {
        return TimingResults.getTimeStdDev(times);
    }

    public void printOut(PrintStream printStream, boolean isVerbose) {
        if (isVerbose) {
            printStream.println("wins: " + won + " draw: " + drew + " loss: " + lost);

            printAdditionalInfo(printStream);
        }

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

    public int[] getWinDepths() {
        return winDepths;
    }

    public int[] getLossDepths() {
        return lossDepths;
    }

    public int[] getWinAsFirstDepths() {
        return winAsFirstDepth;
    }

    public int[] getWinAsSecondDepths() {
        return winAsSecondDepth;
    }

}
