/*
 * Copyright 2015 Morgan Redshaw
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.diusrex.tictactoe.ai;

import com.diusrex.tictactoe.ai.scoring_calculations.Scorer;
import com.diusrex.tictactoe.data_structures.BoardStatus;
import com.diusrex.tictactoe.data_structures.Move;
import com.diusrex.tictactoe.data_structures.Player;
import com.diusrex.tictactoe.data_structures.position.BoxPosition;
import com.diusrex.tictactoe.data_structures.position.SectionPosition;
import com.diusrex.tictactoe.logic.GeneralTicTacToeLogic;
import com.diusrex.tictactoe.logic.GridLists;

/*
 *  This is actually Negamax, it works the same way, but it multiplies the score by -1,
 *  rather than having two different states -> One for self, one for opponent,
 *  and then calculating the score based on how it is for self
 */
public abstract class BaseMiniMaxPlayer extends AIPlayerWithScorer {

    public BaseMiniMaxPlayer(Scorer scorer) {
        super(scorer);
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

            if (bestMoveInSection != null && (bestMove == null || bestMoveInSection.score > bestMove.score)) {
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
                double score = calculateScore(board, depth - 1) * -1;
                if (bestMove == null || score > bestMove.score) {
                    bestMove = new MoveScore(move, score);
                }

                board.undoLastMove();
            }
        }

        return bestMove;
    }

    private double calculateScore(BoardStatus board, int depth) {
        if (board.getWinner() != Player.Unowned) {
            // The previous player won, but this scoring is for the current player
            // Multiplies by depth to prefer (mostly) winning sooner
            // Adds one to ensure that the score wouldn't be 0 if depth is 0
            return -getWinScore() * (depth + 1);
        } else if (GeneralTicTacToeLogic.boardIsFull(board)) {
            return getTieScore();
        } else if (depth == 0) {
            return getScore(board, board.getNextPlayer());
        }
        MoveScore bestMove = getBestMoveAndItsScore(board, depth);
        return bestMove.score;
    }
}
