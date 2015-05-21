package com.diusrex.tictactoe.ai.tournament;

import java.io.PrintStream;

import com.diusrex.tictactoe.ai.AIPlayer;
import com.diusrex.tictactoe.ai.scoring_calculations.ScoringFunction;
import com.diusrex.tictactoe.ai.scoring_calculations.ScoringValues;


public abstract class BaseScoringValuesTestResults implements Comparable<BaseScoringValuesTestResults> {
    private final ScoringValues ownValue;

    private int won, drew, lost;

    public BaseScoringValuesTestResults(ScoringValues ownValue) {
        this.ownValue = ownValue;

        won = drew = lost = 0;
    }

    @Override
    public int compareTo(BaseScoringValuesTestResults other) {
        if (won != other.won)
            return won - other.won;

        else if (drew != other.drew)
            return drew - other.drew;

        return 0;
    }

    public abstract AIPlayer getPlayer();

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

        printAdditionalInfo(printStream);

        ScoringFunction cF = ownValue.getMainScoring();
        printStream.println("MainScoring: " + cF.getCannotWinPointScore() + " " + cF.getOwnsOnlyTakenInLine() + " "
                + cF.getOwnsBothOnlyTakenInLine() + " " + cF.blockedPlayerInLine());

        cF = ownValue.getSectionScoring();
        printStream.println("SectionScoring: " + cF.getCannotWinPointScore() + " " + cF.getOwnsOnlyTakenInLine() + " "
                + cF.getOwnsBothOnlyTakenInLine() + " " + cF.blockedPlayerInLine());
    }

    protected abstract void printAdditionalInfo(PrintStream printStream);

    public void reset() {
        won = lost = drew = 0;
    }
}
