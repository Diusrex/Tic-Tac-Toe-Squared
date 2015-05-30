package com.diusrex.tictactoe.logic.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.diusrex.tictactoe.data_structures.BoardStatus;
import com.diusrex.tictactoe.data_structures.BoxPosition;
import com.diusrex.tictactoe.data_structures.Line;
import com.diusrex.tictactoe.data_structures.Move;
import com.diusrex.tictactoe.data_structures.Player;
import com.diusrex.tictactoe.data_structures.SectionPosition;
import com.diusrex.tictactoe.logic.GeneralTicTacToeLogic;
import com.diusrex.tictactoe.logic.GridLists;
import com.diusrex.tictactoe.logic.StandardTicTacToeEngine;

public class TestUtils {
    public static class MockBoardStatus extends BoardStatus {

        public MockBoardStatus() {
            super(new StandardTicTacToeEngine());
        }

        public Player playerToGoNext;
        public SectionPosition fakedSectionToPlayIn;

        public Player getActualPlayer() {
            return super.getNextPlayer();
        }

        @Override
        public Player getNextPlayer() {
            if (playerToGoNext != null)
                return playerToGoNext;

            return super.getNextPlayer();
        }

        @Override
        public SectionPosition getSectionToPlayIn() {
            if (fakedSectionToPlayIn != null)
                return fakedSectionToPlayIn;

            return super.getSectionToPlayIn();
        }

        public void useDefaultSection() {
            fakedSectionToPlayIn = null;
        }
    }

    public static void applyMoveToBoard(BoardStatus board, Move move) {
        assertTrue("Reason: " + GeneralTicTacToeLogic.getMoveValidity(board, move),
                GeneralTicTacToeLogic.isValidMove(board, move));

        board.applyMoveIfValid(move);
    }

    public static void testInvalidMoveOnBoard(BoardStatus board, Move move) {
        assertFalse(GeneralTicTacToeLogic.isValidMove(board, move));

        board.applyMoveIfValid(move);
    }

    public static void assertAreEqual(Move expected, Move actual) {
        assertAreEqual(expected.getBox(), actual.getBox());
        assertAreEqual(expected.getSection(), actual.getSection());
        assertTrue(expected.getPlayer() == actual.getPlayer());
    }

    public static void assertAreEqual(SectionPosition expected, SectionPosition actual) {
        assertEquals(expected.getGridX(), actual.getGridX());
        assertEquals(expected.getGridY(), actual.getGridY());
    }

    public static void assertAreEqual(BoxPosition expected, BoxPosition actual) {
        assertEquals(expected.getGridX(), actual.getGridX());
        assertEquals(expected.getGridY(), actual.getGridY());
    }

    // WARNING: Will not update who owns the section
    public static void fillSection(BoardStatus board, SectionPosition toFill) {
        for (BoxPosition pos : GridLists.getAllStandardBoxPositions()) {
            board.setBoxOwner(toFill, pos, board.getNextPlayer());
        }
    }

    public static void testLinesAreEqual(Line expected, Line actual) {
        if (expected == null) {
            assertTrue(actual == null);
        } else if (actual == null) {
            fail();
        }

        if (expected.getStart().equals(actual.getStart())) {
            TestUtils.assertAreEqual(expected.getEnd(), actual.getEnd());
        }

        else {
            TestUtils.assertAreEqual(expected.getStart(), actual.getEnd());
            TestUtils.assertAreEqual(expected.getEnd(), actual.getStart());
        }
    }
}
