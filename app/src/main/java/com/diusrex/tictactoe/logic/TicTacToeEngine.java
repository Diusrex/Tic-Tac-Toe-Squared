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

import com.diusrex.tictactoe.data_structures.Line;
import com.diusrex.tictactoe.data_structures.LinesFormed;
import com.diusrex.tictactoe.data_structures.Player;
import com.diusrex.tictactoe.data_structures.grid.Grid;
import com.diusrex.tictactoe.data_structures.grid.MainGrid;
import com.diusrex.tictactoe.data_structures.grid.SectionGrid;
import com.diusrex.tictactoe.data_structures.position.Position;
import com.diusrex.tictactoe.data_structures.position.SectionPosition;

public abstract class TicTacToeEngine {
    public static final int MAX_NUM_MOVES = 81;

    private GridChecker gridChecker;

    protected TicTacToeEngine(GridChecker gridChecker) {
        this.gridChecker = gridChecker;
    }

    public abstract void updateSectionOwner(SectionGrid section, Player recentMoverOwner);

    public abstract Player getWinner(Grid grid);

    public boolean possibleToWin(Grid grid) {
        return gridChecker.possibleToWin(grid);
    }

    public Player searchForOwner(Grid grid) {
        return gridChecker.searchForOwner(grid);
    }

    public Line searchForWinLineOrGetNull(Grid grid) {
        return gridChecker.searchForWinLineOrGetNull(grid);
    }

    public boolean positionIsImportantToPlayer(Grid grid, Position position, Player player) {
        return gridChecker.possibleToWinGridForPlayerUsingPosition(grid, position, player);
    }
}
