package com.diusrex.tictactoe.ai;

import com.diusrex.tictactoe.data_structures.BoardStatus;
import com.diusrex.tictactoe.data_structures.Move;
import com.diusrex.tictactoe.logic.GeneralTicTacToeLogic;
import com.diusrex.tictactoe.logic.StringSaver;
import com.diusrex.tictactoe.textbased.PlayerController;

public abstract class AIPlayer implements PlayerController {
    public Move getPositionToPlay(BoardStatus board) {
        String copyString = StringSaver.getSaveString(board);

        BoardStatus newBoard = new BoardStatus(board);
        return choosePosition(StringSaver.loadBoardFromString(newBoard, copyString));
    }

    protected boolean canPlayInAnySection(BoardStatus board) {
        return GeneralTicTacToeLogic.sectionIsFull(board, board.getSectionToPlayIn());
    }

    protected abstract Move choosePosition(BoardStatus board);
    
    protected class MoveScore {
        public final Move move;
        public final int score;
        
        MoveScore(Move move, int score) {
            this.move = move;
            this.score = score;
        }
    }
    
    @Override
    public void alertInvalidMove() {
        // Should never happen
        throw new RuntimeException("Got invalid move");
    }
}
