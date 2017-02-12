package com.diusrex.tictactoe.data_structures.tests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.diusrex.tictactoe.data_structures.position.BoxPosition;
import com.diusrex.tictactoe.data_structures.position.SectionPosition;
import com.diusrex.tictactoe.logic.GridConstants;

public class PositionTests {

    // The idea is that it generates BoxPositions that all point to the same
    // object
    @Test
    public void testGeneratingBoxPositions() {
        BoxPosition zeroIncrease = BoxPosition.make(0, 0);
        for (int x = 0; x < GridConstants.NUMBER_OF_BOXES_PER_SIDE; ++x) {
            for (int y = 0; y < GridConstants.NUMBER_OF_BOXES_PER_SIDE; ++y) {
                BoxPosition p1 = BoxPosition.make(x, y);
                BoxPosition p2 = BoxPosition.make(x, y);
                BoxPosition p3 = p1.increaseBy(zeroIncrease);

                assertTrue(p1 == p2);
                assertTrue(p1 == p3);
            }
        }
    }

    // The idea is that it generates SectionPositions that all point to the same
    // object
    @Test
    public void testGeneratingSectionPositions() {
        SectionPosition zeroIncrease = SectionPosition.make(0, 0);
        for (int x = 0; x < GridConstants.NUMBER_OF_SECTIONS_PER_SIDE; ++x) {
            for (int y = 0; y < GridConstants.NUMBER_OF_SECTIONS_PER_SIDE; ++y) {
                SectionPosition p1 = SectionPosition.make(x, y);
                SectionPosition p2 = SectionPosition.make(x, y);
                SectionPosition p3 = p1.increaseBy(zeroIncrease);

                assertTrue(p1 == p2);
                assertTrue(p1 == p3);
            }
        }
    }
}
