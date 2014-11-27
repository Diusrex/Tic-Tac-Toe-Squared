package com.diusrex.tictactoe.tests;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.diusrex.tictactoe.BoardStatus;
import com.diusrex.tictactoe.BoxPosition;
import com.diusrex.tictactoe.Player;
import com.diusrex.tictactoe.SectionPosition;
import com.diusrex.tictactoe.TicTacToeEngine;

public class BoardStatusTests {
    BoardStatus status;

    @Before
    public void setup() {
        status = new BoardStatus();
    }

    @Test
    public void testIsNotInsideBounds() {
        BoxPosition invalidPos = new BoxPosition(0,
                BoardStatus.NUMBER_OF_BOXES_PER_SIDE);
        Assert.assertFalse(status.isInsideBounds(invalidPos));
        invalidPos = new BoxPosition(BoardStatus.NUMBER_OF_BOXES_PER_SIDE, 0);
        Assert.assertFalse(status.isInsideBounds(invalidPos));
        invalidPos = new BoxPosition(-1, 0);
        Assert.assertFalse(status.isInsideBounds(invalidPos));
        invalidPos = new BoxPosition(0, -1);
        Assert.assertFalse(status.isInsideBounds(invalidPos));
    }

    @Test
    public void testIsInsideBounds() {
        BoxPosition validPos = new BoxPosition(0, 0);
        Assert.assertTrue(status.isInsideBounds(validPos));
        validPos = new BoxPosition(BoardStatus.NUMBER_OF_BOXES_PER_SIDE - 1,
                BoardStatus.NUMBER_OF_BOXES_PER_SIDE - 1);
        Assert.assertTrue(status.isInsideBounds(validPos));
    }

    @Test
    public void testSectionToPlayInUpdate() {
        BoxPosition validPosition = new BoxPosition(3, 3);
        SectionPosition expectedSection = TicTacToeEngine
                .getSectionToPlayInNext(validPosition);
        status.setBoxOwner(validPosition, Player.Player_1);

        assertAreEqual(expectedSection, status.getSectionToPlayIn());
    }

    void assertAreEqual(SectionPosition expected, SectionPosition actual) {
        Assert.assertEquals(expected.getX(), actual.getX());
        Assert.assertEquals(expected.getY(), actual.getY());
    }
}
