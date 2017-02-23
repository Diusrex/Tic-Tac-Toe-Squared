package com.diusrex.tictactoe.ai.scoring_calculations.fixed;

import com.diusrex.tictactoe.data_structures.LineIterator;
import com.diusrex.tictactoe.data_structures.Player;
import com.diusrex.tictactoe.data_structures.grid.Grid;
import com.diusrex.tictactoe.data_structures.position.Position;
import com.diusrex.tictactoe.logic.GridLists;

/*
 * Should be used when both:
 *
 * 1) Able to win the section
 * 2) Able to use the section to win the game
 *
 * Reward for setting up lines where can win and punish for getting blocked by opponent.
 */
public class SectionIsImportantForPlayerScoreCalculator implements PlayerGridScoreCalculator {

    @Override
    public int calculateGridScoreForPlayer(Player currentPlayer, Grid grid, GridScoringFunction scoringFunction) {
        return calculateAllLineScores(currentPlayer, grid, scoringFunction);
    }

    // Must calculate score for both winning lines, and enemy winning lines
    private int calculateAllLineScores(Player currentPlayer, Grid grid, GridScoringFunction scoringFunction) {
        int score = 0;

        for (LineIterator iter : GridLists.getAllLineIterators()) {
            score += calculateLineScore(grid, iter, currentPlayer, scoringFunction);
        }
        return score;
    }

    private int calculateLineScore(Grid grid, LineIterator iter, Player currentPlayer, GridScoringFunction scoringFunction) {
        int numOwnPlayer = 0, numOtherPlayer = 0;

        for (int pos = 0; !iter.isDone(pos); ++pos) {
            Position boxPos = iter.getCurrent(pos);
            Player boxPlayer = grid.getPointOwner(boxPos);
            if (boxPlayer == currentPlayer)
                ++numOwnPlayer;
            else if (boxPlayer != Player.Unowned)
                ++numOtherPlayer;
        }

        // Either 0, or 1. Either way, wouldn't help current person
        if (numOwnPlayer == numOtherPlayer)
            return 0;

        else if (numOwnPlayer == 1 && numOtherPlayer == 0)
            return scoringFunction.getOwnsOnlyTakenInLine();

        else if (numOwnPlayer == 2 && numOtherPlayer == 0)
            return scoringFunction.getOwnsBothOnlyTakenInLine();

        // Doesn't matter if block the other player if this section doesn't matter
        else if (numOwnPlayer == 2 && numOtherPlayer == 1)
            return -scoringFunction.blockedPlayerInLine();

        return 0;
    }
}
