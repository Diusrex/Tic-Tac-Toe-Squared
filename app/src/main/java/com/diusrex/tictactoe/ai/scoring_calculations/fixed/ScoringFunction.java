package com.diusrex.tictactoe.ai.scoring_calculations.fixed;

import com.diusrex.tictactoe.data_structures.position.Position;

public class ScoringFunction {
    private static final int DEFAULT_MULTIPLIER = 1;
    private static final int DEFAULT_SCORE = 1;
    private final int middleMultiplier;
    private final int cornerMultiplier;
    private final int outerCenterMultiplier;
    private final int cannotWinPointScore;
    private final int ownsOnlyTakenInLineScore;
    private final int ownsBothTakenInLineScore;
    private final int blockedPlayerInLineScore;

    public static class Builder {
        private int middleMultiplier, cornerMultiplier, outerCenterMultiplier;
        private int cannotWinPointScore, ownsOnlyTakenInLineScore, ownsBothTakenInLineScore, blockedPlayerInLineScore;

        public Builder() {
            middleMultiplier = cornerMultiplier = outerCenterMultiplier = DEFAULT_MULTIPLIER;
            cannotWinPointScore = ownsOnlyTakenInLineScore = DEFAULT_SCORE;
            ownsBothTakenInLineScore = blockedPlayerInLineScore = DEFAULT_SCORE;
        }

        public Builder setMultiplier(int middleMultiplier, int cornerMultiplier, int outerCenterMultiplier) {
            this.middleMultiplier = middleMultiplier;
            this.cornerMultiplier = cornerMultiplier;
            this.outerCenterMultiplier = outerCenterMultiplier;

            return this;
        }

        public Builder setScoreValues(int cannotWinPointScore, int ownsOnlyTakenInLineScore, int ownsBothTakenInLineScore, int blockedPlayerInLineScore) {
            this.cannotWinPointScore = cannotWinPointScore;
            this.ownsOnlyTakenInLineScore = ownsOnlyTakenInLineScore;
            this.ownsBothTakenInLineScore = ownsBothTakenInLineScore;
            this.blockedPlayerInLineScore = blockedPlayerInLineScore;

            return this;
        }

        public ScoringFunction build() {
            return new ScoringFunction(middleMultiplier, cornerMultiplier, outerCenterMultiplier,
                    cannotWinPointScore, ownsOnlyTakenInLineScore, ownsBothTakenInLineScore,
                    blockedPlayerInLineScore);
        }
    }

    private ScoringFunction(int middleMultiplier, int cornerMultiplier, int outerCenterMultiplier,
            int cannotWinPointScore, int onesOnlyTakenInLineScore, int ownsBothTakenInLineScore,
            int blockedPlayerInLineScore) {
        this.middleMultiplier = middleMultiplier;
        this.cornerMultiplier = cornerMultiplier;
        this.outerCenterMultiplier = outerCenterMultiplier;
        this.cannotWinPointScore = cannotWinPointScore;
        this.ownsOnlyTakenInLineScore = onesOnlyTakenInLineScore;
        this.ownsBothTakenInLineScore = ownsBothTakenInLineScore;
        this.blockedPlayerInLineScore = blockedPlayerInLineScore;
    }

    public int getMultiplier(Position pos) {
        if (pos.getGridX() == 1 && pos.getGridY() == 1)
            return middleMultiplier;
        else if (pos.getGridX() % 2 == 0 && pos.getGridY() % 2 == 0)
            return cornerMultiplier;
        else
            return outerCenterMultiplier;
    }

    public int getCannotWinPointScore() {
        return cannotWinPointScore;
    }

    public int getOwnsOnlyTakenInLine() {
        return ownsOnlyTakenInLineScore;
    }

    public int getOwnsBothOnlyTakenInLine() {
        return ownsBothTakenInLineScore;
    }

    public int blockedPlayerInLine() {
        return blockedPlayerInLineScore;
    }

}
