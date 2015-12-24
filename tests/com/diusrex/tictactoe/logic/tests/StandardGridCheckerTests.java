package com.diusrex.tictactoe.logic.tests;

import com.diusrex.tictactoe.data_structures.*;
import com.diusrex.tictactoe.logic.GridConstants;
import com.diusrex.tictactoe.logic.StandardGridChecker;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class StandardGridCheckerTests {
    static final BoxPosition horizontalIncrease = BoxPosition.make(1, 0);
    static final BoxPosition verticalIncrease = BoxPosition.make(0, 1);

    private MockGrid grid;
    private StandardGridChecker checker;

    @Before
    public void setUp() {
        grid = new MockGrid();
        checker = new StandardGridChecker();
    }

    @Test
    public void testNoPattern() {
        assertEquals(Player.Unowned, checker.searchForOwner(grid));
    }

    @Test
    public void testHorizontalOwner() {
        BoxPosition startPos = BoxPosition.make(0, 0);
        BoxPosition finalPos = BoxPosition.make(2, 0);

        testOwner(startPos, finalPos, Player.Player_1, horizontalIncrease, verticalIncrease);
    }

    @Test
    public void testVerticalOwner() {
        BoxPosition startPos = BoxPosition.make(0, 0);
        BoxPosition finalPos = BoxPosition.make(0, 2);

        testOwner(startPos, finalPos, Player.Player_1, verticalIncrease, horizontalIncrease);
    }

    private void testOwner(BoxPosition startPos, BoxPosition finalPos, Player currentPlayer, BoxPosition increaseTested, BoxPosition otherIncrease) {
        for (int i = 0; i < 3; ++i, startPos = startPos.increaseBy(otherIncrease), finalPos = finalPos.increaseBy(otherIncrease)) {

            // Will not completely fill the line
            fillLine(startPos, increaseTested, currentPlayer, 2);
            assertEquals(Player.Unowned, checker.searchForOwner(grid));

            setGridPlayer(finalPos, currentPlayer);

            assertEquals(currentPlayer, checker.searchForOwner(grid));
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

        assertEquals(Player.Unowned, checker.searchForOwner(grid));

        setGridPlayer(finalPos, currentPlayer);

        assertEquals(currentPlayer, checker.searchForOwner(grid));
    }

    @Test
    public void testDiagonalTwoOwner() {
        BoxPosition startPos = BoxPosition.make(0, 2);
        BoxPosition finalPos = BoxPosition.make(2, 0);

        Player currentPlayer = Player.Player_2;

        BoxPosition diagonalIncrease = BoxPosition.make(1, -1);

        fillLine(startPos, diagonalIncrease, currentPlayer, 2);

        assertEquals(Player.Unowned, checker.searchForOwner(grid));

        setGridPlayer(finalPos, currentPlayer);

        assertEquals(currentPlayer, checker.searchForOwner(grid));
    }

    @Test
    public void testHorizontalLine() {
        BoxPosition startPos = BoxPosition.make(0, 0);
        BoxPosition finalPos = BoxPosition.make(2, 0);

        testLine(startPos, finalPos, Player.Player_1,  horizontalIncrease, verticalIncrease);
    }

    @Test
    public void testVerticalLine() {
        BoxPosition startPos = BoxPosition.make(0, 0);
        BoxPosition finalPos = BoxPosition.make(0, 2);

        testLine(startPos, finalPos, Player.Player_1, verticalIncrease, horizontalIncrease);
    }

    private void testLine(BoxPosition startPos, BoxPosition finalPos, Player currentPlayer, BoxPosition increaseTested, BoxPosition otherIncrease) {
        for (int i = 0; i < 3; ++i, startPos = startPos.increaseBy(otherIncrease), finalPos = finalPos.increaseBy(otherIncrease)) {

            // Will not completely fill the line
            fillLine(startPos, increaseTested, currentPlayer, 2);
            assertEquals(null, checker.searchForWinLineOrGetNull(grid));

            setGridPlayer(finalPos, currentPlayer);

            Line foundLine = checker.searchForWinLineOrGetNull(grid);

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

        assertEquals(null, checker.searchForWinLineOrGetNull(grid));

        setGridPlayer(finalPos, currentPlayer);

        TestUtils.testLinesAreEqual(new Line(startPos, finalPos), checker.searchForWinLineOrGetNull(grid));
    }

    @Test
    public void testDiagonalLineTwo() {
        BoxPosition startPos = BoxPosition.make(0, 2);
        BoxPosition finalPos = BoxPosition.make(2, 0);

        Player currentPlayer = Player.Player_2;

        BoxPosition diagonalIncrease = BoxPosition.make(1, -1);

        fillLine(startPos, diagonalIncrease, currentPlayer, 2);

        assertEquals(Player.Unowned, checker.searchForOwner(grid));

        setGridPlayer(finalPos, currentPlayer);

        TestUtils.testLinesAreEqual(new Line(startPos, finalPos), checker.searchForWinLineOrGetNull(grid));
    }

    @Test
    public void testPossibleToWinGridForPlayerUsingPositionEmptyGrid() {
        assertTrue(checker.possibleToWinGridForPlayerUsingPosition(grid, new BasicPosition(0, 0), Player.Player_1));
        assertTrue(checker.possibleToWinGridForPlayerUsingPosition(grid, new BasicPosition(1, 1), Player.Player_1));
    }

    @Test
    public void testPossibleToWinGridForPlayerUsingPositionAllLinesCoveredByOther() {
        Player testedPlayer = Player.Player_1;
        Player otherPlayer = testedPlayer.opposite();

        grid.grid[1][1] = grid.grid[1][0] = grid.grid[0][1] = otherPlayer;
        assertFalse(checker.possibleToWinGridForPlayerUsingPosition(grid, new BasicPosition(0, 0), testedPlayer));
    }

    @Test
    public void testPossibleToWinGridForPlayerUsingPositionAllLinesOwnedBySelf() {
        Player testedPlayer = Player.Player_1;

        grid.grid[1][1] = grid.grid[1][0] = grid.grid[0][1] = testedPlayer;
        assertTrue(checker.possibleToWinGridForPlayerUsingPosition(grid, new BasicPosition(0, 0), testedPlayer));
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
            return Player.Unowned;
        }

        @Override
        public Player getPointOwner(Position pos) {
            return grid[pos.getGridX()][pos.getGridY()];
        }

    }
}
