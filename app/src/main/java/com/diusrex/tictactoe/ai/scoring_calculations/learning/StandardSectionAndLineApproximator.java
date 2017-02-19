package com.diusrex.tictactoe.ai.scoring_calculations.learning;

import java.io.PrintStream;

import com.diusrex.tictactoe.data_structures.BoardStatus;
import com.diusrex.tictactoe.data_structures.LinesFormed;
import com.diusrex.tictactoe.data_structures.Player;
import com.diusrex.tictactoe.data_structures.grid.Grid;
import com.diusrex.tictactoe.data_structures.position.BoxPosition;
import com.diusrex.tictactoe.data_structures.position.SectionPosition;
import com.diusrex.tictactoe.logic.GridLists;

public class StandardSectionAndLineApproximator implements FunctionApproximator {
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
    
    // Grid form: <1 in row>-<2 in row>
    //  0   0-1 0-2 0-3 0-4 -> offset of 0
    //  1-0 1-1 1-2 1-3 -> offset of 5
    //  2-0 2-1 2-2 -> offset of 9
    //  3-0 3-1 -> offset of 12
    //  4-0 -> offset of 14
    private static final int[] NUM_IN_SINGLE_TO_OFFSET = {PER_IN_ROW_BASE_OFFSET,
                                                          PER_IN_ROW_BASE_OFFSET + 5,
                                                          PER_IN_ROW_BASE_OFFSET + 9,
                                                          PER_IN_ROW_BASE_OFFSET + 12,
                                                          PER_IN_ROW_BASE_OFFSET + 14};
    private static final int NUM_FEATURES = SIZE_FOR_SECTION_TYPE + NUM_PER_IN_ROW * FEATURES_PER_IN_ROW;
    
    // For testing purposes
    private boolean shouldCalculateSectionsScores;

    // TODO: Could this be put in FunctionApproximator, to get loading easier for multiple different types
    double[] weights = new double[NUM_FEATURES];

    public StandardSectionAndLineApproximator(boolean shouldCalculateSectionsScores) {
        this.shouldCalculateSectionsScores = shouldCalculateSectionsScores;
    }

    @Override
    public double getScore(Player positivePlayer, BoardStatus board, double[] gradient) {
        Player winner = board.getWinner();
        if (winner != Player.Unowned) {
            return (winner == positivePlayer) ? WIN_SCORE : -WIN_SCORE;
        }

        for (int i = 0; i < NUM_FEATURES; ++i)
            gradient[i] = 0;

        int offset = 0;
        for (SectionPosition cornerPos : GridLists.getAllCornerSections()) {
            calculateFeaturesForSection(positivePlayer, board, cornerPos, gradient, offset);
        }

        offset += FEATURES_PER_SECTION_TYPE;

        for (SectionPosition midEdgePos : GridLists.getAllMidEdgeSections()) {
            calculateFeaturesForSection(positivePlayer, board, midEdgePos, gradient, offset);
        }
        offset += FEATURES_PER_SECTION_TYPE;

        calculateFeaturesForSection(positivePlayer, board, SectionPosition.make(1, 1), gradient, offset);

        offset += FEATURES_PER_SECTION_TYPE;
        // Calculate for the maingrid specially since it will always be important
        LinesFormed linesFormed = new LinesFormed(positivePlayer);
        updateScoreForSection(board.getMainGrid(), gradient, offset, true, true,
                linesFormed);
        
        double score = 0;
        for (int i = 0; i < NUM_FEATURES; ++i)
            score += gradient[i] * weights[i];
        return score;
    }

    private void calculateFeaturesForSection(Player positivePlayer, BoardStatus board, SectionPosition section,
            double[] gradient, int offset) {
        Grid subGrid = board.getSubGrid(section);
        // TODO: If this construction takes too much time, can pass it into function, and just construct once
        // per call
        LinesFormed linesFormedForSection = new LinesFormed(positivePlayer);
        if (!subGrid.canBeWon()) {
            linesFormedForSection.reset();
            calculateFeatures(positivePlayer, subGrid, gradient, offset, linesFormedForSection);
        } else {
            board.getLinesUsingSection(section, linesFormedForSection);
            calculateFeatures(positivePlayer, subGrid, gradient, offset, linesFormedForSection);
        }
    }

    private void calculateFeatures(Player positivePlayer, Grid grid, double[] gradient, int offset,
            LinesFormed linesFormedForSection) {
        boolean isImportantToPositivePlayer = linesFormedForSection.emptyLines + linesFormedForSection.oneFormedForMain
                + linesFormedForSection.twoFormedForMain != 0;
        boolean isImportantToOtherPlayer = linesFormedForSection.emptyLines + linesFormedForSection.oneFormedForOther
                + linesFormedForSection.twoFormedForOther != 0;
        if (isImportantToPositivePlayer || isImportantToOtherPlayer) {
            LinesFormed linesFormed = new LinesFormed(positivePlayer);
            grid.getLinesFormed(linesFormed);
            if (shouldCalculateSectionsScores) {
                updateScoreForSection(grid, gradient, offset, isImportantToPositivePlayer, isImportantToOtherPlayer,
                        linesFormed);
            }
            
            // Do the scoring for the counts based on # in line
            // Main player first
            int mainPlayerLinesOffset = NUM_IN_SINGLE_TO_OFFSET[linesFormedForSection.oneFormedForMain] + 
                    linesFormedForSection.twoFormedForMain;
            
            gradient[mainPlayerLinesOffset] += linesFormed.oneFormedForMain;
            gradient[mainPlayerLinesOffset + 1] += linesFormed.twoFormedForMain;
            gradient[mainPlayerLinesOffset + 2] += linesFormed.mainBlocked;

            int otherPlayerLinesOffset = NUM_IN_SINGLE_TO_OFFSET[linesFormedForSection.oneFormedForOther] + 
                    linesFormedForSection.twoFormedForOther;
            gradient[otherPlayerLinesOffset] -= linesFormed.oneFormedForOther;
            gradient[otherPlayerLinesOffset + 1] -= linesFormed.twoFormedForOther;
            gradient[otherPlayerLinesOffset + 2] -= linesFormed.otherBlocked;
        }

        if (!isImportantToPositivePlayer || !isImportantToOtherPlayer) {
            if (!shouldCalculateSectionsScores)
                return;
            
            for (BoxPosition box : GridLists.getAllStandardBoxPositions()) {
                if (!isImportantToPositivePlayer && grid.getPointOwner(box) == positivePlayer) {
                    ++gradient[offset + 3];

                } else if (!isImportantToOtherPlayer && grid.getPointOwner(box) == positivePlayer.opposite()) {
                    --gradient[offset + 3];
                }
            }
        }
    }

    private void updateScoreForSection(Grid grid, double[] gradient, int offset, boolean isImportantToPositivePlayer,
            boolean isImportantToOtherPlayer, LinesFormed linesFormed) {
        grid.getLinesFormed(linesFormed);
        
        // Do scoring for the basic sections
        if (isImportantToPositivePlayer) {
            gradient[offset] += linesFormed.oneFormedForMain;
            gradient[offset + 1] += linesFormed.twoFormedForMain;
            gradient[offset + 2] += linesFormed.mainBlocked;
        }
        
        if (isImportantToOtherPlayer) {
            gradient[offset] -= linesFormed.oneFormedForOther;
            gradient[offset + 1] -= linesFormed.twoFormedForOther;
            gradient[offset + 2] -= linesFormed.otherBlocked;
        }
    }

    @Override
    public int numberElements() {
        return NUM_FEATURES;
    }

    @Override
    public void update(int element, double change) {
        weights[element] += change;
    }

    @Override
    public double getWinScore() {
        return WIN_SCORE;
    }

    @Override
    public void saveState(PrintStream logger) {
        for (int i = 0; i < NUM_FEATURES; ++i) {
            logger.append(weights[i] + " ");
        }
        logger.println("");
    }

    @Override
    public String getIdentifier() {
        return IDENTIFIER;
    }

    @Override
    public double[] getParametersCopy() {
        double temp[] = new double[NUM_FEATURES];
        for (int i = 0; i < NUM_FEATURES; ++i) {
            temp[i] = weights[i];
        }
        return temp;
    }
}
