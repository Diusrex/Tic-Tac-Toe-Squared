package com.diusrex.tictactoe.ai.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.junit.Test;

import com.diusrex.tictactoe.ai.AIPlayer;
import com.diusrex.tictactoe.ai.ForgetfulMonteCarloPlayer;
import com.diusrex.tictactoe.ai.FullMonteCarloPlayer;
import com.diusrex.tictactoe.ai.ScalingAlphaBetaPlayer;
import com.diusrex.tictactoe.ai.ScalingMiniMaxPlayer;
import com.diusrex.tictactoe.ai.UnScalingAlphaBetaPlayer;
import com.diusrex.tictactoe.ai.UnScalingMiniMaxPlayer;
import com.diusrex.tictactoe.data_structures.BoardStatus;
import com.diusrex.tictactoe.data_structures.Move;
import com.diusrex.tictactoe.data_structures.Player;
import com.diusrex.tictactoe.logic.PlayerFactory;
import com.diusrex.tictactoe.logic.StandardTicTacToeEngine;
import com.diusrex.tictactoe.logic.StringSaver;
import com.diusrex.tictactoe.logic.tests.TestUtils;

// Some of the board state tests are so long because none of the players will prefer winning quickly.
// Instead, they just care about guaranteeing their win sometime in the future. So can go on for a long time...
public class SanityTests {
    AIPlayer[] allPlayers = {
        new UnScalingAlphaBetaPlayer(PlayerFactory.getHardValues(), 2),
        new ScalingAlphaBetaPlayer(PlayerFactory.getHardValues()),
        new ScalingMiniMaxPlayer(PlayerFactory.getHardValues()),
        new UnScalingMiniMaxPlayer(PlayerFactory.getHardValues(), 3),
        new ForgetfulMonteCarloPlayerWrapper(),
        new FullMonteCarloPlayerWrapper()
    };
    
    // Do require the AIPlayers to have different identifiers.
    @Test
    public void testAllHaveUniqueIdentifier() {
        Map<String, Integer> identifiers = new HashMap<>();
        
        for (int i = 0; i < allPlayers.length; ++i) {
            String identifier = allPlayers[i].getIdentifier();
            
            assertFalse("Failed for index " + i, identifier == null);
            
            if (identifiers.containsKey(identifier)) {
                fail("Failed for index " + i + " and " + identifiers.get(identifier));
            }
            identifiers.put(identifier, i);
        }
    }
    
    @Test
    public void testAllWillTryToWin() {
        for (AIPlayer player : allPlayers) {
            BoardStatus board = getSimpleBoardWhereWillEndByTwoTurns();
            final Player aiPlayer = board.getNextPlayer();
            Move wantedMove = player.getPositionToPlay(board);

            TestUtils.applyMoveToBoard(board, wantedMove);

            assertTrue("Failed for player " + player.getIdentifier() + ", went to s" + wantedMove.getSection() + " b" + wantedMove.getBox(),
                    aiPlayer == board.getWinner());
        }
    }
    
    @Test
    public void testWillPlayOutsideSectionToWin() {
        for (AIPlayer player : allPlayers) {
            BoardStatus board = getBoardWherePlayOutsideSectionToWin();
            final Player aiPlayer = board.getNextPlayer();
            Move wantedMove = player.getPositionToPlay(board);

            TestUtils.applyMoveToBoard(board, wantedMove);

            assertTrue("Failed for player " + player.getIdentifier() + ", went to " + wantedMove + "   " + StringSaver.getSaveString(board)
                    + " Winner: "+ board.getWinner(),
                    aiPlayer == board.getWinner());
        }
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
        BoardStatus board = new BoardStatus(new StandardTicTacToeEngine());
        StringSaver.loadBoardFromString(board,
                "110010011211021020020002102102100210220220201200120100100012011011001201201202122100100212211011021221201201121121121212211111122222111112022002102022021111111211121121021022122222221011012212001001021000100202201011010210201201221212112112");
        return board;
    }

    /*
    Grid:

    2 1 1  1 1 2  2 1 2
    2 1 1  2 1 2  1 2 2
    1 1 1  1 2 2  1 1 2

    1 2 1  2 2 1  2 1 1
    1 1 1  2 1 2  2 1 1
    1 2 1  1 2 2  2 1 1

    2 2 2  2 1 2  2 2 1
    2 2 2  2 1 2  2 1 2
    1 2 0  1 1 2  0 1 1

    With the ownership grid:
    1 2 2
    1 1 2
    2 1 0

    Player is playing into middle (as P2). Can play into bottom right and win, or bottom left and lose.
    */
    private BoardStatus getBoardWherePlayOutsideSectionToWin() {
        BoardStatus board = new BoardStatus(new StandardTicTacToeEngine());
        StringSaver.loadBoardFromString(board,
                "11201201121102102112111111100200021020020020120002001111122222201202222211111102101111101201111111221211111212212112100200211210120121121022020210221221221220020022122012010010001201021021221202102012010110110210001000020010110202201011021221101102222222122212212012021221121120020012112012012012020220021021021010110012012212210210021022022001101122121011012212121122022012112222221211221221111");
        return board;
    }

    private class FullMonteCarloPlayerWrapper extends FullMonteCarloPlayer {
        public FullMonteCarloPlayerWrapper() {
            super(500);
        }

        @Override
        protected Random getRandom() {
            return new Random(0);
        }
    }
    private class ForgetfulMonteCarloPlayerWrapper extends ForgetfulMonteCarloPlayer {
        public ForgetfulMonteCarloPlayerWrapper() {
            super(500);
        }

        @Override
        protected Random getRandom() {
            return new Random(0);
        }
    }
}
