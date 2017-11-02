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

import java.io.PrintStream;

import com.diusrex.tictactoe.ai.scoring_calculations.Scorer;
import com.diusrex.tictactoe.data_structures.BoardStatus;
import com.diusrex.tictactoe.data_structures.Move;
import com.diusrex.tictactoe.data_structures.Player;

public abstract class AIPlayerWithScorer extends AIPlayer {
    private final Scorer scorer;

    public AIPlayerWithScorer(Scorer scorer) {
        this.scorer = scorer;
    }
    
    @Override
    public void learnFromChange(BoardStatus board) {
        scorer.learnFromChange(board);
    }
    
    protected class MoveScore {
        public final Move move;
        public final double score;

        public MoveScore(Move move, double score) {
            this.move = move;
            this.score = score;
        }
    }

    @Override
    public void newGame(BoardStatus board) {
        // May be necessary to reset state
        scorer.newGame(board);
    }
    
    protected double getWinScore() {
        return scorer.getWinScore();
    }
    
    protected double getTieScore() {
        return scorer.getTieScore();
    }
    
    protected double getScore(BoardStatus board, Player player) {
        return scorer.calculateScore(player, board);
    }

    @Override
    protected void saveInternalPlayerSpecification(PrintStream logger) {
        scorer.saveIdentifiers(logger);
    }

    @Override
    public final void saveParameters(PrintStream logger) {
        scorer.saveParameters(logger);  
    }
}
