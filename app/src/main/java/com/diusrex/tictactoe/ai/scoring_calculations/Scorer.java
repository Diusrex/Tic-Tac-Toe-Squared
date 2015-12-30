package com.diusrex.tictactoe.ai.scoring_calculations;


import com.diusrex.tictactoe.data_structures.BoardStatus;
import com.diusrex.tictactoe.data_structures.MainGrid;
import com.diusrex.tictactoe.data_structures.Player;

public abstract class Scorer {
    public abstract int calculateScore(Player positivePlayer, BoardStatus board, MainGrid mainGrid);

    public int getTieScore() {
        return 0;
    }
}
