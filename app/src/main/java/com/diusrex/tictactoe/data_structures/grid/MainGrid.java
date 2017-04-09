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
import com.diusrex.tictactoe.data_structures.Move;
import com.diusrex.tictactoe.data_structures.Player;
import com.diusrex.tictactoe.data_structures.position.BoxPosition;
import com.diusrex.tictactoe.data_structures.position.Position;
import com.diusrex.tictactoe.data_structures.position.SectionPosition;
import com.diusrex.tictactoe.logic.GridConstants;
import com.diusrex.tictactoe.logic.TicTacToeEngine;

public class MainGrid implements Grid {
    private Player owner;
    protected SectionGrid[][] sections;

    private final TicTacToeEngine engine;

    private LinesFormed linesFormed;

    public MainGrid(TicTacToeEngine engine) {
        this.engine = engine;
        sections = new SectionGrid[GridConstants.NUMBER_OF_SECTIONS_PER_SIDE][GridConstants.NUMBER_OF_BOXES_PER_SIDE];

        for (int x = 0; x < sections.length; ++x) {
            for (int y = 0; y < sections.length; ++y) {
                sections[x][y] = new SectionGrid(engine);
            }
        }

        owner = Player.Unowned;

        // Choose arbitrary player
        linesFormed = new LinesFormed(Player.Player_1);
        // Setup lines formed.
        engine.getLinesFormed(this, linesFormed);
    }

    @Override
    public Player getGridOwner() {
        return owner;
    }

    @Override
    public Player getPointOwner(Position pos) {
        return getSectionGrid(pos).getGridOwner();
    }

    @Override
    public boolean canBeWon() {
        if (owner != Player.Unowned) {
            return false;
        }

        return canBeWonByPlayer(Player.Player_1) || canBeWonByPlayer(Player.Player_2);
    }

    @Override
    public boolean canBeWonByPlayer(Player player) {
        if (owner != Player.Unowned) {
            return false;
        }

        if (player == Player.Unowned) {
            return false;
        } else if (player == linesFormed.mainPlayer) {
            return linesFormed.unownedButWinnableForMain + linesFormed.oneFormedForMain
                    + linesFormed.twoFormedForMain > 0;
        } else {
            return linesFormed.unownedButWinnableForOther + linesFormed.oneFormedForOther
                    + linesFormed.twoFormedForOther > 0;
        }
    }

    @Override
    public boolean pointCanBeWonByPlayer(Position pos, Player other) {
        return getSectionGrid(pos).canBeWonByPlayer(other);
    }

    @Override
    public void getLinesFormed(LinesFormed linesFormed) {
        linesFormed.copyFrom(this.linesFormed);
    }

    public void setBoxOwner(SectionPosition sectionPos, BoxPosition pos, Player newOwner) {
        SectionGrid changedSection = getSectionGrid(sectionPos);
        Player originalOwner = changedSection.getGridOwner();

        boolean p1Winnable = changedSection.canBeWonByPlayer(Player.Player_1);
        boolean p2Winnable = changedSection.canBeWonByPlayer(Player.Player_2);

        changedSection.setPointOwner(pos, newOwner);

        if (originalOwner != changedSection.getGridOwner()) {
            owner = engine.getWinner(this);
            
            // Update lines owned
            engine.getLinesFormed(this, linesFormed);
        } else if (p1Winnable != changedSection.canBeWonByPlayer(Player.Player_1)
                || p2Winnable != changedSection.canBeWonByPlayer(Player.Player_2)) {
            
            // Update lines owned, since their values may have changed.
            engine.getLinesFormed(this, linesFormed);
        }
    }

    public void undoMove(Move undoneTopMove) {
        assert(undoneTopMove.getPlayer() != Player.Unowned);
        
        setBoxOwner(undoneTopMove.getSection(), undoneTopMove.getBox(), Player.Unowned);
    }

    public Player getBoxOwner(SectionPosition section, BoxPosition pos) {
        return getSectionGrid(section).getPointOwner(pos);
    }

    public Grid getGrid(SectionPosition pos) {
        return sections[pos.getGridX()][pos.getGridY()];
    }

    private SectionGrid getSectionGrid(SectionPosition pos) {
        return getSectionGrid((Position) pos);
    }

    private SectionGrid getSectionGrid(Position pos) {
        return sections[pos.getGridX()][pos.getGridY()];
    }

    public boolean isInsideBounds(SectionPosition sectionPosition, BoxPosition pos) {
        return sectionPosition.getGridX() >= 0
                && sectionPosition.getGridX() < GridConstants.NUMBER_OF_SECTIONS_PER_SIDE
                && sectionPosition.getGridY() >= 0
                && sectionPosition.getGridY() < GridConstants.NUMBER_OF_SECTIONS_PER_SIDE
                && getSectionGrid(sectionPosition).isInsideBounds(pos);
    }

    public Line getLine(SectionPosition sectionPosition) {
        return getSectionGrid(sectionPosition).getWinLine();
    }

}
