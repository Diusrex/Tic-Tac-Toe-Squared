package com.diusrex.tictactoe.ai.tournament;

import com.diusrex.tictactoe.ai.ScalingAlphaBetaPlayer;
import com.diusrex.tictactoe.ai.ScalingMiniMaxPlayer;
import com.diusrex.tictactoe.ai.UnScalingAlphaBetaPlayer;
import com.diusrex.tictactoe.ai.UnScalingMiniMaxPlayer;
import com.diusrex.tictactoe.ai.scoring_calculations.ScoringValues;
import com.diusrex.tictactoe.ai.tournament.test_results.BaseScoringValuesTestResults;
import com.diusrex.tictactoe.ai.tournament.test_results.ScalingScoringValuesTestResults;
import com.diusrex.tictactoe.ai.tournament.test_results.UnScalingScoringValuesTestResults;

import java.util.List;
import java.util.Set;

public abstract class TournamentAIGenerator {
    private final List<String> allWantedAICopies;

    TournamentAIGenerator(List<String> allWantedAICopies) {
        this.allWantedAICopies = allWantedAICopies;
    }

    public final void generateAIScorings(List<BaseScoringValuesTestResults> results, int numberOfUniqueAI) {
        int numberPerScoringValue = allWantedAICopies.size();
        int numberRemainingForResults = numberOfUniqueAI - results.size();
        if (numberRemainingForResults % numberPerScoringValue != 0) {
            System.out.println("WARNING: there will be fewer AI than expected due to rounding");
        }

        Set<ScoringValues> allValues = generateAIInformation(numberRemainingForResults / numberPerScoringValue);


        for (ScoringValues value : allValues) {
            addToResults(value, results);
        }
    }

    protected abstract Set<ScoringValues> generateAIInformation(int numberOfAIInformation);

    private void addToResults(ScoringValues scoringValue, List<BaseScoringValuesTestResults> results) {

        if (allWantedAICopies.contains(UnScalingMiniMaxPlayer.IDENTIFIER)) {
            results.add(UnScalingScoringValuesTestResults.makeMiniMaxPlayer(scoringValue, UnScalingMiniMaxPlayer.STANDARD_DEPTH));
        }

        if (allWantedAICopies.contains(ScalingMiniMaxPlayer.IDENTIFIER)) {
            results.add(ScalingScoringValuesTestResults.makeMiniMaxPlayer(scoringValue));
        }

        if (allWantedAICopies.contains(UnScalingAlphaBetaPlayer.IDENTIFIER)) {
            results.add(UnScalingScoringValuesTestResults.makeAlphaBetaPlayer(scoringValue, UnScalingAlphaBetaPlayer.STANDARD_DEPTH));
        }

        if (allWantedAICopies.contains(ScalingAlphaBetaPlayer.IDENTIFIER)) {
            results.add(ScalingScoringValuesTestResults.makeAlphaBetaPlayer(scoringValue));
        }
    }
}
