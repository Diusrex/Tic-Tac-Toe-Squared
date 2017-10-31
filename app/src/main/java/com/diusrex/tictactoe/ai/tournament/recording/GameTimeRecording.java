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

package com.diusrex.tictactoe.ai.tournament.recording;

public class GameTimeRecording {
    private final long times[];

    public GameTimeRecording() {
        times = new long[2];
        times[0] = times[1] = 0;
    }

    public void addTime(long time, int player) {
        times[player] += time;
    }

    public long getPlayerOneTime() {
        return times[0];
    }

    public long getPlayerTwoTime() {
        return times[1];
    }
}
