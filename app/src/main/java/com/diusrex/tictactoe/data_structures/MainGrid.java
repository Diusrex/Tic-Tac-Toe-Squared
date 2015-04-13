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
package com.diusrex.tictactoe.data_structures;

import com.diusrex.tictactoe.logic.GridChecker;



public class MainGrid implements Grid {
    private Player owner;
    private Player[][] boardOwners;

    MainGrid() {
        boardOwners = new Player[BoardStatus.NUMBER_OF_SECTIONS_PER_SIDE][BoardStatus.NUMBER_OF_BOXES_PER_SIDE];

        for (int x = 0; x < boardOwners.length; ++x) {
            for (int y = 0; y < boardOwners.length; ++y) {
                boardOwners[x][y] = Player.Unowned;
            }
        }

        owner = Player.Unowned;
    }

    @Override
    public Player getGridOwner() {
        return owner;
    }

    @Override
    public Player getPointOwner(Position pos) {
        return getOwner(pos.getGridX(), pos.getGridY());
    }

    private Player getOwner(int x, int y) {
        return boardOwners[x][y];
    }

    @Override
    public boolean canBeWon() {
        return GridChecker.possibleToWin(this);
    }

    public void setOwner(SectionPosition pos, Player owner) {
        boardOwners[pos.getGridX()][pos.getGridY()] = owner;
    }

}
