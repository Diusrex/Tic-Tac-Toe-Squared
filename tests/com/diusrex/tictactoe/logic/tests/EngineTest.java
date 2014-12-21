package com.diusrex.tictactoe.logic.tests;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.diusrex.tictactoe.logic.BoardStatus;
import com.diusrex.tictactoe.logic.BoxPosition;
import com.diusrex.tictactoe.logic.Move;
import com.diusrex.tictactoe.logic.Player;
import com.diusrex.tictactoe.logic.SectionPosition;
import com.diusrex.tictactoe.logic.TicTacToeEngine;

public class EngineTest {
    BoardStatus board;
    Move moveP1;
    Move moveP1_2;
    Move moveP2SameAsMoveP1;
    Move moveP2;
    Move moveP2_WrongSectionToP1;

    Move invalidPosition;
    Move invalidPlayer;

    @Before
    public void setup() {
        board = new BoardStatus(SectionPosition.make(0, 0));
        BoxPosition duplicatedPosition = BoxPosition.make(0, 0);
        BoxPosition validPosition = BoxPosition.make(1, 1);
        moveP1 = new Move(duplicatedPosition, Player.Player_1); // Position 0, 0
        moveP1_2 = new Move(validPosition, Player.Player_1);
        moveP2SameAsMoveP1 = new Move(duplicatedPosition, Player.Player_2);
        moveP2 = new Move(BoxPosition.make(1, 1), Player.Player_2);

        moveP2_WrongSectionToP1 = new Move(BoxPosition.make(7, 7), Player.Player_2);

        invalidPosition = new Move(BoxPosition.make(-1, -1), Player.Player_1);
        invalidPlayer = new Move(validPosition, Player.Unowned);
    }

    @Test
    public void testBoardIsUnknown() {
        // Make sure it is not taken
        Assert.assertEquals(Player.Unowned, board.getBoxOwner(moveP1.getPosition()));
        Assert.assertEquals(Player.Unowned, board.getBoxOwner(moveP1_2.getPosition()));
        Assert.assertEquals(Player.Unowned, board.getBoxOwner(moveP2.getPosition()));
    }

    @Test
    public void testNullMove() {
        TestUtils.testInvalidMoveOnBoard(board, null);
    }

    @Test
    public void testApplyMove() {
        TestUtils.applyMoveToBoard(board, moveP1);

        Assert.assertEquals(moveP1.getPlayer(), board.getBoxOwner(moveP1.getPosition()));
    }

    @Test
    public void testMultipleMoves() {
        TestUtils.applyMoveToBoard(board, moveP1);

        TestUtils.applyMoveToBoard(board, moveP2);

        Assert.assertEquals(moveP2.getPlayer(), board.getBoxOwner(moveP2.getPosition()));
    }

    @Test
    public void testTooManyP1Moves() {
        TestUtils.applyMoveToBoard(board, moveP1);

        TestUtils.testInvalidMoveOnBoard(board, moveP1_2);

        Assert.assertNotSame(moveP1_2.getPlayer(), board.getBoxOwner(moveP1_2.getPosition()));
    }

    @Test
    public void testTooManyP2Moves() {
        TestUtils.testInvalidMoveOnBoard(board, moveP2);

        Assert.assertNotSame(moveP2.getPlayer(), board.getBoxOwner(moveP2.getPosition()));
    }

    @Test
    public void testAlreadyOwned() {
        TestUtils.applyMoveToBoard(board, moveP1);

        TestUtils.testInvalidMoveOnBoard(board, moveP2SameAsMoveP1);

        Assert.assertEquals(moveP1.getPlayer(), board.getBoxOwner(moveP1.getPosition()));
    }

    @Test
    public void testInvalidPosition() {
        Assert.assertFalse(TicTacToeEngine.isValidMove(board, invalidPosition));
    }

    @Test
    public void testUnknownPlayerMove() {
        TestUtils.applyMoveToBoard(board, moveP1);

        TestUtils.testInvalidMoveOnBoard(board, invalidPlayer);
        Assert.assertEquals(Player.Unowned, board.getBoxOwner(invalidPlayer.getPosition()));
    }

    @Test
    public void testWrongSection() {
        TestUtils.applyMoveToBoard(board, moveP1);

        TestUtils.testInvalidMoveOnBoard(board, moveP2_WrongSectionToP1);
    }

    @Test
    public void testSectionToPlayInNextBasic() {
        BoxPosition pos = BoxPosition.make(0, 0);
        SectionPosition expectedSection = SectionPosition.make(0, 0);

        TestUtils.assertAreEqual(expectedSection, TicTacToeEngine.getSectionToPlayInNext(pos));

        pos = BoxPosition.make(2, 2);
        expectedSection = SectionPosition.make(2, 2);

        TestUtils.assertAreEqual(expectedSection, TicTacToeEngine.getSectionToPlayInNext(pos));

        pos = BoxPosition.make(3, 4);
        expectedSection = SectionPosition.make(0, 1);

        TestUtils.assertAreEqual(expectedSection, TicTacToeEngine.getSectionToPlayInNext(pos));
    }

    @Test
    public void testApplyMoveSectionToPlayInNext() {
        BoxPosition pos = BoxPosition.make(2, 2);
        Player player = Player.Player_1;
        Move move = new Move(pos, player);
        TestUtils.applyMoveToBoard(board, move);
        Assert.assertEquals(TicTacToeEngine.getSectionToPlayInNext(pos), board.getSectionToPlayIn());

        pos = BoxPosition.make(8, 6);
        player = Player.Player_2;
        move = new Move(pos, player);
        TestUtils.applyMoveToBoard(board, move);
        Assert.assertEquals(TicTacToeEngine.getSectionToPlayInNext(pos), board.getSectionToPlayIn());
    }

    @Test
    public void testSectionToPlayInFull() {
        SectionPosition fullSection = SectionPosition.make(0, 0);
        TestUtils.fillSection(board, fullSection);

        // Need to make sure is the correct player to play next
        Player playerToPlayNext = Player.Player_1;

        // Need to make it so the player must play inside that section
        board.setBoxOwner(fullSection.getTopLeftPosition(), playerToPlayNext);

        playerToPlayNext = Player.Player_2;

        BoxPosition untakenPosition = BoxPosition.make(5, 5);

        TestUtils.applyMoveToBoard(board, new Move(untakenPosition, playerToPlayNext));
    }

    @Test
    public void testGetNextPlayer() {
        Assert.assertEquals(Player.Player_1, TicTacToeEngine.getNextPlayer(board));
        
        TestUtils.applyMoveToBoard(board, moveP1);
        Assert.assertEquals(Player.Player_2, TicTacToeEngine.getNextPlayer(board));
    }
}
