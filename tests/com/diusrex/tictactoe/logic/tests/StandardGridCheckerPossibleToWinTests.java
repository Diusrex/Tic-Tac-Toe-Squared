package com.diusrex.tictactoe.logic.tests;

import com.diusrex.tictactoe.data_structures.BoardStatus;
import com.diusrex.tictactoe.data_structures.BoxPosition;
import com.diusrex.tictactoe.data_structures.Player;
import com.diusrex.tictactoe.data_structures.SectionPosition;
import com.diusrex.tictactoe.logic.StandardTicTacToeEngine;
import com.diusrex.tictactoe.logic.TicTacToeEngine;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

// For all of the tests, the board will be using the grid checker
public class StandardGridCheckerPossibleToWinTests {
    private BoardStatusWrapper board;

    @Before
    public void setup() {
        board = new BoardStatusWrapper(new StandardTicTacToeEngine());
    }

    @Test
    public void testBoardCanBeWonSimple() {
        assertTrue(board.possibleToWin());
    }

    // 1 1 2
    // 2 2 1
    // 1 0 0
    // 1 can win on bottom row
    @Test
    public void testBoardCanBeWonComplexHorizontalOnly() {
        takeOwnershipOfSection(SectionPosition.make(0, 0), Player.Player_1);
        takeOwnershipOfSection(SectionPosition.make(1, 0), Player.Player_1);
        takeOwnershipOfSection(SectionPosition.make(2, 0), Player.Player_2);
        takeOwnershipOfSection(SectionPosition.make(0, 1), Player.Player_2);
        takeOwnershipOfSection(SectionPosition.make(1, 1), Player.Player_2);
        takeOwnershipOfSection(SectionPosition.make(2, 1), Player.Player_1);
        takeOwnershipOfSection(SectionPosition.make(0, 2), Player.Player_1);
        assertTrue(board.possibleToWin());
    }

    // 1 2 1
    // 0 2 1
    // 0 1 2
    @Test
    public void testBoardCanBeWonComplexVerticalOnly() {
        takeOwnershipOfSection(SectionPosition.make(0, 0), Player.Player_1);
        takeOwnershipOfSection(SectionPosition.make(1, 0), Player.Player_2);
        takeOwnershipOfSection(SectionPosition.make(2, 0), Player.Player_1);
        takeOwnershipOfSection(SectionPosition.make(1, 1), Player.Player_2);
        takeOwnershipOfSection(SectionPosition.make(2, 1), Player.Player_1);
        takeOwnershipOfSection(SectionPosition.make(1, 2), Player.Player_1);
        takeOwnershipOfSection(SectionPosition.make(2, 2), Player.Player_2);
        assertTrue(board.possibleToWin());
    }

    // 1 2 1
    // 1 0 2
    // 2 1 1
    @Test
    public void testBoardCanBeWonComplexDiagonalTL_To_BR_Only() {
        takeOwnershipOfSection(SectionPosition.make(0, 0), Player.Player_1);
        takeOwnershipOfSection(SectionPosition.make(1, 0), Player.Player_2);
        takeOwnershipOfSection(SectionPosition.make(2, 0), Player.Player_1);
        takeOwnershipOfSection(SectionPosition.make(0, 1), Player.Player_1);
        takeOwnershipOfSection(SectionPosition.make(2, 1), Player.Player_2);
        takeOwnershipOfSection(SectionPosition.make(0, 2), Player.Player_2);
        takeOwnershipOfSection(SectionPosition.make(1, 2), Player.Player_1);
        takeOwnershipOfSection(SectionPosition.make(2, 2), Player.Player_1);
        assertTrue(board.possibleToWin());
    }

    // 2 1 2
    // 1 0 2
    // 2 2 1
    @Test
    public void testBoardCanBeWonComplexDiagonalTR_To_BL_Only() {
        takeOwnershipOfSection(SectionPosition.make(0, 0), Player.Player_2);
        takeOwnershipOfSection(SectionPosition.make(1, 0), Player.Player_1);
        takeOwnershipOfSection(SectionPosition.make(2, 0), Player.Player_2);
        takeOwnershipOfSection(SectionPosition.make(0, 1), Player.Player_1);
        takeOwnershipOfSection(SectionPosition.make(2, 1), Player.Player_2);
        takeOwnershipOfSection(SectionPosition.make(0, 2), Player.Player_2);
        takeOwnershipOfSection(SectionPosition.make(1, 2), Player.Player_2);
        takeOwnershipOfSection(SectionPosition.make(2, 2), Player.Player_1);
        assertTrue(board.possibleToWin());
    }

    @Test
    public void testBoardCannotBeWonWhenIsWon() {
        takeOwnershipOfSection(SectionPosition.make(0, 0), Player.Player_1);
        takeOwnershipOfSection(SectionPosition.make(1, 0), Player.Player_1);
        takeOwnershipOfSection(SectionPosition.make(2, 0), Player.Player_1);
        assertFalse(board.possibleToWin());
    }

    // 1 2 1
    // 1 2 1
    // 2 1 2
    @Test
    public void testBoardCannotBeWonBoardIsFull() {
        takeOwnershipOfSection(SectionPosition.make(0, 0), Player.Player_1);
        takeOwnershipOfSection(SectionPosition.make(1, 0), Player.Player_2);
        takeOwnershipOfSection(SectionPosition.make(2, 0), Player.Player_1);
        takeOwnershipOfSection(SectionPosition.make(0, 1), Player.Player_1);
        takeOwnershipOfSection(SectionPosition.make(1, 1), Player.Player_2);
        takeOwnershipOfSection(SectionPosition.make(2, 1), Player.Player_1);
        takeOwnershipOfSection(SectionPosition.make(0, 2), Player.Player_2);
        takeOwnershipOfSection(SectionPosition.make(1, 2), Player.Player_1);
        takeOwnershipOfSection(SectionPosition.make(2, 2), Player.Player_2);
        assertFalse(board.possibleToWin());
    }

    private void takeOwnershipOfSection(SectionPosition section, Player player) {
        board.setSectionOwner(section, player);
    }

    private class BoardStatusWrapper extends BoardStatus {

        public BoardStatusWrapper(TicTacToeEngine engine) {
            super(engine);
        }

        public void setSectionOwner(SectionPosition section, Player player) {
            // Make a diagonal line
            sectionsOwnersGrid.setBoxOwner(section, BoxPosition.make(0,  0), player);
            sectionsOwnersGrid.setBoxOwner(section, BoxPosition.make(1,  1), player);
            sectionsOwnersGrid.setBoxOwner(section, BoxPosition.make(2,  2), player);
        }
    }
}
