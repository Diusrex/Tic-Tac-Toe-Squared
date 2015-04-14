package com.diusrex.tictactoe.logic;

import java.util.List;

import com.diusrex.tictactoe.data_structures.Grid;
import com.diusrex.tictactoe.data_structures.Line;
import com.diusrex.tictactoe.data_structures.LineIterator;
import com.diusrex.tictactoe.data_structures.Player;

public class StandardGridChecker implements GridChecker {
    @Override
    public boolean possibleToWin(Grid grid) {
        if (grid.getGridOwner() == Player.Unowned) {
            return searchForUnownedLine(grid);
        }

        return false;
    }

    private boolean searchForUnownedLine(Grid grid) {
        List<LineIterator> allIterators = GridLists.getAllLineIterators();

        for (LineIterator iter : allIterators) {
            if (lineEmptyOrOwnedBySinglePlayer(grid, iter))
                return true;
        }

        return false;
    }

    @Override
    public Player searchForOwner(Grid grid) {
        Line foundLine = searchForWinLineOrGetNull(grid);

        if (foundLine != null) {
            return grid.getPointOwner(foundLine.getStart());
        }

        return Player.Unowned;
    }

    @Override
    public Line searchForWinLineOrGetNull(Grid grid) {
        List<LineIterator> allIterators = GridLists.getAllLineIterators();

        for (LineIterator iter : allIterators) {
            if (ownedByOnePlayer(grid, iter)) {
                return iter.getLine();
            }
        }

        return null;
    }

    private boolean ownedByOnePlayer(Grid grid, LineIterator iter) {
        int posInIter = 0;
        final Player firstPlayer = grid.getPointOwner(iter.getCurrent(posInIter));

        if (firstPlayer == Player.Unowned) {
            return false;
        }

        ++posInIter;

        for (; !iter.isDone(posInIter); ++posInIter) {
            final Player currentPlayer = grid.getPointOwner(iter.getCurrent(posInIter));

            if (currentPlayer != firstPlayer)
                return false;
        }

        return true;
    }

    private boolean lineEmptyOrOwnedBySinglePlayer(Grid grid, LineIterator iter) {
        boolean containsPlayerOne = false;
        boolean containsPlayerTwo = false;
        boolean empty = true;

        int posInIter = 0;

        for (; !iter.isDone(posInIter); ++posInIter) {
            final Player currentPlayer = grid.getPointOwner(iter.getCurrent(posInIter));

            if (currentPlayer != Player.Unowned) {
                empty = false;

                if (currentPlayer == Player.Player_1)
                    containsPlayerOne = true;
                else
                    containsPlayerTwo = true;
            }
        }

        return (containsPlayerOne ^ containsPlayerTwo) || empty;
    }
}
