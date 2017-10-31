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

package com.diusrex.tictactoe.logic;

import com.diusrex.tictactoe.ai.AIPlayer;
import com.diusrex.tictactoe.ai.ScalingAlphaBetaPlayer;
import com.diusrex.tictactoe.ai.UnScalingMiniMaxPlayer;
import com.diusrex.tictactoe.ai.scoring_calculations.Scorer;
import com.diusrex.tictactoe.ai.scoring_calculations.fixed.GridScoringFunction;
import com.diusrex.tictactoe.ai.scoring_calculations.fixed.ScoringValues;
import com.diusrex.tictactoe.ai.scoring_calculations.fixed.StaticScorer;

public class PlayerFactory {
    public enum WantedPlayer {
        Human, Easy, Medium, Hard
    }

    public static AIPlayer createAIPlayer(WantedPlayer player) {
        switch (player) {
            case Easy:
                return createEasyPlayer();

            case Medium:
                return createMediumPlayer();

            case Hard:
                return createHardPlayer();

            default:
                return null;
        }
    }

    public static Scorer getEasyValues() {
        GridScoringFunction.Builder scoringBuilder = new GridScoringFunction.Builder();
        GridScoringFunction mainGridFunction = scoringBuilder.setScoreValues(0, 30, 50, 40).build();
        GridScoringFunction sectionsFunction = scoringBuilder.setScoreValues(-1, 2, 4, 3).build();
        return new StaticScorer(new ScoringValues(mainGridFunction, sectionsFunction));
    }

    private static AIPlayer createEasyPlayer() {
        return new UnScalingMiniMaxPlayer(getEasyValues(), 2);
    }

    public static Scorer getMediumValues() {
        GridScoringFunction.Builder scoringBuilder = new GridScoringFunction.Builder();
        GridScoringFunction mainGridFunction = scoringBuilder.setScoreValues(0, 27, 94, 56).build();
        GridScoringFunction sectionsFunction = scoringBuilder.setScoreValues(-14, 0, 11, 4).build();
        return new StaticScorer(new ScoringValues(mainGridFunction, sectionsFunction));
    }

    private static AIPlayer createMediumPlayer() {
        return new UnScalingMiniMaxPlayer(getMediumValues(), 4);
    }

    public static Scorer getHardValues() {
        GridScoringFunction.Builder scoringBuilder = new GridScoringFunction.Builder();
        GridScoringFunction mainGridFunction = scoringBuilder.setScoreValues(0, 42, 99, 33).build();
        GridScoringFunction sectionsFunction = scoringBuilder.setScoreValues(-2, 8, 24, 3).build();
        return new StaticScorer(new ScoringValues(mainGridFunction, sectionsFunction));
    }

    private static AIPlayer createHardPlayer() {
        return new ScalingAlphaBetaPlayer(getHardValues());
    }
}
