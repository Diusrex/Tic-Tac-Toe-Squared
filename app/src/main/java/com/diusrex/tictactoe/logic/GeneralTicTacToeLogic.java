package com.diusrex.tictactoe.logic;

import com.diusrex.tictactoe.data_structures.BoardStatus;
import com.diusrex.tictactoe.data_structures.BoxPosition;
import com.diusrex.tictactoe.data_structures.Move;
import com.diusrex.tictactoe.data_structures.Player;
import com.diusrex.tictactoe.data_structures.SectionPosition;

public class GeneralTicTacToeLogic {
    enum MoveValidity {
        VALID, OUTSIDE_BOUNDS, OUT_OF_ORDER, IN_WRONG_SECTION, IS_OWNED, MOVE_PLAYER_IS_UNOWNED, MOVE_IS_NULL
    }

    public static SectionPosition getSectionToPlayInNext(Move move) {
        return getSectionToPlayInNext(move.getBox());
    }

    public static SectionPosition getSectionToPlayInNext(BoxPosition pos) {
        return SectionPosition.make(pos.getGridX(), pos.getGridY());
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

    public static boolean boardIsFull(BoardStatus board) {
        for (SectionPosition section : GridLists.getAllStandardSections()) {
            for (BoxPosition box : GridLists.getAllStandardBoxPositions()) {
                if (board.getBoxOwner(section, box) == Player.Unowned)
                    return false;
            }
        }

        return true;
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
        for (BoxPosition pos : GridLists.getAllStandardBoxPositions()) {
            if (board.getBoxOwner(requiredSection, pos) == Player.Unowned)
                return false;
        }

        return true;
    }
}
