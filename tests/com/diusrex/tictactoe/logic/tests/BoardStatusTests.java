package com.diusrex.tictactoe.logic.tests;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.diusrex.tictactoe.logic.BoardStatus;
import com.diusrex.tictactoe.logic.BoxPosition;
import com.diusrex.tictactoe.logic.Move;
import com.diusrex.tictactoe.logic.Player;

public class BoardStatusTests {
    BoardStatus status;

    @Before
    public void setup() {
        status = new BoardStatus();
    }

    @Test
    public void testIsNotInsideBounds() {
        BoxPosition invalidPos = BoxPosition.make(0, BoardStatus.NUMBER_OF_BOXES_PER_SIDE);
        Assert.assertFalse(status.isInsideBounds(invalidPos));
        invalidPos = BoxPosition.make(BoardStatus.NUMBER_OF_BOXES_PER_SIDE, 0);
        Assert.assertFalse(status.isInsideBounds(invalidPos));
        invalidPos = BoxPosition.make(-1, 0);
        Assert.assertFalse(status.isInsideBounds(invalidPos));
        invalidPos = BoxPosition.make(0, -1);
        Assert.assertFalse(status.isInsideBounds(invalidPos));
    }

    @Test
    public void testIsInsideBounds() {
        BoxPosition validPos = BoxPosition.make(0, 0);
        Assert.assertTrue(status.isInsideBounds(validPos));
        validPos = BoxPosition.make(BoardStatus.NUMBER_OF_BOXES_PER_SIDE - 1, BoardStatus.NUMBER_OF_BOXES_PER_SIDE - 1);
        Assert.assertTrue(status.isInsideBounds(validPos));
    }

    @Test
    public void testAddingMovesToStack() {
        Assert.assertEquals(0, getMovesSize());

        BoxPosition movePos = BoxPosition.make(0, 0);
        Move move = new Move(movePos, Player.Player_1);

        status.applyMove(move);

        Assert.assertEquals(1, getMovesSize());
        Assert.assertEquals(move, status.getAllMoves().peek());
    }

    private int getMovesSize() {
        return status.getAllMoves().size();
    }
}
