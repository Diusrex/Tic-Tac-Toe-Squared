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
import com.diusrex.tictactoe.data_structures.Move;
import com.diusrex.tictactoe.data_structures.Player;
import com.diusrex.tictactoe.data_structures.board_status.BoardStatus;
import com.diusrex.tictactoe.data_structures.board_status.BoardStatusFactory;
import com.diusrex.tictactoe.data_structures.board_status.StringSaver;
import com.diusrex.tictactoe.data_structures.position.SectionPosition;
import com.diusrex.tictactoe.logic.PlayerFactory;
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
     Player is playing into middle (as P1). Correct move causes win, incorrect causes other player to win
     */
    public static BoardStatus getSimpleBoardWhereWillEndByTwoTurns() {
        final Player un = Player.Unowned;
        final Player p1 = Player.Player_1;
        final Player p2 = Player.Player_2;
        return BoardStatusFactory.createSpecificStandardBoard(Player.Player_1,
                SectionPosition.make(1, 1), new Player[][]{
            {un, p2, p2, p1, p2, p1, un, p1, p1},
            {p2, p2, p2, p2, un, p2, p2, p2, p2},
            {p1, un, un, p1, p2, p1, p1, p2, un},
            
            {p1, p1, p1, p1, un, p2, p1, p1, p1},
            {un, un, un, un, p2, p1, un, p1, p2},
            {un, un, un, p1, p1, p2, un, un, un},
            
            {p2, p2, p2, p1, p2, un, un, p1, un},
            {un, p1, un, un, p2, un, un, p1, un},
            {p2, un, un, un, p1, un, un, un, p2}
        }, new Player[][]{
            {p2, un, p2},
            {p1, un, p1},
            {p2, un, un}
        });
    }

    /*

    Player is playing into middle (as P2). Can play into bottom right and win, or bottom left and lose.
    */
    private BoardStatus getBoardWherePlayOutsideSectionToWin() {
        final Player un = Player.Unowned;
        final Player p1 = Player.Player_1;
        final Player p2 = Player.Player_2;
        return BoardStatusFactory.createSpecificStandardBoard(Player.Player_2,
                SectionPosition.make(1, 1), new Player[][]{
            {p2, p1, p1, p1, p1, p2, p2, p1, p2},
            {p2, p1, p1, p2, p1, p2, p1, p2, p2},
            {p1, p1, p1, p1, p2, p2, p1, p1, p2},
            
            {p1, p2, p1, p2, p2, p1, p2, p1, p1},
            {p1, p1, p1, p2, p1, p2, p2, p1, p1},
            {p1, p2, p1, p1, p2, p2, p2, p1, p1},
            
            {p2, p2, p2, p2, p1, p2, p2, p2, p1},
            {p2, p2, p2, p2, p1, p2, p2, p1, p2},
            {p1, p2, un, p1, p1, p2, un, p1, p1}
        }, new Player[][]{
            {p1, p2, p2},
            {p1, p1, p2},
            {p2, p1, un}
        });
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
