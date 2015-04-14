package com.diusrex.tictactoe.logic.tests;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.diusrex.tictactoe.data_structures.BoxPosition;
import com.diusrex.tictactoe.data_structures.Line;
import com.diusrex.tictactoe.data_structures.Move;
import com.diusrex.tictactoe.data_structures.Player;
import com.diusrex.tictactoe.data_structures.SectionPosition;
import com.diusrex.tictactoe.logic.GeneralTicTacToeLogic;
import com.diusrex.tictactoe.logic.GridConstants;
import com.diusrex.tictactoe.logic.GridLists;
import com.diusrex.tictactoe.logic.tests.TestUtils.MockBoardStatus;

public class UndoActionTests {
    MockBoardStatus board;

    Player mainPlayer;

    // Backup state
    int stackSize;
    Player expectedPlayer;
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
        appliedMove = new Move(mainSection, BoxPosition.make(1, 1), mainPlayer);

        TestUtils.applyMoveToBoard(board, appliedMove);

        validMove = new Move(GeneralTicTacToeLogic.getSectionToPlayInNext(appliedMove.getBox()), BoxPosition.make(0, 0),
                mainPlayer);

        backupBoardState();
    }

    @Test
    public void testUndoWithNoMovesLeft() {
        board.undoLastMove();

        // This should not throw
        try {
            board.undoLastMove();
        } catch (Exception e) {
            Assert.fail();
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
        fillSection(fullSection);

        BoxPosition pos = BoxPosition.make(0, 0);
        Move move = new Move(otherSection, pos, mainPlayer);

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

        Assert.assertEquals(mainPlayer, board.getSectionOwner(sectionToWin));
        board.undoLastMove();

        Assert.assertEquals(Player.Unowned, board.getSectionOwner(sectionToWin));
        Assert.assertEquals(null, board.getLine(sectionToWin));
    }

    @Test
    public void testUndoSectionDoesntLoseSection() {
        SectionPosition sectionToWin = SectionPosition.make(0, 0);
        winSection(sectionToWin);

        Assert.assertEquals(mainPlayer, board.getSectionOwner(sectionToWin));

        board.fakedSectionToPlayIn = sectionToWin;
        BoxPosition moveThatDoesntEffectOwnership = BoxPosition.make(2, 2);
        TestUtils.applyMoveToBoard(board, new Move(sectionToWin, moveThatDoesntEffectOwnership, mainPlayer));

        board.undoLastMove();

        Assert.assertEquals(mainPlayer, board.getSectionOwner(sectionToWin));
        TestUtils.testLinesAreEqual(new Line(BoxPosition.make(0, 0), BoxPosition.make(2, 0)),
                board.getLine(sectionToWin));
    }

    private void fillSection(SectionPosition fullSection) {
        for (BoxPosition box : GridLists.getAllStandardBoxPositions()) {
            board.setBoxOwner(fullSection, box, mainPlayer);
        }
    }

    private void winSection(SectionPosition section) {
        BoxPosition current = BoxPosition.make(0, 0);
        BoxPosition increase = BoxPosition.make(1, 0);

        board.fakedSectionToPlayIn = section;
        TestUtils.applyMoveToBoard(board, new Move(section, current, mainPlayer));
        current = current.increaseBy(increase);

        board.fakedSectionToPlayIn = section;
        TestUtils.applyMoveToBoard(board, new Move(section, current, mainPlayer));
        current = current.increaseBy(increase);

        board.fakedSectionToPlayIn = section;
        TestUtils.applyMoveToBoard(board, new Move(section, current, mainPlayer));
    }

    private void backupBoardState() {
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
                origionalBoxOwners[x][y] = board.getBoxOwner(SectionPosition.make(x / 3, y / 3), BoxPosition.make(x % 3, y % 3));
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
                lines[x][y] = board.getLine(SectionPosition.make(x, y));
    }

    private void assertBoardStateUnchanged() {
        Assert.assertEquals(stackSize, board.getAllMoves().size());
        TestUtils.assertAreEqual(origionalSectionToPlayIn, board.getSectionToPlayIn());

        for (int x = 0; x < GridConstants.NUMBER_OF_BOXES_PER_SIDE; ++x)
            for (int y = 0; y < GridConstants.NUMBER_OF_BOXES_PER_SIDE; ++y)
                Assert.assertEquals(origionalBoxOwners[x][y], board.getBoxOwner(SectionPosition.make(x / 3, y / 3), BoxPosition.make(x % 3, y % 3)));

        for (int x = 0; x < GridConstants.NUMBER_OF_SECTIONS_PER_SIDE; ++x)
            for (int y = 0; y < GridConstants.NUMBER_OF_SECTIONS_PER_SIDE; ++y)
                Assert.assertEquals(origionalSectionOwners[x][y], board.getSectionOwner(SectionPosition.make(x, y)));

        for (int x = 0; x < GridConstants.NUMBER_OF_SECTIONS_PER_SIDE; ++x)
            for (int y = 0; y < GridConstants.NUMBER_OF_SECTIONS_PER_SIDE; ++y)
                Assert.assertEquals(lines[x][y], board.getLine(SectionPosition.make(x, y)));
    }
}
