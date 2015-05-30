package com.diusrex.tictactoe.ai.tournament;

import java.io.PrintStream;

import com.diusrex.tictactoe.ai.AIPlayer;
import com.diusrex.tictactoe.ai.UnScalingAlphaBetaPlayer;
import com.diusrex.tictactoe.ai.UnScalingMiniMaxPlayer;
import com.diusrex.tictactoe.ai.scoring_calculations.ScoringValues;

public class UnScalingScoringValuesTestResults extends BaseScoringValuesTestResults {
    private final int maxDepth;

    public static BaseScoringValuesTestResults makeMiniMaxPlayer(ScoringValues ownValues, int maxDepth) {
        return new UnScalingScoringValuesTestResults(new UnScalingMiniMaxPlayer(ownValues, maxDepth), ownValues,
                maxDepth);
    }

    public static BaseScoringValuesTestResults makeAlphaBetaPlayer(ScoringValues ownValues, int maxDepth) {
        return new UnScalingScoringValuesTestResults(new UnScalingAlphaBetaPlayer(ownValues, maxDepth), ownValues,
                maxDepth);
    }

    private UnScalingScoringValuesTestResults(AIPlayer player, ScoringValues ownValue, int maxDepth) {
        super(ownValue, player);
        this.maxDepth = maxDepth;
    }

    @Override
    protected void printAdditionalInfo(PrintStream printStream) {
        printStream.println("Type: " + getPlayer().getIdentifier() + " " + maxDepth);
    }
}
