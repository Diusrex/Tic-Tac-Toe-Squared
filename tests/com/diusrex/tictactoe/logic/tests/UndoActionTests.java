package com.diusrex.tictactoe.logic.tests;

import com.diusrex.tictactoe.data_structures.*;
import com.diusrex.tictactoe.logic.GeneralTicTacToeLogic;
import com.diusrex.tictactoe.logic.GridConstants;
import com.diusrex.tictactoe.logic.tests.TestUtils.MockBoardStatus;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class UndoActionTests {
    MockBoardStatus board;

    Player mainPlayer;

    // Backup state
    int stackSize;
    Player nextPlayer;
    SectionPosition origionalSectionToPlayIn;
    Player[][] origionalBoxOwners;
    Player[][] origionalSectionOwners;
    Line[][] lines;

    SectionPosition mainSection;
    Move appliedMove;
    Move validMove;

    @Before
    public void setup() {
        board = new MockBoardStatus();
        mainPlayer = Player.Player_1;
        board.playerToGoNext = mainPlayer;

        mainSection = board.getSectionToPlayIn();
        appliedMove = Move.make(mainSection, BoxPosition.make(1, 1), mainPlayer);

        TestUtils.applyMoveToBoard(board, appliedMove);

        validMove = Move.make(GeneralTicTacToeLogic.getSectionToPlayInNext(appliedMove.getBox()),
                BoxPosition.make(0, 0), mainPlayer);

        backupBoardState();
    }

    @Test
    public void testUndoWithNoMovesLeft() {
        board.undoLastMove();

        // This should not throw
        try {
            board.undoLastMove();
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testUndoMove() {
        TestUtils.applyMoveToBoard(board, validMove);

        board.undoLastMove();

        assertBoardStateUnchanged();
    }

    @Test
    public void testUndoMoveSupposedToPlayInFull() {
        SectionPosition fullSection = SectionPosition.make(1, 1);
        SectionPosition otherSection = SectionPosition.make(0, 0);
        TestUtils.fillSection(board, fullSection);

        BoxPosition pos = BoxPosition.make(0, 0);
        Move move = Move.make(otherSection, pos, mainPlayer);

        board.fakedSectionToPlayIn = fullSection;

        backupBoardState();

        TestUtils.applyMoveToBoard(board, move);

        board.undoLastMove();

        assertBoardStateUnchanged();
    }

    @Test
    public void testUndoSectionWinningMove() {
        SectionPosition sectionToWin = SectionPosition.make(0, 0);
        winSection(sectionToWin);

        assertEquals(mainPlayer, board.getSectionOwner(sectionToWin));
        board.undoLastMove();

        assertEquals(Player.Unowned, board.getSectionOwner(sectionToWin));
        assertEquals(null, board.getSectionWinLine(sectionToWin));
    }

    @Test
    public void testUndoSectionDoesntLoseSection() {
        SectionPosition sectionToWin = SectionPosition.make(0, 0);
        winSection(sectionToWin);

        assertEquals(mainPlayer, board.getSectionOwner(sectionToWin));

        backupBoardState();

        board.fakedSectionToPlayIn = sectionToWin;
        BoxPosition moveThatDoesntEffectOwnership = BoxPosition.make(2, 2);
        TestUtils.applyMoveToBoard(board, Move.make(sectionToWin, moveThatDoesntEffectOwnership, mainPlayer));

        board.undoLastMove();

        assertEquals(mainPlayer, board.getSectionOwner(sectionToWin));
        TestUtils.testLinesAreEqual(new Line(BoxPosition.make(0, 0), BoxPosition.make(2, 0)),
                board.getSectionWinLine(sectionToWin));

        assertBoardStateUnchanged();
    }

    private void winSection(SectionPosition section) {
        BoxPosition current = BoxPosition.make(0, 0);
        BoxPosition increase = BoxPosition.make(1, 0);

        board.fakedSectionToPlayIn = section;
        TestUtils.applyMoveToBoard(board, Move.make(section, current, mainPlayer));
        current = current.increaseBy(increase);

        board.fakedSectionToPlayIn = section;
        TestUtils.applyMoveToBoard(board, Move.make(section, current, mainPlayer));
        current = current.increaseBy(increase);

        board.fakedSectionToPlayIn = section;
        TestUtils.applyMoveToBoard(board, Move.make(section, current, mainPlayer));
    }

    private void backupBoardState() {
        nextPlayer = board.getActualPlayer();
        stackSize = board.getAllMoves().size();
        origionalSectionToPlayIn = board.getSectionToPlayIn();

        backupBoxOwners();

        backupSectionOwners();

        backupLines();
    }

    private void backupBoxOwners() {
        origionalBoxOwners = new Player[GridConstants.NUMBER_OF_BOXES_PER_SIDE][GridConstants.NUMBER_OF_BOXES_PER_SIDE];
        for (int x = 0; x < GridConstants.NUMBER_OF_BOXES_PER_SIDE; ++x)
            for (int y = 0; y < GridConstants.NUMBER_OF_BOXES_PER_SIDE; ++y)
                origionalBoxOwners[x][y] = board.getBoxOwner(SectionPosition.make(x / 3, y / 3),
                        BoxPosition.make(x % 3, y % 3));
    }

    private void backupSectionOwners() {
        origionalSectionOwners = new Player[GridConstants.NUMBER_OF_SECTIONS_PER_SIDE][GridConstants.NUMBER_OF_SECTIONS_PER_SIDE];
        for (int x = 0; x < GridConstants.NUMBER_OF_SECTIONS_PER_SIDE; ++x)
            for (int y = 0; y < GridConstants.NUMBER_OF_SECTIONS_PER_SIDE; ++y)
                origionalSectionOwners[x][y] = board.getSectionOwner(SectionPosition.make(x, y));
    }

    private void backupLines() {
        lines = new Line[GridConstants.NUMBER_OF_SECTIONS_PER_SIDE][GridConstants.NUMBER_OF_SECTIONS_PER_SIDE];
        for (int x = 0; x < GridConstants.NUMBER_OF_SECTIONS_PER_SIDE; ++x)
            for (int y = 0; y < GridConstants.NUMBER_OF_SECTIONS_PER_SIDE; ++y)
                lines[x][y] = board.getSectionWinLine(SectionPosition.make(x, y));
    }

    private void assertBoardStateUnchanged() {
        assertEquals(nextPlayer, board.getActualPlayer());
        assertEquals(stackSize, board.getAllMoves().size());
        TestUtils.assertAreEqual(origionalSectionToPlayIn, board.getSectionToPlayIn());

        for (int x = 0; x < GridConstants.NUMBER_OF_BOXES_PER_SIDE; ++x)
            for (int y = 0; y < GridConstants.NUMBER_OF_BOXES_PER_SIDE; ++y)
                assertEquals(origionalBoxOwners[x][y],
                        board.getBoxOwner(SectionPosition.make(x / 3, y / 3), BoxPosition.make(x % 3, y % 3)));

        for (int x = 0; x < GridConstants.NUMBER_OF_SECTIONS_PER_SIDE; ++x)
            for (int y = 0; y < GridConstants.NUMBER_OF_SECTIONS_PER_SIDE; ++y)
                assertEquals(origionalSectionOwners[x][y], board.getSectionOwner(SectionPosition.make(x, y)));

        for (int x = 0; x < GridConstants.NUMBER_OF_SECTIONS_PER_SIDE; ++x)
            for (int y = 0; y < GridConstants.NUMBER_OF_SECTIONS_PER_SIDE; ++y)
                assertEquals(lines[x][y], board.getSectionWinLine(SectionPosition.make(x, y)));
    }
}
