package com.diusrex.tictactoe.logic.tests;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.diusrex.tictactoe.logic.BoardStatus;
import com.diusrex.tictactoe.logic.BoxPosition;
import com.diusrex.tictactoe.logic.Move;
import com.diusrex.tictactoe.logic.Player;
import com.diusrex.tictactoe.logic.TicTacToeEngine;

public class SaveStringTests {
    BoardStatus board;
    Move playerOneMove;
    Move playerTwoMove;

    @Before
    public void setup() {
        board = new BoardStatus();
        playerOneMove = new Move(new BoxPosition(3, 3), Player.Player_1);
        playerTwoMove = new Move(new BoxPosition(1, 1), Player.Player_2);
    }

    @Test
    public void testSaveSingleMove() {
        board.applyMove(playerOneMove);

        String expectedString = moveToString(playerOneMove);

        Assert.assertEquals(expectedString, TicTacToeEngine.getSaveString(board));
    }

    @Test
    public void testSaveMultipleMoves() {
        board.applyMove(playerOneMove);
        board.applyMove(playerTwoMove);

        Assert.assertEquals(moveToString(playerOneMove) + moveToString(playerTwoMove), TicTacToeEngine
                .getSaveString(board));

        // Also need to make sure it doesn't change the boards state
        Assert.assertEquals(2, board.getAllMoves().size());
    }

    @Test
    public void testLoadFromString() {
        TestUtils.applyMoveToBoard(board, playerOneMove);

        String savedBoardStatus = TicTacToeEngine.getSaveString(board);

        BoardStatus actualBoard = TicTacToeEngine.loadBoardFromString(savedBoardStatus);
        Assert.assertTrue(board.equals(actualBoard));
    }

    @Test
    public void testLoadMultipleFromString() {
        TestUtils.applyMoveToBoard(board, playerOneMove);
        TestUtils.applyMoveToBoard(board, playerTwoMove);

        String savedBoardStatus = TicTacToeEngine.getSaveString(board);

        BoardStatus actualBoard = TicTacToeEngine.loadBoardFromString(savedBoardStatus);
        Assert.assertTrue(board.equals(actualBoard));
    }

    private String moveToString(Move move) {
        return String.format("%d%d%s", move.getPosition().getX(), move.getPosition().getY(), move.getPlayer()
                .toString());
    }
}
