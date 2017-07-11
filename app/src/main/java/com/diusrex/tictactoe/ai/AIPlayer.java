package com.diusrex.tictactoe.ai;

import java.io.PrintStream;

import com.diusrex.tictactoe.data_structures.Move;
import com.diusrex.tictactoe.data_structures.board_status.BoardStatus;
import com.diusrex.tictactoe.logic.GeneralTicTacToeLogic;

public abstract class AIPlayer {
    public Move getPositionToPlay(BoardStatus board) {
        BoardStatus newBoard;
        try {
            newBoard = (BoardStatus) board.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Was unable to clone board");
        }
        return choosePosition(newBoard);
    }

    protected boolean canPlayInAnySection(BoardStatus board) {
        return GeneralTicTacToeLogic.sectionIsFull(board, board.getSectionToPlayIn());
    }

    protected abstract Move choosePosition(BoardStatus board);

    public abstract String getIdentifier();
    
    // Should be learning from this, if it will do any learning
    public abstract void learnFromChange(BoardStatus board);
    
    // Should be used to reset any learning it was doing
    public abstract void newGame(BoardStatus board);
    
    // This should allow the bot type to be completely recreated, both the types inside it
    // and the weights used.
    public final void completelySavePlayer(PrintStream printStream) {
        savePlayerSpecification(printStream);
        saveParameters(printStream);
    }
    
    // This should allow the bot type to be completely recreated, albiet without
    // any weights
    public final void savePlayerSpecification(PrintStream printStream) {
        printStream.println(getIdentifier());
        saveInternalPlayerSpecification(printStream);
    }

    // This should allow the bot type to be completely recreated, other than the necessary weights.
    // Should NOT save the weights.
    protected abstract void saveInternalPlayerSpecification(PrintStream printStream);

    // This should allow the bot to be completely recreated given that we already knew
    // what type of bot it was - just need to know the numbers
    public abstract void saveParameters(PrintStream printStream);
}
