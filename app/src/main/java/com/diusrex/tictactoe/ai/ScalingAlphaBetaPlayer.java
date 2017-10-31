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

public class ScalingAlphaBetaPlayer extends BaseAlphaBetaPlayer {
    public static final String IDENTIFIER = "SAlphaB";

    public ScalingAlphaBetaPlayer(Scorer scorer) {
        super(scorer);
    }

    @Override
    protected int getMaxDepth(BoardStatus board) {
        int numMoves = board.getAllMoves().size();
        if (numMoves < 6) {
            return 2;
        } else if (numMoves < 30) {
            return 6;
        } else if (numMoves < 50) {
            return 7;
        } else if (numMoves < 65) {
            return 8;
        } else {
            return 9;
        }
    }

    @Override
    public String getIdentifier() {
        return IDENTIFIER;
    }

}
