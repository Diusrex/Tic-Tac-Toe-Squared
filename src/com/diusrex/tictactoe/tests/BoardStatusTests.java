package com.diusrex.tictactoe.tests;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.diusrex.tictactoe.BoardStatus;
import com.diusrex.tictactoe.Position;

public class BoardStatusTests {
    BoardStatus status;
    
    @Before
    public void setup() {
        status = new BoardStatus();
    }
    
    @Test
    public void testIsNotInsideBounds() {
        Position invalidPos = new Position(0, BoardStatus.NUMBER_OF_BOXES_PER_SIDE);
        Assert.assertFalse(status.isInsideBounds(invalidPos));
        invalidPos = new Position(BoardStatus.NUMBER_OF_BOXES_PER_SIDE, 0);
        Assert.assertFalse(status.isInsideBounds(invalidPos));
        invalidPos = new Position(-1, 0);
        Assert.assertFalse(status.isInsideBounds(invalidPos));
        invalidPos = new Position(0, -1);
        Assert.assertFalse(status.isInsideBounds(invalidPos));
    }
    
    @Test
    public void testIsInsideBounds() {
        Position validPos = new Position(0, 0);
        Assert.assertTrue(status.isInsideBounds(validPos));
        validPos = new Position(BoardStatus.NUMBER_OF_BOXES_PER_SIDE - 1, BoardStatus.NUMBER_OF_BOXES_PER_SIDE - 1);
        Assert.assertTrue(status.isInsideBounds(validPos));
    }
}
