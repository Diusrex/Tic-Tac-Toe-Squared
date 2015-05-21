package com.diusrex.tictactoe.ai.tournament;

import java.util.List;
import java.util.Scanner;

import com.diusrex.tictactoe.ai.scoring_calculations.ScoringFunction;
import com.diusrex.tictactoe.ai.scoring_calculations.ScoringValues;

public class ScoringsFromFile {

    /*
     * Assumes that there are enough AI's in file. Otherwise will be undefined
     * behavior Storage format: <identifier> mainScoringInOrderForBuilder
     * sectionScoringInOrderForBuilder <specific info>
     * 
     * ownsOnlyTakenInLineScore ownsBothTakenInLineScore
     * blockedPlayerInLineScore
     */
    public static void loadAIScorings(Scanner scanner, List<BaseScoringValuesTestResults> results, int numberOfUniqueAI) {
        while (results.size() < numberOfUniqueAI) {
            //String identifier = scanner.next();
            int depth = scanner.nextInt(); // Remove this
            ScoringFunction mainScoring = loadScoring(scanner);
            ScoringFunction sectionScoring = loadScoring(scanner);
            /*
            if (identifier == UnScalingMiniMaxPlayer.IDENTIFIER) {
                int depth = scanner.nextInt();
                results.add(UnScalingScoringValuesTestResults.makeMiniMaxPlayer(new ScoringValues(mainScoring,
                        sectionScoring), depth));
            } else if (identifier == UnScalingMiniMaxPlayer.IDENTIFIER) {
                results.add(new ScalingMiniMaxScoringValuesTestResults(new ScoringValues(mainScoring, sectionScoring)));
            } else if (identifier == AlphaBetaPlayer.IDENTIFIER) {
                int depth = scanner.nextInt();
                results.add(UnScalingScoringValuesTestResults.makeAlphaBetaPlayer(new ScoringValues(mainScoring,
                        sectionScoring), depth));
            }
            */
            
            results.add(UnScalingScoringValuesTestResults.makeMiniMaxPlayer(new ScoringValues(mainScoring,
                    sectionScoring), depth));
            results.add(UnScalingScoringValuesTestResults.makeAlphaBetaPlayer(new ScoringValues(mainScoring,
                    sectionScoring), depth));
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
