package com.diusrex.tictactoe.ai.scoring_calculations.learning;

import com.diusrex.tictactoe.data_structures.LinesFormed;
import com.diusrex.tictactoe.data_structures.Player;
import com.diusrex.tictactoe.data_structures.grid.Grid;
import com.diusrex.tictactoe.data_structures.position.BoxPosition;
import com.diusrex.tictactoe.logic.GridLists;

public class StandardSectionAndLineApproximator extends FunctionApproximatorBase {
    public static final String IDENTIFIER = "StandardSectionAndLineApproximator";
    public final double WIN_SCORE = 1;
    
    // Will break it up into 4 major different types of sections:
    // Corner sections
    // Middle edge sections
    // Middle section
    // This is to reflect their general importance
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

    // (If not winnable) different in number of points -> 4th feature
    private static final int FEATURES_PER_SECTION_TYPE = 4;
    private static final int SIZE_FOR_SECTION_TYPE = NUM_SECTION_TYPES * FEATURES_PER_SECTION_TYPE;
    
    // This is for count of 1, 2 in row for a specific section
        // None (1)
        // 1 in row: 1, 2, 3, 4 (4)
        // 2 in row: 1, 2, 3, 4 (4)
        // 1 in row once with some 2: 1 two, 2 two, 3 two (3)
        // 1 in row twice with some 2: 1 two, 2 two (2)
        // 1 in row three times with 1 two (1)
    
    // Note that I don't expect that the end ones  (4 in row, mix of 1 or 2)
    // will happen, since that is just for each individual player.
    // Although may want to better handle those possibilities in the future,
    // since won't be trained very well.
    private static final int NUM_PER_IN_ROW = 15;
    // numOwnPlayer == 1 && numOtherPlayer == 0
    // numOwnPlayer == 2 && numOtherPlayer == 0
    // numOwnPlayer == 1 && numOtherPlayer == 2 -> is bad to have high
    private static final int FEATURES_PER_IN_ROW = 3;
    private static final int PER_IN_ROW_BASE_OFFSET = SIZE_FOR_SECTION_TYPE;
    
    // TODO: I think the offset could be invalid?
    // Grid form: <1 in row>-<2 in row>
    //  0-0 0-1 0-2 0-3 0-4 -> offset of 0
    //  1-0 1-1 1-2 1-3 -> offset of 5
    //  2-0 2-1 2-2 -> offset of 9
    //  3-0 3-1 -> offset of 12
    //  4-0 -> offset of 14 TODO: Is this one even possible?
    private static final int[] NUM_IN_SINGLE_TO_OFFSET = {PER_IN_ROW_BASE_OFFSET,
                                                          PER_IN_ROW_BASE_OFFSET + 5,
                                                          PER_IN_ROW_BASE_OFFSET + 9,
                                                          PER_IN_ROW_BASE_OFFSET + 12,
                                                          PER_IN_ROW_BASE_OFFSET + 14};
    public static final int NUM_FEATURES = SIZE_FOR_SECTION_TYPE + NUM_PER_IN_ROW * FEATURES_PER_IN_ROW;
    
    // TODO: What should the default for this be? Set in constructor
    // For testing purposes
    private boolean shouldCalculateSectionsScores;

    // TODO: Could this be put in FunctionApproximator, to get loading easier for multiple different types
    double[] weights = new double[NUM_FEATURES];

    public StandardSectionAndLineApproximator(boolean shouldCalculateSectionsScores) {
        this.shouldCalculateSectionsScores = shouldCalculateSectionsScores;
    }

    @Override
    protected void calculateFeaturesForMainGrid(Player positivePlayer, Grid mainGrid, double[] gradient, int offset,
            LinesFormed linesFormedInMainGrid) {
        calculateGridInternalFeatures(mainGrid, gradient, offset, true, true,
                linesFormedInMainGrid);
    }

    @Override
    protected void calculateFeaturesForGrid(Player positivePlayer, Grid grid, double[] gradient, int offset,
            LinesFormed linesFormedForSection) {
        boolean isImportantToPositivePlayer = isImportantForPositivePlayer(linesFormedForSection);
        boolean isImportantToOtherPlayer = isImportantForOtherPlayer(linesFormedForSection);
        
        if (isImportantToPositivePlayer || isImportantToOtherPlayer) {
            // Calculates two different scores for each grid:
                // Scores based on the type of the section. Is the same for both players
                // Scores based  on the significance of the section to each of the players
                // This is based on the number of lines they can make with the section
            // Both should only be calculated for the player(s) who can win using the grid.
            
            // TODO: Have checks here to make sure that the index doesn't overflow into what it would be for oneFormedForMain + 1
            int mainPlayerLinesOffset = NUM_IN_SINGLE_TO_OFFSET[linesFormedForSection.oneFormedForMain] + 
                    linesFormedForSection.twoFormedForMain;
            int otherPlayerLinesOffset = NUM_IN_SINGLE_TO_OFFSET[linesFormedForSection.oneFormedForOther] + 
                    linesFormedForSection.twoFormedForOther;
            
            // WARNING: Cannot use linesFormedForSection after this line.
            LinesFormed linesFormed = linesFormedForSection;
            grid.getLinesFormed(linesFormed);
            if (shouldCalculateSectionsScores) {
                calculateGridInternalFeatures(grid, gradient, offset, isImportantToPositivePlayer, isImportantToOtherPlayer,
                        linesFormed);
            }
            
            // Do the scoring for the counts based on the lines this grid is a part of.
            if (isImportantToPositivePlayer) {
                gradient[mainPlayerLinesOffset + OFFSET_FOR_ONE_IN_ROW] += linesFormed.oneFormedForMain;
                gradient[mainPlayerLinesOffset + OFFSET_FOR_TWO_IN_ROW] += linesFormed.twoFormedForMain;
                gradient[mainPlayerLinesOffset + OFFSET_FOR_ROWS_BLOCKED] += linesFormed.twoWereBlockedForMain;
            }

            if (isImportantToOtherPlayer) {
                gradient[otherPlayerLinesOffset + OFFSET_FOR_ONE_IN_ROW] -= linesFormed.oneFormedForOther;
                gradient[otherPlayerLinesOffset + OFFSET_FOR_TWO_IN_ROW] -= linesFormed.twoFormedForOther;
                gradient[otherPlayerLinesOffset + OFFSET_FOR_ROWS_BLOCKED] -= linesFormed.twoWereBlockedForOther;
            }
        }

        if (!isImportantToPositivePlayer || !isImportantToOtherPlayer) {
            calculateScoreForUnimportantSection(positivePlayer, grid, gradient, offset, isImportantToPositivePlayer,
                    isImportantToOtherPlayer);
        }
    }

    protected void calculateScoreForUnimportantSection(Player positivePlayer, Grid grid, double[] gradient, int offset,
            boolean isImportantToPositivePlayer, boolean isImportantToOtherPlayer) {
        if (!shouldCalculateSectionsScores)
            return;
        
        for (BoxPosition box : GridLists.getAllStandardBoxPositions()) {
            if (!isImportantToPositivePlayer && grid.getPointOwner(box) == positivePlayer) {
                ++gradient[offset + OFFSET_FOR_EXCESS_BLOCKS_IN_GRID];

            } else if (!isImportantToOtherPlayer && grid.getPointOwner(box) == positivePlayer.opposite()) {
                --gradient[offset + OFFSET_FOR_EXCESS_BLOCKS_IN_GRID];
            }
        }
    }

    // Will use the lines formed within the grid, cancelling out the two players within the offset for this grid.
    // Note: Assumes linesFormed has already been initialized with the internal lines for the given grid.
    private void calculateGridInternalFeatures(Grid grid, double[] gradient, int offset, boolean isImportantToPositivePlayer,
            boolean isImportantToOtherPlayer, LinesFormed linesFormed) {
        
        // Do scoring for the basic sections
        if (isImportantToPositivePlayer) {
            gradient[offset + OFFSET_FOR_ONE_IN_ROW] += linesFormed.oneFormedForMain;
            gradient[offset + OFFSET_FOR_TWO_IN_ROW] += linesFormed.twoFormedForMain;
            gradient[offset + OFFSET_FOR_ROWS_BLOCKED] += linesFormed.twoWereBlockedForMain;
        }
        
        if (isImportantToOtherPlayer) {
            gradient[offset + OFFSET_FOR_ONE_IN_ROW] -= linesFormed.oneFormedForOther;
            gradient[offset + OFFSET_FOR_TWO_IN_ROW] -= linesFormed.twoFormedForOther;
            gradient[offset + OFFSET_FOR_ROWS_BLOCKED] -= linesFormed.twoWereBlockedForOther;
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
