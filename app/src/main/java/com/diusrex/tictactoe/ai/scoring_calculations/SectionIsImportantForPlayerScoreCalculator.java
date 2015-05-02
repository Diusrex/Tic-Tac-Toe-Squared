package com.diusrex.tictactoe.ai.scoring_calculations;

import com.diusrex.tictactoe.data_structures.Grid;
import com.diusrex.tictactoe.data_structures.LineIterator;
import com.diusrex.tictactoe.data_structures.Player;
import com.diusrex.tictactoe.data_structures.Position;
import com.diusrex.tictactoe.logic.GridLists;

public class SectionIsImportantForPlayerScoreCalculator implements PlayerScoreCalculator {

    @Override
    public int calculateSetupScore(Player currentPlayer, Grid grid, ScoringFunction scoringFunction) {
        int score = 0;

        score += calculateAllLineScores(currentPlayer, grid, scoringFunction);

        return score;
    }

    // Must calculate score for both winning lines, and enemy winning lines
    private int calculateAllLineScores(Player currentPlayer, Grid grid, ScoringFunction scoringFunction) {
        int score = 0;

        for (LineIterator iter : GridLists.getAllLineIterators())
            score += calculateLineScore(grid, iter, currentPlayer, scoringFunction);

        return score;
    }

    private int calculateLineScore(Grid grid, LineIterator iter, Player currentPlayer, ScoringFunction scoringFunction) {
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
