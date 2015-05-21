package com.diusrex.tictactoe.ai.tournament;

import java.io.PrintStream;

import com.diusrex.tictactoe.ai.AIPlayer;
import com.diusrex.tictactoe.ai.AlphaBetaPlayer;
import com.diusrex.tictactoe.ai.UnScalingMiniMaxPlayer;
import com.diusrex.tictactoe.ai.scoring_calculations.ScoringValues;

public class UnScalingScoringValuesTestResults extends BaseScoringValuesTestResults {
    private AIPlayer player;
    private final int maxDepth;
    
    public static BaseScoringValuesTestResults makeMiniMaxPlayer(ScoringValues ownValues, int maxDepth) {
        return new UnScalingScoringValuesTestResults(new UnScalingMiniMaxPlayer(ownValues, maxDepth), ownValues, maxDepth);
    }
    
    public static BaseScoringValuesTestResults makeAlphaBetaPlayer(ScoringValues ownValues, int maxDepth) {
        return new UnScalingScoringValuesTestResults(new AlphaBetaPlayer(ownValues, maxDepth), ownValues, maxDepth);
    }

    private UnScalingScoringValuesTestResults(AIPlayer player, ScoringValues ownValue, int maxDepth) {
        super(ownValue);
        this.player = player;
        this.maxDepth = maxDepth;
    }

    @Override
    public AIPlayer getPlayer() {
        return player;
    }

    @Override
    protected void printAdditionalInfo(PrintStream printStream) {
        printStream.println("Type: " + player.getIdentifier() + " " + maxDepth);
    }

}
