package com.diusrex.tictactoe.ai.tournament;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.diusrex.tictactoe.ai.scoring_calculations.ScoringFunction;
import com.diusrex.tictactoe.ai.scoring_calculations.ScoringValues;

public class GenerateAIRandomly extends TournamentAIGenerator {

    public GenerateAIRandomly(List<String> allWantedAICopies) {
        super(allWantedAICopies);
    }
    
    @Override
    protected Set<ScoringValues> generateAIInformation(int numberOfAIInformation) {
        Set<ScoringValues> allInformations = new HashSet<>();

        Random random = new Random();

        while (allInformations.size() < numberOfAIInformation) {
            // It makes no sense to give a better score for having one in a row
            // compared to two in a row
            int mainTwoInRowScore = random.nextInt(99) + 1;
            ScoringFunction mainFunction = new ScoringFunction.Builder().setScoreValues(0,
                    random.nextInt(mainTwoInRowScore), mainTwoInRowScore, random.nextInt(100)).build();
            int secondaryTwoInRowScore = random.nextInt(49) + 1;
            ScoringFunction sectionsFunction = new ScoringFunction.Builder().setScoreValues(random.nextInt(20) - 20,
                    random.nextInt(secondaryTwoInRowScore), secondaryTwoInRowScore, random.nextInt(50)).build();
            allInformations.add(new ScoringValues(mainFunction, sectionsFunction));
        }
        
        return allInformations;
    }

}
