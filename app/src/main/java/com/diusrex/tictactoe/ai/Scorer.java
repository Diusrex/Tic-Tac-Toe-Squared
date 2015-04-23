package com.diusrex.tictactoe.ai;

import com.diusrex.tictactoe.data_structures.BoardStatus;
import com.diusrex.tictactoe.data_structures.Grid;
import com.diusrex.tictactoe.data_structures.Player;
import com.diusrex.tictactoe.data_structures.SectionPosition;
import com.diusrex.tictactoe.logic.GridLists;

public class Scorer {
    private static final ScoreCalculator canWinWrapper = new CanBeWonScoreCalculator();
    private static final ScoreCalculator cannotWinWrapper = new CannotBeWonScoreWrapper();
    private final ScoringValues scoring;

    public Scorer(ScoringValues scoring) {
        this.scoring = scoring;
    }

    public int calculateScore(Player currentPlayer, BoardStatus board) {
        int score = calculateGridOwnedPartsScore(currentPlayer, board.getMainGrid(), scoring.getMainScoring())
                * scoring.getMainGridMultiplier();

        for (SectionPosition section : GridLists.getAllStandardSections()) {
            score += calculateGridOwnedPartsScore(currentPlayer, board.getSectionGrid(section),
                    scoring.getSectionScoring())
                    * scoring.getSectionGridMultiplier(section);
        }

        return score;
    }

    private int calculateGridOwnedPartsScore(Player currentPlayer, Grid grid, ScoringFunction scoringFunction) {
        // Owning the grid itself doesn't matter here
        ScoreCalculator wrapper;

        if (grid.canBeWon())
            wrapper = canWinWrapper;
        else
            wrapper = cannotWinWrapper;

        return wrapper.calculateSetupScore(currentPlayer, grid, scoringFunction);
    }

    public int getTieScore() {
        return 0;
    }
}
