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

import java.util.Arrays;
import java.util.Stack;

import com.diusrex.tictactoe.logic.TicTacToeEngine;
import com.diusrex.tictactoe.logic.UndoAction;

public class BoardStatus {
    private static final SectionPosition DEFAULT_STARTING_SECTION_TO_PLAY_IN = SectionPosition.make(1, 1);

    private SectionPosition sectionToPlayIn;
    private Player nextPlayer;

    private MainGrid sectionsOwnersGrid;
    private SectionGrid[][] sections;

    private Stack<Move> allMoves;

    public BoardStatus() {
        this(DEFAULT_STARTING_SECTION_TO_PLAY_IN);
    }

    public BoardStatus(SectionPosition startingSection) {
        nextPlayer = Player.Player_1;

        sectionsOwnersGrid = new MainGrid();
        sections = new SectionGrid[NUMBER_OF_SECTIONS_PER_SIDE][NUMBER_OF_SECTIONS_PER_SIDE];
        for (int x = 0; x < NUMBER_OF_SECTIONS_PER_SIDE; ++x) {
            for (int y = 0; y < NUMBER_OF_SECTIONS_PER_SIDE; ++y) {
                sections[x][y] = new SectionGrid();
            }
        }

        sectionToPlayIn = startingSection;

        allMoves = new Stack<Move>();
    }

    public void undoLastMove() {
        // Cannot do anything in this case
        if (allMoves.size() == 0)
            return;

        Move undoneTopMove = allMoves.pop();

        setBoxOwner(undoneTopMove.getSection(), undoneTopMove.getBox(), Player.Unowned);

        if (UndoAction.moveLostOwnership(getSection(undoneTopMove.getSection()), undoneTopMove)) {
            setSectionOwner(undoneTopMove.getSection(), null, Player.Unowned);
        }

        sectionToPlayIn = UndoAction.getSectionToPlayIn(allMoves, undoneTopMove);
    }

    public SectionPosition getSectionToPlayIn() {
        return sectionToPlayIn;
    }

    public void setSectionOwner(SectionPosition changedSection, Line line, Player owner) {
        sectionsOwnersGrid.setOwner(changedSection, owner);

        SectionGrid section = getSection(changedSection);
        section.setSectionOwner(owner, line);
    }

    public void applyMove(Move move) {
        allMoves.push(move);

        sectionToPlayIn = TicTacToeEngine.getSectionToPlayInNext(move);

        setBoxOwner(move);
        nextPlayer = move.getPlayer().opposite();
    }

    public void setBoxOwner(Move move) {
        setBoxOwner(move.getSection(), move.getBox(), move.getPlayer());
    }

    public void setBoxOwner(SectionPosition sectionPos, BoxPosition pos, Player newOwner) {
        SectionGrid section = getSection(sectionPos);
        section.setPointOwner(pos, newOwner);
    }

    public Player getBoxOwner(Move move) {
        return getBoxOwner(move.getSection(), move.getBox());
    }

    public Player getBoxOwner(SectionPosition section, BoxPosition pos) {
        return getSection(section).getPointOwner(pos);
    }

    private SectionGrid getSection(SectionPosition section) {
        return sections[section.getGridX()][section.getGridY()];
    }

    public Player getSectionOwner(SectionPosition changedSection) {
        return getSection(changedSection).getGridOwner();
    }

    public Player getNextPlayer() {
        return nextPlayer;
    }

    public boolean isInsideBounds(SectionPosition sectionPosition, BoxPosition pos) {
        if (sectionPosition.getGridX() < 0 || sectionPosition.getGridX() >= NUMBER_OF_SECTIONS_PER_SIDE
                || sectionPosition.getGridY() < 0 || sectionPosition.getGridY() >= NUMBER_OF_SECTIONS_PER_SIDE)
            return false;

        return getSection(sectionPosition).isInsideBounds(pos);
    }

    public Grid getSectionGrid(SectionPosition section) {
        return sections[section.getGridX()][section.getGridY()];
    }

    public Grid getOwnerGrid() {
        return sectionsOwnersGrid;
    }

    public Stack<Move> getAllMoves() {
        return allMoves;
    }

    public Line getLine(SectionPosition sectionPosition) {
        SectionGrid section = getSection(sectionPosition);
        return section.getLine();
    }

    public Grid getMainGrid() {
        return sectionsOwnersGrid;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BoardStatus other = (BoardStatus) obj;
        if (allMoves == null) {
            if (other.allMoves != null)
                return false;
        } else if (allMoves.size() != other.allMoves.size())
            return false;
        if (!sectionsOwnersGrid.equals(other.sectionsOwnersGrid))
            return false;
        if (!Arrays.deepEquals(sections, other.sections))
            return false;
        if (sectionToPlayIn == null) {
            if (other.sectionToPlayIn != null)
                return false;
        } else if (!sectionToPlayIn.equals(other.sectionToPlayIn))
            return false;
        return true;
    }
}
