package com.diusrex.tictactoe.logic;

import java.util.Stack;

import com.diusrex.tictactoe.data_structures.BoardStatus;
import com.diusrex.tictactoe.data_structures.Move;

public class StringSaver {
    private StringSaver() {
    }

    public static String getSaveString(BoardStatus board) {
        Stack<Move> allMoves = board.getAllMoves();
        StringBuffer buffer = new StringBuffer();
        for (Move move : allMoves) {
            buffer.append(move.toString());
        }

        return buffer.toString();
    }

    /*
     * Will change the given board
     * All of the moves stored in the string must be valid
     * Will not be a problem unless the string was not created from this class
     */
    public static BoardStatus loadBoardFromString(BoardStatus board, String savedBoardStatus) {
        // Is <= because i + Move.SIZE_OF_SAVED_MOVE is not inclusive on the end
        for (int i = 0; i + Move.SIZE_OF_SAVED_MOVE <= savedBoardStatus.length(); i += Move.SIZE_OF_SAVED_MOVE) {
            Move move = Move.fromString(savedBoardStatus.substring(i, i + Move.SIZE_OF_SAVED_MOVE));
            TicTacToeEngine.applyMoveIfValid(board, move);
        }

        return board;
    }
}
