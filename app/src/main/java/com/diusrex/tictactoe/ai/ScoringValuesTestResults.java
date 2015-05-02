package com.diusrex.tictactoe.ai;

import java.io.PrintStream;


public class ScoringValuesTestResults implements Comparable<ScoringValuesTestResults> {
    private final ScoringValues ownValue;
    private final int depth;
    private final MiniMaxPlayer player;

    private int won, drew, lost;

    public ScoringValuesTestResults(ScoringValues ownValue, int depth) {
        this.ownValue = ownValue;
        this.depth = depth;
        player = new MiniMaxPlayer(ownValue, depth);

        won = drew = lost = 0;
    }

    @Override
    public int compareTo(ScoringValuesTestResults other) {
        if (won != other.won)
            return won - other.won;

        else if (drew != other.drew)
            return drew - other.drew;

        return 0;
    }

    public MiniMaxPlayer getPlayer() {
        return player;
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

    public void printOut(PrintStream printStream) {
        printStream.println("wins: " + won + " draw: " + drew + " loss: " + lost);
        
        printStream.println("Depth: " + depth);

        ScoringFunction cF = ownValue.getMainScoring();
        printStream.println("MainScoring: " + cF.getCannotWinPointScore() + " " + cF.getOwnsOnlyTakenInLine() + " "
                + cF.getOwnsBothOnlyTakenInLine() + " " + cF.blockedPlayerInLine());

        cF = ownValue.getSectionScoring();
        printStream.println("SectionScoring: " + cF.getCannotWinPointScore() + " " + cF.getOwnsOnlyTakenInLine() + " "
                + cF.getOwnsBothOnlyTakenInLine() + " " + cF.blockedPlayerInLine());
    }

    public void reset() {
        won = lost = drew = 0;
    }
}
