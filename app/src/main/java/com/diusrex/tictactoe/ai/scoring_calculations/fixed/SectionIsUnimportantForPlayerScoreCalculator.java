package com.diusrex.tictactoe.ai.scoring_calculations.fixed;

import com.diusrex.tictactoe.ai.scoring_calculations.PlayerScoreCalculator;
import com.diusrex.tictactoe.data_structures.BoxPosition;
import com.diusrex.tictactoe.data_structures.Grid;
import com.diusrex.tictactoe.data_structures.Player;
import com.diusrex.tictactoe.logic.GridLists;

/*
 * Should be used when:
 *
 * 1) Unable to win the current section
 * 2) Unable to use the section to win the game
 *
 * Basic idea is, punish moves that are unimportant
 */
public class SectionIsUnimportantForPlayerScoreCalculator implements PlayerScoreCalculator {

    @Override
    public int calculateSetupScore(Player currentPlayer, Grid grid, ScoringFunction scoringFunction) {
        return getAllPointScores(currentPlayer, grid, scoringFunction);
    }

    private int getAllPointScores(Player currentPlayer, Grid grid, ScoringFunction scoringFunction) {
        int score = 0;
        for (BoxPosition box : GridLists.getAllStandardBoxPositions()) {
            score += getPointScore(grid.getPointOwner(box), currentPlayer, scoringFunction, box);
        }

        return score;
    }

    private int getPointScore(Player pointOwner, Player currentPlayer, ScoringFunction scoringFunction, BoxPosition box) {
        if (pointOwner == currentPlayer)
            return scoringFunction.getCannotWinPointScore();

        else
            return 0;
    }
}
