package com.diusrex.tictactoe.ai.tournament;

import java.util.List;

import com.diusrex.tictactoe.data_structures.Player;

/*
 * Will run the given range of tests against all of the AI's
 */
public class TestRunner implements Runnable {
    private final List<ScoringValuesTestResults> results;
    private final int start, exclusiveEnd;

    TestRunner(List<ScoringValuesTestResults> results, int start, int exclusiveEnd) {
        this.results = results;
        this.start = start;
        this.exclusiveEnd = exclusiveEnd;
    }

    @Override
    public void run() {
        for (int index = start; index < exclusiveEnd; ++index) {
            System.out.println("At " + index + " for " + start);

            for (int otherAI = 0; otherAI < results.size(); ++otherAI) {
                if (index == otherAI)
                    continue;

                ScoringValuesTestResults first = results.get(index);
                ScoringValuesTestResults second = results.get(otherAI);

                runGameAndUpdate(first, second);
            }
        }
    }

    private void runGameAndUpdate(ScoringValuesTestResults first, ScoringValuesTestResults second) {
        Player winner = AIvsAI.runGame(first.getPlayer(), second.getPlayer());

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
    }
}
