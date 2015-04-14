package com.diusrex.tictactoe.logic.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.diusrex.tictactoe.data_structures.BoxPosition;
import com.diusrex.tictactoe.data_structures.Line;
import com.diusrex.tictactoe.data_structures.LineIterator;

public class LineIteratorTests {
    private static final int ORIGIONAL_POSITION = 0;
    private static final int ORITIONAL_POSITION_PLUS_INCREASE = 1;

    private BoxPosition boardSize;
    private BoxPosition start;
    private BoxPosition increase;
    private BoxPosition end;
    private LineIterator iter;

    @Before
    public void setup() {
        boardSize = BoxPosition.make(3, 3);
    }

    @Test
    public void fromLinePositiveSlope() {
        setUpWithPositiveSlope();

        testSlope();
    }

    @Test
    public void fromLineNegativeSlope() {
        setUpWithNegativeSlope();

        testSlope();
    }

    @Test
    public void iterateThroughAllMovesPositive() {
        setUpWithPositiveSlope();

        BoxPosition allPositions[] = { BoxPosition.make(0, 0), BoxPosition.make(1, 1), BoxPosition.make(2, 2) };

        ensureIteratesProperly(allPositions);
    }

    @Test
    public void iterateThroughAllMovesNegative() {
        setUpWithNegativeSlope();

        BoxPosition allPositions[] = { BoxPosition.make(2, 2), BoxPosition.make(1, 1), BoxPosition.make(0, 0) };

        ensureIteratesProperly(allPositions);
    }

    private void ensureIteratesProperly(BoxPosition[] allPositions) {
        int posInIter = 0;

        for (; posInIter < boardSize(); ++posInIter) {
            assertTrue(!iter.isDone(posInIter));

            assertEquals(allPositions[posInIter], iter.getCurrent(posInIter));
        }

        assertTrue(iter.isDone(posInIter));
    }

    private void testSlope() {
        assertEquals("Current should be at start", start, iter.getCurrent(ORIGIONAL_POSITION));

        assertEquals(start.increaseBy(increase), iter.getCurrent(ORITIONAL_POSITION_PLUS_INCREASE));
    }

    private BoxPosition getEnd(BoxPosition start, BoxPosition increase, int size) {
        for (int i = 0; i < size - 1; ++i) {
            start = start.increaseBy(increase);
        }

        return start;
    }

    private void setUpWithPositiveSlope() {
        start = BoxPosition.make(0, 0);
        increase = BoxPosition.make(1, 1);

        setUpEnd();
        setUpIterator();
    }

    private void setUpWithNegativeSlope() {
        start = BoxPosition.make(2, 2);
        increase = BoxPosition.make(-1, -1);

        setUpEnd();
        setUpIterator();
    }

    private void setUpIterator() {
        Line line = new Line(start, end);

        iter = new LineIterator(line);
    }

    private int boardSize() {
        return boardSize.getGridX();
    }

    private void setUpEnd() {
        end = getEnd(start, increase, boardSize());
    }
}
