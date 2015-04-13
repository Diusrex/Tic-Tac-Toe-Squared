package com.diusrex.tictactoe.logic.tests;

import junit.framework.Assert;

import com.diusrex.tictactoe.data_structures.BoardStatus;
import com.diusrex.tictactoe.data_structures.BoxPosition;
import com.diusrex.tictactoe.data_structures.Line;
import com.diusrex.tictactoe.data_structures.Move;
import com.diusrex.tictactoe.data_structures.Player;
import com.diusrex.tictactoe.data_structures.SectionPosition;
import com.diusrex.tictactoe.logic.GridLists;
import com.diusrex.tictactoe.logic.TicTacToeEngine;

public class TestUtils {
    public static class MockBoardStatus extends BoardStatus {

        // TODO: This constructor not be needed
        public MockBoardStatus(SectionPosition pos) {
            super(pos);
        }

        public MockBoardStatus() {
            super();
        }

        public Player playerToGoNext;
        public SectionPosition fakedSectionToPlayIn;

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
        Assert.assertTrue("Reason: " + TicTacToeEngine.getMoveValidity(board, move),
                TicTacToeEngine.isValidMove(board, move));

        TicTacToeEngine.applyMoveIfValid(board, move);
    }

    public static void testInvalidMoveOnBoard(BoardStatus board, Move move) {
        Assert.assertFalse(TicTacToeEngine.isValidMove(board, move));
        TicTacToeEngine.applyMoveIfValid(board, move);
    }
    
    public static void assertAreEqual(Move expected, Move actual) {
        assertAreEqual(expected.getBox(), actual.getBox());
        assertAreEqual(expected.getSection(), actual.getSection());
        Assert.assertTrue(expected.getPlayer() == actual.getPlayer());
    }
    
    public static void assertAreEqual(SectionPosition expected, SectionPosition actual) {
        Assert.assertEquals(expected.getGridX(), actual.getGridX());
        Assert.assertEquals(expected.getGridY(), actual.getGridY());
    }

    public static void assertAreEqual(BoxPosition expected, BoxPosition actual) {
        Assert.assertEquals(expected.getGridX(), actual.getGridX());
        Assert.assertEquals(expected.getGridY(), actual.getGridY());
    }

    // WARNING: Will not update who owns the section
    public static void fillSection(BoardStatus board, SectionPosition toFill) {
        for (BoxPosition pos : GridLists.getAllStandardBoxPositions()) {
            board.setBoxOwner(toFill, pos, board.getNextPlayer());
        }
    }

    public static void testLinesAreEqual(Line expected, Line actual) {
        if (expected == null) {
            Assert.assertTrue(actual == null);
        } else if (actual == null) {
            Assert.fail();
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
