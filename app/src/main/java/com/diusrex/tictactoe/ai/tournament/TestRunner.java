package com.diusrex.tictactoe.ai.tournament;

import java.util.List;

import com.diusrex.tictactoe.data_structures.Player;

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
        TimeInfo.GameTimeInfo time = new TimeInfo.GameTimeInfo();
        Player winner = AIvsAI.runGame(first.getPlayer(), second.getPlayer(), time);

        if (winner == Player.Player_1) {
            first.increaseWon();
            second.increaseLost();
        } else if (winner == Player.Player_2) {
            first.increaseLost();
            second.increaseWon();
        } else {
            first.increaseDraw();
            second.increaseDraw();
        }

        first.addTime(time.getPlayerOneTime());
        second.addTime(time.getPlayerTwoTime());
    }
}
