package com.diusrex.tictactoe.ai.scoring_calculations.fixed.tests;

import com.diusrex.tictactoe.data_structures.LinesFormed;
import com.diusrex.tictactoe.data_structures.Player;
import com.diusrex.tictactoe.data_structures.grid.Grid;
import com.diusrex.tictactoe.data_structures.position.Position;

// Convenience class that is meant to make testing functions that use a grid easier.
public class TestingGrid implements Grid {
    
    public TestingGrid(Player owner, Player[][] ownership) {
        this.owner = owner;
        this.ownership = ownership;
    }

    public Player owner;
    @Override
    public Player getGridOwner() {
        return owner;
    }
    
    public Player[][] ownership;

    @Override
    public Player getPointOwner(Position pos) {
        return ownership[pos.getGridX()][pos.getGridY()];
    }

    public boolean isWinnable = false;
    @Override
    public boolean canBeWon() {
        return isWinnable;
    }
    
    public LinesFormed allLinesFormed;

    @Override
    public void getLinesFormed(LinesFormed linesFormed) {
        linesFormed.copyFrom(allLinesFormed);
    }

    @Override
    public boolean canBeWonByPlayer(Player player) {
        return isWinnable;
    }

    @Override
    public boolean pointCanBeWonByPlayer(Position pos, Player player) {
        return isWinnable;
    }

}
