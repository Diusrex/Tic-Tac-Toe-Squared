package com.diusrex.tictactoe.logic.tests;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.diusrex.tictactoe.data_structures.BoardStatus;
import com.diusrex.tictactoe.data_structures.BoxPosition;
import com.diusrex.tictactoe.data_structures.Move;
import com.diusrex.tictactoe.data_structures.Player;
import com.diusrex.tictactoe.data_structures.SectionPosition;
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
        board = new BoardStatus();
        SectionPosition mainSection = board.getSectionToPlayIn();
        SectionPosition wrongSection = board.getSectionToPlayIn().increaseBy(SectionPosition.make(1, 1));
        
        BoxPosition validPosition = BoxPosition.make(1, 1);
        BoxPosition duplicatedPosition = validPosition;
        BoxPosition validPositionForSecond = BoxPosition.make(2, 2);
        
        moveP1 = new Move(mainSection, validPosition, Player.Player_1); // Position 0, 0
        moveP1_2 = new Move(mainSection, validPositionForSecond, Player.Player_1);
        moveP2SameAsMoveP1 = new Move(mainSection, duplicatedPosition, Player.Player_2);
        moveP2 = new Move(mainSection, validPositionForSecond, Player.Player_2);

        moveP2_WrongSectionToP1 = new Move(wrongSection, BoxPosition.make(0, 0), Player.Player_2);

        invalidPosition = new Move(mainSection, BoxPosition.make(-1, -1), Player.Player_1);
        invalidPlayer = new Move(mainSection, validPosition, Player.Unowned);
    }

    @Test
    public void testBoardIsUnowned() {
        // Make sure it is not taken
        Assert.assertEquals(Player.Unowned, board.getBoxOwner(moveP1));
        Assert.assertEquals(Player.Unowned, board.getBoxOwner(moveP1_2));
        Assert.assertEquals(Player.Unowned, board.getBoxOwner(moveP2));
    }

    @Test
    public void testNullMove() {
        TestUtils.testInvalidMoveOnBoard(board, null);
    }

    @Test
    public void testApplyMove() {
        TestUtils.applyMoveToBoard(board, moveP1);

        Assert.assertEquals(moveP1.getPlayer(), board.getBoxOwner(moveP1));
    }

    @Test
    public void testMultipleMoves() {
        TestUtils.applyMoveToBoard(board, moveP1);

        TestUtils.applyMoveToBoard(board, moveP2);

        Assert.assertEquals(moveP2.getPlayer(), board.getBoxOwner(moveP2));
    }

    @Test
    public void testTooManyP1Moves() {
        TestUtils.applyMoveToBoard(board, moveP1);

        TestUtils.testInvalidMoveOnBoard(board, moveP1_2);

        Assert.assertNotSame(moveP1_2.getPlayer(), board.getBoxOwner(moveP1_2));
    }

    @Test
    public void testTooManyP2Moves() {
        TestUtils.testInvalidMoveOnBoard(board, moveP2);

        Assert.assertNotSame(moveP2.getPlayer(), board.getBoxOwner(moveP2));
    }

    @Test
    public void testAlreadyOwned() {
        TestUtils.applyMoveToBoard(board, moveP1);

        TestUtils.testInvalidMoveOnBoard(board, moveP2SameAsMoveP1);

        Assert.assertEquals(moveP1.getPlayer(), board.getBoxOwner(moveP1));
    }

    @Test
    public void testInvalidPosition() {
        Assert.assertFalse(TicTacToeEngine.isValidMove(board, invalidPosition));
    }

    @Test
    public void testUnknownPlayerMove() {
        TestUtils.applyMoveToBoard(board, moveP1);

        TestUtils.testInvalidMoveOnBoard(board, invalidPlayer);
        Assert.assertEquals(moveP1.getPlayer(), board.getBoxOwner(invalidPlayer));
    }

    @Test
    public void testWrongSection() {
        TestUtils.applyMoveToBoard(board, moveP1);

        TestUtils.testInvalidMoveOnBoard(board, moveP2_WrongSectionToP1);
    }

    @Test
    public void testSectionToPlayInNextBasic() {
        BoxPosition pos = BoxPosition.make(1, 0);
        SectionPosition expectedSection = SectionPosition.make(1, 0);

        TestUtils.assertAreEqual(expectedSection, TicTacToeEngine.getSectionToPlayInNext(pos));

        pos = BoxPosition.make(2, 2);
        expectedSection = SectionPosition.make(2, 2);

        TestUtils.assertAreEqual(expectedSection, TicTacToeEngine.getSectionToPlayInNext(pos));
    }

    @Test
    public void testApplyMoveSectionToPlayInNext() {
        BoxPosition pos = BoxPosition.make(2, 2);
        SectionPosition expectedSection = board.getSectionToPlayIn();
        SectionPosition secondSection = SectionPosition.make(2, 2);
        
        Player player = Player.Player_1;
        Move move = new Move(expectedSection, pos, player);
        TestUtils.applyMoveToBoard(board, move);
        Assert.assertEquals(TicTacToeEngine.getSectionToPlayInNext(pos), board.getSectionToPlayIn());

        pos = BoxPosition.make(0, 0);
        player = Player.Player_2;
        move = new Move(secondSection, pos, player);
        TestUtils.applyMoveToBoard(board, move);
        Assert.assertEquals(TicTacToeEngine.getSectionToPlayInNext(pos), board.getSectionToPlayIn());
    }

    @Test
    public void testSectionToPlayInFull() {
        SectionPosition fullSection = SectionPosition.make(0, 0);
        SectionPosition otherSection = SectionPosition.make(1, 1);
        TestUtils.fillSection(board, fullSection);

        // Need to make it so the player must play inside that section
        board.setSectionToPlayIn(fullSection);

        BoxPosition untakenPosition = BoxPosition.make(0, 0);

        TestUtils.applyMoveToBoard(board, new Move(otherSection, untakenPosition, board.getNextPlayer()));
    }

    // TODO: This doesn't belong here
    @Test
    public void testGetNextPlayer() {
        Assert.assertEquals(Player.Player_1, board.getNextPlayer());

        TestUtils.applyMoveToBoard(board, moveP1);
        Assert.assertEquals(Player.Player_2, board.getNextPlayer());
    }

    @Test
    public void testBoardIsNotFullWhenNoMoves() {
        Assert.assertFalse(TicTacToeEngine.boardIsFull(board));
    }

    @Test
    public void testBoardIsNotFullWithMultipleMoves() {
        TestUtils.applyMoveToBoard(board, moveP1);
        TestUtils.applyMoveToBoard(board, moveP2);
        Assert.assertFalse(TicTacToeEngine.boardIsFull(board));
    }

    @Test
    public void testBoardIsActuallFull() {
        for (int y = 0; y < BoardStatus.NUMBER_OF_SECTIONS_PER_SIDE; ++y) {
            for (int x = 0; x < BoardStatus.NUMBER_OF_SECTIONS_PER_SIDE; ++x) {
                TestUtils.fillSection(board, SectionPosition.make(x, y));
            }
        }
        Assert.assertTrue(TicTacToeEngine.boardIsFull(board));
    }

    
}
