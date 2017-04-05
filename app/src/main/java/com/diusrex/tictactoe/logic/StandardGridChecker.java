package com.diusrex.tictactoe.logic;

import java.util.List;

import com.diusrex.tictactoe.data_structures.Line;
import com.diusrex.tictactoe.data_structures.LineIterator;
import com.diusrex.tictactoe.data_structures.LinesFormed;
import com.diusrex.tictactoe.data_structures.Player;
import com.diusrex.tictactoe.data_structures.grid.Grid;
import com.diusrex.tictactoe.data_structures.position.Position;

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

    @Override
    public boolean possibleToWinGridForPlayerUsingPosition(Grid grid, Position position, Player player) {
        Player other = player.opposite();
        for (LineIterator lineIter : GridLists.getAllLineIterators()) {
            if (!lineIter.containsPosition(position))
                continue;
            boolean hasOtherPlayer = false;

            for (int index = 0; !lineIter.isDone(index); ++index) {
                Position pos = lineIter.getCurrent(index);

                if (grid.getPointOwner(pos) == other)
                    hasOtherPlayer = true;
            }

            if (!hasOtherPlayer)
                return true;
        }

        return false;
    }
    
    @Override
    public void getLinesFormedUsingPosition(Grid grid, LinesFormed linesFormed, Position position) {
        Player main = linesFormed.mainPlayer;
        Player other = main.opposite();
        linesFormed.reset();
        
        for (LineIterator lineIter : GridLists.getAllLineIterators()) {
            if (position != null && !lineIter.containsPosition(position))
                continue;
            
            int mainCount = 0, otherCount = 0;
            
            for (int index = 0; !lineIter.isDone(index); ++index) {
                Position pos = lineIter.getCurrent(index);

                if (grid.getPointOwner(pos) == main)
                    ++mainCount;

                else if (grid.getPointOwner(pos) == other)
                    ++otherCount;
            }

            if (mainCount == 0 && otherCount == 0) {
                ++linesFormed.emptyLines;
            } else if (mainCount == otherCount) {
                // No point, since cancel out
            } else if (mainCount == 3 || otherCount == 3) {
                // Doesn't currently handle this case. Board is already won, so shouldn't care
                // as much about lines.
            } else if (mainCount == 2) {
                if (otherCount == 0)
                    ++linesFormed.twoFormedForMain;
                else // otherCount == 1
                    ++linesFormed.twoWereBlockedForMain;
            } else if (mainCount == 1) {
                if (otherCount == 0)
                    ++linesFormed.oneFormedForMain;
                else // otherCount == 2
                    ++linesFormed.twoWereBlockedForOther;
            } else { // mainCount == 0
                if (otherCount == 2)
                    ++linesFormed.twoFormedForOther;
                else // otherCount == 1
                    ++linesFormed.oneFormedForOther;
            }
        }
    }
    
    @Override
    public void getLinesFormed(Grid grid, LinesFormed linesFormed) {
        getLinesFormedUsingPosition(grid, linesFormed, null);
    }
}
