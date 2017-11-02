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

package com.diusrex.tictactoe.ai.tournament.test_results;

import java.io.PrintStream;

import com.diusrex.tictactoe.ai.AIPlayer;
import com.diusrex.tictactoe.logic.TicTacToeEngine;

public class BaseScoringValuesTestResults implements Comparable<BaseScoringValuesTestResults> {
    private final AIPlayer player;

    private int won, drew, lost;
    private PlayerTimeResults timeResults;

    private int[] winDepths;
    private int[] lossDepths;
    private int[] winAsFirstDepth;
    private int[] winAsSecondDepth;

    public BaseScoringValuesTestResults(AIPlayer player) {
        this.player = player;

        winDepths = new int[TicTacToeEngine.MAX_NUM_MOVES + 1];
        lossDepths = new int[TicTacToeEngine.MAX_NUM_MOVES + 1];
        winAsFirstDepth = new int[TicTacToeEngine.MAX_NUM_MOVES + 1];
        winAsSecondDepth = new int[TicTacToeEngine.MAX_NUM_MOVES + 1];

        won = drew = lost = 0;
        timeResults = new PlayerTimeResults();
    }

    @Override
    public int compareTo(BaseScoringValuesTestResults other) {
        if (won != other.won)
            return won - other.won;

        else if (drew != other.drew)
            return drew - other.drew;

        return 0;
    }

    public AIPlayer getPlayer() {
        return player;
    }

    public synchronized void addTime(long timeTaken) {timeResults.add(timeTaken);
    }

    public synchronized void increaseWon(int depth, boolean first) {
        ++won;
        ++winDepths[depth];

        if (first)
            ++winAsFirstDepth[depth];
        else
            ++winAsSecondDepth[depth];
    }

    public synchronized void increaseLost(int depth) {
        ++lost;
        ++lossDepths[depth];
    }

    public synchronized void increaseDraw() {
        ++drew;
    }

    public PlayerTimeResults getTimeResults() {
        return timeResults;
    }

    public void printOutCompact(PrintStream printStream) {
        player.saveParameters(printStream);
    }
    
    public void printOutVerbose(PrintStream printStream) {
        printStream.println("wins: " + won + " draw: " + drew + " loss: " + lost);
        player.completelySavePlayer(printStream);
    }

    public void reset() {
        won = lost = drew = 0;
        timeResults.clear();
    }

    public int[] getWinDepths() {
        return winDepths;
    }

    public int[] getLossDepths() {
        return lossDepths;
    }

    public int[] getWinAsFirstDepths() {
        return winAsFirstDepth;
    }

    public int[] getWinAsSecondDepths() {
        return winAsSecondDepth;
    }

}
