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
import com.diusrex.tictactoe.logic.GridConstants;



public class MainGrid implements Grid {
    private Player owner;
    private SectionGrid[][] sections;

    MainGrid() {
        sections = new SectionGrid[GridConstants.NUMBER_OF_SECTIONS_PER_SIDE][GridConstants.NUMBER_OF_BOXES_PER_SIDE];

        for (int x = 0; x < sections.length; ++x) {
            for (int y = 0; y < sections.length; ++y) {
                sections[x][y] = new SectionGrid();
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
        return getSectionGrid(pos).getGridOwner();
    }

    @Override
    public boolean canBeWon() {
        return GridChecker.possibleToWin(this);
    }

    public void setBoxOwner(SectionPosition sectionPos, BoxPosition pos, Player newOwner) {
        getSectionGrid(sectionPos).setPointOwner(pos, newOwner);
    }
    
    public Player getBoxOwner(SectionPosition section, BoxPosition pos) {
        return getSectionGrid(section).getPointOwner(pos);
    }
    
    public void setOwner(SectionPosition pos, Line winningLine, Player owner) {
        getSectionGrid(pos).setSectionOwner(owner, winningLine);
    }

    public SectionGrid getSectionGrid(SectionPosition pos) {
        return getSectionGrid((Position) pos);
    }
    
    private SectionGrid getSectionGrid(Position pos) {
        return sections[pos.getGridX()][pos.getGridY()];
    }

    public boolean isInsideBounds(SectionPosition sectionPosition, BoxPosition pos) {
        if (sectionPosition.getGridX() < 0 || sectionPosition.getGridX() >= GridConstants.NUMBER_OF_SECTIONS_PER_SIDE
                || sectionPosition.getGridY() < 0 || sectionPosition.getGridY() >= GridConstants.NUMBER_OF_SECTIONS_PER_SIDE)
            return false;

        return getSectionGrid(sectionPosition).isInsideBounds(pos);
    }

    public Line getLine(SectionPosition sectionPosition) {
        return getSectionGrid(sectionPosition).getLine();
    }
}
