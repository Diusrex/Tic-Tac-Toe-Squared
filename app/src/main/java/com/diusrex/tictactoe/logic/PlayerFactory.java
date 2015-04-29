package com.diusrex.tictactoe.logic;

import com.diusrex.tictactoe.ai.MiniMaxPlayer;
import com.diusrex.tictactoe.ai.ScoringFunction;
import com.diusrex.tictactoe.ai.ScoringValues;
import com.diusrex.tictactoe.textbased.PlayerController;

public class PlayerFactory {
    public enum WantedPlayer {
        Human, Easy, Medium, Hard
    };

    public static PlayerController createAIPlayer(WantedPlayer player) {
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

    private static PlayerController createEasyPlayer() {
        ScoringFunction.Builder scoringBuilder = new ScoringFunction.Builder();
        ScoringFunction mainFunction = scoringBuilder.setScoreValues(0, 30, 50, 40).build();
        ScoringFunction sectionsFunction = scoringBuilder.setScoreValues(-1, 2, 4, 3).build();
        ScoringValues easyValues = new ScoringValues(mainFunction, sectionsFunction);
        return new MiniMaxPlayer(easyValues, 2);
    }

    private static PlayerController createMediumPlayer() {
        ScoringFunction.Builder scoringBuilder = new ScoringFunction.Builder();
        ScoringFunction mainFunction = scoringBuilder.setScoreValues(0, 30, 50, 40).build();
        ScoringFunction sectionsFunction = scoringBuilder.setScoreValues(-1, 2, 4, 3).build();
        ScoringValues easyValues = new ScoringValues(mainFunction, sectionsFunction);
        return new MiniMaxPlayer(easyValues, 5);
    }

    private static PlayerController createHardPlayer() {
        // TODO Auto-generated method stub
        return createMediumPlayer();
    }
}
