package com.diusrex.tictactoe.ai;

import com.diusrex.tictactoe.ai.scoring_calculations.ScoringValues;
import com.diusrex.tictactoe.data_structures.BoardStatus;

public class UnScalingMiniMaxPlayer extends BaseMiniMaxPlayer {
    public static final String IDENTIFIER = "UMiniM";
    private int maxDepth;

    public UnScalingMiniMaxPlayer(ScoringValues scoringInfo, int maxDepth) {
        super(scoringInfo);
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

}
