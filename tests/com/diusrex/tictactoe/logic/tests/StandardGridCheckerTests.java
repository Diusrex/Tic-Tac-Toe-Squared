package com.diusrex.tictactoe.logic.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.diusrex.tictactoe.data_structures.Line;
import com.diusrex.tictactoe.data_structures.LinesFormed;
import com.diusrex.tictactoe.data_structures.Player;
import com.diusrex.tictactoe.data_structures.grid.Grid;
import com.diusrex.tictactoe.data_structures.position.BasicPosition;
import com.diusrex.tictactoe.data_structures.position.BoxPosition;
import com.diusrex.tictactoe.data_structures.position.Position;
import com.diusrex.tictactoe.logic.GridConstants;
import com.diusrex.tictactoe.logic.StandardGridChecker;

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
    
    @Test
    public void testLinesSingle() {
        Player testedPlayer = Player.Player_1;
        LinesFormed lines = new LinesFormed(testedPlayer);

        // Both effect just 2 lines, don't effect each other at all
        grid.grid[0][1] = testedPlayer;
        grid.grid[1][0] = testedPlayer.opposite();
        checker.getLinesFormed(grid, lines);
        // Only 4 lines are unaffected
        assertEquals(4, lines.unownedButWinnableForMain);
        assertEquals(4, lines.unownedButWinnableForOther);
        
        assertEquals(2, lines.oneFormedForMain);
        assertEquals(2, lines.oneFormedForOther);
        assertEquals(0, lines.twoFormedForMain);
        assertEquals(0, lines.twoFormedForOther);
        assertEquals(0, lines.twoInRowWasBlockedForMain);
        assertEquals(0, lines.twoInRowWasBlockedForOther);

        // Check that it properly resets values.
        checker.getLinesFormed(grid, lines);
        // Only 4 lines are unaffected
        assertEquals(4, lines.unownedButWinnableForMain);
        assertEquals(4, lines.unownedButWinnableForOther);
        
        assertEquals(2, lines.oneFormedForMain);
        assertEquals(2, lines.oneFormedForOther);
    }
    
    @Test
    public void testLinesDoubles() {
        Player mainPlayer = Player.Player_1;
        LinesFormed lines = new LinesFormed(mainPlayer);

        // Expect 1 two line 
        grid.grid[0][0] = mainPlayer;
        grid.grid[0][1] = mainPlayer;
        checker.getLinesFormed(grid, lines);
        // Only 4 lines are unaffected
        assertEquals(4, lines.unownedButWinnableForMain);
        assertEquals(4, lines.unownedButWinnableForOther);
        
        assertEquals(1, lines.twoFormedForMain);
        assertEquals(0, lines.twoFormedForOther);
        
        // Expect 1 two line for other
        grid.grid[0][0] = mainPlayer.opposite();
        grid.grid[0][1] = mainPlayer.opposite();
        checker.getLinesFormed(grid, lines);
        // Only 4 lines are unaffected
        assertEquals(4, lines.unownedButWinnableForMain);
        assertEquals(4, lines.unownedButWinnableForOther);
        
        assertEquals(0, lines.twoFormedForMain);
        assertEquals(1, lines.twoFormedForOther);
    }
    
    @Test
    public void testLinesBlocked() {
        Player mainPlayer = Player.Player_1;
        Player otherPlayer = mainPlayer.opposite();
        LinesFormed lines = new LinesFormed(mainPlayer);

        grid.grid[0][0] = mainPlayer;
        grid.grid[0][1] = mainPlayer;
        grid.grid[0][2] = otherPlayer;
        checker.getLinesFormed(grid, lines);
        // Only 2 lines are unaffected
        assertEquals(2, lines.unownedButWinnableForMain);
        assertEquals(2, lines.unownedButWinnableForOther);
        
        assertEquals(1, lines.twoInRowWasBlockedForMain);
        assertEquals(0, lines.twoInRowWasBlockedForOther);
        
        grid.grid[0][0] = otherPlayer;
        grid.grid[0][1] = otherPlayer;
        grid.grid[0][2] = mainPlayer;
        checker.getLinesFormed(grid, lines);
        
        // Only 2 lines are unaffected
        assertEquals(2, lines.unownedButWinnableForMain);
        assertEquals(2, lines.unownedButWinnableForOther);
        
        assertEquals(0, lines.twoInRowWasBlockedForMain);
        assertEquals(1, lines.twoInRowWasBlockedForOther);
    }
    
    @Test
    public void testLinesCorrectPlayer() {
        Player mainPlayer = Player.Player_1;
        LinesFormed lines = new LinesFormed(mainPlayer);

        // Just sets 3 lines, so 5 are empty
        grid.grid[0][0] = mainPlayer;
        checker.getLinesFormed(grid, lines);
        assertEquals(5, lines.unownedButWinnableForMain);
        assertEquals(5, lines.unownedButWinnableForOther);
        assertEquals(3, lines.oneFormedForMain);
        assertEquals(0, lines.oneFormedForOther);

        // Just sets 3 lines for opposite, so 5 are empty
        grid.grid[0][0] = mainPlayer.opposite();
        checker.getLinesFormed(grid, lines);
        assertEquals(5, lines.unownedButWinnableForMain);
        assertEquals(5, lines.unownedButWinnableForOther);
        assertEquals(0, lines.oneFormedForMain);
        assertEquals(3, lines.oneFormedForOther);
    }
    

    @Test
    public void testLinesSomeAreUnwinnable() {
        // 1   1  ?2
        // 1   0   0
        // 2  ?1  ?2
        //   ?1 means the section can only be won by P1
        //   ?2 means the section can only be won by P1
        //   - means the section can't be won by either player
        
        // Both has two possible win lines, but the lines are quite different.
        Player mainPlayer = Player.Player_1;
        LinesFormed lines = new LinesFormed(mainPlayer);
        
        grid.grid[0][0] = mainPlayer;
        grid.grid[0][1] = mainPlayer;
        grid.grid[1][0] = mainPlayer;
        
        grid.grid[0][2] = mainPlayer.opposite();

        grid.winnableStates[1][2] = MockGrid.WinnableState.WinnableP1;
        
        grid.winnableStates[2][0] = MockGrid.WinnableState.WinnableP2;
        grid.winnableStates[2][2] = MockGrid.WinnableState.WinnableP2;

        checker.getLinesFormed(grid, lines);
        assertEquals(0, lines.unownedButWinnableForMain);
        assertEquals(1, lines.unownedButWinnableForOther);
        
        assertEquals(2, lines.oneFormedForMain);
        assertEquals(1, lines.oneInRowWasBlockedForMain);
        assertEquals(0, lines.twoFormedForMain);
        assertEquals(2, lines.twoInRowWasBlockedForMain);
        
        assertEquals(1, lines.oneFormedForOther);
        assertEquals(1, lines.oneInRowWasBlockedForOther);
    }
    
    @Test
    public void testLinesIgnoreOtherPositions() {
        Player mainPlayer = Player.Player_1;
        LinesFormed lines = new LinesFormed(mainPlayer);

        // Just sets 1 line that includes the position
        grid.grid[0][0] = mainPlayer;
        checker.getLinesFormedUsingPosition(grid, lines, BoxPosition.make(1, 0));
        // There are 2 lines including (1, 0), with only 1 having any elements in it.
        assertEquals(1, lines.unownedButWinnableForMain);
        assertEquals(1, lines.unownedButWinnableForOther);
        assertEquals(1, lines.oneFormedForMain);
        assertEquals(0, lines.oneFormedForOther);
        
        checker.getLinesFormedUsingPosition(grid, lines, BoxPosition.make(1, 1));
        // There are 4 lines including (1, 1), with only 1 having any elements in it.
        assertEquals(3, lines.unownedButWinnableForMain);
        assertEquals(3, lines.unownedButWinnableForOther);
        assertEquals(1, lines.oneFormedForMain);
        assertEquals(0, lines.oneFormedForOther);
        
        checker.getLinesFormedUsingPosition(grid, lines, BoxPosition.make(0, 0));
        // There are 3 lines including (0, 0), and all will include (0, 0)
        assertEquals(0, lines.unownedButWinnableForMain);
        assertEquals(0, lines.unownedButWinnableForOther);
        assertEquals(3, lines.oneFormedForMain);
        assertEquals(0, lines.oneFormedForOther);
    }
    

    private void fillLine(BoxPosition startPos, BoxPosition increase, Player player, int length) {
        for (int i = 0; i < length; ++i, startPos = startPos.increaseBy(increase)) {
            setGridPlayer(startPos, player);
        }
    }

    private void setGridPlayer(BoxPosition pos, Player player) {
        grid.grid[pos.getGridX()][pos.getGridY()] = player;
    }

    private static class MockGrid implements Grid {
        public Player grid[][];
        public enum WinnableState{WinnableForBoth, WinnableP1, WinnableP2, Unwinnable};
        public WinnableState winnableStates[][];

        public MockGrid() {
            grid = new Player[GridConstants.SIZE_OF_SECTION][GridConstants.SIZE_OF_SECTION];
            winnableStates = new WinnableState[GridConstants.SIZE_OF_SECTION][GridConstants.SIZE_OF_SECTION];
            reset();
        }

        public void reset() {
            for (int y = 0; y < grid.length; ++y) {
                for (int x = 0; x < grid.length; ++x) {
                    grid[x][y] = Player.Unowned;
                    winnableStates[x][y] = WinnableState.WinnableForBoth;
                }
            }
        }

        @Override
        public boolean canBeWon() {
            // Doesn't matter for these tests
            return false;
        }

        @Override
        public boolean canBeWonByPlayer(Player player) {
            // Doesn't matter for these tests
            return false;
        }

        @Override
        public boolean pointCanBeWonByPlayer(Position pos, Player player) {
            WinnableState currentWinnable = winnableStates[pos.getGridX()][pos.getGridY()];
            switch (currentWinnable) {
            case WinnableForBoth:
                return true;
                
            case WinnableP1:
                return player == Player.Player_1;
                
            case WinnableP2:
                return player == Player.Player_2;
                
            case Unwinnable:
            default:
                return false;
            }
        }

        @Override
        public Player getGridOwner() {
            return Player.Unowned;
        }

        @Override
        public Player getPointOwner(Position pos) {
            return grid[pos.getGridX()][pos.getGridY()];
        }

        @Override
        public void getLinesFormed(LinesFormed linesFormed) {
        }

    }
}
