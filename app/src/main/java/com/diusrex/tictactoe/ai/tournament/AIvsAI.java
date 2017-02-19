package com.diusrex.tictactoe.ai.tournament;

import com.diusrex.tictactoe.ai.AIPlayer;
import com.diusrex.tictactoe.ai.FullMonteCarloPlayer;
import com.diusrex.tictactoe.ai.tournament.recording.GameTimeRecording;
import com.diusrex.tictactoe.data_structures.BoardStatus;
import com.diusrex.tictactoe.data_structures.Player;
import com.diusrex.tictactoe.logic.*;

public class AIvsAI {
    static public void main(String[] args) {
        AIPlayer player1 = new FullMonteCarloPlayer(500);
        AIPlayer player2 = PlayerFactory.createAIPlayer(PlayerFactory.WantedPlayer.Easy);

        testTimeTaken(player1, player2);

        player2 = PlayerFactory.createAIPlayer(PlayerFactory.WantedPlayer.Medium);
        testTimeTaken(player1, player2);

        player2 = PlayerFactory.createAIPlayer(PlayerFactory.WantedPlayer.Hard);
        testTimeTaken(player1, player2);
    }

    private static void testTimeTaken(AIPlayer player1, AIPlayer player2) {
        System.out.println("Started");
        GameTimeRecording time = new GameTimeRecording();
        GameInfo info = runGame(player1, player2, time);
        System.out.println("Time: " + time.getPlayerOneTime() + " " + time.getPlayerTwoTime());
        System.out.println("Won: " + info.getWinner());

        time = new GameTimeRecording();
        info = runGame(player2, player1, time);
        System.out.println("Time: " + time.getPlayerTwoTime() + " " + time.getPlayerOneTime());
        System.out.println("Won: " + info.getWinner().opposite());
    }
    
    public static GameInfo runGame(AIPlayer player, AIPlayer player2, GameTimeRecording times) {
        return runGame(player, player2, times, false);
    }

    public static GameInfo runAndLearnGame(AIPlayer player, AIPlayer player2, GameTimeRecording times) {
        return runGame(player, player2, times, true);
    }
    
    public static GameInfo runGame(AIPlayer player, AIPlayer player2, GameTimeRecording times, boolean learn) {
        AIPlayer[] players = new AIPlayer[2];
        players[0] = player;
        players[1] = player2;

        int currentPlayer = 0;
        BoardStatus board = BoardStatusFactory.createStandardBoard();
        player.newGame(board);
        player2.newGame(board);
        while (board.getWinner() == Player.Unowned && !GeneralTicTacToeLogic.boardIsFull(board)) {
            long timeBefore = AITournament.getCurrentTime();
            
            if (learn) {
                players[currentPlayer].learnFromChange(board);
            }
            

            board.applyMoveIfValid(players[currentPlayer].getPositionToPlay(board));

            times.addTime(AITournament.getCurrentTime() - timeBefore, currentPlayer);

            currentPlayer = 1 - currentPlayer;
        }
        
        if (learn) {
            players[0].learnFromChange(board);
            players[1].learnFromChange(board);
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
