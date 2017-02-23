package com.diusrex.tictactoe.ai.scoring_calculations.fixed;

import com.diusrex.tictactoe.data_structures.Player;
import com.diusrex.tictactoe.data_structures.grid.Grid;

public interface PlayerGridScoreCalculator {
    int calculateGridScoreForPlayer(Player currentPlayer, Grid grid, GridScoringFunction scoringFunction);
}
