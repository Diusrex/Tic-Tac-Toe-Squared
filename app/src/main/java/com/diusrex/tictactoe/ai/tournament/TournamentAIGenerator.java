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

package com.diusrex.tictactoe.ai.tournament;

import java.util.List;
import java.util.Set;

import com.diusrex.tictactoe.ai.AIPlayer;
import com.diusrex.tictactoe.ai.ScalingAlphaBetaPlayer;
import com.diusrex.tictactoe.ai.ScalingMiniMaxPlayer;
import com.diusrex.tictactoe.ai.UnScalingAlphaBetaPlayer;
import com.diusrex.tictactoe.ai.UnScalingMiniMaxPlayer;
import com.diusrex.tictactoe.ai.scoring_calculations.fixed.ScoringValues;
import com.diusrex.tictactoe.ai.scoring_calculations.fixed.StaticScorer;
import com.diusrex.tictactoe.ai.tournament.test_results.BaseScoringValuesTestResults;

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

        // TODO: Change this to allow other types of AI
        // Should instead return a list of Scores I think
        Set<ScoringValues> allValues = generateAIInformation(numberRemainingForResults / numberPerScoringValue);


        for (ScoringValues value : allValues) {
            addToResults(value, results);
        }
    }

    protected abstract Set<ScoringValues> generateAIInformation(int numberOfAIInformation);

    private void addToResults(ScoringValues scoringValue, List<BaseScoringValuesTestResults> results) {
        if (allWantedAICopies.contains(UnScalingMiniMaxPlayer.IDENTIFIER)) {
            AIPlayer player = new UnScalingMiniMaxPlayer(new StaticScorer(scoringValue), UnScalingMiniMaxPlayer.STANDARD_DEPTH);
            results.add(new BaseScoringValuesTestResults(player));
        }

        if (allWantedAICopies.contains(ScalingMiniMaxPlayer.IDENTIFIER)) {
            AIPlayer player = new ScalingMiniMaxPlayer(new StaticScorer(scoringValue));
            results.add(new BaseScoringValuesTestResults(player));
        }

        if (allWantedAICopies.contains(UnScalingAlphaBetaPlayer.IDENTIFIER)) {
            AIPlayer player = new UnScalingAlphaBetaPlayer(new StaticScorer(scoringValue), UnScalingAlphaBetaPlayer.STANDARD_DEPTH);
            results.add(new BaseScoringValuesTestResults(player));
        }

        if (allWantedAICopies.contains(ScalingAlphaBetaPlayer.IDENTIFIER)) {
            AIPlayer player = new ScalingAlphaBetaPlayer(new StaticScorer(scoringValue));
            results.add(new BaseScoringValuesTestResults(player));
        }
    }
}
