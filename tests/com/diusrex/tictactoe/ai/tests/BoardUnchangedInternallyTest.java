package com.diusrex.tictactoe.ai.tests;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.diusrex.tictactoe.ai.AIPlayer;
import com.diusrex.tictactoe.ai.ForgetfulMonteCarloPlayer;
import com.diusrex.tictactoe.ai.FullMonteCarloPlayer;
import com.diusrex.tictactoe.ai.ScalingAlphaBetaPlayer;
import com.diusrex.tictactoe.ai.ScalingMiniMaxPlayer;
import com.diusrex.tictactoe.ai.UnScalingAlphaBetaPlayer;
import com.diusrex.tictactoe.ai.UnScalingMiniMaxPlayer;
import com.diusrex.tictactoe.data_structures.Move;
import com.diusrex.tictactoe.data_structures.Player;
import com.diusrex.tictactoe.data_structures.board_status.BoardStatus;
import com.diusrex.tictactoe.data_structures.board_status.BoardStatusFactory;
import com.diusrex.tictactoe.data_structures.board_status.StringSaver;
import com.diusrex.tictactoe.data_structures.position.SectionPosition;
import com.diusrex.tictactoe.logic.PlayerFactory;
import com.diusrex.tictactoe.logic.StandardTicTacToeEngine;

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

        final Player un = Player.Unowned;
        final Player p1 = Player.Player_1;
        final Player p2 = Player.Player_2;
        // Load arbitrary board.
        board = BoardStatusFactory.createSpecificStandardBoard(Player.Player_1,
                SectionPosition.make(1, 1), new Player[][]{
            {un, p2, p2, p1, p2, p1, un, p1, p1},
            {p2, p2, p2, p2, un, p2, p2, p2, p2},
            {p1, un, un, p1, p2, p1, p1, p2, un},
            
            {p1, p1, p1, p1, un, p2, p1, p1, p1},
            {un, un, un, un, p2, p1, un, p1, p2},
            {un, un, un, p1, un, p2, un, un, un},
            
            {p2, p2, p2, p1, p2, un, un, p1, un},
            {un, p1, un, un, p2, un, un, p1, un},
            {p2, un, un, un, p1, un, un, un, p2}
        }, new Player[][]{
            {p2, un, p2},
            {p1, un, p1},
            {p2, un, un}
        });
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
