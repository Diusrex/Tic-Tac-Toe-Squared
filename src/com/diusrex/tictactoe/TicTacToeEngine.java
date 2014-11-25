package com.diusrex.tictactoe;

public class TicTacToeEngine {

    public static boolean applyMove(BoardStatus board, Move move) {
        if (isInvalidMove(board, move))
            return false;

        board.setBoxValue(move.getPosition(), move.getPlayer());
        return true;
    }

    private static boolean isInvalidMove(BoardStatus board, Move move) {
        return isOutsideBounds(board, move) || isOutOfOrder(board, move)
                || isAlreadyOwned(board, move)
                || move.getPlayer() == Player.Unowned;
    }

    private static boolean isOutOfOrder(BoardStatus board, Move move) {
        int p1Count = board.getPlayerCount(Player.Player_1);
        int p2Count = board.getPlayerCount(Player.Player_2);

        if (move.getPlayer() == Player.Player_1)
            ++p1Count;

        else
            ++p2Count;

        // The two times where it will fail
        return p1Count < p2Count || p1Count > p2Count + 1;
    }

    private static boolean isAlreadyOwned(BoardStatus board, Move move) {
        return board.getBoxOwner(move.getPosition()) != Player.Unowned;
    }

    private static boolean isOutsideBounds(BoardStatus board, Move move) {
        return !board.isInsideBounds(move.getPosition());
    }
}
