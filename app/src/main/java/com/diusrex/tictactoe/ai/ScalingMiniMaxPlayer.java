package com.diusrex.tictactoe.ai;

import com.diusrex.tictactoe.ai.scoring_calculations.Scorer;
import com.diusrex.tictactoe.data_structures.board_status.BoardStatus;

public class ScalingMiniMaxPlayer extends BaseMiniMaxPlayer {
    public static final String IDENTIFIER = "SMiniM";

    public ScalingMiniMaxPlayer(Scorer scorer) {
        super(scorer);
    }

    @Override
    protected int getMaxDepth(BoardStatus board) {
        int numMoves = board.getAllMoves().size();
        if (numMoves < 6) {
            return 2;
        } else if (numMoves < 30) {
            return 4;
        } else if (numMoves < 50) {
            return 5;
        } else if (numMoves < 65) {
            return 6;
        } else {
            return 7;
        }
    }

    @Override
    public String getIdentifier() {
        return IDENTIFIER;
    }

}
