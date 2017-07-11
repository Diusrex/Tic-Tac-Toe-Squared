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
package com.diusrex.tictactoe.data_structures.board_status;

import java.util.Stack;

import com.diusrex.tictactoe.data_structures.Line;
import com.diusrex.tictactoe.data_structures.LinesFormed;
import com.diusrex.tictactoe.data_structures.Move;
import com.diusrex.tictactoe.data_structures.Player;
import com.diusrex.tictactoe.data_structures.grid.Grid;
import com.diusrex.tictactoe.data_structures.grid.MainGrid;
import com.diusrex.tictactoe.data_structures.position.BoxPosition;
import com.diusrex.tictactoe.data_structures.position.SectionPosition;
import com.diusrex.tictactoe.logic.GeneralTicTacToeLogic;
import com.diusrex.tictactoe.logic.TicTacToeEngine;
import com.diusrex.tictactoe.logic.UndoAction;

public class BoardStatus implements Cloneable {
    private static final SectionPosition DEFAULT_STARTING_SECTION_TO_PLAY_IN = SectionPosition.make(1, 1);

    private final TicTacToeEngine engine;
    SectionPosition sectionToPlayIn;
    Player nextPlayer;

    protected MainGrid sectionsOwnersGrid;

    private Stack<Move> allMoves;

    public BoardStatus(TicTacToeEngine engine) {
        this.engine = engine;

        nextPlayer = Player.Player_1;

        sectionsOwnersGrid = new MainGrid(engine);

        sectionToPlayIn = DEFAULT_STARTING_SECTION_TO_PLAY_IN;

        allMoves = new Stack<>();
    }

    public BoardStatus(BoardStatus board) {
        this.engine = board.engine;

        nextPlayer = Player.Player_1;

        sectionsOwnersGrid = new MainGrid(engine);

        sectionToPlayIn = DEFAULT_STARTING_SECTION_TO_PLAY_IN;

        allMoves = new Stack<>();
    }

    @SuppressWarnings("unchecked")
    public Object clone() throws CloneNotSupportedException {
        BoardStatus other = (BoardStatus) super.clone();

        other.sectionToPlayIn = sectionToPlayIn;
        other.nextPlayer = nextPlayer;
        other.sectionsOwnersGrid = sectionsOwnersGrid;
        other.allMoves = (Stack<Move>) allMoves.clone();

        return other;
    }

    public boolean possibleToWin() {
        return engine.possibleToWin(sectionsOwnersGrid);
    }

    public void undoLastMove() {
        // Cannot do anything in this case
        if (!ableToUndoLastMove())
            return;

        Move undoneTopMove = allMoves.pop();

        sectionsOwnersGrid.undoMove(undoneTopMove);


        nextPlayer = nextPlayer.opposite();
        sectionToPlayIn = UndoAction.getSectionToPlayIn(allMoves, undoneTopMove);
    }

    public boolean ableToUndoLastMove() {
        return allMoves.size() != 0;
    }

    public SectionPosition getSectionToPlayIn() {
        return sectionToPlayIn;
    }

    public Player getWinner() {
        return sectionsOwnersGrid.getGridOwner();
    }

    public boolean isValidMove(Move move) {
        return GeneralTicTacToeLogic.isValidMove(this, move);
    }

    public void applyMoveIfValid(Move move) {
        if (isValidMove(move)) {
            applyMove(move);
        }
    }

    private void applyMove(Move move) {
        allMoves.push(move);

        sectionToPlayIn = GeneralTicTacToeLogic.getSectionToPlayInNext(move);

        setBoxOwner(move.getSection(), move.getBox(), move.getPlayer());
        nextPlayer = nextPlayer.opposite();
    }

    protected void setBoxOwner(SectionPosition sectionPos, BoxPosition pos, Player newOwner) {
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

    public Stack<Move> getAllMoves() {
        return allMoves;
    }

    public Line getSectionWinLine(SectionPosition sectionPosition) {
        return sectionsOwnersGrid.getLine(sectionPosition);
    }

    public Line getWinLineOrNull() {
        return engine.searchForWinLineOrGetNull(sectionsOwnersGrid);
    }

    public boolean sectionIsImportantToPlayer(SectionPosition section, Player player) {
        return engine.positionIsImportantToPlayer(sectionsOwnersGrid, section, player);
    }
    
    public Grid getMainGrid() {
        return sectionsOwnersGrid;
    }
    
    public Grid getSubGrid(SectionPosition section) {
        return sectionsOwnersGrid.getGrid(section);
    }

    public void getLinesUsingSection(SectionPosition section, LinesFormed linesFormed) {
        engine.getLinesFormedUsingPosition(sectionsOwnersGrid, linesFormed, section);
    }
}
