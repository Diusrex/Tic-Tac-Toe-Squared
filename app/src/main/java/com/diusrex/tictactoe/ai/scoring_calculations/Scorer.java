package com.diusrex.tictactoe.ai.scoring_calculations;


import java.io.PrintStream;

import com.diusrex.tictactoe.data_structures.BoardStatus;
import com.diusrex.tictactoe.data_structures.Player;

public abstract class Scorer {
    public abstract double calculateScore(Player positivePlayer, BoardStatus board);

    public double getTieScore() {
        return 0;
    }

    public abstract void newGame(BoardStatus board);

    public abstract void learnFromChange(BoardStatus board);

    public abstract double getWinScore();

    public void saveState(PrintStream logger) {
        logger.println(getIdentifier());
        saveInternalState(logger);
    }
    
    protected abstract void saveInternalState(PrintStream logger);

    public abstract String getIdentifier();
}
