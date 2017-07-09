package com.diusrex.tictactoe.logic.tests;

import com.diusrex.tictactoe.data_structures.*;
import com.diusrex.tictactoe.data_structures.board_status.BoardStatus;
import com.diusrex.tictactoe.data_structures.position.BoxPosition;
import com.diusrex.tictactoe.data_structures.position.SectionPosition;
import com.diusrex.tictactoe.logic.GeneralTicTacToeLogic;
import com.diusrex.tictactoe.logic.GridLists;
import com.diusrex.tictactoe.logic.StandardTicTacToeEngine;

import static org.junit.Assert.*;

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

        public void changeBoxOwner(SectionPosition sectionPos, BoxPosition pos, Player newOwner) {
            sectionsOwnersGrid.setBoxOwner(sectionPos, pos, newOwner);
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
    public static void fillSection(MockBoardStatus board, SectionPosition toFill) {
        for (BoxPosition pos : GridLists.getAllStandardBoxPositions()) {
            board.changeBoxOwner(toFill, pos, board.getNextPlayer());
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
