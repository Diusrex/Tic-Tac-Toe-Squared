package com.diusrex.tictactoe.logic;

import java.util.Stack;

import com.diusrex.tictactoe.data_structures.BoardStatus;
import com.diusrex.tictactoe.data_structures.Move;

/*
 * Is to be used to save/load the state of the board from a string.
 * Will work with any special board, so long as the given board for loadBoardFromString has the same Engine as the one used for getSaveString.
 */
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
            
            board.applyMoveIfValid(move);
        }

        return board;
    }
}
