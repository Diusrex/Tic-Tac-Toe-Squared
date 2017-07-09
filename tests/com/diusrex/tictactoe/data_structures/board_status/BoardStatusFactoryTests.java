package com.diusrex.tictactoe.data_structures.board_status;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.diusrex.tictactoe.data_structures.Player;
import com.diusrex.tictactoe.data_structures.position.BoxPosition;
import com.diusrex.tictactoe.data_structures.position.SectionPosition;
import com.diusrex.tictactoe.logic.GridLists;

public class BoardStatusFactoryTests {
    private static final Player un = Player.Unowned;
    private static final Player p1 = Player.Player_1;
    private static final Player p2 = Player.Player_2;
    
    private static final Player[] blankRow = new Player[]{un, un, un, un, un, un, un, un, un};

    @Test
    public void testCreateValidBoard() {
        Player[][] wantedPlayerGrid =
                new Player[][]{
                blankRow,
                blankRow,
                blankRow,
                blankRow,
                {un, un, p1, p2, p1, un, un, un, un},
                blankRow,
                blankRow,
                blankRow,
                blankRow};
        
        // Have it be: p1 plays into (1, 1), p2 plays in (0, 1), then p1 plays into (2, 1).
        BoardStatus board = BoardStatusFactory.createSpecificStandardBoard(Player.Player_2,
                SectionPosition.make(1, 1), wantedPlayerGrid);
        
        assertEquals(Player.Player_2, board.getNextPlayer());
        assertPointOwnerGridsAreEqual(board, wantedPlayerGrid);
    }
    
    @Test
    public void testCreateInvalidBoard() {
        Player[] playerOneRow = new Player[]{p1, p1, p1, p1, p1, p1, p1, p1, p1};
        Player[][] wantedPlayerGrid =
                new Player[][]{
                playerOneRow,
                playerOneRow,
                playerOneRow,
                playerOneRow,
                playerOneRow,
                playerOneRow,
                playerOneRow,
                playerOneRow,
                blankRow
                };
        // It should successfully create the board
        // Have it be: p1 plays into (1, 1), p2 plays in (0, 1), then p1 plays into (2, 1).
        BoardStatus board = BoardStatusFactory.createSpecificStandardBoard(Player.Player_1,
                SectionPosition.make(1, 1), wantedPlayerGrid);
                

        assertEquals(Player.Player_1, board.getNextPlayer());
        assertPointOwnerGridsAreEqual(board, wantedPlayerGrid);
        
        // Section ownerships should be updated too.
        for (SectionPosition section : GridLists.getAllStandardSections()) {
            assertEquals(Player.Player_1, board.getSectionOwner(section));
        }
        
        // Same with winner
        assertEquals(Player.Player_1, board.getWinner());
    }
    
    @Test
    public void testCreateBoardSpecifyGridOwner() {
        Player[] playerOneRow = new Player[]{p1, p1, p1, p1, p1, p1, p1, p1, p1};
        Player[] playerTwoRow = new Player[]{p2, p2, p2, p2, p2, p2, p2, p2, p2};
        Player[][] wantedPlayerGrid =
                new Player[][]{
                playerOneRow,
                playerTwoRow,
                playerOneRow,
                playerTwoRow,
                playerOneRow,
                playerOneRow,
                blankRow,
                blankRow,
                blankRow
                };
        Player[][] wantedSectionOwnerGrid =
                new Player[][]{
                {p1, p2, p1},
                {p2, p1, p2},
                {un, un, un} // Note that this row can't have owners applied, since it couldn't be won.
        };
        // It should successfully create the board
        // Have it be: p1 plays into (1, 1), p2 plays in (0, 1), then p1 plays into (2, 1).
        BoardStatus board = BoardStatusFactory.createSpecificStandardBoard(Player.Player_1,
                SectionPosition.make(1, 1), wantedPlayerGrid, wantedSectionOwnerGrid);
                

        assertEquals(Player.Player_1, board.getNextPlayer());
        assertPointOwnerGridsAreEqual(board, wantedPlayerGrid);
        assertSectionOwnerGridsAreEqual(board, wantedSectionOwnerGrid);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testRaiseExceptionOnInvalidOwnership() {
        Player[] playerOneRow = new Player[]{p1, p1, p1, p1, p1, p1, p1, p1, p1};
        Player[] playerTwoRow = new Player[]{p2, p2, p2, p2, p2, p2, p2, p2, p2};
        Player[][] wantedPlayerGrid =
                new Player[][]{
                playerOneRow,
                playerOneRow,
                playerOneRow,
                playerTwoRow,
                playerTwoRow,
                playerTwoRow,
                blankRow,
                blankRow,
                blankRow
                };
        Player[][] wantedSectionOwnerGrid =
                new Player[][]{
                {p1, p2, p1},
                {p2, p1, p2},
                {un, un, un} // Note that this row can't have owners applied, since it couldn't be won.
        };
        // It should successfully create the board
        // Have it be: p1 plays into (1, 1), p2 plays in (0, 1), then p1 plays into (2, 1).
        BoardStatusFactory.createSpecificStandardBoard(Player.Player_1,
                SectionPosition.make(1, 1), wantedPlayerGrid, wantedSectionOwnerGrid);
    }
    
    private void assertPointOwnerGridsAreEqual(BoardStatus board, Player[][] wantedPlayerGrid) {
        for (int y = 0; y < 9; ++y) {
            for (int x = 0; x < 9; ++x) {
                SectionPosition sectionPos = SectionPosition.make(x / 3, y / 3);
                BoxPosition pos = BoxPosition.make(x % 3, y % 3);
                assertEquals(wantedPlayerGrid[y][x], board.getBoxOwner(sectionPos, pos));
            }
        }
    }
    
    private void assertSectionOwnerGridsAreEqual(BoardStatus board, Player[][] wantedSectionOwners) {
        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 3; ++x) {
                SectionPosition sectionPos = SectionPosition.make(x, y);
                assertEquals(wantedSectionOwners[y][x], board.getSectionOwner(sectionPos));
            }
        }
    }
}
