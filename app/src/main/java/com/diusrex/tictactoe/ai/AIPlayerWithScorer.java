package com.diusrex.tictactoe.ai;

import java.io.PrintStream;

import com.diusrex.tictactoe.ai.scoring_calculations.Scorer;
import com.diusrex.tictactoe.data_structures.BoardStatus;
import com.diusrex.tictactoe.data_structures.Player;

public abstract class AIPlayerWithScorer extends AIPlayer {
    private final Scorer scorer;

    public AIPlayerWithScorer(Scorer scorer) {
        this.scorer = scorer;
    }
    
    @Override
    public void learnFromChange(BoardStatus board) {
        scorer.learnFromChange(board);
    }

    @Override
    public void newGame(BoardStatus board) {
        scorer.newGame(board);
    }
    
    protected double getWinScore() {
        return scorer.getWinScore();
    }
    
    protected double getTieScore() {
        return scorer.getTieScore();
    }
    
    protected double getScore(BoardStatus board, Player player) {
        return scorer.calculateScore(player, board);
    }

    @Override
    public void saveInternalState(PrintStream logger) {
        saveAdditionalPlayerState(logger);
        scorer.saveState(logger);
    }

    // Default to nothing
    protected void saveAdditionalPlayerState(PrintStream logger) {
    }
}
