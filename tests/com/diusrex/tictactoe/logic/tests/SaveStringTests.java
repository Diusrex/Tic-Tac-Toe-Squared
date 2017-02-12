package com.diusrex.tictactoe.logic.tests;

import static org.junit.Assert.assertEquals;

import java.util.Stack;

import org.junit.Before;
import org.junit.Test;

import com.diusrex.tictactoe.data_structures.BoardStatus;
import com.diusrex.tictactoe.data_structures.Move;
import com.diusrex.tictactoe.data_structures.Player;
import com.diusrex.tictactoe.data_structures.position.BoxPosition;
import com.diusrex.tictactoe.logic.GeneralTicTacToeLogic;
import com.diusrex.tictactoe.logic.StandardTicTacToeEngine;
import com.diusrex.tictactoe.logic.StringSaver;

public class SaveStringTests {
    BoardStatus mainBoard;
    BoardStatus generatedBoard;
    Move playerOneMove;
    Move playerTwoMove;

    @Before
    public void setup() {
        mainBoard = new BoardStatus(new StandardTicTacToeEngine());
        generatedBoard = new BoardStatus(new StandardTicTacToeEngine());
        playerOneMove = Move.make(mainBoard.getSectionToPlayIn(), BoxPosition.make(0, 0), Player.Player_1);
        playerTwoMove = Move.make(GeneralTicTacToeLogic.getSectionToPlayInNext(playerOneMove.getBox()),
                BoxPosition.make(1, 1), Player.Player_2);
    }

    @Test
    public void testSaveSingleMove() {
        TestUtils.applyMoveToBoard(mainBoard, playerOneMove);

        String saveString = StringSaver.getSaveString(mainBoard);

        // Need to make sure it doesn't change the boards state
        assertEquals(1, mainBoard.getAllMoves().size());

        generatedBoard = StringSaver.loadBoardFromString(generatedBoard, saveString);

        assertBoardsAreEqual();
    }

    @Test
    public void testSaveMultipleMoves() {
        TestUtils.applyMoveToBoard(mainBoard, playerOneMove);
        TestUtils.applyMoveToBoard(mainBoard, playerTwoMove);

        String saveString = StringSaver.getSaveString(mainBoard);

        // Need to make sure it doesn't change the boards state
        assertEquals(2, mainBoard.getAllMoves().size());

        generatedBoard = StringSaver.loadBoardFromString(generatedBoard, saveString);

        assertBoardsAreEqual();
    }

    private void assertBoardsAreEqual() {
        Stack<Move> expectedMoves = mainBoard.getAllMoves();
        Stack<Move> acutalMoves = generatedBoard.getAllMoves();

        assertEquals(expectedMoves.size(), acutalMoves.size());

        for (int i = 0; i < expectedMoves.size(); ++i) {
            TestUtils.assertAreEqual(expectedMoves.get(i), acutalMoves.get(i));
        }
    }
}
