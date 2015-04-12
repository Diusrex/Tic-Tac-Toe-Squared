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


/*
 * GridChecker
 * This class is built around checking to see if a player has made a pattern of 3 inside of the given grid, or optionally inside of the
 * section within the grid
 */
public class GridChecker {
    static final int LINE_LENGTH = 3;

    public static Player searchForOwner(Grid grid) {
        Line foundLine = searchForLineOrGetNull(grid);

        if (foundLine != null) {
            return grid.getPointOwner(foundLine.getStart());
        }

        return Player.Unowned;
    }

    public static Line searchForLineOrGetNull(Grid grid) {
        if (grid == null) {
            return null;
        }

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
}
