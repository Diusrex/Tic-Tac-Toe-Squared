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

import java.util.Stack;

import com.diusrex.tictactoe.logic.GeneralTicTacToeLogic;
import com.diusrex.tictactoe.logic.TicTacToeEngine;
import com.diusrex.tictactoe.logic.UndoAction;

public class BoardStatus {
    private static final SectionPosition DEFAULT_STARTING_SECTION_TO_PLAY_IN = SectionPosition.make(1, 1);

    private final TicTacToeEngine engine;
    private SectionPosition sectionToPlayIn;
    private Player nextPlayer;

    private MainGrid sectionsOwnersGrid;

    private Stack<Move> allMoves;

    public BoardStatus(TicTacToeEngine engine) {
        this.engine = engine;

        nextPlayer = Player.Player_1;

        sectionsOwnersGrid = new MainGrid(engine);

        sectionToPlayIn = DEFAULT_STARTING_SECTION_TO_PLAY_IN;

        allMoves = new Stack<Move>();
    }
    
    public boolean possibleToWin() {
        return engine.possibleToWin(sectionsOwnersGrid);
    }

    public void undoLastMove() {
        // Cannot do anything in this case
        if (allMoves.size() == 0)
            return;

        Move undoneTopMove = allMoves.pop();

        setBoxOwner(undoneTopMove.getSection(), undoneTopMove.getBox(), Player.Unowned);

        if (UndoAction.moveLostOwnership(engine, getSectionGrid(undoneTopMove.getSection()), undoneTopMove)) {
            setSectionOwner(undoneTopMove.getSection(), null, Player.Unowned);
        }

        sectionToPlayIn = UndoAction.getSectionToPlayIn(allMoves, undoneTopMove);
    }

    public SectionPosition getSectionToPlayIn() {
        return sectionToPlayIn;
    }

    public void setSectionOwner(SectionPosition changedSection, Line line, Player owner) {
        sectionsOwnersGrid.setOwner(changedSection, line, owner);
    }
    
    public Player getWinner() {
        return engine.getWinner(sectionsOwnersGrid);
    }
    
    public void applyMoveIfValid(Move move) {
        if (GeneralTicTacToeLogic.isValidMove(this, move)) {
            applyMove(move);
            
            engine.updateSectionOwner(getSectionGrid(move.getSection()), move);
        }
    }

    // TODO: Private?
    public void applyMove(Move move) {
        allMoves.push(move);

        sectionToPlayIn = GeneralTicTacToeLogic.getSectionToPlayInNext(move);

        setBoxOwner(move);
        nextPlayer = move.getPlayer().opposite();
    }

    public void setBoxOwner(Move move) {
        setBoxOwner(move.getSection(), move.getBox(), move.getPlayer());
    }

    public void setBoxOwner(SectionPosition sectionPos, BoxPosition pos, Player newOwner) {
        sectionsOwnersGrid.setBoxOwner(sectionPos, pos, newOwner);
    }

    public Player getBoxOwner(Move move) {
        return getBoxOwner(move.getSection(), move.getBox());
    }

    public Player getBoxOwner(SectionPosition section, BoxPosition pos) {
        return sectionsOwnersGrid.getBoxOwner(section, pos);
    }

    public Player getSectionOwner(SectionPosition changedSection) {
        return sectionsOwnersGrid.getPointOwner(changedSection);
    }

    public Player getNextPlayer() {
        return nextPlayer;
    }

    public boolean isInsideBounds(SectionPosition sectionPosition, BoxPosition pos) {
        return sectionsOwnersGrid.isInsideBounds(sectionPosition, pos);
    }

    public SectionGrid getSectionGrid(SectionPosition section) {
        return sectionsOwnersGrid.getSectionGrid(section);
    }

    // TODO: This is probably unneeded now
    public Grid getOwnerGrid() {
        return sectionsOwnersGrid;
    }

    public Stack<Move> getAllMoves() {
        return allMoves;
    }

    public Line getLine(SectionPosition sectionPosition) {
        return sectionsOwnersGrid.getLine(sectionPosition);
    }

    public Grid getMainGrid() {
        return sectionsOwnersGrid;
    }

    
}
