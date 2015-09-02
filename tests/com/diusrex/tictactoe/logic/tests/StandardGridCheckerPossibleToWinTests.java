package com.diusrex.tictactoe.logic.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.diusrex.tictactoe.data_structures.BoardStatus;
import com.diusrex.tictactoe.data_structures.Grid;
import com.diusrex.tictactoe.data_structures.Move;
import com.diusrex.tictactoe.data_structures.Player;
import com.diusrex.tictactoe.data_structures.SectionGrid;
import com.diusrex.tictactoe.data_structures.SectionPosition;
import com.diusrex.tictactoe.logic.StandardGridChecker;
import com.diusrex.tictactoe.logic.StandardTicTacToeEngine;
import com.diusrex.tictactoe.logic.TicTacToeEngine;

// For all of the tests, the board will be using the grid checker
public class StandardGridCheckerPossibleToWinTests {
    private BoardStatus board;

    @Before
    public void setup() {
        board = new BoardStatus(new StandardTicTacToeEngine());
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
    public void testBoardCanBeWonWhenIsWon() {
        takeOwnershipOfSection(SectionPosition.make(0, 0), Player.Player_1);
        takeOwnershipOfSection(SectionPosition.make(1, 0), Player.Player_1);
        takeOwnershipOfSection(SectionPosition.make(2, 0), Player.Player_1);
        assertTrue(board.possibleToWin());
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
        board.setSectionOwner(section, null, player);
    }
}
