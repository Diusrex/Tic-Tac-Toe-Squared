package com.diusrex.tictactoe.ai.scoring_calculations;

import com.diusrex.tictactoe.ai.scoring_calculations.fixed.ScoringFunction;
import com.diusrex.tictactoe.data_structures.Player;
import com.diusrex.tictactoe.data_structures.grid.Grid;

public interface PlayerScoreCalculator {
    int calculateSetupScore(Player currentPlayer, Grid grid, ScoringFunction scoringFunction);
}
