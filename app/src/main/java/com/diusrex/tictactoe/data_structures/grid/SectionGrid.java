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

    private Player owner;
    private Line winningLine;
    
    private Player[][] pointOwners;

    private LinesFormed linesFormed;

    private final TicTacToeEngine engine;

    public SectionGrid(TicTacToeEngine engine) {
        this.engine = engine;

        owner = Player.Unowned;
        pointOwners = new Player[SECTION_SIZE][SECTION_SIZE];

        for (int x = 0; x < SECTION_SIZE; ++x) {
            for (int y = 0; y < SECTION_SIZE; ++y) {
                pointOwners[x][y] = Player.Unowned;
            }
        }
        
        // Choose arbitrary player
        linesFormed = new LinesFormed(Player.Player_1);
        // Setup lines formed.
        engine.getLinesFormed(this, linesFormed);
    }

    @Override
    public Player getGridOwner() {
        return owner;
    }

    // Can add or remove ownership of this section. Can only remove
    // ownership if move was undone.
    public void setPointOwner(BoxPosition pos, Player newOwner) {
        pointOwners[pos.getGridX()][pos.getGridY()] = newOwner;
        engine.getLinesFormed(this, linesFormed);
        
        if (owner == Player.Unowned) {
            // Not owned, so see if either player should be winning.
            if (sectionMustBeOwned()) {
                // The player who just played is the owner
                owner = newOwner;
                winningLine = engine.searchForWinLineOrGetNull(this);
            }
        } else {
            // Was owned, so see if the player lost the ownership.
            if (!sectionMustBeOwned()) {
                // They lost ownership
                owner = Player.Unowned;
                winningLine = null;
            }
        }
    }
    
    private boolean sectionMustBeOwned() {
        return linesFormed.threeFormedForMain != 0 ||
                linesFormed.threeFormedForOther != 0;
    }

    @Override
    public Player getPointOwner(Position pos) {
        return pointOwners[pos.getGridX()][pos.getGridY()];
    }

    @Override
    public boolean canBeWon() {
        return canBeWonByPlayer(Player.Player_1) || canBeWonByPlayer(Player.Player_2);
    }

    @Override
    public boolean canBeWonByPlayer(Player player) {
        if (owner != Player.Unowned) {
            return false;
        }

        if (player == Player.Unowned) {
            return true;
        } else if (player == linesFormed.mainPlayer) {
            return linesFormed.unownedButWinnableForMain + linesFormed.oneFormedForMain
                    + linesFormed.twoFormedForMain > 0;
        } else {
            return linesFormed.unownedButWinnableForOther + linesFormed.oneFormedForOther
                    + linesFormed.twoFormedForOther > 0;
        }
    }

    @Override
    public boolean pointCanBeWonByPlayer(Position pos, Player player) {
        return getPointOwner(pos) == Player.Unowned;
    }

    @Override
    public void getLinesFormed(LinesFormed linesFormed) {
        linesFormed.copyFrom(this.linesFormed);
    }

    public Line getWinLine() {
        return winningLine;
    }

    public boolean isInsideBounds(BoxPosition pos) {
        return pos.getGridX() >= 0 && pos.getGridX() < SECTION_SIZE && pos.getGridY() >= 0
                && pos.getGridY() < SECTION_SIZE;
    }
}
