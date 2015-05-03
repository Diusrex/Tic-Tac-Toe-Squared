package com.diusrex.tictactoe.ai.tournament;

import java.util.List;
import java.util.Scanner;

import com.diusrex.tictactoe.ai.scoring_calculations.ScoringFunction;
import com.diusrex.tictactoe.ai.scoring_calculations.ScoringValues;

public class ScoringsFromFile {

    /*
     * Assumes that there are enough AI's in file. Otherwise will be undefined
     * behavior Storage format: depth mainScoringInOrderForBuilder
     * sectionScoringInOrderForBuilder scoring breakdown: cannotWinPointScore
     * ownsOnlyTakenInLineScore ownsBothTakenInLineScore
     * blockedPlayerInLineScore
     */
    public static void loadAIScorings(Scanner scanner, List<ScoringValuesTestResults> results, int numberOfUniqueAI) {
        for (int i = 0; i < numberOfUniqueAI; ++i) {
            int depth = scanner.nextInt();
            ScoringFunction mainScoring = loadScoring(scanner);
            ScoringFunction sectionScoring = loadScoring(scanner);

            results.add(new ScoringValuesTestResults(new ScoringValues(mainScoring, sectionScoring), depth));
        }
    }

    private static ScoringFunction loadScoring(Scanner scanner) {
        int cannotWinPointScore = scanner.nextInt();
        int ownsOnlyTakenInLineScore = scanner.nextInt();
        int ownsBothTakenInLineScore = scanner.nextInt();
        int blockedPlayerInLineScore = scanner.nextInt();

        return new ScoringFunction.Builder().setScoreValues(cannotWinPointScore, ownsOnlyTakenInLineScore,
                ownsBothTakenInLineScore, blockedPlayerInLineScore).build();
    }
}
