package com.diusrex.tictactoe.ai;

import java.io.PrintStream;

import com.diusrex.tictactoe.ai.scoring_calculations.Scorer;
import com.diusrex.tictactoe.data_structures.BoardStatus;

public class UnScalingMiniMaxPlayer extends BaseMiniMaxPlayer {
    public static final String IDENTIFIER = "UMiniM";
    public static final int STANDARD_DEPTH = 4;

    private int maxDepth;

    public UnScalingMiniMaxPlayer(Scorer scorer, int maxDepth) {
        super(scorer);
        this.maxDepth = maxDepth;
    }

    @Override
    protected int getMaxDepth(BoardStatus board) {
        return maxDepth;
    }

    @Override
    public String getIdentifier() {
        return IDENTIFIER;
    }
    
    protected void saveAdditionalPlayerState(PrintStream logger) {
        logger.println(maxDepth);
    }
}
