package com.diusrex.tictactoe.logic.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;

import com.diusrex.tictactoe.data_structures.BoxPosition;
import com.diusrex.tictactoe.data_structures.Line;
import com.diusrex.tictactoe.logic.GridLines;

// There should be 8 lines:
// 3 horizontal
// 3 vertical
// 2 diagonal
public class GridLinesTests {
    static private final int NUMBER_LINES = 8;
    static private final BoxPosition fakePosition = BoxPosition.make(0, 0);

    private List<Line> allLines;

    @Test
    public void ensureContainsAllLines() {
        allLines = GridLines.getAllLines();

        assertHasAllHorizontalLines();
        assertHasAllVerticalLines();
        assertHasAllDiagonalLines();

        assertEquals(NUMBER_LINES, allLines.size());
    }

    private void assertHasAllHorizontalLines() {
        hasLineWithCoordinates(BoxPosition.make(0, 0), BoxPosition.make(2, 0));
        hasLineWithCoordinates(BoxPosition.make(0, 1), BoxPosition.make(2, 1));
        hasLineWithCoordinates(BoxPosition.make(0, 2), BoxPosition.make(2, 2));
    }

    private void assertHasAllVerticalLines() {
        hasLineWithCoordinates(BoxPosition.make(0, 0), BoxPosition.make(2, 0));
        hasLineWithCoordinates(BoxPosition.make(0, 1), BoxPosition.make(2, 1));
        hasLineWithCoordinates(BoxPosition.make(0, 2), BoxPosition.make(2, 2));
    }

    private void assertHasAllDiagonalLines() {
        hasLineWithCoordinates(BoxPosition.make(0, 0), BoxPosition.make(0, 2));
        hasLineWithCoordinates(BoxPosition.make(1, 0), BoxPosition.make(1, 2));
        hasLineWithCoordinates(BoxPosition.make(2, 0), BoxPosition.make(2, 2));
    }

    @Test
    public void givesImmutableLines() {
        allLines = GridLines.getAllLines();

        try {
            allLines.add(new Line(fakePosition, fakePosition));
            fail(); // Should have thrown an exception
        } catch (UnsupportedOperationException e) {
            // Success
        }
    }

    private void hasLineWithCoordinates(BoxPosition start, BoxPosition end) {
        // The actual order of start/end does not matter
        Line lineOne = new Line(start, end);
        Line lineTwo = new Line(end, start);

        assertTrue(allLines.contains(lineOne) || allLines.contains(lineTwo));
    }
}
