package com.diusrex.tictactoe.ai.scoring_calculations.learning;

import java.io.PrintStream;

import com.diusrex.tictactoe.data_structures.BoardStatus;
import com.diusrex.tictactoe.data_structures.LinesFormed;
import com.diusrex.tictactoe.data_structures.Player;
import com.diusrex.tictactoe.data_structures.grid.Grid;
import com.diusrex.tictactoe.data_structures.position.SectionPosition;
import com.diusrex.tictactoe.logic.GridLists;

public abstract class FunctionApproximatorBase implements FunctionApproximator {
    
    private double[] weights = new double[numberElements()];
    
    protected abstract int getNumFeaturesPerSectionType();

    @Override
    public final double getScore(Player positivePlayer, BoardStatus board, double[] features) {
        Player winner = board.getWinner();
        if (winner != Player.Unowned) {
            return (winner == positivePlayer) ? getWinScore() : -getWinScore();
        }

        for (int i = 0; i < numberElements(); ++i)
            features[i] = 0;
        
        // The value won't be used  from previous, but should speed it up by not allocating every step
        LinesFormed linesFormedUsingSection = new LinesFormed(positivePlayer);

        int offset = 0;
        for (SectionPosition cornerPos : GridLists.getAllCornerSections()) {
            calculateFeaturesForSection(positivePlayer, board, cornerPos, features, offset, linesFormedUsingSection);
        }

        offset += getNumFeaturesPerSectionType();

        for (SectionPosition midEdgePos : GridLists.getAllMidEdgeSections()) {
            calculateFeaturesForSection(positivePlayer, board, midEdgePos, features, offset, linesFormedUsingSection);
        }
        offset += getNumFeaturesPerSectionType();

        calculateFeaturesForSection(positivePlayer, board, SectionPosition.make(1, 1), features, offset, linesFormedUsingSection);

        offset += getNumFeaturesPerSectionType();
        
        // Calculate for the maingrid specially since it will always be important
        board.getMainGrid().getLinesFormed(linesFormedUsingSection);
        calculateFeaturesForMainGrid(positivePlayer, board.getMainGrid(), features, offset, linesFormedUsingSection);
        
        double score = 0;
        for (int i = 0; i < numberElements(); ++i)
            score += features[i] * weights[i];
        return score;
    }

    private final void calculateFeaturesForSection(Player positivePlayer, BoardStatus board, SectionPosition section,
            double[] gradient, int offset, LinesFormed linesFormedUsingSection) {
        Grid subGrid = board.getSubGrid(section);
        
        if (!subGrid.canBeWon()) {
            // Set all lines to 0, so will treat it as not being important to either player
            linesFormedUsingSection.reset();
            calculateFeaturesForGrid(positivePlayer, subGrid, gradient, offset, linesFormedUsingSection);
        } else {
            board.getLinesUsingSection(section, linesFormedUsingSection);
            calculateFeaturesForGrid(positivePlayer, subGrid, gradient, offset, linesFormedUsingSection);
        }
    }
    
    // linesFormedForSection must be the lines that are formed using this section, from the perspective of positivePlayer
    protected abstract void calculateFeaturesForGrid(Player positivePlayer, Grid grid, double[] gradient, int offset,
            LinesFormed linesFormedForSection);

    protected abstract void calculateFeaturesForMainGrid(Player positivePlayer, Grid mainGrid, double[] gradient, int offset,
            LinesFormed linesFormedInMainGrid);
    
    protected final boolean isImportantForPositivePlayer(LinesFormed linesFormedForSection) {
        return linesFormedForSection.unownedButWinnableForMain + linesFormedForSection.oneFormedForMain
                + linesFormedForSection.twoFormedForMain != 0;
    }
    
    protected final boolean isImportantForOtherPlayer(LinesFormed linesFormedForSection) {
        return linesFormedForSection.unownedButWinnableForOther + linesFormedForSection.oneFormedForOther
                + linesFormedForSection.twoFormedForOther != 0;
    }

    @Override
    public final void update(int element, double change) {
        weights[element] += change;
    }

    @Override
    public final void saveParameters(PrintStream logger) {
        for (int i = 0; i < numberElements(); ++i) {
            logger.append(weights[i] + " ");
        }
        logger.println("");
    }

    @Override
    public final double[] getParametersCopy() {
        return weights.clone();
    }

}
