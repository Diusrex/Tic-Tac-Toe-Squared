package com.diusrex.tictactoe.ai;

import java.io.PrintStream;

import com.diusrex.tictactoe.data_structures.BoardStatus;
import com.diusrex.tictactoe.data_structures.Move;
import com.diusrex.tictactoe.logic.GeneralTicTacToeLogic;
import com.diusrex.tictactoe.logic.StringSaver;

public abstract class AIPlayer {
    public Move getPositionToPlay(BoardStatus board) {
        String copyString = StringSaver.getSaveString(board);

        BoardStatus newBoard = new BoardStatus(board);
        return choosePosition(StringSaver.loadBoardFromString(newBoard, copyString));
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
    
    public final void saveState(PrintStream logger) {
        logger.println(getIdentifier());
        saveInternalState(logger);
    }

    protected abstract void saveInternalState(PrintStream logger);
}
