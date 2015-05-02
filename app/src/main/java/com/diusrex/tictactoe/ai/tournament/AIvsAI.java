package com.diusrex.tictactoe.ai.tournament;

import com.diusrex.tictactoe.ai.AIPlayer;
import com.diusrex.tictactoe.data_structures.BoardStatus;
import com.diusrex.tictactoe.data_structures.Player;
import com.diusrex.tictactoe.logic.BoardStatusFactory;
import com.diusrex.tictactoe.logic.GeneralTicTacToeLogic;

public class AIvsAI {
    public static Player runGame(AIPlayer player, AIPlayer player2) {
        AIPlayer[] players = new AIPlayer[2];
        players[0] = player;
        players[1] = player2;

        int currentPlayer = 0;
        BoardStatus board = BoardStatusFactory.createStandardBoard();
        while (board.getWinner() == Player.Unowned && !GeneralTicTacToeLogic.boardIsFull(board)) {
            board.applyMoveIfValid(players[currentPlayer].getPositionToPlay(board));

            currentPlayer = 1 - currentPlayer;
        }

        return board.getWinner();
    }
}
