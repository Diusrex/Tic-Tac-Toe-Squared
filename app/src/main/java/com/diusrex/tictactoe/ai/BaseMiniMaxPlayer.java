package com.diusrex.tictactoe.ai;

import com.diusrex.tictactoe.ai.scoring_calculations.Scorer;
import com.diusrex.tictactoe.ai.scoring_calculations.ScoringValues;
import com.diusrex.tictactoe.ai.scoring_calculations.StaticScorer;
import com.diusrex.tictactoe.data_structures.*;
import com.diusrex.tictactoe.logic.GeneralTicTacToeLogic;
import com.diusrex.tictactoe.logic.GridLists;

/*
 *  This is actually Negamax, it works the same way, but it multiplies the score by -1,
 *  rather than having two different states -> One for self, one for opponent,
 *  and then calculating the score based on how it is for self
 */
public abstract class BaseMiniMaxPlayer extends AIPlayer {
    private static final int WIN_SCORE = 10000000;
    private final Scorer scorer;

    public BaseMiniMaxPlayer(ScoringValues scoringInfo) {
        this.scorer = new StaticScorer(scoringInfo);
    }

    @Override
    protected Move choosePosition(BoardStatus board) {
        return getBestMoveAndItsScore(board, getMaxDepth(board)).move;
    }

    protected abstract int getMaxDepth(BoardStatus board);

    private MoveScore getBestMoveAndItsScore(BoardStatus board, int depth) {
        if (canPlayInAnySection(board)) {
            return getBestMoveScoreInAnySection(board, depth);
        } else {
            return getBestMoveScoreInRequiredSection(board, depth);
        }
    }

    private MoveScore getBestMoveScoreInRequiredSection(BoardStatus board, int depth) {
        SectionPosition section = board.getSectionToPlayIn();
        MoveScore bestMove = getBestMoveInSection(board, section, depth);

        return bestMove;
    }

    private MoveScore getBestMoveScoreInAnySection(BoardStatus board, int depth) {
        MoveScore bestMove = null;

        for (SectionPosition section : GridLists.getAllStandardSections()) {
            MoveScore bestMoveInSection = getBestMoveInSection(board, section, depth);

            if (bestMoveInSection != null && (bestMove == null || bestMove.score > bestMove.score)) {
                bestMove = bestMoveInSection;
            }
        }

        return bestMove;
    }

    private MoveScore getBestMoveInSection(BoardStatus board, SectionPosition section, int depth) {
        MoveScore bestMove = null;

        for (BoxPosition pos : GridLists.getAllStandardBoxPositions()) {
            Move move = Move.make(section, pos, board.getNextPlayer());

            if (board.isValidMove(move)) {
                board.applyMoveIfValid(move);
                // The score calculated is always for the other player
                int score = calculateScore(board, depth - 1) * -1;
                if (bestMove == null || score > bestMove.score) {
                    bestMove = new MoveScore(move, score);
                }

                board.undoLastMove();
            }
        }

        return bestMove;
    }

    private int calculateScore(BoardStatus board, int depth) {
        if (board.getWinner() != Player.Unowned) {
            // The previous player won, but this scoring is for the current player
            // Multiplies by depth to prefer (mostly) winning sooner
            // Adds one to ensure that the score wouldn't be 0 if depth is 0
            return -WIN_SCORE * (depth + 1);
        } else if (GeneralTicTacToeLogic.boardIsFull(board)) {
            return scorer.getTieScore();
        } else if (depth == 0) {
            return board.calculateScore(scorer, board.getNextPlayer());
        }
        MoveScore bestMove = getBestMoveAndItsScore(board, depth);
        return bestMove.score;
    }
}
