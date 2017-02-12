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
package com.diusrex.tictactoe.data_structures.grid;

import com.diusrex.tictactoe.data_structures.Line;
import com.diusrex.tictactoe.data_structures.LinesFormed;
import com.diusrex.tictactoe.data_structures.Player;
import com.diusrex.tictactoe.data_structures.position.BoxPosition;
import com.diusrex.tictactoe.data_structures.position.Position;
import com.diusrex.tictactoe.logic.TicTacToeEngine;

public class SectionGrid implements Grid {
    static private final int SECTION_SIZE = 3;

    private Player[][] owners;
    private Player owner;

    private Line winningLine;

    // TODO: Alternative methods?
    private final TicTacToeEngine engine;

    public SectionGrid(TicTacToeEngine engine) {
        this.engine = engine;

        owners = new Player[SECTION_SIZE][SECTION_SIZE];
        owner = Player.Unowned;

        for (int x = 0; x < SECTION_SIZE; ++x) {
            for (int y = 0; y < SECTION_SIZE; ++y) {
                owners[x][y] = Player.Unowned;
            }
        }
    }

    public void setSectionOwner(Player owner, Line winningLine) {
        this.owner = owner;
        this.winningLine = winningLine;
    }

    @Override
    public Player getGridOwner() {
        return owner;
    }

    public void setPointOwner(BoxPosition pos, Player newOwner) {
        owners[pos.getGridX()][pos.getGridY()] = newOwner;
        engine.updateSectionOwner(this, newOwner);
    }

    @Override
    public Player getPointOwner(Position pos) {
        return owners[pos.getGridX()][pos.getGridY()];
    }

    @Override
    public boolean canBeWon() {
        return engine.possibleToWin(this);
    }

    public Line getLine() {
        return winningLine;
    }

    public boolean isInsideBounds(BoxPosition pos) {
        return pos.getGridX() >= 0 && pos.getGridX() < SECTION_SIZE && pos.getGridY() >= 0
                && pos.getGridY() < SECTION_SIZE;
    }
}
