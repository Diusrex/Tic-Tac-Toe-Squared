package com.diusrex.tictactoe.ai.tournament;

import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import com.diusrex.tictactoe.ai.scoring_calculations.fixed.GridScoringFunction;
import com.diusrex.tictactoe.ai.scoring_calculations.fixed.ScoringValues;

/*
 * The file MUST have at least as many entries as there are AI's wanted
 *
 * To use, the format must be:
 * <depth> <mainScoring> <sectionScoring>
 *
 * Where the scorings are stored in the order used by the builder for ScoringFunctions
 *
 */
public class GenerateAIFromFile extends TournamentAIGenerator {
    final Scanner scanner;

    public GenerateAIFromFile(List<String> allWantedAICopies, Scanner scanner) {
        super(allWantedAICopies);
        this.scanner = scanner;
    }

    @Override
    protected Set<ScoringValues> generateAIInformation(int numberOfAIInformation) {
        Set<ScoringValues> allInformations = new HashSet<ScoringValues>();

        while (allInformations.size() < numberOfAIInformation) {
            GridScoringFunction mainGridScoring = loadScoring();
            GridScoringFunction sectionScoring = loadScoring();
            allInformations.add(new ScoringValues(mainGridScoring, sectionScoring));
        }

        return allInformations;
    }

    private GridScoringFunction loadScoring() {
        int cannotWinPointScore = scanner.nextInt();
        int ownsOnlyTakenInLineScore = scanner.nextInt();
        int ownsBothTakenInLineScore = scanner.nextInt();
        int blockedPlayerInLineScore = scanner.nextInt();

        return new GridScoringFunction.Builder().setScoreValues(cannotWinPointScore, ownsOnlyTakenInLineScore,
                ownsBothTakenInLineScore, blockedPlayerInLineScore).build();
    }

}
