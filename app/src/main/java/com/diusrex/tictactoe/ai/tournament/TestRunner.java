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

package com.diusrex.tictactoe.ai.tournament;

import com.diusrex.tictactoe.ai.tournament.AIvsAI.GameInfo;
import com.diusrex.tictactoe.ai.tournament.recording.GameTimeRecording;
import com.diusrex.tictactoe.ai.tournament.test_results.BaseScoringValuesTestResults;
import com.diusrex.tictactoe.data_structures.Player;

import java.util.List;

/*
 * Will run the given range of tests against all of the AI's
 */
public class TestRunner implements Runnable {
    private final List<BaseScoringValuesTestResults> results;
    private final int start, exclusiveEnd;

    TestRunner(List<BaseScoringValuesTestResults> results, int start, int exclusiveEnd) {
        this.results = results;
        this.start = start;
        this.exclusiveEnd = exclusiveEnd;
    }

    @Override
    public void run() {
        for (int index = start; index < exclusiveEnd; ++index) {
            // To reduce output, only print out for one
            if (start == 0) {
                System.out.println("At " + index + " for " + start);
            }

            BaseScoringValuesTestResults first = results.get(index);

            for (int otherAI = 0; otherAI < results.size(); ++otherAI) {
                if (index == otherAI)
                    continue;

                BaseScoringValuesTestResults second = results.get(otherAI);

                runGameAndUpdate(first, second);
            }
        }
    }

    private void runGameAndUpdate(BaseScoringValuesTestResults first, BaseScoringValuesTestResults second) {
        GameTimeRecording time = new GameTimeRecording();
        GameInfo gameInfo = AIvsAI.runGame(first.getPlayer(), second.getPlayer(), time);

        if (gameInfo.getWinner() == Player.Player_1) {
            first.increaseWon(gameInfo.getDepth(), true);
            second.increaseLost(gameInfo.getDepth());
        } else if (gameInfo.getWinner() == Player.Player_2) {
            first.increaseLost(gameInfo.getDepth());
            second.increaseWon(gameInfo.getDepth(), false);
        } else {
            first.increaseDraw();
            second.increaseDraw();
        }

        first.addTime(time.getPlayerOneTime());
        second.addTime(time.getPlayerTwoTime());
    }
}
