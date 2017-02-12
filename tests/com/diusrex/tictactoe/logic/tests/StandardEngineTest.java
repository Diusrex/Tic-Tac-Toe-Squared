package com.diusrex.tictactoe.logic.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.diusrex.tictactoe.data_structures.Move;
import com.diusrex.tictactoe.data_structures.Player;
import com.diusrex.tictactoe.data_structures.position.BoxPosition;
import com.diusrex.tictactoe.data_structures.position.SectionPosition;
import com.diusrex.tictactoe.logic.GeneralTicTacToeLogic;
import com.diusrex.tictactoe.logic.GridConstants;
import com.diusrex.tictactoe.logic.tests.TestUtils.MockBoardStatus;

public class StandardEngineTest {
    MockBoardStatus board;
    Move moveP1;
    Move moveP1_2;
    Move moveP2SameAsMoveP1;
    Move moveP2;
    Move moveP2_WrongSectionToP1;

    Move invalidPosition;
    Move invalidPlayer;

    @Before
    public void setup() {
        board = new MockBoardStatus();
        SectionPosition mainSection = board.getSectionToPlayIn();
        SectionPosition wrongSection = board.getSectionToPlayIn().increaseBy(SectionPosition.make(1, 1));

        BoxPosition validPosition = BoxPosition.make(1, 1);
        BoxPosition duplicatedPosition = validPosition;
        BoxPosition validPositionForSecond = BoxPosition.make(2, 2);

        moveP1 = Move.make(mainSection, validPosition, Player.Player_1); // Position
                                                                         // 0, 0
        moveP1_2 = Move.make(mainSection, validPositionForSecond, Player.Player_1);
        moveP2SameAsMoveP1 = Move.make(mainSection, duplicatedPosition, Player.Player_2);
        moveP2 = Move.make(mainSection, validPositionForSecond, Player.Player_2);

        moveP2_WrongSectionToP1 = Move.make(wrongSection, BoxPosition.make(0, 0), Player.Player_2);

        invalidPosition = Move.make(mainSection, BoxPosition.make(-1, -1), Player.Player_1);
        invalidPlayer = Move.make(mainSection, validPosition, Player.Unowned);
    }

    @Test
    public void testBoardIsUnowned() {
        // Make sure it is not taken
        assertEquals(Player.Unowned, board.getBoxOwner(moveP1));
        assertEquals(Player.Unowned, board.getBoxOwner(moveP1_2));
        assertEquals(Player.Unowned, board.getBoxOwner(moveP2));
    }

    @Test
    public void testNullMove() {
        TestUtils.testInvalidMoveOnBoard(board, null);
    }

    @Test
    public void testApplyMove() {
        TestUtils.applyMoveToBoard(board, moveP1);

        assertEquals(moveP1.getPlayer(), board.getBoxOwner(moveP1));
    }

    @Test
    public void testMultipleMoves() {
        TestUtils.applyMoveToBoard(board, moveP1);

        TestUtils.applyMoveToBoard(board, moveP2);

        assertEquals(moveP2.getPlayer(), board.getBoxOwner(moveP2));
    }

    @Test
    public void testTooManyP1Moves() {
        TestUtils.applyMoveToBoard(board, moveP1);

        TestUtils.testInvalidMoveOnBoard(board, moveP1_2);

        assertNotSame(moveP1_2.getPlayer(), board.getBoxOwner(moveP1_2));
    }

    @Test
    public void testTooManyP2Moves() {
        TestUtils.testInvalidMoveOnBoard(board, moveP2);

        assertNotSame(moveP2.getPlayer(), board.getBoxOwner(moveP2));
    }

    @Test
    public void testAlreadyOwned() {
        TestUtils.applyMoveToBoard(board, moveP1);

        TestUtils.testInvalidMoveOnBoard(board, moveP2SameAsMoveP1);

        assertEquals(moveP1.getPlayer(), board.getBoxOwner(moveP1));
    }

    @Test
    public void testInvalidPosition() {
        assertFalse(GeneralTicTacToeLogic.isValidMove(board, invalidPosition));
    }

    @Test
    public void testUnknownPlayerMove() {
        TestUtils.applyMoveToBoard(board, moveP1);

        TestUtils.testInvalidMoveOnBoard(board, invalidPlayer);
        assertEquals(moveP1.getPlayer(), board.getBoxOwner(invalidPlayer));
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

        TestUtils.assertAreEqual(expectedSection, GeneralTicTacToeLogic.getSectionToPlayInNext(pos));

        pos = BoxPosition.make(2, 2);
        expectedSection = SectionPosition.make(2, 2);

        TestUtils.assertAreEqual(expectedSection, GeneralTicTacToeLogic.getSectionToPlayInNext(pos));
    }

    @Test
    public void testApplyMoveSectionToPlayInNext() {
        BoxPosition pos = BoxPosition.make(2, 2);
        SectionPosition expectedSection = board.getSectionToPlayIn();
        SectionPosition secondSection = SectionPosition.make(2, 2);

        Player player = Player.Player_1;
        Move move = Move.make(expectedSection, pos, player);
        TestUtils.applyMoveToBoard(board, move);
        assertEquals(GeneralTicTacToeLogic.getSectionToPlayInNext(pos), board.getSectionToPlayIn());

        pos = BoxPosition.make(0, 0);
        player = Player.Player_2;
        move = Move.make(secondSection, pos, player);
        TestUtils.applyMoveToBoard(board, move);
        assertEquals(GeneralTicTacToeLogic.getSectionToPlayInNext(pos), board.getSectionToPlayIn());
    }

    @Test
    public void testSectionToPlayInFull() {
        SectionPosition fullSection = SectionPosition.make(0, 0);
        SectionPosition otherSection = SectionPosition.make(1, 1);
        TestUtils.fillSection(board, fullSection);

        // Need to make it so the player must play inside that section
        board.fakedSectionToPlayIn = fullSection;

        BoxPosition untakenPosition = BoxPosition.make(0, 0);

        TestUtils.applyMoveToBoard(board, Move.make(otherSection, untakenPosition, board.getNextPlayer()));
    }

    @Test
    public void testBoardIsNotFullWhenNoMoves() {
        assertFalse(GeneralTicTacToeLogic.boardIsFull(board));
    }

    @Test
    public void testBoardIsNotFullWithMultipleMoves() {
        TestUtils.applyMoveToBoard(board, moveP1);
        TestUtils.applyMoveToBoard(board, moveP2);
        assertFalse(GeneralTicTacToeLogic.boardIsFull(board));
    }

    @Test
    public void testBoardIsActuallFull() {
        for (int y = 0; y < GridConstants.NUMBER_OF_SECTIONS_PER_SIDE; ++y) {
            for (int x = 0; x < GridConstants.NUMBER_OF_SECTIONS_PER_SIDE; ++x) {
                TestUtils.fillSection(board, SectionPosition.make(x, y));
            }
        }
        assertTrue(GeneralTicTacToeLogic.boardIsFull(board));
    }

}
