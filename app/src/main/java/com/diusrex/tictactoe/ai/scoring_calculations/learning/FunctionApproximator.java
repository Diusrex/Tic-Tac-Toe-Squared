package com.diusrex.tictactoe.ai.scoring_calculations.learning;

import java.io.PrintStream;

import com.diusrex.tictactoe.data_structures.BoardStatus;
import com.diusrex.tictactoe.data_structures.Player;

public interface FunctionApproximator {
    double getScore(Player positivePlayer, BoardStatus board, double gradient[]);
    
    void update(int element, double change);
    
    int numberElements();

    double getWinScore();

    void saveState(PrintStream logger);

    String getIdentifier();

    double[] getParametersCopy();
}
