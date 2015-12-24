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

import com.diusrex.tictactoe.data_structures.Grid;
import com.diusrex.tictactoe.data_structures.Line;
import com.diusrex.tictactoe.data_structures.Player;
import com.diusrex.tictactoe.data_structures.Position;

/*
 * GridChecker
 * This class is built around checking to see if a player has made a pattern of 3 inside of the given grid, or optionally inside of the
 * section within the grid
 */
public interface GridChecker {
    boolean possibleToWin(Grid grid);

    Player searchForOwner(Grid grid);

    Line searchForWinLineOrGetNull(Grid grid);

    boolean possibleToWinGridForPlayerUsingPosition(Grid grid, Position position, Player player);
}
