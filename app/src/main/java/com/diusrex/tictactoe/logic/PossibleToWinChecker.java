package com.diusrex.tictactoe.logic;

public class PossibleToWinChecker {
    static private final SectionPosition horizontalIncrease = SectionPosition.make(1, 0);
    static private final SectionPosition verticalIncrease = SectionPosition.make(0, 1);
    static private final SectionPosition topLeftToBottomRightIncrease = SectionPosition.make(1, 1);
    static private final SectionPosition topRightToBottomLeftIncrease = SectionPosition.make(-1, 1);

    public static boolean isStillPossibleToWin(BoardStatus board) {
        if (TicTacToeEngine.getWinner(board) != Player.Unowned)
            return true;

        // Will search through each line, and check to see if it does not
        // contain different players
        if (canWinHorizontal(board))
            return true;
        if (canWinVertical(board))
            return true;
        if (canWinDiagonalTL_To_BR(board))
            return true;
        if (canWinDiagonalTR_To_BL(board))
            return true;
        return false;
    }

    private static boolean canWinHorizontal(BoardStatus board) {
        SectionPosition start = SectionPosition.make(0, 0);
        for (int i = 0; i < BoardStatus.NUMBER_OF_SECTIONS_PER_SIDE; ++i, start = start.increaseBy(verticalIncrease)) {
            if (lineContainsOwnedAndOrSinglePlayer(board, start, horizontalIncrease))
                return true;
        }
        return false;
    }

    private static boolean canWinVertical(BoardStatus board) {
        SectionPosition start = SectionPosition.make(0, 0);
        for (int i = 0; i < BoardStatus.NUMBER_OF_SECTIONS_PER_SIDE; ++i, start = start.increaseBy(horizontalIncrease)) {
            if (lineContainsOwnedAndOrSinglePlayer(board, start, verticalIncrease))
                return true;
        }
        return false;
    }

    private static boolean canWinDiagonalTL_To_BR(BoardStatus board) {
        return lineContainsOwnedAndOrSinglePlayer(board, SectionPosition.make(0, 0), topLeftToBottomRightIncrease);
    }

    private static boolean canWinDiagonalTR_To_BL(BoardStatus board) {
        return lineContainsOwnedAndOrSinglePlayer(board, SectionPosition.make(2, 0), topRightToBottomLeftIncrease);
        }

    private static boolean lineContainsOwnedAndOrSinglePlayer(BoardStatus board, SectionPosition start,
            SectionPosition increase) {
        boolean containsPlayerOne = false;
        boolean containsPlayerTwo = false;
        boolean empty = true;

        for (int i = 0; i < BoardStatus.NUMBER_OF_SECTIONS_PER_SIDE; ++i, start = start.increaseBy(increase)) {
            Player p = board.getSectionOwner(start);
            if (p != Player.Unowned) {
                empty = false;

                if (p == Player.Player_1)
                    containsPlayerOne = true;
                else
                    containsPlayerTwo = true;
            }
        }

        return containsPlayerOne ^ containsPlayerTwo ^ empty;
    }
}
