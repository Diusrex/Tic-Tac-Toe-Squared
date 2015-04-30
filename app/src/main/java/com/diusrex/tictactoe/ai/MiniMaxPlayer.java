package com.diusrex.tictactoe.ai;

import com.diusrex.tictactoe.data_structures.BoardStatus;
import com.diusrex.tictactoe.data_structures.BoxPosition;
import com.diusrex.tictactoe.data_structures.Move;
import com.diusrex.tictactoe.data_structures.Player;
import com.diusrex.tictactoe.data_structures.SectionPosition;
import com.diusrex.tictactoe.logic.GeneralTicTacToeLogic;
import com.diusrex.tictactoe.logic.GridLists;

public class MiniMaxPlayer extends AIPlayer {
    private final int WIN_SCORE = 10000000;
    private final Scorer scorer;
    private int maxDepth;

    public MiniMaxPlayer(ScoringValues scoringInfo, int maxDepth) {
        this.scorer = new Scorer(scoringInfo);
        this.maxDepth = maxDepth;
    }

    @Override
    protected Move choosePosition(BoardStatus board) {
        return getBestMoveAndItsScore(board, maxDepth).move;
    }

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
            Move move = new Move(section, pos, board.getNextPlayer());

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
            return -WIN_SCORE;
        } else if (GeneralTicTacToeLogic.boardIsFull(board)) {
            return scorer.getTieScore();
        } else if (depth == 0) {
            return scorer.calculateScore(board.getNextPlayer(), board);
        }
        MoveScore bestMove = getBestMoveAndItsScore(board, depth);
        return bestMove.score;
    }
}
