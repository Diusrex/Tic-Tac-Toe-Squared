package com.diusrex.tictactoe.logic.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.diusrex.tictactoe.logic.BoxPosition;
import com.diusrex.tictactoe.logic.Line;
import com.diusrex.tictactoe.logic.LineIterator;

public class LineIteratorTests {
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

        for (int i = 0; i < boardSize(); ++i) {
            assertTrue(!iter.isDone(boardSize));
            
            assertEquals(allPositions[i], iter.getCurrent());
            iter.next();
        }
        
        assertTrue(iter.isDone(boardSize));
    }
    
    @Test
    public void iterateThroughAllMovesNegative() {
        setUpWithNegativeSlope();

        BoxPosition allPositions[] = { BoxPosition.make(2, 2), BoxPosition.make(1, 1), BoxPosition.make(0, 0) };

        for (int i = 0; i < boardSize(); ++i) {
            assertTrue(!iter.isDone(boardSize));
            
            assertEquals(allPositions[i], iter.getCurrent());
            iter.next();
        }
        
        assertTrue(iter.isDone(boardSize));
    }

    private void testSlope() {
        assertEquals("Current should be at start", start, iter.getCurrent());

        iter.next();
        assertEquals(start.increaseBy(increase), iter.getCurrent());
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
        iter.reset();
    }

    private int boardSize() {
        return boardSize.getX();
    }

    private void setUpEnd() {
        end = getEnd(start, increase, boardSize());
    }
}
