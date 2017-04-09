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
        for (LineIterator iter : GridLists.getAllLineIterators()) {
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
    
    // TODO: Should be updated more
    @Override
    public void getLinesFormedUsingPosition(Grid grid, LinesFormed linesFormed, Position position) {
        Player main = linesFormed.mainPlayer;
        Player other = main.opposite();
        linesFormed.reset();
        
        for (LineIterator lineIter : GridLists.getAllLineIterators()) {
            if (position != null && !lineIter.containsPosition(position))
                continue;
            
            int mainCount = 0, otherCount = 0;
            int mainCanOwn = 0, otherCanOwn = 0;
            
            // TODO: More counts -> safe, unsafe, able to win, unable to win, etc.
            for (int index = 0; !lineIter.isDone(index); ++index) {
                Position pos = lineIter.getCurrent(index);

                if (grid.getPointOwner(pos) == main) {
                    ++mainCount;
                } else if (grid.getPointOwner(pos) == other) {
                    ++otherCount;
                } else {
                    // Point is unowned, so see who can take ownership of it.
                    if (grid.pointCanBeWonByPlayer(pos, main)) {
                        ++mainCanOwn;
                    }
                    if (grid.pointCanBeWonByPlayer(pos, other)) {
                        ++otherCanOwn;
                    }
                }
            }
            
            boolean canBeWonByMain =
                    (mainCount + mainCanOwn == 3);
            boolean canBeWonByOther =
                    (otherCount + otherCanOwn == 3);
            
            if (mainCount == 0 && canBeWonByMain) {
                // Both are 0, and main can win this line
                ++linesFormed.unownedButWinnableForMain;
            }
            
            if (otherCount == 0 && canBeWonByOther) {
                // Both are 0, and main can win this line
                ++linesFormed.unownedButWinnableForOther;
            }
            
            // Don't do any more checks if they are equal
            if (mainCount == otherCount) {
                continue;
            }
            
            
            // Quickly calculate for main
            if (mainCount == 1) {
                if (canBeWonByMain) {
                    ++linesFormed.oneFormedForMain;
                } else if (otherCount != 2){
                    // Only count the one as being blocked if it isn't blocking a two
                    ++linesFormed.oneInRowWasBlockedForMain;
                }
            } else if (mainCount == 2) {
                if (canBeWonByMain) {
                    ++linesFormed.twoFormedForMain;
                } else { // Is blocked by unwinnable or other.
                    ++linesFormed.twoInRowWasBlockedForMain;
                }
            } else if (mainCount == 3) {
                ++linesFormed.threeFormedForMain;
            }
            
            // Quickly count for other
            if (otherCount == 1) {
                if (canBeWonByOther) {
                    ++linesFormed.oneFormedForOther;
                } else if (mainCount != 2){
                    // Only count the one as being blocked if it isn't blocking a two
                    ++linesFormed.oneInRowWasBlockedForOther;
                }
            } else if (otherCount == 2) {
                if (canBeWonByOther) {
                    ++linesFormed.twoFormedForOther;
                } else { // Is blocked by unwinnable or main player.
                    ++linesFormed.twoInRowWasBlockedForOther;
                }
            } else if (otherCount == 3) {
                ++linesFormed.threeFormedForOther;
            }
        }
    }
    
    @Override
    public void getLinesFormed(Grid grid, LinesFormed linesFormed) {
        getLinesFormedUsingPosition(grid, linesFormed, null);
    }
}
