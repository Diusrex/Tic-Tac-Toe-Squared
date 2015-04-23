package com.diusrex.tictactoe.ai;

import com.diusrex.tictactoe.data_structures.Grid;
import com.diusrex.tictactoe.data_structures.Player;

public interface ScoreCalculator {
    public int calculateSetupScore(Player currentPlayer, Grid grid, ScoringFunction scoringFunction);
}
