package com.diusrex.tictactoe.ai.tournament;

import java.io.PrintStream;

import com.diusrex.tictactoe.ai.AIPlayer;
import com.diusrex.tictactoe.ai.ScalingAlphaBetaPlayer;
import com.diusrex.tictactoe.ai.ScalingMiniMaxPlayer;
import com.diusrex.tictactoe.ai.scoring_calculations.ScoringValues;

public class ScalingScoringValuesTestResults extends BaseScoringValuesTestResults {
    public static BaseScoringValuesTestResults makeMiniMaxPlayer(ScoringValues ownValues) {
        return new ScalingScoringValuesTestResults(new ScalingMiniMaxPlayer(ownValues), ownValues);
    }

    public static BaseScoringValuesTestResults makeAlphaBetaPlayer(ScoringValues ownValues) {
        return new ScalingScoringValuesTestResults(new ScalingAlphaBetaPlayer(ownValues), ownValues);
    }

    private ScalingScoringValuesTestResults(AIPlayer player, ScoringValues ownValue) {
        super(ownValue, player);
        player = new ScalingMiniMaxPlayer(ownValue);
    }

    @Override
    protected void printAdditionalInfo(PrintStream printStream) {
        printStream.println("Type: " + getPlayer().getIdentifier());
    }
}
