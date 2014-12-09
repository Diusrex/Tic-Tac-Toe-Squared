package com.diusrex.tictactoe.textbased;

import java.util.Scanner;

import com.diusrex.tictactoe.logic.BoardStatus;
import com.diusrex.tictactoe.logic.BoxPosition;
import com.diusrex.tictactoe.logic.Move;
import com.diusrex.tictactoe.logic.Player;
import com.diusrex.tictactoe.logic.TicTacToeEngine;

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
            TicTacToeEngine.applyMove(board, nextMove);
            
            currentPlayer = (currentPlayer + 1) % 2;
        }
        
    }

    private static Move getCurrentPlayersMove() {
        BoxPosition chosenPos = players[currentPlayer].getPositionToPlay(board);
        return new Move(chosenPos, players[currentPlayer].getWhichPlayer());
    }
}
