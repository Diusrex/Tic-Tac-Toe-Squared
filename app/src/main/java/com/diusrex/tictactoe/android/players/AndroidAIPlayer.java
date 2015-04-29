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

package com.diusrex.tictactoe.android.players;

import com.diusrex.tictactoe.ai.AIPlayer;

public class AndroidAIPlayer implements AndroidPlayerController {
    private final AIPlayer aiPlayer;

    public AndroidAIPlayer(AIPlayer aiPlayer) {
        this.aiPlayer = aiPlayer;
    }

    @Override
    public void promptForMove(MoveListener listener) {

    }

    @Override
    public void undoWasPressed() {

    }

    @Override
    public boolean mayUndo() {
        return false;
    }
}
