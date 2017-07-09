package com.diusrex.tictactoe.ai.scoring_calculations.learning;

import java.io.PrintStream;

import com.diusrex.tictactoe.data_structures.Player;
import com.diusrex.tictactoe.data_structures.board_status.BoardStatus;

public interface FunctionApproximator {
    // features is calculated by the approximator. Must be numberElements() sized
    double getScore(Player positivePlayer, BoardStatus board, double features[]);
    
    void update(int element, double change);
    
    int numberElements();

    double getWinScore();

    // Can be used to recreate the FunctionApproximator, given that we already know its type.
    void saveParameters(PrintStream logger);

    String getIdentifier();

    double[] getParametersCopy();
}
