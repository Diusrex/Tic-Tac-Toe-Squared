package com.diusrex.tictactoe.logic.tests;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.diusrex.tictactoe.logic.BoxPosition;
import com.diusrex.tictactoe.logic.Line;
import com.diusrex.tictactoe.logic.Move;
import com.diusrex.tictactoe.logic.Player;
import com.diusrex.tictactoe.logic.SectionPosition;
import com.diusrex.tictactoe.logic.TicTacToeEngine;
import com.diusrex.tictactoe.logic.tests.TestUtils.BoardStatusNoCount;

public class EngineSectionTests {
    final int lengthOfCompleteLine = 3;

    BoardStatusNoCount board;

    @Before
    public void setup() {
        resetBoard();
    }

    private void resetBoard() {
        board = new BoardStatusNoCount(SectionPosition.make(0, 0));
    }

    private void winSection(SectionPosition player1Section, Player mainPlayer) {
        takeSectionForPlayerHorizontal(player1Section.getTopLeftPosition(), mainPlayer);
    }

    // Will take the given section for one player, and the other section for the
    // other player
    private void takeSectionForPlayerHorizontal(BoxPosition lineStart, Player player) {
        takeLineInBoard(lineStart, BoxPosition.make(1, 0), lengthOfCompleteLine, player);
    }

    private void takeSectionForPlayerVertical(BoxPosition lineStart, Player player) {
        takeLineInBoard(lineStart, BoxPosition.make(0, 1), lengthOfCompleteLine, player);
    }

    private void takeLineInBoard(BoxPosition start, BoxPosition increase, int count, Player player) {
        for (int i = 0; i < count; ++i, start = start.increaseBy(increase)) {
            board.setSectionToPlayIn(start.getSectionIn());
            TestUtils.applyMoveToBoard(board, new Move(start, player));
        }
    }

    @Test
    public void testSectionOwned() {
        BoxPosition player1PositionStart = BoxPosition.make(0, 0);
        SectionPosition player1Section = player1PositionStart.getSectionIn();

        Player currentPlayer = Player.Player_1;
        board.playerToGoNext = currentPlayer;
        takeSectionForPlayerHorizontal(player1PositionStart, currentPlayer);

        Assert.assertEquals(currentPlayer, board.getSectionOwner(player1Section));
        TestUtils.testLinesAreEqual(new Line(player1PositionStart, player1PositionStart.increaseBy(2, 0)), board.getLine(player1Section));
    }

    @Test
    public void testSectionCannotBeRetaken() {
        BoxPosition player2PositionStart = BoxPosition.make(1, 0);
        SectionPosition player2Section = player2PositionStart.getSectionIn();

        Player currentPlayer = Player.Player_2;
        board.playerToGoNext = currentPlayer;
        takeSectionForPlayerVertical(player2PositionStart, currentPlayer);

        // Will be to the left
        BoxPosition player1PositionStart = BoxPosition.make(0, 0);

        currentPlayer = Player.Player_1;
        board.playerToGoNext = currentPlayer;
        takeSectionForPlayerVertical(player1PositionStart, currentPlayer);

        Assert.assertEquals(Player.Player_2, board.getSectionOwner(player2Section));
    }

    @Test
    public void testSectionIncomplete() {
        BoxPosition player1PositionStart = BoxPosition.make(0, 0);
        SectionPosition player1Section = player1PositionStart.getSectionIn();

        Player mainPlayer = Player.Player_1;
        board.playerToGoNext = mainPlayer;

        // Diagonal version 1
        takeLineInBoard(player1PositionStart, BoxPosition.make(1, 1), 2, mainPlayer);
        Assert.assertEquals(Player.Unowned, board.getSectionOwner(player1Section));

        // Take the final spot
        BoxPosition winningPos = BoxPosition.make(2, 2);
        board.setSectionToPlayIn(player1Section);

        TestUtils.applyMoveToBoard(board, new Move(winningPos, mainPlayer));

        Assert.assertEquals(mainPlayer, board.getSectionOwner(player1Section));
    }

    @Test
    public void testOtherDiagonal() {
        BoxPosition player1PositionStart = BoxPosition.make(2, 0);
        SectionPosition player1Section = player1PositionStart.getSectionIn();

        Player currentPlayer = Player.Player_1;
        board.playerToGoNext = currentPlayer;

        // Diagonal version 2
        takeLineInBoard(player1PositionStart, BoxPosition.make(-1, 1), 2, currentPlayer);
        Assert.assertEquals(Player.Unowned, board.getSectionOwner(player1Section));

        // Take the final spot
        BoxPosition winningPos = BoxPosition.make(0, 2);
        board.setSectionToPlayIn(player1Section);

        TestUtils.applyMoveToBoard(board, new Move(winningPos, currentPlayer));

        Assert.assertEquals(currentPlayer, board.getSectionOwner(player1Section));
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
        Assert.assertEquals(Player.Unowned, TicTacToeEngine.getWinner(board));
        SectionPosition secondSection = start.increaseBy(increase);
        SectionPosition thirdSection = secondSection.increaseBy(increase);
        Player mainPlayer = Player.Player_1;

        board.playerToGoNext = mainPlayer;
        winSection(start, mainPlayer);
        Assert.assertEquals(Player.Unowned, TicTacToeEngine.getWinner(board));

        winSection(secondSection, mainPlayer);
        Assert.assertEquals(Player.Unowned, TicTacToeEngine.getWinner(board));

        winSection(thirdSection, mainPlayer);
        Assert.assertEquals(mainPlayer, TicTacToeEngine.getWinner(board));
    }
}
