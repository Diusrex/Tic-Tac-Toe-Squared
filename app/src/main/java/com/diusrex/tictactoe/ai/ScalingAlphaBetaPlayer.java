package com.diusrex.tictactoe.ai;

import com.diusrex.tictactoe.ai.scoring_calculations.Scorer;
import com.diusrex.tictactoe.data_structures.BoardStatus;

public class ScalingAlphaBetaPlayer extends BaseAlphaBetaPlayer {
    public static final String IDENTIFIER = "SAlphaB";

    public ScalingAlphaBetaPlayer(Scorer scorer) {
        super(scorer);
    }

    @Override
    protected int getMaxDepth(BoardStatus board) {
        int numMoves = board.getAllMoves().size();
        if (numMoves < 6) {
            return 2;
        } else if (numMoves < 30) {
            return 6;
        } else if (numMoves < 50) {
            return 7;
        } else if (numMoves < 65) {
            return 8;
        } else {
            return 9;
        }
    }

    @Override
    public String getIdentifier() {
        return IDENTIFIER;
    }

}
