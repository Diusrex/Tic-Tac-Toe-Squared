package com.diusrex.tictactoe.logic;

import com.diusrex.tictactoe.ai.AIPlayer;
import com.diusrex.tictactoe.ai.MiniMaxPlayer;
import com.diusrex.tictactoe.ai.scoring_calculations.ScoringFunction;
import com.diusrex.tictactoe.ai.scoring_calculations.ScoringValues;

public class PlayerFactory {
    public enum WantedPlayer {
        Human, Easy, Medium, Hard
    };

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

    private static AIPlayer createEasyPlayer() {
        ScoringFunction.Builder scoringBuilder = new ScoringFunction.Builder();
        ScoringFunction mainFunction = scoringBuilder.setScoreValues(0, 30, 50, 40).build();
        ScoringFunction sectionsFunction = scoringBuilder.setScoreValues(-1, 2, 4, 3).build();
        ScoringValues easyValues = new ScoringValues(mainFunction, sectionsFunction);
        return new MiniMaxPlayer(easyValues, 2);
    }

    private static AIPlayer createMediumPlayer() {
        ScoringFunction.Builder scoringBuilder = new ScoringFunction.Builder();
        ScoringFunction mainFunction = scoringBuilder.setScoreValues(0, 30, 50, 40).build();
        ScoringFunction sectionsFunction = scoringBuilder.setScoreValues(-1, 2, 4, 3).build();
        ScoringValues easyValues = new ScoringValues(mainFunction, sectionsFunction);
        return new MiniMaxPlayer(easyValues, 5);
    }

    private static AIPlayer createHardPlayer() {
        ScoringFunction.Builder scoringBuilder = new ScoringFunction.Builder();
        ScoringFunction mainFunction = scoringBuilder.setScoreValues(0, 27, 94, 56).build();
        ScoringFunction sectionsFunction = scoringBuilder.setScoreValues(-14, 0, 11, 4).build();
        ScoringValues hardValues = new ScoringValues(mainFunction, sectionsFunction);
        return new MiniMaxPlayer(hardValues, 4);
    }
}
