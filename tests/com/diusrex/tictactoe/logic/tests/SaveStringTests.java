package com.diusrex.tictactoe.logic.tests;

import java.util.Stack;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.diusrex.tictactoe.data_structures.BoardStatus;
import com.diusrex.tictactoe.data_structures.BoxPosition;
import com.diusrex.tictactoe.data_structures.Move;
import com.diusrex.tictactoe.data_structures.Player;
import com.diusrex.tictactoe.logic.StringSaver;
import com.diusrex.tictactoe.logic.TicTacToeEngine;

public class SaveStringTests {
    BoardStatus mainBoard;
    BoardStatus generatedBoard;
    Move playerOneMove;
    Move playerTwoMove;

    @Before
    public void setup() {
        mainBoard = new BoardStatus();
        generatedBoard = new BoardStatus();
        playerOneMove = new Move(mainBoard.getSectionToPlayIn(), BoxPosition.make(0, 0), Player.Player_1);
        playerTwoMove = new Move(TicTacToeEngine.getSectionToPlayInNext(playerOneMove.getBox()),
                BoxPosition.make(1, 1), Player.Player_2);
    }

    @Test
    public void testSaveSingleMove() {
        mainBoard.applyMove(playerOneMove);

        String saveString = StringSaver.getSaveString(mainBoard);

        // Need to make sure it doesn't change the boards state
        Assert.assertEquals(1, mainBoard.getAllMoves().size());

        generatedBoard = StringSaver.loadBoardFromString(generatedBoard, saveString);

        assertBoardsAreEqual();
    }

    @Test
    public void testSaveMultipleMoves() {
        mainBoard.applyMove(playerOneMove);
        mainBoard.applyMove(playerTwoMove);

        String saveString = StringSaver.getSaveString(mainBoard);

        // Need to make sure it doesn't change the boards state
        Assert.assertEquals(2, mainBoard.getAllMoves().size());

        generatedBoard = StringSaver.loadBoardFromString(generatedBoard, saveString);

        assertBoardsAreEqual();
    }

    private void assertBoardsAreEqual() {
        Stack<Move> expectedMoves = mainBoard.getAllMoves();
        Stack<Move> acutalMoves = generatedBoard.getAllMoves();
        
        Assert.assertEquals(expectedMoves.size(), acutalMoves.size());
        
        for (int i = 0; i < expectedMoves.size(); ++i) {
            TestUtils.assertAreEqual(expectedMoves.get(i), acutalMoves.get(i));
        }
    }
}
