package com.diusrex.tictactoe.ai;

import com.diusrex.tictactoe.data_structures.BoxPosition;
import com.diusrex.tictactoe.data_structures.Grid;
import com.diusrex.tictactoe.data_structures.Player;
import com.diusrex.tictactoe.logic.GridLists;

// Basic idea is, punish moves that were lost due to the section being won.
// So the actual score for a won section is won - 3 * scoringFunction.getCannotWinPoint()
public class CannotBeWonScoreWrapper implements ScoreCalculator {

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
            return scoringFunction.getCannotWinPointScore(box);

        else if (pointOwner != Player.Unowned)
            return -scoringFunction.getCannotWinPointScore(box);

        else
            return 0;
    }
}
