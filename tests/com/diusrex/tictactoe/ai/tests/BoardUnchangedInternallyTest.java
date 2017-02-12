package com.diusrex.tictactoe.ai.tests;

import com.diusrex.tictactoe.ai.*;
import com.diusrex.tictactoe.data_structures.BoardStatus;
import com.diusrex.tictactoe.data_structures.Move;
import com.diusrex.tictactoe.logic.PlayerFactory;
import com.diusrex.tictactoe.logic.StandardTicTacToeEngine;
import com.diusrex.tictactoe.logic.StringSaver;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

// Uses multiple different custom wrappers that don't create a copy of the board to ensure
// the bot doesn't alter the game unexpectedly.
public class BoardUnchangedInternallyTest {
    BoardStatus board;

    @Before
    public void setup() {
        board = new BoardStatus(new StandardTicTacToeEngine());
    }

    @Test
    public void testFullMonteCarloPlayer() {
        AIPlayer player = new FullMonteCarloPlayerWrapper();
        assertWillNotChangeState(player);
    }

    @Test
    public void testForgetfulMonteCarloPlayer() {
        AIPlayer player = new ForgetfulMonteCarloPlayerWrapper();
        assertWillNotChangeState(player);
    }

    @Test
    public void testScalingAlphaBetaPlayer() {
        AIPlayer player = new ScalingAlphaBetaPlayerWrapper();
        assertWillNotChangeState(player);
    }

    @Test
    public void tesUnScalingAlphaBetaPlayer() {
        AIPlayer player = new UnScalingAlphaBetaPlayerWrapper();
        assertWillNotChangeState(player);
    }

    @Test
    public void testScalingMiniMaxPlayer() {
        AIPlayer player = new ScalingMiniMaxPlayerWrapper();
        assertWillNotChangeState(player);
    }

    @Test
    public void testUnScalingMiniMaxPlayer() {
        AIPlayer player = new UnScalingMiniMaxPlayerWrapper();
        assertWillNotChangeState(player);
    }

    private void assertWillNotChangeState(AIPlayer player) {
        assertBoardUnchanged(player);

        addMovesToBoard(board);

        assertBoardUnchanged(player);
    }

    private void assertBoardUnchanged(AIPlayer player) {
        String stateBefore = StringSaver.getSaveString(board);
        player.getPositionToPlay(board);
        assertEquals(stateBefore, StringSaver.getSaveString(board));
    }

    private void addMovesToBoard(BoardStatus board) {
        StringSaver.loadBoardFromString(board, "110010011211021020020002102102100210220220201200120100100012011011001201201202122100100212211011021221201201121121121212211111122222111112022002102022021111111211121121021022122222221011012212001001021000100202201011010210201201221212112112");
    }

    private class FullMonteCarloPlayerWrapper extends FullMonteCarloPlayer {
        public FullMonteCarloPlayerWrapper() {
            super(500);
        }

        @Override
        public Move getPositionToPlay(BoardStatus board) {
            return choosePosition(board);
        }
    }

    private class ForgetfulMonteCarloPlayerWrapper extends ForgetfulMonteCarloPlayer {
        public ForgetfulMonteCarloPlayerWrapper() {
            super(500);
        }

        @Override
        public Move getPositionToPlay(BoardStatus board) {
            return choosePosition(board);
        }
    }

    private class ScalingAlphaBetaPlayerWrapper extends ScalingAlphaBetaPlayer {
        public ScalingAlphaBetaPlayerWrapper() {
            super(PlayerFactory.getHardValues());
        }

        @Override
        public Move getPositionToPlay(BoardStatus board) {
            return choosePosition(board);
        }
    }

    private class UnScalingAlphaBetaPlayerWrapper extends UnScalingAlphaBetaPlayer {
        public UnScalingAlphaBetaPlayerWrapper() {
            super(PlayerFactory.getHardValues(), 3);
        }

        @Override
        public Move getPositionToPlay(BoardStatus board) {
            return choosePosition(board);
        }
    }

    private class ScalingMiniMaxPlayerWrapper extends ScalingMiniMaxPlayer {
        public ScalingMiniMaxPlayerWrapper() {
            super(PlayerFactory.getHardValues());
        }

        @Override
        public Move getPositionToPlay(BoardStatus board) {
            return choosePosition(board);
        }
    }

    private class UnScalingMiniMaxPlayerWrapper extends UnScalingMiniMaxPlayer {
        public UnScalingMiniMaxPlayerWrapper() {
            super(PlayerFactory.getHardValues(), 3);
        }

        @Override
        public Move getPositionToPlay(BoardStatus board) {
            return choosePosition(board);
        }
    }
}
