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

public class UnScalingMiniMaxPlayer extends BaseMiniMaxPlayer {
    public static final String IDENTIFIER = "UMiniM";
    public static final int STANDARD_DEPTH = 4;

    private int maxDepth;

    public UnScalingMiniMaxPlayer(Scorer scorer, int maxDepth) {
        super(scorer);
        this.maxDepth = maxDepth;
    }

    @Override
    protected int getMaxDepth(BoardStatus board) {
        return maxDepth;
    }

    @Override
    public String getIdentifier() {
        return IDENTIFIER;
    }
    
    protected void saveAdditionalPlayerState(PrintStream logger) {
        logger.println(maxDepth);
    }
}
