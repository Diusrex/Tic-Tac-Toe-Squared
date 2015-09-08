package com.diusrex.tictactoe.logic;

import com.diusrex.tictactoe.ai.AIPlayer;
import com.diusrex.tictactoe.ai.ScalingAlphaBetaPlayer;
import com.diusrex.tictactoe.ai.UnScalingMiniMaxPlayer;
import com.diusrex.tictactoe.ai.scoring_calculations.ScoringFunction;
import com.diusrex.tictactoe.ai.scoring_calculations.ScoringValues;

public class PlayerFactory {
    public enum WantedPlayer {
        Human, Easy, Medium, Hard
    }

    ;

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

    public static ScoringValues getEasyValues() {
        ScoringFunction.Builder scoringBuilder = new ScoringFunction.Builder();
        ScoringFunction mainFunction = scoringBuilder.setScoreValues(0, 30, 50, 40).build();
        ScoringFunction sectionsFunction = scoringBuilder.setScoreValues(-1, 2, 4, 3).build();
        return new ScoringValues(mainFunction, sectionsFunction);
    }

    private static AIPlayer createEasyPlayer() {
        return new UnScalingMiniMaxPlayer(getEasyValues(), 2);
    }

    public static ScoringValues getMediumValues() {
        ScoringFunction.Builder scoringBuilder = new ScoringFunction.Builder();
        ScoringFunction mainFunction = scoringBuilder.setScoreValues(0, 27, 94, 56).build();
        ScoringFunction sectionsFunction = scoringBuilder.setScoreValues(-14, 0, 11, 4).build();
        return new ScoringValues(mainFunction, sectionsFunction);
    }

    private static AIPlayer createMediumPlayer() {
        return new UnScalingMiniMaxPlayer(getMediumValues(), 4);
    }

    public static ScoringValues getHardValues() {
        ScoringFunction.Builder scoringBuilder = new ScoringFunction.Builder();
        ScoringFunction mainFunction = scoringBuilder.setScoreValues(0, 33, 70, 63).build();
        ScoringFunction sectionsFunction = scoringBuilder.setScoreValues(-9, 3, 12, 14).build();
        return new ScoringValues(mainFunction, sectionsFunction);
    }

    private static AIPlayer createHardPlayer() {
        return new ScalingAlphaBetaPlayer(getHardValues());
    }
}
