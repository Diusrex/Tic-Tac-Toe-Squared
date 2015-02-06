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

import com.diusrex.tictactoe.logic.BoardStatus;
import com.diusrex.tictactoe.logic.BoxPosition;
import com.diusrex.tictactoe.logic.Move;
import com.diusrex.tictactoe.logic.Player;
import com.diusrex.tictactoe.logic.TicTacToeEngine;

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
        players[0] = new HumanPlayer(Player.Player_1, scanner);
        players[1] = new HumanPlayer(Player.Player_2, scanner);
        board = new BoardStatus();

        currentPlayer = 0;
        
        while (TicTacToeEngine.getWinner(board) == Player.Unowned) {
            Move nextMove = getCurrentPlayersMove();
            
            while (!TicTacToeEngine.isValidMove(board, nextMove)) {
                players[currentPlayer].alertInvalidMove();
                
                nextMove = getCurrentPlayersMove();
            }
            TicTacToeEngine.applyMoveIfValid(board, nextMove);
            
            currentPlayer = (currentPlayer + 1) % 2;
        }
        
    }

    private static Move getCurrentPlayersMove() {
        BoxPosition chosenPos = players[currentPlayer].getPositionToPlay(board);
        return new Move(chosenPos, players[currentPlayer].getWhichPlayer());
    }
}
