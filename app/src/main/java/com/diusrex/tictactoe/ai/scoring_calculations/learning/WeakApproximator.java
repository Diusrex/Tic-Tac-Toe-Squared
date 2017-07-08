package com.diusrex.tictactoe.ai.scoring_calculations.learning;

import com.diusrex.tictactoe.data_structures.LinesFormed;
import com.diusrex.tictactoe.data_structures.Player;
import com.diusrex.tictactoe.data_structures.grid.Grid;
import com.diusrex.tictactoe.data_structures.position.BoxPosition;
import com.diusrex.tictactoe.logic.GridLists;

// Approximator that is based off of the StaticScorer, but is able to learn.
//TODO: Start to store oneInRowWasBlockedForMain
public class WeakApproximator extends FunctionApproximatorBase {
    public static final String IDENTIFIER = "WeakApproximator";
    public static final double WIN_SCORE = 1;

    // Will break it up into different types of sections:
    // Corner sections
    // Middle edge sections
    // Middle section
    // This way, they can have different weights
    private static final int NUM_SECTION_TYPES = 4;

    // For each section:
    // (If winnable) Number of lines that are:
    // numOwnPlayer == 1 && numOtherPlayer == 0
    // numOwnPlayer == 2 && numOtherPlayer == 0
    // numOwnPlayer == 2 && numOtherPlayer == 1

    // Subtracted by opposite for enemy player
    // Can do in one loop
    private static final int OFFSET_FOR_ONE_IN_ROW = 0;
    private static final int OFFSET_FOR_TWO_IN_ROW = 1;
    private static final int OFFSET_FOR_ROWS_BLOCKED = 2;
    private static final int OFFSET_FOR_EXCESS_BLOCKS_IN_GRID = 3;

    // (If not winnable) different in number of points

    // So for each type of section
    // Will be 4 types.
    private static final int FEATURES_PER_SECTION_TYPE = 4;
    private static final int NUM_FEATURES = NUM_SECTION_TYPES * FEATURES_PER_SECTION_TYPE;

    double[] weights = new double[NUM_FEATURES];

    @Override
    protected void calculateFeaturesForMainGrid(Player positivePlayer, Grid mainGrid, double[] features, int offset,
            LinesFormed linesFormedInMainGrid) {
        calculateFeaturesForImportantGrid(features, offset, true, true, linesFormedInMainGrid);
    }

    @Override
    protected void calculateFeaturesForGrid(Player positivePlayer, Grid grid, double[] features, int offset,
            LinesFormed linesFormedForSection) {
        boolean isImportantToPositivePlayer = isImportantForPositivePlayer(linesFormedForSection);
        boolean isImportantToOtherPlayer = isImportantForOtherPlayer(linesFormedForSection);

        if (isImportantToPositivePlayer || isImportantToOtherPlayer) {
            // Warning: After this, can't use linesFormedForSection.
            LinesFormed linesFormed = linesFormedForSection;

            grid.getLinesFormed(linesFormed);

            calculateFeaturesForImportantGrid(features, offset, isImportantToPositivePlayer, isImportantToOtherPlayer,
                    linesFormed);
        }

        if (!isImportantToPositivePlayer || !isImportantToOtherPlayer) {
            Player negativePlayer = positivePlayer.opposite();
            for (BoxPosition box : GridLists.getAllStandardBoxPositions()) {
                if (!isImportantToPositivePlayer && grid.getPointOwner(box) == positivePlayer) {
                    ++features[offset + OFFSET_FOR_EXCESS_BLOCKS_IN_GRID];

                } else if (!isImportantToOtherPlayer && grid.getPointOwner(box) == negativePlayer) {
                    --features[offset + OFFSET_FOR_EXCESS_BLOCKS_IN_GRID];
                }
            }
        }
    }

    private void calculateFeaturesForImportantGrid(double[] features, int offset, boolean isImportantToPositivePlayer,
            boolean isImportantToOtherPlayer, LinesFormed linesFormed) {
        if (isImportantToPositivePlayer) {
            features[offset + OFFSET_FOR_ONE_IN_ROW] += linesFormed.oneFormedForMain;
            features[offset + OFFSET_FOR_TWO_IN_ROW] += linesFormed.twoFormedForMain;
            features[offset + OFFSET_FOR_ROWS_BLOCKED] += linesFormed.twoInRowWasBlockedForMain;
        }

        if (isImportantToOtherPlayer) {
            features[offset + OFFSET_FOR_ONE_IN_ROW] -= linesFormed.oneFormedForOther;
            features[offset + OFFSET_FOR_TWO_IN_ROW] -= linesFormed.twoFormedForOther;
            features[offset + OFFSET_FOR_ROWS_BLOCKED] -= linesFormed.twoInRowWasBlockedForOther;
        }
    }

    @Override
    protected int getNumFeaturesPerSectionType() {
        return FEATURES_PER_SECTION_TYPE;
    }

    @Override
    public int numberElements() {
        return NUM_FEATURES;
    }

    @Override
    public double getWinScore() {
        return WIN_SCORE;
    }

    @Override
    public String getIdentifier() {
        return IDENTIFIER;
    }
}
