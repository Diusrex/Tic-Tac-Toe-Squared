package com.diusrex.tictactoe.logic.tests;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.diusrex.tictactoe.logic.BoardStatus;
import com.diusrex.tictactoe.logic.BoxPosition;
import com.diusrex.tictactoe.logic.GridChecker;
import com.diusrex.tictactoe.logic.Line;
import com.diusrex.tictactoe.logic.Player;
import com.diusrex.tictactoe.logic.SectionPosition;

public class GridCheckerTests {
    static final int SIZE = 9;
    Player[][] grid;

    static final BoxPosition horizontalIncrease = new BoxPosition(1, 0);
    static final BoxPosition verticalIncrease = new BoxPosition(0, 1);

    @Before
    public void setUp() {
        grid = new Player[SIZE][SIZE];

        resetGrid();
    }

    private void resetGrid() {
        for (int y = 0; y < grid.length; ++y) {
            for (int x = 0; x < grid.length; ++x) {
                grid[x][y] = Player.Unowned;
            }
        }
    }

    @Test
    public void testNoPattern() {
        Assert.assertEquals(Player.Unowned, GridChecker.searchForPattern(grid, new SectionPosition(0, 0)));
    }

    @Test
    public void testNullGridGiven() {
        GridChecker.searchForPattern(null, new SectionPosition(0, 0));
    }

    // These are all invalid positions for the given grid
    @Test
    public void testSectionOutside() {
        final int NUMBER_OF_SECTIONS_IN_GRID = 1;
        grid = new Player[NUMBER_OF_SECTIONS_IN_GRID * BoardStatus.SIZE_OF_SECTION][NUMBER_OF_SECTIONS_IN_GRID
                * BoardStatus.SIZE_OF_SECTION];

        SectionPosition sectionIn = new SectionPosition(-1, 0);
        GridChecker.searchForPattern(grid, sectionIn);

        sectionIn = new SectionPosition(NUMBER_OF_SECTIONS_IN_GRID, 0);
        GridChecker.searchForPattern(grid, sectionIn);

        sectionIn = new SectionPosition(0, -1);
        GridChecker.searchForPattern(grid, sectionIn);

        sectionIn = new SectionPosition(0, NUMBER_OF_SECTIONS_IN_GRID);
        GridChecker.searchForPattern(grid, sectionIn);
    }

    @Test
    public void testHorizontalPatterns() {
        BoxPosition startPos = new BoxPosition(0, 0);
        BoxPosition finalPos = new BoxPosition(2, 0);
        SectionPosition sectionIn = startPos.getSectionIn();

        Player currentPlayer = Player.Player_1;

        for (int i = 0; i < 3; ++i, startPos = startPos.increaseBy(verticalIncrease), finalPos = finalPos
                .increaseBy(verticalIncrease)) {
            // Will not completely fill the line
            fillLine(startPos, horizontalIncrease, currentPlayer, 2);
            Assert.assertEquals(Player.Unowned, GridChecker.searchForPattern(grid, sectionIn));

            setGridPlayer(finalPos, currentPlayer);

            Assert.assertEquals(currentPlayer, GridChecker.searchForPattern(grid, sectionIn));
            resetGrid();
        }
    }

    @Test
    public void testVerticalPatterns() {
        BoxPosition startPos = new BoxPosition(3, 6);
        BoxPosition finalPos = new BoxPosition(3, 8);
        SectionPosition sectionIn = startPos.getSectionIn();

        Player currentPlayer = Player.Player_1;

        for (int i = 0; i < 3; ++i, startPos = startPos.increaseBy(horizontalIncrease), finalPos = finalPos
                .increaseBy(horizontalIncrease)) {
            // Will not completely fill the line
            fillLine(startPos, verticalIncrease, currentPlayer, 2);
            Assert.assertEquals(Player.Unowned, GridChecker.searchForPattern(grid, sectionIn));

            setGridPlayer(finalPos, currentPlayer);

            Assert.assertEquals(currentPlayer, GridChecker.searchForPattern(grid, sectionIn));
            resetGrid();
        }
    }

    @Test
    public void testDiagonalPatternOne() {
        BoxPosition startPos = new BoxPosition(3, 3);
        BoxPosition finalPos = new BoxPosition(5, 5);
        SectionPosition sectionIn = startPos.getSectionIn();

        Player currentPlayer = Player.Player_2;

        BoxPosition diagonalIncrease = new BoxPosition(1, 1);

        fillLine(startPos, diagonalIncrease, currentPlayer, 2);

        Assert.assertEquals(Player.Unowned, GridChecker.searchForPattern(grid, sectionIn));

        setGridPlayer(finalPos, currentPlayer);

        Assert.assertEquals(currentPlayer, GridChecker.searchForPattern(grid, sectionIn));
    }

    @Test
    public void testDiagonalPatternTwo() {
        BoxPosition startPos = new BoxPosition(3, 5);
        BoxPosition finalPos = new BoxPosition(5, 3);
        SectionPosition sectionIn = startPos.getSectionIn();

        Player currentPlayer = Player.Player_2;

        BoxPosition diagonalIncrease = new BoxPosition(1, -1);

        fillLine(startPos, diagonalIncrease, currentPlayer, 2);

        Assert.assertEquals(Player.Unowned, GridChecker.searchForPattern(grid, sectionIn));

        setGridPlayer(finalPos, currentPlayer);

        Assert.assertEquals(currentPlayer, GridChecker.searchForPattern(grid, sectionIn));
    }

    @Test
    public void testHorizontalLine() {
        BoxPosition startPos = new BoxPosition(0, 0);
        BoxPosition finalPos = new BoxPosition(2, 0);
        SectionPosition sectionIn = startPos.getSectionIn();

        Player currentPlayer = Player.Player_1;

        for (int i = 0; i < 3; ++i, startPos = startPos.increaseBy(verticalIncrease), finalPos = finalPos
                .increaseBy(verticalIncrease)) {
            // Will not completely fill the line
            fillLine(startPos, horizontalIncrease, currentPlayer, 2);
            Assert.assertEquals(null, GridChecker.searchForLineOrGetNull(grid, sectionIn));

            setGridPlayer(finalPos, currentPlayer);

            Line foundLine = GridChecker.searchForLineOrGetNull(grid, sectionIn);

            TestUtils.testLinesAreEqual(new Line(startPos, finalPos), foundLine);

            resetGrid();
        }
    }

    @Test
    public void testVerticalLine() {
        BoxPosition startPos = new BoxPosition(3, 6);
        BoxPosition finalPos = new BoxPosition(3, 8);
        SectionPosition sectionIn = startPos.getSectionIn();

        Player currentPlayer = Player.Player_1;

        for (int i = 0; i < 3; ++i, startPos = startPos.increaseBy(horizontalIncrease), finalPos = finalPos
                .increaseBy(horizontalIncrease)) {
            // Will not completely fill the line
            fillLine(startPos, verticalIncrease, currentPlayer, 2);

            setGridPlayer(finalPos, currentPlayer);

            Line foundLine = GridChecker.searchForLineOrGetNull(grid, sectionIn);

            TestUtils.testLinesAreEqual(new Line(startPos, finalPos), foundLine);
            resetGrid();
        }
    }

    @Test
    public void testDiagonalLineOne() {
        BoxPosition startPos = new BoxPosition(3, 3);
        BoxPosition finalPos = new BoxPosition(5, 5);
        SectionPosition sectionIn = startPos.getSectionIn();

        Player currentPlayer = Player.Player_2;

        BoxPosition diagonalIncrease = new BoxPosition(1, 1);

        fillLine(startPos, diagonalIncrease, currentPlayer, 2);

        Assert.assertEquals(null, GridChecker.searchForLineOrGetNull(grid, sectionIn));

        setGridPlayer(finalPos, currentPlayer);

        TestUtils.testLinesAreEqual(new Line(startPos, finalPos), GridChecker.searchForLineOrGetNull(grid, sectionIn));
    }

    @Test
    public void testDiagonalLineTwo() {
        BoxPosition startPos = new BoxPosition(3, 5);
        BoxPosition finalPos = new BoxPosition(5, 3);
        SectionPosition sectionIn = startPos.getSectionIn();

        Player currentPlayer = Player.Player_2;

        BoxPosition diagonalIncrease = new BoxPosition(1, -1);

        fillLine(startPos, diagonalIncrease, currentPlayer, 2);

        Assert.assertEquals(Player.Unowned, GridChecker.searchForPattern(grid, sectionIn));

        setGridPlayer(finalPos, currentPlayer);

        TestUtils.testLinesAreEqual(new Line(startPos, finalPos), GridChecker.searchForLineOrGetNull(grid, sectionIn));
    }

    private void fillLine(BoxPosition startPos, BoxPosition increase, Player player, int length) {
        for (int i = 0; i < length; ++i, startPos = startPos.increaseBy(increase)) {
            setGridPlayer(startPos, player);
        }
    }

    private void setGridPlayer(BoxPosition pos, Player player) {
        grid[pos.getX()][pos.getY()] = player;
    }

    
}
