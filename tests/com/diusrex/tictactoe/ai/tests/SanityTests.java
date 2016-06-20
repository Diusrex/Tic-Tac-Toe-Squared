package com.diusrex.tictactoe.ai.tests;

import com.diusrex.tictactoe.ai.*;
import com.diusrex.tictactoe.data_structures.*;
import com.diusrex.tictactoe.logic.PlayerFactory;
import com.diusrex.tictactoe.logic.StandardTicTacToeEngine;
import com.diusrex.tictactoe.logic.StringSaver;
import com.diusrex.tictactoe.logic.tests.TestUtils;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertTrue;

public class SanityTests {
    @Test
    public void testForgetfulMonteCarlo() {
        AIPlayer player = new ForgetfulMonteCarloPlayer(10000);
        assertWillTryToWin(player);
        assertIsAbleToHandleDraw(player);
    }

    @Test
    public void testFullMonteCarlo() {
        AIPlayer player = new FullMonteCarloPlayerWrapper();
        assertWillTryToWin(player);
        assertIsAbleToHandleDraw(player);
    }

    @Test
    public void testScalingAlphaBetaPlayer() {
        AIPlayer player = new ScalingAlphaBetaPlayer(PlayerFactory.getHardValues());
        assertWillTryToWin(player);
        assertIsAbleToHandleDraw(player);
    }

    @Test
    public void testUnScalingAlphaBetaPlayer() {
        AIPlayer player = new UnScalingAlphaBetaPlayer(PlayerFactory.getHardValues(), 3);
        assertWillTryToWin(player);
        assertIsAbleToHandleDraw(player);
    }

    @Test
    public void testScalingMiniMaxPlayer() {
        AIPlayer player = new ScalingMiniMaxPlayer(PlayerFactory.getHardValues());
        assertWillTryToWin(player);
        assertIsAbleToHandleDraw(player);
    }

    @Test
    public void testUnScalingMiniMaxPlayer() {
        AIPlayer player = new UnScalingMiniMaxPlayer(PlayerFactory.getHardValues(), 3);
        assertWillTryToWin(player);
        assertIsAbleToHandleDraw(player);
    }

    private void assertWillTryToWin(AIPlayer player) {
        BoardStatus board = getSimpleBoardWhereWillEndByTwoTurns();

        final Player aiPlayer = board.getNextPlayer();
        Move wantedMove = player.getPositionToPlay(board);

        TestUtils.applyMoveToBoard(board, wantedMove);

        assertTrue(aiPlayer == board.getWinner());
    }

    /*
     Grid:

     0 2 2  1 2 1  0 1 1
     2 2 2  2 0 2  2 2 2
     1 0 0  1 2 1  1 2 0

     1 1 1  1 0 2  1 1 1
     0 0 0  0 2 1  0 1 2
     0 0 0  1 1 2  0 0 0

     2 2 2  1 2 0  0 1 0
     0 1 0  0 2 0  0 1 0
     2 0 0  0 1 0  0 0 2

     With the ownership grid:
     2 0 2
     1 0 1
     2 0 0

     Player is playing into middle (as P1). Correct move causes win, incorrect causes other player to win
     */
    public static BoardStatus getSimpleBoardWhereWillEndByTwoTurns() {
        // There are two moves it can make.
        BoardStatus board = new BoardStatus(new StandardTicTacToeEngine());
        StringSaver.loadBoardFromString(board, "110010011211021020020002102102100210220220201200120100100012011011001201201202122100100212211011021221201201121121121212211111122222111112022002102022021111111211121121021022122222221011012212001001021000100202201011010210201201221212112112");
        return board;
    }

    private void assertIsAbleToHandleDraw(AIPlayer player) {
        BoardStatus board = getBoardWhereWillDrawAfterCurrentMove();

        Move wantedMove = player.getPositionToPlay(board);

        TestUtils.applyMoveToBoard(board, wantedMove);

        //TODO: This test doesn't actually work....
        // So all should fail
        assertTrue("Expected to fail for now", board.getWinner() == Player.Unowned);
    }

    private BoardStatus getBoardWhereWillDrawAfterCurrentMove() {
        // There is only move it can make, and it will end in a draw
        BoardStatus board = new BoardStatus(new StandardTicTacToeEngine());
        StringSaver.loadBoardFromString(board, "110010011211021020020002102102100210220220201200120100100012011011001201201202122100100212211011021221201201121121121212211111122222111112022002102022021111111211121121021022122222221011012212001001021000100202201011010210201201221212112112");
        return board;
    }



    private class FullMonteCarloPlayerWrapper extends FullMonteCarloPlayer {
        public FullMonteCarloPlayerWrapper() {
            super(100);
        }

        @Override
        protected Random getRandom() {
            return new Random(0);
        }
    }
}
