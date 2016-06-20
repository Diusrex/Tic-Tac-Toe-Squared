package com.diusrex.tictactoe.ai.tournament.test_results;

import java.io.PrintStream;

import com.diusrex.tictactoe.ai.AIPlayer;
import com.diusrex.tictactoe.logic.TicTacToeEngine;

public class BaseScoringValuesTestResults implements Comparable<BaseScoringValuesTestResults> {
    private final AIPlayer player;

    private int won, drew, lost;
    private PlayerTimeResults timeResults;

    private int[] winDepths;
    private int[] lossDepths;
    private int[] winAsFirstDepth;
    private int[] winAsSecondDepth;

    public BaseScoringValuesTestResults(AIPlayer player) {
        this.player = player;

        winDepths = new int[TicTacToeEngine.MAX_NUM_MOVES + 1];
        lossDepths = new int[TicTacToeEngine.MAX_NUM_MOVES + 1];
        winAsFirstDepth = new int[TicTacToeEngine.MAX_NUM_MOVES + 1];
        winAsSecondDepth = new int[TicTacToeEngine.MAX_NUM_MOVES + 1];

        won = drew = lost = 0;
        timeResults = new PlayerTimeResults();
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

    public synchronized void addTime(long timeTaken) {timeResults.add(timeTaken);
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

    public PlayerTimeResults getTimeResults() {
        return timeResults;
    }

    public void printOut(PrintStream printStream, boolean isVerbose) {
        if (isVerbose) {
            printStream.println("wins: " + won + " draw: " + drew + " loss: " + lost);
        }
        
        player.saveState(printStream);
    }

    public void reset() {
        won = lost = drew = 0;
        timeResults.clear();
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
