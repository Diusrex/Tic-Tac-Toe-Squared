/*
 * Copyright 2015 Morgan Redshaw
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 **/
package com.diusrex.tictactoe.logic;

import java.util.List;

import com.diusrex.tictactoe.data_structures.Grid;
import com.diusrex.tictactoe.data_structures.Line;
import com.diusrex.tictactoe.data_structures.LineIterator;
import com.diusrex.tictactoe.data_structures.Player;

/*
 * GridChecker
 * This class is built around checking to see if a player has made a pattern of 3 inside of the given grid, or optionally inside of the
 * section within the grid
 */
public class GridChecker {
    static final int LINE_LENGTH = 3;

    public static boolean possibleToWin(Grid grid) {
        if (grid.getGridOwner() == Player.Unowned) {
            return searchForUnownedLine(grid);
        }

        return false;
    }

    private static boolean searchForUnownedLine(Grid grid) {
        List<LineIterator> allIterators = GridLines.getAllLineIterators();

        for (LineIterator iter : allIterators) {
            if (lineEmptyOrOwnedBySinglePlayer(grid, iter))
                return true;
        }

        return false;
    }

    public static Player searchForOwner(Grid grid) {
        Line foundLine = searchForWinLineOrGetNull(grid);

        if (foundLine != null) {
            return grid.getPointOwner(foundLine.getStart());
        }

        return Player.Unowned;
    }

    public static Line searchForWinLineOrGetNull(Grid grid) {
        List<LineIterator> allIterators = GridLines.getAllLineIterators();

        for (LineIterator iter : allIterators) {
            if (ownedByOnePlayer(grid, iter)) {
                return iter.getLine();
            }
        }

        return null;
    }

    private static boolean ownedByOnePlayer(Grid grid, LineIterator iter) {
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

    private static boolean lineEmptyOrOwnedBySinglePlayer(Grid grid, LineIterator iter) {
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
