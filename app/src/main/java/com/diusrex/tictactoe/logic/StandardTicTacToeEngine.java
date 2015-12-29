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
import com.diusrex.tictactoe.data_structures.Move;
import com.diusrex.tictactoe.data_structures.Player;
import com.diusrex.tictactoe.data_structures.SectionGrid;


public class StandardTicTacToeEngine extends TicTacToeEngine {
    public StandardTicTacToeEngine() {
        super(new StandardGridChecker());
    }

    @Override
    public void updateSectionOwner(SectionGrid section, Player recentMoverOwner) {
        // Cannot take a section from other player
        if (section.getGridOwner() != Player.Unowned)
            return;

        Player detectedSectionOwner = searchForOwner(section);
        if (detectedSectionOwner != Player.Unowned) {
            Line winLine = searchForWinLineOrGetNull(section);
            section.setSectionOwner(detectedSectionOwner, winLine);
        }
    }

    @Override
    public Player getWinner(Grid grid) {
        return searchForOwner(grid);
    }
}
