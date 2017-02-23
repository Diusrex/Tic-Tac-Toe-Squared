package com.diusrex.tictactoe.ai.tournament;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.diusrex.tictactoe.ai.scoring_calculations.fixed.GridScoringFunction;
import com.diusrex.tictactoe.ai.scoring_calculations.fixed.ScoringValues;

public class GenerateAIRandomly extends TournamentAIGenerator {

    public GenerateAIRandomly(List<String> allWantedAICopies) {
        super(allWantedAICopies);
    }

    @Override
    protected Set<ScoringValues> generateAIInformation(int numberOfAIInformation) {
        Set<ScoringValues> allInformations = new HashSet<ScoringValues>();

        Random random = new Random();

        while (allInformations.size() < numberOfAIInformation) {
            // It makes no sense to give a better score for having one in a row
            // compared to two in a row
            int mainTwoInRowScore = random.nextInt(99) + 1;
            GridScoringFunction mainGridFunction = new GridScoringFunction.Builder().setScoreValues(0,
                    random.nextInt(mainTwoInRowScore), mainTwoInRowScore, random.nextInt(100)).build();
            int secondaryTwoInRowScore = random.nextInt(49) + 1;
            GridScoringFunction sectionsFunction = new GridScoringFunction.Builder().setScoreValues(random.nextInt(20) - 20,
                    random.nextInt(secondaryTwoInRowScore), secondaryTwoInRowScore, random.nextInt(50)).build();
            allInformations.add(new ScoringValues(mainGridFunction, sectionsFunction));
        }

        return allInformations;
    }

}
