package com.diusrex.tictactoe.ai.scoring_calculations.learning;

import com.diusrex.tictactoe.ai.scoring_calculations.fixed.GridScoringFunction;
import com.diusrex.tictactoe.ai.scoring_calculations.fixed.ScoringValues;
import com.diusrex.tictactoe.data_structures.LinesFormed;
import com.diusrex.tictactoe.data_structures.Player;
import com.diusrex.tictactoe.data_structures.grid.Grid;
import com.diusrex.tictactoe.data_structures.position.BoxPosition;
import com.diusrex.tictactoe.data_structures.position.Position;
import com.diusrex.tictactoe.logic.GridLists;

// Approximator that is based off of the StaticScorer, but is able to learn.
// Currently, to follow the static scorer, will need to disable using OFFSET_FOR_ONE_IN_ROW_BLOCKED.
// Or not give any weight to it.
// TODO: Look at improving by taking into account how often can be sent to a grid or
//  something similar. Like by weighing each section by the number of different ways to
//  reach it. Or almost 
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
    public static final int OFFSET_FOR_ONE_IN_ROW = 0;
    public static final int OFFSET_FOR_TWO_IN_ROW = 1;
    public static final int OFFSET_FOR_TWO_IN_ROW_BLOCKED = 2;
    public static final int OFFSET_FOR_EXCESS_BLOCKS_IN_GRID = 3;

    // (If not winnable) different in number of points

    // So for each type of section
    // Will be 4 types.
    public static final int FEATURES_PER_SECTION_TYPE = 4;
    
    // One in row blocked with ONLY occur on main grid, so can store + calculate it specially
    private static final int OFFSET_FOR_MAIN_GRID_ONE_IN_ROW_BOCKED = NUM_SECTION_TYPES * FEATURES_PER_SECTION_TYPE;
    public static final int NUM_FEATURES = OFFSET_FOR_MAIN_GRID_ONE_IN_ROW_BOCKED + 1;
    
    public static WeakApproximator fromScoringValues(ScoringValues values) {
        WeakApproximator approximator = new WeakApproximator();
        
        // Update the 3 main section weights
        int offset = 0;
        for (int i = 0; i < 3; ++i, offset += FEATURES_PER_SECTION_TYPE) {
            updateApproximatorFromGridScoringFunction(
                    approximator, values.getSectionScoring(),
                    offset);
        }
        
        updateApproximatorFromGridScoringFunction(approximator,
                values.getMainScoring(), offset);
        
        return approximator;
    }
    
    private static void updateApproximatorFromGridScoringFunction(
            WeakApproximator approximator, GridScoringFunction function,
            int offset) {
        approximator.update(offset + OFFSET_FOR_ONE_IN_ROW,
                function.getOwnsOnlyTakenInLine());
        approximator.update(offset + OFFSET_FOR_TWO_IN_ROW,
                function.getOwnsBothOnlyTakenInLine());
        approximator.update(offset + OFFSET_FOR_TWO_IN_ROW_BLOCKED,
                -function.blockedPlayerInLine());
        approximator.update(offset + OFFSET_FOR_EXCESS_BLOCKS_IN_GRID,
                function.getCannotWinPointScore());
    }

    @Override
    protected void calculateFeaturesForMainGrid(Player positivePlayer, Grid mainGrid, double[] features, int offset,
            LinesFormed linesFormedInMainGrid) {
        // Add the values for oneInRowWasBlockedForMain, since this is the only spot where it can happen
        // TODO: This is test code, to check that the calculations for this element are correct.
        if (linesFormedInMainGrid.oneInRowWasBlockedForMain + linesFormedInMainGrid.oneInRowWasBlockedForOther
                != 0) {
            System.out.println("Hello there!");
            System.exit(1);
        }
        features[OFFSET_FOR_MAIN_GRID_ONE_IN_ROW_BOCKED] =
                linesFormedInMainGrid.oneInRowWasBlockedForMain -
                linesFormedInMainGrid.oneInRowWasBlockedForOther;
        calculateFeaturesForImportantGrid(features, offset, true, true, linesFormedInMainGrid);
    }

    @Override
    protected void calculateFeaturesForGrid(Player positivePlayer, Grid grid, double[] features, int offset,
            LinesFormed linesFormedForSection) {
        // TODO: remove this later?
        // It makes NO difference
        if (noMovesPlayed(grid)) {
            return;
        }
        
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

    private boolean noMovesPlayed(Grid grid) {
        for (Position pos : GridLists.getAllStandardPositions()) {
            if (grid.getPointOwner(pos) != Player.Unowned) {
                return false;
            }
        }
        return true;
    }

    private void calculateFeaturesForImportantGrid(double[] features, int offset, boolean isImportantToPositivePlayer,
            boolean isImportantToOtherPlayer, LinesFormed linesFormed) {
        if (isImportantToPositivePlayer) {
            features[offset + OFFSET_FOR_ONE_IN_ROW] += linesFormed.oneFormedForMain;
            features[offset + OFFSET_FOR_TWO_IN_ROW] += linesFormed.twoFormedForMain;
            features[offset + OFFSET_FOR_TWO_IN_ROW_BLOCKED] += linesFormed.twoInRowWasBlockedForMain;
        }

        if (isImportantToOtherPlayer) {
            features[offset + OFFSET_FOR_ONE_IN_ROW] -= linesFormed.oneFormedForOther;
            features[offset + OFFSET_FOR_TWO_IN_ROW] -= linesFormed.twoFormedForOther;
            features[offset + OFFSET_FOR_TWO_IN_ROW_BLOCKED] -= linesFormed.twoInRowWasBlockedForOther;
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
