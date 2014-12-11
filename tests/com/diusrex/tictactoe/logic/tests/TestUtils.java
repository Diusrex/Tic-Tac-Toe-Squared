package com.diusrex.tictactoe.logic.tests;

import junit.framework.Assert;

import com.diusrex.tictactoe.logic.BoardStatus;
import com.diusrex.tictactoe.logic.BoxPosition;
import com.diusrex.tictactoe.logic.Move;
import com.diusrex.tictactoe.logic.Player;
import com.diusrex.tictactoe.logic.SectionPosition;
import com.diusrex.tictactoe.logic.TicTacToeEngine;

public class TestUtils {
    public static class BoardStatusNoCount extends BoardStatus {
        public BoardStatusNoCount(SectionPosition pos) {
            super(pos);
        }

        public Player playerToGoNext;

        public int getPlayerCount(Player wantedPlayer) {
            if (playerToGoNext == Player.Player_1) {
                return 0;
            } else {
                return (wantedPlayer == Player.Player_1) ? 1 : 0;
            }
        }
    }

    public static void applyMoveToBoard(BoardStatus board, Move move) {
        Assert.assertTrue(TicTacToeEngine.isValidMove(board, move));
        TicTacToeEngine.applyMoveIfValid(board, move);
    }

    public static void testInvalidMoveOnBoard(BoardStatus board, Move move) {
        Assert.assertFalse(TicTacToeEngine.isValidMove(board, move));
        TicTacToeEngine.applyMoveIfValid(board, move);
    }

    public static void assertAreEqual(SectionPosition expected, SectionPosition actual) {
        Assert.assertEquals(expected.getX(), actual.getX());
        Assert.assertEquals(expected.getY(), actual.getY());
    }

    public static void assertAreEqual(BoxPosition expected, BoxPosition actual) {
        Assert.assertEquals(expected.getX(), actual.getX());
        Assert.assertEquals(expected.getY(), actual.getY());
    }

    // WARNING: Will not update who owns the section
    public static void fillSection(BoardStatus board, SectionPosition toFill) {
        BoxPosition offset = toFill.getTopLeftPosition();
        Player playerToOwn = Player.Player_1;
        for (int x = 0; x < 3; ++x) {
            for (int y = 0; y < 3; ++y) {
                BoxPosition basePosition = new BoxPosition(x, y);
                board.setBoxOwner(basePosition.increaseBy(offset), playerToOwn);

                // Switch players
                if (playerToOwn == Player.Player_1)
                    playerToOwn = Player.Player_2;
                else
                    playerToOwn = Player.Player_1;
            }
        }
    }
}
