package com.diusrex.tictactoe.ai.scoring_calculations.learning;

import java.io.PrintStream;

import com.diusrex.tictactoe.ai.scoring_calculations.Scorer;
import com.diusrex.tictactoe.data_structures.BoardStatus;
import com.diusrex.tictactoe.data_structures.Player;

// Will call the other functions in this class
public class FunctionApproximatorScorer extends Scorer {
    public final static String IDENTIFIER = "FunctionApproximator";
    double previousBoardEstimate;
    double carryOver[];
    double previousGradient[];

    // Formula using these variables is in learnFromChange
    double alpha;
    // Can likely have gamma be quite large
    // Since is fine for the reward to be in the future
    double gamma;
    double lambda;

    // Is just used to not need to allocate new array every single call
    double ignoredGradient[];
    
    long numberTimesExceededCount = 0;

    // Allows different feature sets
    FunctionApproximator approximator;
    private boolean shouldEnsureWinHigher;
    private boolean shouldPrintout;

    private FunctionApproximatorScorer(Builder builder) {
        approximator = builder.approximator;
        alpha = builder.alpha;
        gamma = builder.gamma;
        lambda = builder.lambda;
        
        shouldEnsureWinHigher = builder.shouldEnsureWinHigher;
        shouldPrintout = builder.shouldPrintout;
        
        carryOver = new double[approximator.numberElements()];
        previousGradient = new double[approximator.numberElements()];
        ignoredGradient = new double[approximator.numberElements()];
    }
    
    public static class Builder {

        FunctionApproximator approximator;

        double alpha = 0.005;
        double gamma = 0.005;
        double lambda = 0.005;

        boolean shouldEnsureWinHigher;

        private boolean shouldPrintout;

        public Builder(FunctionApproximator approximator, boolean shouldEnsureWinHigher) {
            this.approximator = approximator;
            this.shouldEnsureWinHigher  = shouldEnsureWinHigher;
        }
        public Builder alpha(double alpha) {
            this.alpha = alpha;
            return this;
        }
        public Builder gamma(double gamma) {
            this.gamma = gamma;
            return this;
        }
        public Builder lambda(double lambda) {
            this.lambda = lambda;
            return this;
        }
        public FunctionApproximatorScorer build() {
            return new FunctionApproximatorScorer(this);
        }
        public Builder shouldPrintout(boolean shouldPrintout) {
            this.shouldPrintout = shouldPrintout;
            return this;
        }
    }

    @Override
    public double calculateScore(Player positivePlayer, BoardStatus board) {
        double score = approximator.getScore(positivePlayer, board, ignoredGradient);
        
        if (shouldPrintout && score > getWinScore()) {
            ++numberTimesExceededCount;
            //System.out.println("Exceeded win score anyway, score is " + score);
        }
        
        return score;
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

        //System.out.print("Carry over: ");
        for (int i = 0; i < approximator.numberElements(); ++i) {
            carryOver[i] = gamma * lambda * carryOver[i] + previousGradient[i];
            approximator.update(i, alpha * delta * carryOver[i]);
            //System.out.print(carryOver[i] + " ");
        }
        
        //System.out.println();
        //System.out.print("Changes: ");
        //for (int i = 0; i < approximator.numberElements(); ++i) {
            //System.out.print(alpha * delta * carryOver[i] + " ");
        //}
        
        //System.out.println();
        //System.out.println("Delta: " + delta);

        // Then, determine estimate for the newly updated weights
        previousBoardEstimate = approximator.getScore(board.getNextPlayer(), board, previousGradient);
        //System.out.println("Estimate: " + previousBoardEstimate);
        //System.out.print("Gradient: " );
        //for (int i = 0; i < approximator.numberElements(); ++i) {
        //    System.out.print(previousGradient[i] + " ");
        //}
        
        //System.out.println();
    }

    public void saveState(PrintStream logger) {
        logger.append("FunctionApproximator\n");
        logger.append("" + alpha + " " + lambda + " " + gamma + "\n");
        logger.append("Win increased: " + shouldEnsureWinHigher);
        if (shouldPrintout) {
            logger.append("Exceeded basic win score " + numberTimesExceededCount + " times");
        }
        logger.append(approximator.getIdentifier() + "\n");
        approximator.saveState(logger);
    }

    @Override
    public double getWinScore() {
        if (shouldEnsureWinHigher) {
            return 10000000;
        } else {
            return approximator.getWinScore();
        }
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
