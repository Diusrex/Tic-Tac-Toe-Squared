/*
 * Copyright 2015 Morgan Redshaw
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 **/
package com.diusrex.tictactoe.textbased;

import com.diusrex.tictactoe.data_structures.BoardStatus;
import com.diusrex.tictactoe.data_structures.Move;
import com.diusrex.tictactoe.data_structures.Player;
import com.diusrex.tictactoe.logic.BoardStatusFactory;
import com.diusrex.tictactoe.logic.GeneralTicTacToeLogic;
import com.diusrex.tictactoe.logic.PlayerFactory;
import com.diusrex.tictactoe.logic.PlayerFactory.WantedPlayer;

import java.util.Scanner;

public class Main {
    static PlayerController[] players = new PlayerController[2];
    static int currentPlayer;
    static BoardStatus board;

    static Scanner scanner;

    static public void main(String[] args) {
        scanner = new Scanner(System.in);
        runGame();
    }

    private static void runGame() {
        players[0] = new HumanPlayer(scanner);

        PlayerFactory.WantedPlayer wantedPlayer = getWantedPlayer();
        if (wantedPlayer == PlayerFactory.WantedPlayer.Human) {
            players[1] = new HumanPlayer(scanner);
        } else {
            players[1] = new AIPlayerController(PlayerFactory.createAIPlayer(wantedPlayer));
        }
        board = BoardStatusFactory.createStandardBoard();

        currentPlayer = 0;

        while (board.getWinner() == Player.Unowned && !GeneralTicTacToeLogic.boardIsFull(board)) {
            Move nextMove = getCurrentPlayersMove();

            while (!board.isValidMove(nextMove)) {
                players[currentPlayer].alertInvalidMove();

                nextMove = getCurrentPlayersMove();
            }
            board.applyMoveIfValid(nextMove);

            currentPlayer = (currentPlayer + 1) % 2;
        }

        if (board.getWinner() != Player.Unowned) {
            System.out.println("The winner is Player " + board.getWinner());
        } else {
            System.out.println("There was a tie");
        }
    }

    private static WantedPlayer getWantedPlayer() {
        while (true) {
            System.out.println("Do you want to play against a 'human', or an 'easy', 'medium', or 'hard' bot?");
            
            String wanted = scanner.next();
            switch (wanted) {
            case "human":
                return WantedPlayer.Human;
            case "easy":
                return WantedPlayer.Easy;
            case "medium":
                return WantedPlayer.Medium;
            case "hard":
                return WantedPlayer.Hard;
            }

            System.out.println("\nPlease enter a valid input.\n");
        }
    }

    private static Move getCurrentPlayersMove() {
        return players[currentPlayer].getPositionToPlay(board);
    }
}
