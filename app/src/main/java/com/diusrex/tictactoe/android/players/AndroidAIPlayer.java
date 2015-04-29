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
import com.diusrex.tictactoe.android.GameActivity;
import com.diusrex.tictactoe.data_structures.BoardStatus;
import com.diusrex.tictactoe.data_structures.Move;

public class AndroidAIPlayer implements AndroidPlayerController {
    private final AIPlayer aiPlayer;
    private Thread thread;

    public AndroidAIPlayer(AIPlayer aiPlayer) {
        this.aiPlayer = aiPlayer;
    }

    @Override
    public void promptForMove(final BoardStatus board, final MoveListener listener) {
        final long timeStarted = GameActivity.getCurrentTime();

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Move wantedMove = aiPlayer.getPositionToPlay(board);

                final long timeTaken = GameActivity.getCurrentTime() - timeStarted;
                if (timeTaken < GameActivity.COOLDOWN) {
                    android.os.SystemClock.sleep(GameActivity.COOLDOWN - timeTaken);
                }

                listener.moveChosen(wantedMove);
            }
        });

        thread.start();
    }

    @Override
    public void undoWasPressed() {
        thread.interrupt();
    }

    @Override
    public boolean mayUndo() {
        return false;
    }
}
