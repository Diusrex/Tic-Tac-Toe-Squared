package com.diusrex.tictactoe.ai.scoring_calculations;


import java.io.PrintStream;

import com.diusrex.tictactoe.data_structures.Player;
import com.diusrex.tictactoe.data_structures.board_status.BoardStatus;

public abstract class Scorer {
    public abstract double calculateScore(Player positivePlayer, BoardStatus board);

    public double getTieScore() {
        return 0;
    }

    public abstract void newGame(BoardStatus board);

    public abstract void learnFromChange(BoardStatus board);

    public abstract double getWinScore();

    // This should allow the scorer to be completely recreated (without any weights)
    // given that we already knew what type it was - just need weights as well
    public final void saveIdentifiers(PrintStream logger) {
        logger.println(getIdentifier());
        saveInternalIdentifiers(logger);
    }
    
    protected abstract void saveInternalIdentifiers(PrintStream logger);

    // This will save all the parameters like bots - including weights (which may be changed over time).
    // Just requires knowing what classes were used, which is stored by saveIdentifiers
    public abstract void saveParameters(PrintStream logger);

    public abstract String getIdentifier();
}
