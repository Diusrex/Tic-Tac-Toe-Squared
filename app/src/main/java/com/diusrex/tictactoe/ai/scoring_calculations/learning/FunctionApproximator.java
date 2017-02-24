package com.diusrex.tictactoe.ai.scoring_calculations.learning;

import java.io.PrintStream;

import com.diusrex.tictactoe.data_structures.BoardStatus;
import com.diusrex.tictactoe.data_structures.Player;

// TODO: Subclasses should be more merged together, since they act very similar
public interface FunctionApproximator {
    double getScore(Player positivePlayer, BoardStatus board, double gradient[]);
    
    void update(int element, double change);
    
    int numberElements();

    double getWinScore();

    // Can be used to recreate the FunctionApproximator, given that we already know its type.
    void saveParameters(PrintStream logger);

    String getIdentifier();

    double[] getParametersCopy();
}
