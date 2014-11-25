package com.diusrex.tictactoe.tests;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.diusrex.tictactoe.BoardStatus;
import com.diusrex.tictactoe.Move;
import com.diusrex.tictactoe.Player;
import com.diusrex.tictactoe.Position;
import com.diusrex.tictactoe.TicTacToeEngine;

public class EngineTest {
    BoardStatus board;
    Move moveP1;
    Move moveP1_2;
    Move moveP2SameAsMoveP1;
    Move moveP2;

    Move invalidPosition;
    Move invalidPlayer;

    @Before
    public void setup() {
        board = new BoardStatus();
        Position duplicatedPosition = new Position(0, 0);
        Position validPosition = new Position(1, 1);
        moveP1 = new Move(duplicatedPosition, Player.Player_1); // Position 0, 0
        moveP1_2 = new Move(validPosition, Player.Player_1);
        moveP2SameAsMoveP1 = new Move(duplicatedPosition, Player.Player_2);
        moveP2 = new Move(new Position(1, 1), Player.Player_2);

        invalidPosition = new Move(new Position(-1, -1), Player.Player_1);
        invalidPlayer = new Move(validPosition, Player.Unowned);
    }

    @Test
    public void testBoardIsUnknown() {
        // Make sure it is not taken
        Assert.assertEquals(Player.Unowned,
                board.getBoxOwner(moveP1.getPosition()));
        Assert.assertEquals(Player.Unowned,
                board.getBoxOwner(moveP1_2.getPosition()));
        Assert.assertEquals(Player.Unowned,
                board.getBoxOwner(moveP2.getPosition()));
    }

    @Test
    public void testApplyMove() {
        Assert.assertTrue(TicTacToeEngine.applyMove(board, moveP1));
        Assert.assertEquals(moveP1.getPlayer(),
                board.getBoxOwner(moveP1.getPosition()));
    }

    @Test
    public void testMultipleMoves() {
        TicTacToeEngine.applyMove(board, moveP1);

        Assert.assertTrue(TicTacToeEngine.applyMove(board, moveP2));
        Assert.assertEquals(moveP2.getPlayer(),
                board.getBoxOwner(moveP2.getPosition()));
    }

    @Test
    public void testTooManyP1Moves() {
        TicTacToeEngine.applyMove(board, moveP1);

        Assert.assertFalse(TicTacToeEngine.applyMove(board, moveP1_2));
        Assert.assertNotSame(moveP1_2.getPlayer(),
                board.getBoxOwner(moveP1_2.getPosition()));
    }

    @Test
    public void testTooManyP2Moves() {
        Assert.assertFalse(TicTacToeEngine.applyMove(board, moveP2));
        Assert.assertNotSame(moveP2.getPlayer(),
                board.getBoxOwner(moveP2.getPosition()));
    }

    @Test
    public void testAlreadyOwned() {
        TicTacToeEngine.applyMove(board, moveP1);

        Assert.assertFalse(TicTacToeEngine.applyMove(board, moveP2SameAsMoveP1));
        Assert.assertEquals(moveP1.getPlayer(),
                board.getBoxOwner(moveP1.getPosition()));
    }

    @Test
    public void testInvalidPosition() {
        Assert.assertFalse(TicTacToeEngine.applyMove(board, invalidPosition));
    }

    @Test
    public void testUnknownPlayerMove() {
        TicTacToeEngine.applyMove(board, moveP1);

        Assert.assertFalse(TicTacToeEngine.applyMove(board, invalidPlayer));
        Assert.assertEquals(Player.Unowned,
                board.getBoxOwner(invalidPlayer.getPosition()));
    }

}
