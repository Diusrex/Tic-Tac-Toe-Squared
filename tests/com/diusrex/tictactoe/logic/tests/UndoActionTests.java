package com.diusrex.tictactoe.logic.tests;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.diusrex.tictactoe.logic.BoardStatus;
import com.diusrex.tictactoe.logic.BoxPosition;
import com.diusrex.tictactoe.logic.Line;
import com.diusrex.tictactoe.logic.Move;
import com.diusrex.tictactoe.logic.Player;
import com.diusrex.tictactoe.logic.SectionPosition;
import com.diusrex.tictactoe.logic.UndoAction;
import com.diusrex.tictactoe.logic.tests.TestUtils.BoardStatusNoCount;

public class UndoActionTests {
    BoardStatusNoCount board;

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
        board = new BoardStatusNoCount(new SectionPosition(0, 0));
        mainPlayer = Player.Player_1;
        board.playerToGoNext = mainPlayer;

        mainSection = new SectionPosition(1, 1);
        appliedMove = new Move(new BoxPosition(1, 1), mainPlayer);

        TestUtils.applyMoveToBoard(board, appliedMove);

        validMove = new Move(new BoxPosition(3, 3), mainPlayer);

        backupBoardState();
    }

    @Test
    public void testUndoWithNoMovesLeft() {
        // This will be fine
        UndoAction.undoLastMove(board);

        // This should not throw
        try {
            UndoAction.undoLastMove(board);
        } catch (Exception e) {
            Assert.assertTrue(false);
        }
    }

    @Test
    public void testUndoMove() {
        TestUtils.applyMoveToBoard(board, validMove);

        UndoAction.undoLastMove(board);

        assertBoardStateUnchanged();
    }

    @Test
    public void testUndoMoveSupposedToPlayInFull() {
        SectionPosition fullSection = new SectionPosition(1, 1);
        fillSection(fullSection);

        BoxPosition pos = new BoxPosition(8, 8);
        Move move = new Move(pos, mainPlayer);

        board.setSectionToPlayIn(fullSection);
        backupBoardState();

        TestUtils.applyMoveToBoard(board, move);

        UndoAction.undoLastMove(board);

        assertBoardStateUnchanged();
    }

    @Test
    public void testUndoSectionWinningMove() {
        SectionPosition sectionToWin = new SectionPosition(0, 0);
        winSection(sectionToWin);

        Assert.assertEquals(mainPlayer, board.getSectionOwner(sectionToWin));
        UndoAction.undoLastMove(board);

        Assert.assertEquals(Player.Unowned, board.getSectionOwner(sectionToWin));
        Assert.assertEquals(null, board.getLine(sectionToWin));
    }

    @Test
    public void testUndoSectionDoesntLoseSection() {
        SectionPosition sectionToWin = new SectionPosition(0, 0);
        winSection(sectionToWin);

        Assert.assertEquals(mainPlayer, board.getSectionOwner(sectionToWin));

        board.setSectionToPlayIn(sectionToWin);
        BoxPosition moveThatDoesntEffectOwnership = new BoxPosition(2, 2);
        TestUtils.applyMoveToBoard(board, new Move(moveThatDoesntEffectOwnership, mainPlayer));

        UndoAction.undoLastMove(board);

        Assert.assertEquals(mainPlayer, board.getSectionOwner(sectionToWin));
        TestUtils.testLinesAreEqual(new Line(new BoxPosition(0, 0), new BoxPosition(2, 0)), board.getLine(sectionToWin));
    }

    private void fillSection(SectionPosition fullSection) {
        BoxPosition offset = fullSection.getTopLeftPosition();
        for (int x = 0; x < 3; ++x) {
            for (int y = 0; y < 3; ++y) {
                BoxPosition basePosition = new BoxPosition(x, y);
                board.setBoxOwner(basePosition.increaseBy(offset), mainPlayer);
            }
        }
    }

    private void winSection(SectionPosition section) {
        BoxPosition current = section.getTopLeftPosition();
        BoxPosition increase = new BoxPosition(1, 0);
        
        board.setSectionToPlayIn(section);
        TestUtils.applyMoveToBoard(board, new Move(current, mainPlayer));
        current = current.increaseBy(increase);
        
        board.setSectionToPlayIn(section);
        TestUtils.applyMoveToBoard(board, new Move(current, mainPlayer));
        current = current.increaseBy(increase);
        
        board.setSectionToPlayIn(section);
        TestUtils.applyMoveToBoard(board, new Move(current, mainPlayer));
    }

    private void backupBoardState() {
        stackSize = board.getAllMoves().size();
        origionalSectionToPlayIn = board.getSectionToPlayIn();

        backupBoxOwners();

        backupSectionOwners();

        backupLines();
    }

    private void backupBoxOwners() {
        origionalBoxOwners = new Player[BoardStatus.NUMBER_OF_BOXES_PER_SIDE][BoardStatus.NUMBER_OF_BOXES_PER_SIDE];
        for (int x = 0; x < BoardStatus.NUMBER_OF_BOXES_PER_SIDE; ++x)
            for (int y = 0; y < BoardStatus.NUMBER_OF_BOXES_PER_SIDE; ++y)
                origionalBoxOwners[x][y] = board.getBoxOwner(new BoxPosition(x, y));
    }

    private void backupSectionOwners() {
        origionalSectionOwners = new Player[BoardStatus.NUMBER_OF_SECTIONS_PER_SIDE][BoardStatus.NUMBER_OF_SECTIONS_PER_SIDE];
        for (int x = 0; x < BoardStatus.NUMBER_OF_SECTIONS_PER_SIDE; ++x)
            for (int y = 0; y < BoardStatus.NUMBER_OF_SECTIONS_PER_SIDE; ++y)
                origionalSectionOwners[x][y] = board.getSectionOwner(new SectionPosition(x, y));
    }
    
    private void backupLines() {
        lines = new Line[BoardStatus.NUMBER_OF_SECTIONS_PER_SIDE][BoardStatus.NUMBER_OF_SECTIONS_PER_SIDE];
        for (int x = 0; x < BoardStatus.NUMBER_OF_SECTIONS_PER_SIDE; ++x)
            for (int y = 0; y < BoardStatus.NUMBER_OF_SECTIONS_PER_SIDE; ++y)
                lines[x][y] = board.getLine(new SectionPosition(x, y));
    }

    private void assertBoardStateUnchanged() {
        Assert.assertEquals(stackSize, board.getAllMoves().size());
        TestUtils.assertAreEqual(origionalSectionToPlayIn, board.getSectionToPlayIn());

        for (int x = 0; x < BoardStatus.NUMBER_OF_BOXES_PER_SIDE; ++x)
            for (int y = 0; y < BoardStatus.NUMBER_OF_BOXES_PER_SIDE; ++y)
                Assert.assertEquals(origionalBoxOwners[x][y], board.getBoxOwner(new BoxPosition(x, y)));

        for (int x = 0; x < BoardStatus.NUMBER_OF_SECTIONS_PER_SIDE; ++x)
            for (int y = 0; y < BoardStatus.NUMBER_OF_SECTIONS_PER_SIDE; ++y)
                Assert.assertEquals(origionalSectionOwners[x][y], board.getSectionOwner(new SectionPosition(x, y)));

        for (int x = 0; x < BoardStatus.NUMBER_OF_SECTIONS_PER_SIDE; ++x)
            for (int y = 0; y < BoardStatus.NUMBER_OF_SECTIONS_PER_SIDE; ++y)
                Assert.assertEquals(lines[x][y], board.getLine(new SectionPosition(x, y)));
    }
}
