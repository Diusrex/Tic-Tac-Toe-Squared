package com.diusrex.tictactoe.logic.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.diusrex.tictactoe.data_structures.BoxPosition;
import com.diusrex.tictactoe.data_structures.Line;
import com.diusrex.tictactoe.data_structures.SectionPosition;
import com.diusrex.tictactoe.logic.GridConstants;
import com.diusrex.tictactoe.logic.GridLists;

// There should be 8 lines:
// 3 horizontal
// 3 vertical
// 2 diagonal
// Should be 9 SectionPositions and 9 BoxPositions
public class GridLinesTests {
    static private final int NUMBER_LINES = 8;
    static private final int NUMBER_SECTIONS = 9;
    static private final int NUMBER_CORNER_SECTIONS = 4;
    static private final int NUMBER_MID_EDGE_SECTIONS = 4;
    static private final int NUMBER_BOX_POSITIONS = 9;
    static private final BoxPosition fakePosition = BoxPosition.make(0, 0);

    private List<Line> allLines;
    private List<SectionPosition> allSectionPositions;
    private List<BoxPosition> allBoxPositions;

    @Before
    public void setup() {
        allLines = GridLists.getAllLines();
        allSectionPositions = GridLists.getAllStandardSections();
        allBoxPositions = GridLists.getAllStandardBoxPositions();
    }

    @Test
    public void ensureContainsAllSections() {
        assertEquals(NUMBER_SECTIONS, allSectionPositions.size());

        for (int x = 0; x < GridConstants.NUMBER_OF_SECTIONS_PER_SIDE; ++x) {
            for (int y = 0; y < GridConstants.NUMBER_OF_SECTIONS_PER_SIDE; ++y) {
               assertTrue(allSectionPositions.contains(SectionPosition.make(x, y)));
            }
        }
    }

    @Test
    public void ensureContainsAllCornerSections() {
        List<SectionPosition> allCornerSections = GridLists.getAllCornerSections();
        assertEquals(NUMBER_CORNER_SECTIONS, allCornerSections.size());

        assertTrue(allCornerSections.contains(SectionPosition.make(0, 0)));
        assertTrue(allCornerSections.contains(SectionPosition.make(0, 2)));
        assertTrue(allCornerSections.contains(SectionPosition.make(2, 0)));
        assertTrue(allCornerSections.contains(SectionPosition.make(2, 2)));
    }

    @Test
    public void ensureContainsAllMidEdgeSections() {
        List<SectionPosition> allMidEdgeSections = GridLists.getAllMidEdgeSections();
        assertEquals(NUMBER_MID_EDGE_SECTIONS, allMidEdgeSections.size());

        assertTrue(allMidEdgeSections.contains(SectionPosition.make(1, 0)));
        assertTrue(allMidEdgeSections.contains(SectionPosition.make(0, 1)));
        assertTrue(allMidEdgeSections.contains(SectionPosition.make(2, 1)));
        assertTrue(allMidEdgeSections.contains(SectionPosition.make(1, 2)));
    }

    @Test
    public void ensureContainsAllBoxPositions() {
        assertEquals(NUMBER_BOX_POSITIONS, allBoxPositions.size());

        for (int x = 0; x < GridConstants.SIZE_OF_SECTION; ++x) {
            for (int y = 0; y < GridConstants.SIZE_OF_SECTION; ++y) {
                assertTrue(allBoxPositions.contains(BoxPosition.make(x, y)));
            }
        }
    }

    @Test
    public void ensureContainsAllLines() {

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
    public void givesImmutableLists() {
        try {
            allLines.add(new Line(fakePosition, fakePosition));
            fail(); // Should have thrown an exception
        } catch (UnsupportedOperationException e) {
            // Success
        }

        try {
            allSectionPositions.add(SectionPosition.make(0, 0));
            fail(); // Should have thrown an exception
        } catch (UnsupportedOperationException e) {
            // Success
        }

        try {
            allBoxPositions.add(BoxPosition.make(0, 0));
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
