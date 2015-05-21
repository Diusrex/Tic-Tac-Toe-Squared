package com.diusrex.tictactoe.ai.tournament;

import java.io.PrintStream;

import com.diusrex.tictactoe.ai.AIPlayer;
import com.diusrex.tictactoe.ai.ScalingMiniMaxPlayer;
import com.diusrex.tictactoe.ai.scoring_calculations.ScoringValues;

public class ScalingMiniMaxScoringValuesTestResults extends BaseScoringValuesTestResults {
    
    private ScalingMiniMaxPlayer player;

    public ScalingMiniMaxScoringValuesTestResults(ScoringValues ownValue) {
        super(ownValue);
        player = new ScalingMiniMaxPlayer(ownValue);
    }

    @Override
    public AIPlayer getPlayer() {
        return player;
    }

    @Override
    protected void printAdditionalInfo(PrintStream printStream) {
        printStream.println("Type: ScalingMini");
    }

}
