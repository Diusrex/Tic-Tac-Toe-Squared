package com.diusrex.tictactoe.ai.scoring_calculations.learning;

import java.io.PrintStream;

import com.diusrex.tictactoe.data_structures.BoardStatus;
import com.diusrex.tictactoe.data_structures.LinesFormed;
import com.diusrex.tictactoe.data_structures.Player;
import com.diusrex.tictactoe.data_structures.grid.Grid;
import com.diusrex.tictactoe.data_structures.position.BoxPosition;
import com.diusrex.tictactoe.data_structures.position.SectionPosition;
import com.diusrex.tictactoe.logic.GridLists;

// Approximator that is based off of the StaticScorer, but is able to learn.
public class WeakApproximator implements FunctionApproximator {
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
    
        // (If not winnable) different in number of points
    
        // So for each type of section
        // Will be 4 types.
    private static final int FEATURES_PER_SECTION_TYPE = 4;
    private static final int NUM_FEATURES = NUM_SECTION_TYPES * FEATURES_PER_SECTION_TYPE;

    double[] weights = new double[NUM_FEATURES];
    
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
        calculateFeatures(positivePlayer, board.getMainGrid(), gradient, offset, true, true);

        double score = 0;
        for (int i = 0; i < NUM_FEATURES; ++i)
            score += gradient[i] * weights[i];
        
        return score;
    }

    private void calculateFeaturesForSection(Player positivePlayer, BoardStatus board, SectionPosition section, double[] gradient, int offset) {
        Grid subGrid = board.getSubGrid(section);
        if (!subGrid.canBeWon()) {
            calculateFeatures(positivePlayer, subGrid, gradient, offset, false, false);
        } else {
            calculateFeatures(positivePlayer, subGrid, gradient, offset,
                    board.sectionIsImportantToPlayer(section, positivePlayer),
                    board.sectionIsImportantToPlayer(section, positivePlayer.opposite()));
        }
    }

    private void calculateFeatures(Player positivePlayer, Grid grid, double[] gradient, int offset, 
            boolean isImportantToPositivePlayer, boolean isImportantToOtherPlayer) {
        if (isImportantToPositivePlayer || isImportantToOtherPlayer) {
            LinesFormed linesFormed = new LinesFormed(positivePlayer);
            
            grid.getLinesFormed(linesFormed);
            
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
        
        if (!isImportantToPositivePlayer || !isImportantToOtherPlayer) {
            for (BoxPosition box : GridLists.getAllStandardBoxPositions()) {
                if (!isImportantToPositivePlayer && grid.getPointOwner(box) == positivePlayer) {
                   ++gradient[offset + 3];
                    
                } else if (!isImportantToOtherPlayer && grid.getPointOwner(box) == positivePlayer.opposite()) {
                   --gradient[offset + 3];
                }
            }
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
    public void saveParameters(PrintStream logger) {
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
        return weights.clone();
    }

}
