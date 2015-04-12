package com.diusrex.tictactoe.logic.tests;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.diusrex.tictactoe.data_structures.BoardStatus;
import com.diusrex.tictactoe.data_structures.BoxPosition;
import com.diusrex.tictactoe.data_structures.Move;
import com.diusrex.tictactoe.data_structures.Player;
import com.diusrex.tictactoe.data_structures.SectionPosition;

public class BoardStatusTests {
    BoardStatus status;

    @Before
    public void setup() {
        status = new BoardStatus();
    }
    
    @Test
    public void allPositionsStartAsUnowned() {
        for (SectionPosition section : SectionPosition.allSections()) {
            for (BoxPosition box : BoxPosition.allBoxesInSection()) {
                Assert.assertEquals(Player.Unowned, status.getBoxOwner(section, box));
            }
        }
    }

    @Test
    public void testIsNotInsideBoundsBox() {
        SectionPosition validSection = SectionPosition.make(0, 0);
        BoxPosition invalidPos = BoxPosition.make(0, BoardStatus.SIZE_OF_SECTION);
        Assert.assertFalse(status.isInsideBounds(validSection, invalidPos));
        
        invalidPos = BoxPosition.make(BoardStatus.SIZE_OF_SECTION, 0);
        Assert.assertFalse(status.isInsideBounds(validSection, invalidPos));
        
        invalidPos = BoxPosition.make(-1, 0);
        Assert.assertFalse(status.isInsideBounds(validSection, invalidPos));
        
        invalidPos = BoxPosition.make(0, -1);
        Assert.assertFalse(status.isInsideBounds(validSection, invalidPos));
    }
    
    @Test
    public void testIsNotInsideBoundsSection() {
        SectionPosition invalidSection = SectionPosition.make(BoardStatus.NUMBER_OF_SECTIONS_PER_SIDE, 0);
        BoxPosition validPos = BoxPosition.make(0, 0);
        Assert.assertFalse(status.isInsideBounds(invalidSection, validPos));
        
        invalidSection = SectionPosition.make(0, BoardStatus.NUMBER_OF_SECTIONS_PER_SIDE);
        Assert.assertFalse(status.isInsideBounds(invalidSection, validPos));
        
        invalidSection = SectionPosition.make(-1, 0);
        Assert.assertFalse(status.isInsideBounds(invalidSection, validPos));
        
        invalidSection = SectionPosition.make(0, -1);
        Assert.assertFalse(status.isInsideBounds(invalidSection, validPos));
    }

    @Test
    public void testIsInsideBounds() {
        /* Test all sections */
        for (SectionPosition section : SectionPosition.allSections()) {
            for (BoxPosition boxPosition : BoxPosition.allBoxesInSection()) {
                Assert.assertTrue(status.isInsideBounds(section, boxPosition));
            }
        }
    }

    @Test
    public void testAddingMovesToStack() {
        Assert.assertEquals(0, getMovesSize());

        SectionPosition sectionPos = SectionPosition.make(0, 0);
        BoxPosition movePos = BoxPosition.make(0, 0);
        Move move = new Move(sectionPos, movePos, Player.Player_1);

        status.applyMove(move);

        Assert.assertEquals(1, getMovesSize());
        Assert.assertEquals(move, status.getAllMoves().peek());
    }

    private int getMovesSize() {
        return status.getAllMoves().size();
    }
}
