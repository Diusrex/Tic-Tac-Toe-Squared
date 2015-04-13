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

import java.util.Stack;

import com.diusrex.tictactoe.data_structures.BoardStatus;
import com.diusrex.tictactoe.data_structures.BoxPosition;
import com.diusrex.tictactoe.data_structures.Grid;
import com.diusrex.tictactoe.data_structures.Line;
import com.diusrex.tictactoe.data_structures.Move;
import com.diusrex.tictactoe.data_structures.Player;
import com.diusrex.tictactoe.data_structures.SectionPosition;


public class TicTacToeEngine {
    enum MoveValidity {
        VALID, OUTSIDE_BOUNDS, OUT_OF_ORDER, IN_WRONG_SECTION, IS_OWNED, MOVE_PLAYER_IS_UNOWNED, MOVE_IS_NULL
    }

    public static MoveValidity getMoveValidity(BoardStatus board, Move move) {
        if (move == null)
            return MoveValidity.MOVE_IS_NULL;

        else if (!isInsideBounds(board, move))
            return MoveValidity.OUTSIDE_BOUNDS;

        else if (!isInOrder(board, move))
            return MoveValidity.OUT_OF_ORDER;

        else if (!isInCorrectSection(board, move))
            return MoveValidity.IN_WRONG_SECTION;

        else if (isOwned(board, move))
            return MoveValidity.IS_OWNED;

        else if (move.getPlayer() == Player.Unowned)
            return MoveValidity.MOVE_PLAYER_IS_UNOWNED;

        else
            return MoveValidity.VALID;
    }

    public static boolean isValidMove(BoardStatus board, Move move) {
        return getMoveValidity(board, move) == MoveValidity.VALID;
    }

    public static void applyMoveIfValid(BoardStatus board, Move move) {
        if (isValidMove(board, move)) {
            applyMove(board, move);
        }
    }

    private static void applyMove(BoardStatus board, Move move) {
        board.applyMove(move);
        updateSectionOwner(board, move);
    }

    private static boolean isInsideBounds(BoardStatus board, Move move) {
        return board.isInsideBounds(move.getSection(), move.getBox());
    }

    private static boolean isInOrder(BoardStatus board, Move move) {
        return move.getPlayer() == board.getNextPlayer();
    }

    private static boolean isOwned(BoardStatus board, Move move) {
        return board.getBoxOwner(move) != Player.Unowned;
    }

    private static boolean isInCorrectSection(BoardStatus board, Move move) {
        SectionPosition requiredSection = board.getSectionToPlayIn();
        SectionPosition actualSection = move.getSection();

        // Checks if it is in the correct section
        return requiredSection.equals(actualSection) || sectionIsFull(board, requiredSection);
    }

    private static boolean sectionIsFull(BoardStatus board, SectionPosition requiredSection) {
        for (int x = 0; x < 3; ++x) {
            for (int y = 0; y < 3; ++y) {
                BoxPosition positionInSection = BoxPosition.make(x, y);
                if (board.getBoxOwner(requiredSection, positionInSection) == Player.Unowned)
                    return false;
            }
        }

        return true;
    }

    private static void updateSectionOwner(BoardStatus board, Move move) {
        SectionPosition changedSection = move.getSection();
        // Cannot take a section from other player
        if (board.getSectionOwner(changedSection) != Player.Unowned)
            return;

        Grid sectionGrid = board.getSectionGrid(changedSection);
        Player detectedSectionOwner = GridChecker.searchForOwner(sectionGrid);
        if (detectedSectionOwner != Player.Unowned) {
            Line winLine = GridChecker.searchForWinLineOrGetNull(sectionGrid);
            board.setSectionOwner(changedSection, winLine, detectedSectionOwner);
        }
    }

    public static SectionPosition getSectionToPlayInNext(Move move) {
        return getSectionToPlayInNext(move.getBox());
    }

    public static SectionPosition getSectionToPlayInNext(BoxPosition pos) {
        return SectionPosition.make(pos.getGridX(), pos.getGridY());
    }

    public static Player getWinner(BoardStatus board) {
        return GridChecker.searchForOwner(board.getMainGrid());
    }

    static final int SIZE_OF_SAVED_MOVE = 5;

    public static String getSaveString(BoardStatus board) {
        Stack<Move> allMoves = board.getAllMoves();
        StringBuffer buffer = new StringBuffer();
        for (Move move : allMoves) {
            buffer.append(moveToString(move));
        }

        return buffer.toString();
    }

    // Will allow any move to be played
    public static BoardStatus loadBoardFromString(String savedBoardStatus) {
        BoardStatus board = new BoardStatus();
        for (int i = 0; i + SIZE_OF_SAVED_MOVE - 2 < savedBoardStatus.length(); i += SIZE_OF_SAVED_MOVE) {
            Move move = stringToMove(savedBoardStatus.substring(i, i + SIZE_OF_SAVED_MOVE));
            applyMove(board, move);
        }

        return board;
    }

    // TODO: Refactor out of engine, most likely into Move/SectionPosition/BoxPosition
    // Format: [SectionPosX][SectionPosY][BoxPosX][BoxPosY][Player]
    public static Move stringToMove(String string) {
        SectionPosition section = stringToSectionPosition(string.substring(0, 2));
        BoxPosition box = stringToBoxPosition(string.substring(2, 4));
        Player player = Player.fromString(string.substring(4, 5));

        return new Move(section, box, player);
    }

    private static String moveToString(Move m) {
        return sectionPositionToString(m.getSection()) + boxPositionToString(m.getBox()) + m.getPlayer().toString();
    }

    public static String sectionPositionToString(SectionPosition sectionPosition) {
        return String.format("%d%d", sectionPosition.getGridX(), sectionPosition.getGridY());
    }
    
    private static String boxPositionToString(BoxPosition box) {
        return String.format("%d%d", box.getGridX(), box.getGridY());
    }

    public static SectionPosition stringToSectionPosition(String string) {
        int totalValue = Integer.parseInt(string);
        int x = totalValue / 10;
        int y = totalValue % 10;

        return SectionPosition.make(x, y);
    }

    public static BoxPosition stringToBoxPosition(String string) {
        int totalValue = Integer.parseInt(string);
        int x = totalValue / 10;
        int y = totalValue % 10;

        return BoxPosition.make(x, y);
    }

    public static boolean boardIsFull(BoardStatus board) {
        for (SectionPosition section : GridLines.getAllStandardSections()) {
            for (BoxPosition box : GridLines.getAllStandardBoxPositions()) {
                if (board.getBoxOwner(section, box) == Player.Unowned)
                    return false;
            }
        }

        return true;
    }
}
