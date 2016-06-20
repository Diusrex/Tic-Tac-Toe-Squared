package com.diusrex.tictactoe.ai.scoring_calculations.learning;

import java.io.PrintStream;

import com.diusrex.tictactoe.ai.scoring_calculations.Scorer;
import com.diusrex.tictactoe.data_structures.BoardStatus;
import com.diusrex.tictactoe.data_structures.Player;

public class FunctionApproximatorScorer extends Scorer {
    public final static String IDENTIFIER = "FunctionApproximator";
    double previousBoardEstimate;
    double carryOver[];
    double previousGradient[];
    
    double alpha;
    // Can likely have gamma be quite large
    // Since is fine for the reward to be in the future
    double gamma;
    double lambda;
    
    // Is just used to not need to allocate new array every single call
    double ignoredGradient[];
    
    // Allows different feature sets
    FunctionApproximator approximator;
    
    public FunctionApproximatorScorer(FunctionApproximator approximator) {
        
        carryOver = new double[approximator.numberElements()];
        previousGradient = new double[approximator.numberElements()];
        ignoredGradient = new double[approximator.numberElements()];
    }

    @Override
    public double calculateScore(Player positivePlayer, BoardStatus board) {
        return approximator.getScore(positivePlayer, board, ignoredGradient);
    }

    @Override
    public void newGame(BoardStatus board) {
        for (int i = 0; i < approximator.numberElements(); ++i) {
            carryOver[i] = 0;
        }
        
        approximator.getScore(board.getNextPlayer(), board, ignoredGradient);
    }

    // General formula will be:
    //      delta = lambda*currentEstimate - previousEstimate
    //      carryOver = gamma * lambda * previousCarryOver + gradient of previousEstimate
    //      new weights = weights + alpha * delta * carryOver
    @Override
    public void learnFromChange(BoardStatus board) {
        // Determine current estimate
        double newStateEstimate = calculateScore(board.getNextPlayer(), board);
        double delta = gamma * newStateEstimate - previousBoardEstimate;
        
        for (int i = 0; i < approximator.numberElements(); ++i) {
            carryOver[i] = gamma * lambda * carryOver[i] + previousGradient[i];
            approximator.update(i, alpha * delta * carryOver[i]);
        }
        
        // Then, determine estimate for the newly updated weights
        previousBoardEstimate = approximator.getScore(board.getNextPlayer(), board, previousGradient);
    }
    
    public void saveState(PrintStream logger) {
        logger.append("FunctionApproximator\n");
        logger.append("" + alpha + "" + lambda + "" + gamma + "\n");
        approximator.saveState(logger);
    }



    @Override
    public double getWinScore() {
        return approximator.getWinScore();
    }

    @Override
    protected void saveInternalState(PrintStream logger) {
        logger.println(approximator.getIdentifier());
        approximator.saveState(logger);
    }

    @Override
    public String getIdentifier() {
        return IDENTIFIER;
    }
}
