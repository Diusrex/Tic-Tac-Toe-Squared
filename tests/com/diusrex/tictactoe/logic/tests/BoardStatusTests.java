package com.diusrex.tictactoe.logic.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.diusrex.tictactoe.data_structures.BoardStatus;
import com.diusrex.tictactoe.data_structures.BoxPosition;
import com.diusrex.tictactoe.data_structures.Move;
import com.diusrex.tictactoe.data_structures.Player;
import com.diusrex.tictactoe.data_structures.SectionPosition;
import com.diusrex.tictactoe.logic.GridConstants;
import com.diusrex.tictactoe.logic.GridLists;
import com.diusrex.tictactoe.logic.StandardTicTacToeEngine;

public class BoardStatusTests {
    BoardStatus board;

    @Before
    public void setup() {
        board = new BoardStatus(new StandardTicTacToeEngine());
    }

    @Test
    public void testGetNextPlayer() {
        assertEquals(Player.Player_1, board.getNextPlayer());

        TestUtils.applyMoveToBoard(board,
                Move.make(board.getSectionToPlayIn(), BoxPosition.make(1, 1), Player.Player_1));

        assertEquals(Player.Player_2, board.getNextPlayer());
    }

    @Test
    public void allPositionsStartAsUnowned() {
        for (SectionPosition section : GridLists.getAllStandardSections()) {
            for (BoxPosition box : GridLists.getAllStandardBoxPositions()) {
                assertEquals(Player.Unowned, board.getBoxOwner(section, box));
            }
        }
    }

    @Test
    public void testIsNotInsideBoundsBox() {
        SectionPosition validSection = SectionPosition.make(0, 0);
        BoxPosition invalidPos = BoxPosition.make(0, GridConstants.SIZE_OF_SECTION);
        assertFalse(board.isInsideBounds(validSection, invalidPos));

        invalidPos = BoxPosition.make(GridConstants.SIZE_OF_SECTION, 0);
        assertFalse(board.isInsideBounds(validSection, invalidPos));

        invalidPos = BoxPosition.make(-1, 0);
        assertFalse(board.isInsideBounds(validSection, invalidPos));

        invalidPos = BoxPosition.make(0, -1);
        assertFalse(board.isInsideBounds(validSection, invalidPos));
    }

    @Test
    public void testIsNotInsideBoundsSection() {
        SectionPosition invalidSection = SectionPosition.make(GridConstants.NUMBER_OF_SECTIONS_PER_SIDE, 0);
        BoxPosition validPos = BoxPosition.make(0, 0);
        assertFalse(board.isInsideBounds(invalidSection, validPos));

        invalidSection = SectionPosition.make(0, GridConstants.NUMBER_OF_SECTIONS_PER_SIDE);
        assertFalse(board.isInsideBounds(invalidSection, validPos));

        invalidSection = SectionPosition.make(-1, 0);
        assertFalse(board.isInsideBounds(invalidSection, validPos));

        invalidSection = SectionPosition.make(0, -1);
        assertFalse(board.isInsideBounds(invalidSection, validPos));
    }

    @Test
    public void testIsInsideBounds() {
        /* Test all sections */
        for (SectionPosition section : GridLists.getAllStandardSections()) {
            for (BoxPosition boxPosition : GridLists.getAllStandardBoxPositions()) {
                assertTrue(board.isInsideBounds(section, boxPosition));
            }
        }
    }

    @Test
    public void testAddingMovesToStack() {
        assertEquals(0, getMovesSize());

        SectionPosition sectionPos = board.getSectionToPlayIn();
        BoxPosition movePos = BoxPosition.make(0, 0);
        Move move = Move.make(sectionPos, movePos, board.getNextPlayer());

        TestUtils.applyMoveToBoard(board, move);

        assertEquals(1, getMovesSize());
        assertEquals(move, board.getAllMoves().peek());
    }

    private int getMovesSize() {
        return board.getAllMoves().size();
    }
}
