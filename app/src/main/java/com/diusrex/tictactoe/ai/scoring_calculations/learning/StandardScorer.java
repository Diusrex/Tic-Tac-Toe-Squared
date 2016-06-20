package com.diusrex.tictactoe.ai.scoring_calculations.learning;

import java.io.PrintStream;

import com.diusrex.tictactoe.data_structures.BoardStatus;
import com.diusrex.tictactoe.data_structures.BoxPosition;
import com.diusrex.tictactoe.data_structures.Grid;
import com.diusrex.tictactoe.data_structures.LineIterator;
import com.diusrex.tictactoe.data_structures.Player;
import com.diusrex.tictactoe.data_structures.Position;
import com.diusrex.tictactoe.data_structures.SectionPosition;
import com.diusrex.tictactoe.logic.GridLists;

public class StandardScorer implements FunctionApproximator {
    public static final String IDENTIFIER = "StandardScorer";
    public final double WIN_SCORE = 10000000;
    private static final int NUM_SECTION_TYPES = 4;
    private static final int FEATURES_PER_SECION_TYPE = 4;
    private static final int NUM_FEATURES = NUM_SECTION_TYPES * FEATURES_PER_SECION_TYPE;
    
    double[] weights = new double[NUM_FEATURES];
    
    // Will break it up into different types of sections:
        // Corner sections
        // Middle edge sections
        // Middle section
        // This way, they can have different weights
    
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
        
        offset += FEATURES_PER_SECION_TYPE;
        
        for (SectionPosition midEdgePos : GridLists.getAllMidEdgeSections()) {
            calculateFeaturesForSection(positivePlayer, board, midEdgePos, gradient, offset);
        }
        offset += FEATURES_PER_SECION_TYPE;
        
        calculateFeaturesForSection(positivePlayer, board, SectionPosition.make(1, 1), gradient, offset);

        offset += FEATURES_PER_SECION_TYPE;
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
            for (LineIterator iter : GridLists.getAllLineIterators()) {
                int numOwnPlayer = 0, numOtherPlayer = 0;
        
                for (int pos = 0; !iter.isDone(pos); ++pos) {
                    Position boxPos = iter.getCurrent(pos);
                    Player boxPlayer = grid.getPointOwner(boxPos);
                    if (boxPlayer == positivePlayer)
                        ++numOwnPlayer;
                    else if (boxPlayer != Player.Unowned)
                        ++numOtherPlayer;
                }
                
    
                if (numOwnPlayer == numOtherPlayer) {
                    continue;
                }
        
                // Either 0, or 1. Either way, wouldn't help current person
                if (isImportantToPositivePlayer) {
                    increaseLineCounts(gradient, offset, numOwnPlayer, numOtherPlayer, 1);
                }
                
                if (isImportantToOtherPlayer) {
                    increaseLineCounts(gradient, offset, numOtherPlayer, numOwnPlayer, -1);
                }
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

    private void increaseLineCounts(double[] gradient, int offset, int numOwnPlayer, int numOtherPlayer, int change) {
        if (numOwnPlayer == 1 && numOtherPlayer == 0)
            gradient[offset] += change;

        else if (numOwnPlayer == 2 && numOtherPlayer == 0)
            gradient[offset + 1] += change;

        // Doesn't matter if block the other player if this section doesn't matter
        else if (numOwnPlayer == 2 && numOtherPlayer == 1)
            gradient[offset + 2] += change;
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
            logger.append(weights[i] + "");
        }
        logger.println("");
    }

    @Override
    public String getIdentifier() {
        return IDENTIFIER;
    }

}
