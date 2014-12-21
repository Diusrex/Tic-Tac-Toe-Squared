package com.diusrex.tictactoe.logic.tests;

import junit.framework.Assert;

import org.junit.Test;

import com.diusrex.tictactoe.logic.BoardStatus;
import com.diusrex.tictactoe.logic.BoxPosition;
import com.diusrex.tictactoe.logic.SectionPosition;

public class PositionTests {
    
    // The idea is that it generates BoxPositions that all point to the same object
    @Test
    public void testGeneratingBoxPositions() {
        BoxPosition zeroIncrease = BoxPosition.make(0, 0);
        for (int x = 0; x < BoardStatus.NUMBER_OF_BOXES_PER_SIDE; ++x) {
            for (int y = 0; y < BoardStatus.NUMBER_OF_BOXES_PER_SIDE; ++y) {
                BoxPosition p1 = BoxPosition.make(x, y);
                BoxPosition p2 = BoxPosition.make(x, y);
                BoxPosition p3 = p1.increaseBy(zeroIncrease);
                
                Assert.assertTrue(p1 == p2);
                Assert.assertTrue(p1 == p3);
            }
        }
    }
    
    @Test
    public void testPositionToSection() {
        BoxPosition boxPos = BoxPosition.make(0, 0);
        TestUtils.assertAreEqual(new SectionPosition(0, 0),
                boxPos.getSectionIn());

        boxPos = BoxPosition.make(3, 8);
        TestUtils.assertAreEqual(new SectionPosition(1, 2),
                boxPos.getSectionIn());

        boxPos = BoxPosition.make(5, 1);
        TestUtils.assertAreEqual(new SectionPosition(1, 0),
                boxPos.getSectionIn());
    }

    @Test
    public void testSectionToPosition() {
        SectionPosition sectionPos = new SectionPosition(0, 0);
        TestUtils.assertAreEqual(BoxPosition.make(0, 0),
                sectionPos.getTopLeftPosition());

        sectionPos = new SectionPosition(1, 2);
        TestUtils.assertAreEqual(BoxPosition.make(3, 6), sectionPos.getTopLeftPosition());
    }

}
