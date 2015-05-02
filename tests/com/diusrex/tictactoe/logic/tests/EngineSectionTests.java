package com.diusrex.tictactoe.logic.tests;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.diusrex.tictactoe.data_structures.BoxPosition;
import com.diusrex.tictactoe.data_structures.Line;
import com.diusrex.tictactoe.data_structures.Move;
import com.diusrex.tictactoe.data_structures.Player;
import com.diusrex.tictactoe.data_structures.SectionPosition;
import com.diusrex.tictactoe.logic.tests.TestUtils.MockBoardStatus;

public class EngineSectionTests {
    final int lengthOfCompleteLine = 3;
    final BoxPosition HORIZONTAL_INCREASE = BoxPosition.make(1, 0);
    final BoxPosition VERTICAL_INCREASE = BoxPosition.make(0, 1);

    MockBoardStatus board;

    @Before
    public void setup() {
        resetBoard();
    }

    private void resetBoard() {
        board = new MockBoardStatus();
    }

    private void winSection(SectionPosition section, Player mainPlayer) {
        takeSectionForPlayerHorizontal(section, BoxPosition.make(0, 0), mainPlayer);
    }
    
    // Will take the given section for one player, and the other section for the
    // other player
    private void takeSectionForPlayerHorizontal(SectionPosition section, BoxPosition lineStart, Player player) {
        takeLineInBoard(section, lineStart, HORIZONTAL_INCREASE, lengthOfCompleteLine, player);
    }

    private void takeSectionForPlayerVertical(SectionPosition section, BoxPosition lineStart, Player player) {
        takeLineInBoard(section, lineStart, VERTICAL_INCREASE, lengthOfCompleteLine, player);
    }

    private void takeLineInBoard(SectionPosition section, BoxPosition start, BoxPosition increase, int count, Player player) {
        for (int i = 0; i < count; ++i, start = start.increaseBy(increase)) {
            board.fakedSectionToPlayIn = section;
            TestUtils.applyMoveToBoard(board, Move.make(section, start, player));
        }
    }

    @Test
    public void testSectionOwned() {
        BoxPosition player1PositionStart = BoxPosition.make(0, 0);
        SectionPosition player1Section = SectionPosition.make(0, 0);

        Player currentPlayer = Player.Player_1;
        board.playerToGoNext = currentPlayer;
        takeSectionForPlayerHorizontal(player1Section, player1PositionStart, currentPlayer);

        Assert.assertEquals(currentPlayer, board.getSectionOwner(player1Section));
        
        BoxPosition lineEnd = player1PositionStart.increaseBy(HORIZONTAL_INCREASE).increaseBy(HORIZONTAL_INCREASE);
        TestUtils.testLinesAreEqual(new Line(player1PositionStart, lineEnd), board.getSectionWinLine(player1Section));
    }

    @Test
    public void testSectionCannotBeRetaken() {
        BoxPosition player1PositionStart = BoxPosition.make(0, 0);
        BoxPosition player2PositionStart = BoxPosition.make(1, 0);
        
        SectionPosition section = SectionPosition.make(0, 0);

        // Player 2 wins the section
        Player currentPlayer = Player.Player_2;
        board.playerToGoNext = currentPlayer;
        takeSectionForPlayerVertical(section, player2PositionStart, currentPlayer);
        Assert.assertEquals(Player.Player_2, board.getSectionOwner(section));

        
        currentPlayer = Player.Player_1;
        board.playerToGoNext = currentPlayer;
        takeSectionForPlayerVertical(section, player1PositionStart, currentPlayer);

        Assert.assertEquals(Player.Player_2, board.getSectionOwner(section));
    }

    @Test
    public void testSectionIncomplete() {
        BoxPosition player1PositionStart = BoxPosition.make(0, 0);
        BoxPosition diagonalIncrease = BoxPosition.make(1, 1);
        BoxPosition winningPos = player1PositionStart.increaseBy(diagonalIncrease).increaseBy(diagonalIncrease);
        SectionPosition section = SectionPosition.make(1, 1);

        Player mainPlayer = Player.Player_1;
        board.playerToGoNext = mainPlayer;

        // Diagonal version 1, not long enough
        takeLineInBoard(section, player1PositionStart, diagonalIncrease, 2, mainPlayer);
        Assert.assertEquals(Player.Unowned, board.getSectionOwner(section));

        // Take the final spot
        board.fakedSectionToPlayIn = section;
        TestUtils.applyMoveToBoard(board, Move.make(section, winningPos, mainPlayer));

        Assert.assertEquals(mainPlayer, board.getSectionOwner(section));
    }

    @Test
    public void testOtherDiagonal() {
        BoxPosition player1PositionStart = BoxPosition.make(2, 0);
        BoxPosition increase = BoxPosition.make(-1, 1);
        BoxPosition winningPos = player1PositionStart.increaseBy(increase).increaseBy(increase);
        SectionPosition section = SectionPosition.make(1, 0);

        Player currentPlayer = Player.Player_1;
        board.playerToGoNext = currentPlayer;

        // Diagonal version 2, not long enough
        takeLineInBoard(section, player1PositionStart, increase, 2, currentPlayer);
        Assert.assertEquals(Player.Unowned, board.getSectionOwner(section));

        // Take the final spot
        board.fakedSectionToPlayIn = section;
        TestUtils.applyMoveToBoard(board, Move.make(section, winningPos, currentPlayer));

        Assert.assertEquals(currentPlayer, board.getSectionOwner(section));
    }

    @Test
    public void testWinGameHorizontal() {
        SectionPosition lineShift = SectionPosition.make(0, 1);
        SectionPosition horizontalIncrease = SectionPosition.make(1, 0);
        SectionPosition firstSection = SectionPosition.make(0, 0);

        // Need to test all 3 different lines
        for (int i = 0; i < 3; ++i, firstSection = firstSection.increaseBy(lineShift)) {
            winBoardWithLine(horizontalIncrease, firstSection);
        }
    }

    @Test
    public void testWinGameVertical() {
        SectionPosition lineShift = SectionPosition.make(1, 0);
        SectionPosition verticalIncrease = SectionPosition.make(0, 1);
        SectionPosition firstSection = SectionPosition.make(0, 0);

        // Need to test all 3 different lines
        for (int i = 0; i < 3; ++i, firstSection = firstSection.increaseBy(lineShift)) {
            winBoardWithLine(verticalIncrease, firstSection);
        }
    }

    @Test
    public void testWinGameDiagonal() {
        SectionPosition diagonalIncrease = SectionPosition.make(1, 1);
        SectionPosition firstSection = SectionPosition.make(0, 0);

        winBoardWithLine(diagonalIncrease, firstSection);

        diagonalIncrease = SectionPosition.make(-1, 1);
        firstSection = SectionPosition.make(2, 0);

        winBoardWithLine(diagonalIncrease, firstSection);
    }

    private void winBoardWithLine(SectionPosition increase, SectionPosition start) {
        resetBoard();
        Assert.assertEquals(Player.Unowned, board.getWinner());
        
        SectionPosition secondSection = start.increaseBy(increase);
        SectionPosition thirdSection = secondSection.increaseBy(increase);
        Player mainPlayer = Player.Player_1;

        board.playerToGoNext = mainPlayer;
        winSection(start, mainPlayer);
        Assert.assertEquals(Player.Unowned, board.getWinner());

        winSection(secondSection, mainPlayer);
        Assert.assertEquals(Player.Unowned, board.getWinner());

        winSection(thirdSection, mainPlayer);
        Assert.assertEquals(mainPlayer, board.getWinner());
    }
}
