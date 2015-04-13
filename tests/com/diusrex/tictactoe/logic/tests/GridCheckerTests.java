package com.diusrex.tictactoe.logic.tests;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.diusrex.tictactoe.data_structures.BoxPosition;
import com.diusrex.tictactoe.data_structures.Grid;
import com.diusrex.tictactoe.data_structures.Line;
import com.diusrex.tictactoe.data_structures.Player;
import com.diusrex.tictactoe.data_structures.Position;
import com.diusrex.tictactoe.logic.GridChecker;
import com.diusrex.tictactoe.logic.GridConstants;

public class GridCheckerTests {
    static final int SIZE = 9;

    static final BoxPosition horizontalIncrease = BoxPosition.make(1, 0);
    static final BoxPosition verticalIncrease = BoxPosition.make(0, 1);

    private MockGrid grid;

    @Before
    public void setUp() {
        grid = new MockGrid();
    }

    @Test
    public void testNoPattern() {
        Assert.assertEquals(Player.Unowned, GridChecker.searchForOwner(grid));
    }

    @Test
    public void testHorizontalOwner() {
        BoxPosition startPos = BoxPosition.make(0, 0);
        BoxPosition finalPos = BoxPosition.make(2, 0);

        Player currentPlayer = Player.Player_1;

        for (int i = 0; i < 3; ++i, startPos = startPos.increaseBy(verticalIncrease), finalPos = finalPos
                .increaseBy(verticalIncrease)) {

            // Will not completely fill the line
            fillLine(startPos, horizontalIncrease, currentPlayer, 2);
            Assert.assertEquals(Player.Unowned, GridChecker.searchForOwner(grid));

            setGridPlayer(finalPos, currentPlayer);

            Assert.assertEquals(currentPlayer, GridChecker.searchForOwner(grid));
            grid.reset();
        }
    }

    @Test
    public void testVerticalOwner() {
        BoxPosition startPos = BoxPosition.make(0, 0);
        BoxPosition finalPos = BoxPosition.make(0, 2);

        Player currentPlayer = Player.Player_1;

        for (int i = 0; i < 3; ++i, startPos = startPos.increaseBy(horizontalIncrease), finalPos = finalPos
                .increaseBy(horizontalIncrease)) {
            // Will not completely fill the line
            fillLine(startPos, verticalIncrease, currentPlayer, 2);
            Assert.assertEquals(Player.Unowned, GridChecker.searchForOwner(grid));

            setGridPlayer(finalPos, currentPlayer);

            Assert.assertEquals(currentPlayer, GridChecker.searchForOwner(grid));
            grid.reset();
        }
    }

    @Test
    public void testDiagonalOneOwner() {
        BoxPosition startPos = BoxPosition.make(0, 0);
        BoxPosition finalPos = BoxPosition.make(2, 2);

        Player currentPlayer = Player.Player_2;

        BoxPosition diagonalIncrease = BoxPosition.make(1, 1);

        fillLine(startPos, diagonalIncrease, currentPlayer, 2);

        Assert.assertEquals(Player.Unowned, GridChecker.searchForOwner(grid));

        setGridPlayer(finalPos, currentPlayer);

        Assert.assertEquals(currentPlayer, GridChecker.searchForOwner(grid));
    }

    @Test
    public void testDiagonalTwoOwner() {
        BoxPosition startPos = BoxPosition.make(0, 2);
        BoxPosition finalPos = BoxPosition.make(2, 0);

        Player currentPlayer = Player.Player_2;

        BoxPosition diagonalIncrease = BoxPosition.make(1, -1);

        fillLine(startPos, diagonalIncrease, currentPlayer, 2);

        Assert.assertEquals(Player.Unowned, GridChecker.searchForOwner(grid));

        setGridPlayer(finalPos, currentPlayer);

        Assert.assertEquals(currentPlayer, GridChecker.searchForOwner(grid));
    }

    @Test
    public void testHorizontalLine() {
        BoxPosition startPos = BoxPosition.make(0, 0);
        BoxPosition finalPos = BoxPosition.make(2, 0);

        Player currentPlayer = Player.Player_1;

        for (int i = 0; i < 3; ++i, startPos = startPos.increaseBy(verticalIncrease), finalPos = finalPos
                .increaseBy(verticalIncrease)) {
            // Will not completely fill the line
            fillLine(startPos, horizontalIncrease, currentPlayer, 2);
            Assert.assertEquals(null, GridChecker.searchForWinLineOrGetNull(grid));

            setGridPlayer(finalPos, currentPlayer);

            Line foundLine = GridChecker.searchForWinLineOrGetNull(grid);

            TestUtils.testLinesAreEqual(new Line(startPos, finalPos), foundLine);

            grid.reset();
        }
    }

    @Test
    public void testVerticalLine() {
        BoxPosition startPos = BoxPosition.make(0, 0);
        BoxPosition finalPos = BoxPosition.make(0, 2);

        Player currentPlayer = Player.Player_1;

        for (int i = 0; i < 3; ++i, startPos = startPos.increaseBy(horizontalIncrease), finalPos = finalPos
                .increaseBy(horizontalIncrease)) {
            // Will not completely fill the line
            fillLine(startPos, verticalIncrease, currentPlayer, 2);
            Assert.assertEquals(null, GridChecker.searchForWinLineOrGetNull(grid));

            setGridPlayer(finalPos, currentPlayer);

            Line foundLine = GridChecker.searchForWinLineOrGetNull(grid);

            TestUtils.testLinesAreEqual(new Line(startPos, finalPos), foundLine);
            grid.reset();
        }
    }

    @Test
    public void testDiagonalLineOne() {
        BoxPosition startPos = BoxPosition.make(0, 0);
        BoxPosition finalPos = BoxPosition.make(2, 2);

        Player currentPlayer = Player.Player_2;

        BoxPosition diagonalIncrease = BoxPosition.make(1, 1);

        fillLine(startPos, diagonalIncrease, currentPlayer, 2);

        Assert.assertEquals(null, GridChecker.searchForWinLineOrGetNull(grid));

        setGridPlayer(finalPos, currentPlayer);

        TestUtils.testLinesAreEqual(new Line(startPos, finalPos), GridChecker.searchForWinLineOrGetNull(grid));
    }

    @Test
    public void testDiagonalLineTwo() {
        BoxPosition startPos = BoxPosition.make(0, 2);
        BoxPosition finalPos = BoxPosition.make(2, 0);

        Player currentPlayer = Player.Player_2;

        BoxPosition diagonalIncrease = BoxPosition.make(1, -1);

        fillLine(startPos, diagonalIncrease, currentPlayer, 2);

        Assert.assertEquals(Player.Unowned, GridChecker.searchForOwner(grid));

        setGridPlayer(finalPos, currentPlayer);

        TestUtils.testLinesAreEqual(new Line(startPos, finalPos), GridChecker.searchForWinLineOrGetNull(grid));
    }

    private void fillLine(BoxPosition startPos, BoxPosition increase, Player player, int length) {
        for (int i = 0; i < length; ++i, startPos = startPos.increaseBy(increase)) {
            setGridPlayer(startPos, player);
        }
    }

    private void setGridPlayer(BoxPosition pos, Player player) {
        grid.grid[pos.getGridX()][pos.getGridY()] = player;
    }

    private class MockGrid implements Grid {
        public Player grid[][];
        public Player owner;

        public MockGrid() {
            grid = new Player[GridConstants.SIZE_OF_SECTION][GridConstants.SIZE_OF_SECTION];

            reset();
        }

        public void reset() {
            for (int y = 0; y < grid.length; ++y) {
                for (int x = 0; x < grid.length; ++x) {
                    grid[x][y] = Player.Unowned;
                }
            }
        }

        @Override
        public boolean canBeWon() {
            return false;
        }

        @Override
        public Player getGridOwner() {
            return owner;
        }

        @Override
        public Player getPointOwner(Position pos) {
            return grid[pos.getGridX()][pos.getGridY()];
        }

    }

}
