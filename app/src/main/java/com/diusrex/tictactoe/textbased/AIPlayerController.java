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

package com.diusrex.tictactoe.textbased;

import com.diusrex.tictactoe.ai.AIPlayer;
import com.diusrex.tictactoe.data_structures.BoardStatus;
import com.diusrex.tictactoe.data_structures.Move;

public class AIPlayerController implements PlayerController {
    private final AIPlayer aiPlayer;

    public AIPlayerController(AIPlayer aiPlayer) {
        this.aiPlayer = aiPlayer;
    }

    @Override
    public Move getPositionToPlay(BoardStatus board) {
        return aiPlayer.getPositionToPlay(board);
    }

    @Override
    public void alertInvalidMove() {
        // Should never happen
        throw new RuntimeException("AI picked invalid move");
    }
}
