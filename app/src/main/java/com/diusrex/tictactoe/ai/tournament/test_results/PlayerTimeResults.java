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
import java.util.ArrayList;
import java.util.List;

public class PlayerTimeResults {
    private final List<Long> times;

    public PlayerTimeResults() {
        times = new ArrayList<>();
    }

    public void add(long time) {
        times.add(time);
    }

    public void copyDataFrom(PlayerTimeResults other) {
        times.addAll(other.times);
    }

    public void clear() {
        times.clear();
    }

    public long getTotalTime() {
        long total = 0;
        for (Long time : times) {
            total += time;
        }

        return total;
    }

    public double getAverageTime() {
        if (times.size() != 0)
            return getTotalTime() / times.size();
        else
            return 0.0;
    }

    public double getTimeStdDev() {
        if (times.size() == 0)
            return 0.0;

        double averageTime = getAverageTime();

        double differences = 0;
        for (long time : times) {
            differences += (time - averageTime) * (time - averageTime);
        }

        return Math.sqrt(differences / times.size());
    }

    public void printTimeBreakdown(PrintStream printStream) {
        printStream.println(getAverageTime() + ", std-dev " + getTimeStdDev());
    }
}
