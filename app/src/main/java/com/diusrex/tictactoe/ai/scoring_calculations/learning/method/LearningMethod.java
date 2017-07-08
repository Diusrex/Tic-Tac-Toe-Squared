package com.diusrex.tictactoe.ai.scoring_calculations.learning.method;

import java.io.PrintStream;

import com.diusrex.tictactoe.ai.scoring_calculations.learning.FunctionApproximator;

public interface LearningMethod {
    public String getIdentifier();

    public void newGame();

    public void learnFromChange(double newStateEstimate, double previousBoardEstimate, double[] previousFeatures,
            FunctionApproximator approximator);

    public void saveParameters(PrintStream logger);
}
