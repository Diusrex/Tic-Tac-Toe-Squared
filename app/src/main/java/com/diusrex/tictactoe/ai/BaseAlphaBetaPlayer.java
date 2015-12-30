package com.diusrex.tictactoe.ai;

import com.diusrex.tictactoe.ai.scoring_calculations.Scorer;
import com.diusrex.tictactoe.ai.scoring_calculations.ScoringValues;
import com.diusrex.tictactoe.ai.scoring_calculations.StaticScorer;
import com.diusrex.tictactoe.data_structures.*;
import com.diusrex.tictactoe.logic.GeneralTicTacToeLogic;
import com.diusrex.tictactoe.logic.GridLists;

import java.util.ArrayList;
import java.util.List;

/*
 *  Is actually a negamax alpha beta player, due to it being easier to code.
 */
public abstract class BaseAlphaBetaPlayer extends AIPlayer {
    private final int WIN_SCORE = 10000000;
    private final Scorer scorer;

    public BaseAlphaBetaPlayer(ScoringValues scoringInfo) {
        this.scorer = new StaticScorer(scoringInfo);
    }

    @Override
    protected Move choosePosition(BoardStatus board) {
        // Is actually possible to get a larger score than WIN_SCORE, due to multiplying it by the depth left
        return getBestMoveAndItsScore(board, getMaxDepth(board), -Integer.MAX_VALUE, Integer.MAX_VALUE).move;
    }

    protected abstract int getMaxDepth(BoardStatus board);

    private MoveScore getBestMoveAndItsScore(BoardStatus board, int depthLeft, int alpha, int beta) {
        if (board.getWinner() != Player.Unowned) {
            // The previous player won, but this scoring is for the current player
            // Multiplies by depth to prefer winning sooner, and also causes it to lose later
            // Adds one to ensure that the score wouldn't be 0 if depth is 0
            return new MoveScore(null, -1 * WIN_SCORE * (depthLeft + 1));
        } else if (GeneralTicTacToeLogic.boardIsFull(board)) {
            return new MoveScore(null, scorer.getTieScore());
        } else if (depthLeft == 0) {
            return new MoveScore(null, board.calculateScore(scorer, board.getNextPlayer()));
        }

        List<Move> allMoves = generateMoves(board);

        MoveScore bestMove = null;

        for (Move move : allMoves) {
            board.applyMoveIfValid(move);
            int score = - getBestMoveAndItsScore(board, depthLeft - 1, -beta, -alpha).score;
            board.undoLastMove();

            if (bestMove == null || score > bestMove.score)
                bestMove = new MoveScore(move, score);

            alpha = Math.max(alpha, score);

            // bestMove will not be null, so it is fine
            if (alpha >= beta) {
                break;
            }
        }

        return bestMove;
    }

    private List<Move> generateMoves(BoardStatus board) {
        if (canPlayInAnySection(board)) {
            return getMovesInAnySection(board);
        } else {
            return getMovesInRequiredSection(board);
        }
    }

    private List<Move> getMovesInRequiredSection(BoardStatus board) {
        SectionPosition requiredSection = board.getSectionToPlayIn();
        Player player = board.getNextPlayer();

        List<Move> allMoves = new ArrayList<Move>();
        for (BoxPosition pos : GridLists.getAllStandardBoxPositions()) {
            Move move = Move.make(requiredSection, pos, player);
            if (board.isValidMove(move)) {
                allMoves.add(move);
            }
        }

        return allMoves;
    }

    private List<Move> getMovesInAnySection(BoardStatus board) {
        Player player = board.getNextPlayer();

        List<Move> allMoves = new ArrayList<Move>();
        for (SectionPosition section : GridLists.getAllStandardSections()) {
            for (BoxPosition pos : GridLists.getAllStandardBoxPositions()) {
                Move move = Move.make(section, pos, player);
                if (board.isValidMove(move)) {
                    allMoves.add(move);
                }
            }
        }

        return allMoves;
    }
}
