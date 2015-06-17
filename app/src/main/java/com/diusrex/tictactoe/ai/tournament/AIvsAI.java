package com.diusrex.tictactoe.ai.tournament;

import com.diusrex.tictactoe.ai.AIPlayer;
import com.diusrex.tictactoe.ai.ScalingAlphaBetaPlayer;
import com.diusrex.tictactoe.ai.ScalingMiniMaxPlayer;
import com.diusrex.tictactoe.ai.UnScalingAlphaBetaPlayer;
import com.diusrex.tictactoe.data_structures.BoardStatus;
import com.diusrex.tictactoe.data_structures.Player;
import com.diusrex.tictactoe.logic.BoardStatusFactory;
import com.diusrex.tictactoe.logic.GeneralTicTacToeLogic;
import com.diusrex.tictactoe.logic.PlayerFactory;

public class AIvsAI {
    static public void main(String[] args) {
        AIPlayer player1 = new ScalingMiniMaxPlayer(PlayerFactory.getHardValues());
        AIPlayer player2 = new ScalingMiniMaxPlayer(PlayerFactory.getHardValues());

        
        System.out.println("With MiniMax");
        testTimeTaken(player1, player2);

        player1 = new UnScalingAlphaBetaPlayer(PlayerFactory.getHardValues(), 6);
        player2 = new UnScalingAlphaBetaPlayer(PlayerFactory.getHardValues(), 6);

        System.out.println("With UnscalingAb");
        testTimeTaken(player1, player2);

        System.out.println("With scalingAb");
        player1 = new ScalingAlphaBetaPlayer(PlayerFactory.getHardValues());
        player2 = new ScalingAlphaBetaPlayer(PlayerFactory.getHardValues());

        testTimeTaken(player1, player2);
    }

    private static void testTimeTaken(AIPlayer player1, AIPlayer player2) {
        TimeInfo.GameTimeInfo time = new TimeInfo.GameTimeInfo();
        runGame(player1, player2, time);
        System.out.println("Time: " + time.getPlayerOneTime() + " " + time.getPlayerTwoTime());
        
        time = new TimeInfo.GameTimeInfo();
        runGame(player2, player1, time);
        System.out.println("Time: " + time.getPlayerOneTime() + " " + time.getPlayerTwoTime());
    }

    public static GameInfo runGame(AIPlayer player, AIPlayer player2, TimeInfo.GameTimeInfo times) {
        AIPlayer[] players = new AIPlayer[2];
        players[0] = player;
        players[1] = player2;

        int currentPlayer = 0;
        BoardStatus board = BoardStatusFactory.createStandardBoard();
        while (board.getWinner() == Player.Unowned && !GeneralTicTacToeLogic.boardIsFull(board)) {
            long timeBefore = AITournament.getCurrentTime();

            board.applyMoveIfValid(players[currentPlayer].getPositionToPlay(board));

            times.addTime(AITournament.getCurrentTime() - timeBefore, currentPlayer);

            currentPlayer = 1 - currentPlayer;
        }

        return new GameInfo(board.getWinner(), board.getAllMoves().size());
    }
    
    public static class GameInfo {
        private final Player winner;
        private final int depth;
        
        GameInfo(Player winner, int depth) {
            this.winner = winner;
            this.depth = depth;
        }

        public Player getWinner() {
            return winner;
        }

        public int getDepth() {
            return depth;
        }
    }
}
