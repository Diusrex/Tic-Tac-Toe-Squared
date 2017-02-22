package com.diusrex.tictactoe.logic.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.diusrex.tictactoe.data_structures.Line;
import com.diusrex.tictactoe.data_structures.LineIterator;
import com.diusrex.tictactoe.data_structures.position.BoxPosition;
import com.diusrex.tictactoe.logic.GridLists;

public class LineIteratorTests {
    private static final int ORIGIONAL_POSITION = 0;
    private static final int ORIGINAL_POSITION_PLUS_INCREASE = 1;
    
    private static final int BOARD_SIZE = 3;

    private BoxPosition start;
    private BoxPosition increase;
    private LineIterator iter;

    @Test
    public void fromLinePositiveDiagonalSlope() {
        start = BoxPosition.make(0, 0);
        increase = BoxPosition.make(1, 1);
        setupLineIterator(start, increase);

        testSlope();

        BoxPosition allPositions[] = { BoxPosition.make(0, 0), BoxPosition.make(1, 1), BoxPosition.make(2, 2) };

        ensureIteratesProperly(allPositions);
        
        ensureContainsCorrectly(allPositions);
    }

    @Test
    public void fromLineNegativeDiagonalSlope() {
        start = BoxPosition.make(2, 2);
        increase = BoxPosition.make(-1, -1);
        setupLineIterator(start, increase);

        testSlope();

        BoxPosition allPositions[] = { BoxPosition.make(2, 2), BoxPosition.make(1, 1), BoxPosition.make(0, 0) };

        ensureIteratesProperly(allPositions);
        
        ensureContainsCorrectly(allPositions);
    }

    @Test
    public void fromLineHorizontalChangeOnly() {
        start = BoxPosition.make(0, 1);
        increase = BoxPosition.make(1, 0);
        setupLineIterator(start, increase);

        testSlope();

        BoxPosition allPositions[] = { BoxPosition.make(0, 1), BoxPosition.make(1, 1), BoxPosition.make(2, 1) };

        ensureIteratesProperly(allPositions);
        
        ensureContainsCorrectly(allPositions);
    }

    @Test
    public void fromLineVerticalChangeOnly() {
        start = BoxPosition.make(2, 0);
        increase = BoxPosition.make(0, 1);
        setupLineIterator(start, increase);

        testSlope();

        BoxPosition allPositions[] = { BoxPosition.make(2, 0), BoxPosition.make(2, 1), BoxPosition.make(2, 2) };

        ensureIteratesProperly(allPositions);
        
        ensureContainsCorrectly(allPositions);
    }

    private void testSlope() {
        assertEquals("Current should be at start", start, iter.getCurrent(ORIGIONAL_POSITION));

        assertEquals(start.increaseBy(increase), iter.getCurrent(ORIGINAL_POSITION_PLUS_INCREASE));
    }
    
    private void ensureIteratesProperly(BoxPosition[] allPositions) {
        int posInIter = 0;

        for (; posInIter < BOARD_SIZE; ++posInIter) {
            assertTrue(!iter.isDone(posInIter));

            assertEquals(allPositions[posInIter], iter.getCurrent(posInIter));
        }

        assertTrue(iter.isDone(posInIter));
    }
    
    private void ensureContainsCorrectly(BoxPosition[] positionsContains) {
        Set<BoxPosition> contained = new HashSet<BoxPosition>();
        contained.add(positionsContains[0]);
        contained.add(positionsContains[1]);
        contained.add(positionsContains[2]);
        
        for (BoxPosition pos : GridLists.getAllStandardBoxPositions()) {
            if (contained.contains(pos)) {
                assertTrue(iter.containsPosition(pos));
            } else {
                assertFalse(iter.containsPosition(pos));
            }
        }
    }
    
    private void setupLineIterator(BoxPosition start, BoxPosition increase) {
        BoxPosition end = start.increaseBy(increase).increaseBy(increase);

        Line line = new Line(start, end);

        iter = new LineIterator(line);
    }
}
