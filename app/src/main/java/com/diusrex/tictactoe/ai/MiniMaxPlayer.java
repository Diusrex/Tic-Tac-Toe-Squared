package com.diusrex.tictactoe.ai;

import com.diusrex.tictactoe.data_structures.BoardStatus;
import com.diusrex.tictactoe.data_structures.BoxPosition;
import com.diusrex.tictactoe.data_structures.Move;
import com.diusrex.tictactoe.data_structures.Player;
import com.diusrex.tictactoe.data_structures.SectionPosition;
import com.diusrex.tictactoe.logic.GeneralTicTacToeLogic;
import com.diusrex.tictactoe.logic.GridLists;

public class MiniMaxPlayer extends AIPlayer {
    private static final int OWN_PLAYER_MULTIPLIER = 1;
    private static final int OTHER_PLAYER_SCORE_MULTIPLIER = -1;
    private final int WIN_SCORE = 10000;
    private final Scorer scorer;
    private int maxDepth;

    public MiniMaxPlayer(ScoringValues scoringInfo, int maxDepth) {
        this.scorer = new Scorer(scoringInfo);
        this.maxDepth = maxDepth;
    }

    @Override
    protected Move choosePosition(BoardStatus board) {
        return getBestMoveAndItsScore(board, maxDepth, OWN_PLAYER_MULTIPLIER).move;
    }

    private MoveScore getBestMoveAndItsScore(BoardStatus board, int depth, int multiplier) {
        if (canPlayInAnySection(board)) {
            return getBestMoveScoreInAnySection(board, depth, multiplier);
        } else {
            return getBestMoveScoreInRequredSection(board, depth, multiplier);
        }
    }

    private MoveScore getBestMoveScoreInRequredSection(BoardStatus board, int depth, int multiplier) {
        SectionPosition section = board.getSectionToPlayIn();
        MoveScore bestMove = getBestMoveInSection(board, section, depth, multiplier);

        return bestMove;
    }

    private MoveScore getBestMoveScoreInAnySection(BoardStatus board, int depth, int multiplier) {
        MoveScore bestMove = null;

        for (SectionPosition section : GridLists.getAllStandardSections()) {
            MoveScore bestMoveInSection = getBestMoveInSection(board, section, depth, multiplier);

            if (bestMoveInSection != null && (bestMove == null || bestMove.score > bestMove.score)) {
                bestMove = bestMoveInSection;
            }
        }

        return bestMove;
    }

    private MoveScore getBestMoveInSection(BoardStatus board, SectionPosition section, int depth, int multiplier) {
        MoveScore bestMove = null;

        for (BoxPosition pos : GridLists.getAllStandardBoxPositions()) {
            Move move = new Move(section, pos, board.getNextPlayer());

            if (board.isValidMove(move)) {
                board.applyMoveIfValid(move);
                int score = calculateScore(board, depth - 1) * multiplier;
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
            return WIN_SCORE;
        } else if (GeneralTicTacToeLogic.boardIsFull(board)) {
            return scorer.getTieScore();
        } else if (depth == 0) {
            return scorer.calculateScore(board.getNextPlayer(), board);
        }
        MoveScore bestMove = getBestMoveAndItsScore(board, depth, OTHER_PLAYER_SCORE_MULTIPLIER);
        return bestMove.score;
    }
}
