package com.diusrex.tictactoe.ai.scoring_calculations.learning;

import java.io.PrintStream;

import com.diusrex.tictactoe.ai.scoring_calculations.Scorer;
import com.diusrex.tictactoe.data_structures.BoardStatus;
import com.diusrex.tictactoe.data_structures.Player;

// Will call the other functions in this class
// This function uses eligibility traces to train - will constantly
// train on the features of the grid that we had once seen, keeping an
// idea of the features using the trace
// Try: high lambda, low gamma -> want the accumulation to reduce quickly, since getting
// quite a bit each time
// NOTE: If alpha value is too large, can become unstable and perform far worse than expected!
// TODO: Way to make this guy sometimes choose 'worse' solution?
public class FunctionApproximatorScorer extends Scorer {
    public final static String IDENTIFIER = "FunctionApproximator";
    double previousBoardEstimate;
    double trace[];
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
        
        trace = new double[approximator.numberElements()];
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
            trace[i] = 0;
        }

        approximator.getScore(board.getNextPlayer(), board, ignoredGradient);
    }

    // TODO: This should be refactored to use an algorithmic class to update to make it easier
    // to try out + compare different learning methods
    // General formula will be:
    //      delta = lambda*currentEstimate - previousEstimate
    //      trace = gamma * lambda * previousTrace + gradient of previousEstimate
    //      weights += alpha * delta * trace
    @Override
    public void learnFromChange(BoardStatus board) {
        // Determine current estimate
        double newStateEstimate = calculateScore(board.getNextPlayer(), board);
        double delta = gamma * newStateEstimate - previousBoardEstimate;

        //System.out.print("Carry over: ");
        for (int i = 0; i < approximator.numberElements(); ++i) {
            trace[i] = gamma * lambda * trace[i] + previousGradient[i];
            approximator.update(i, alpha * delta * trace[i]);
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

    @Override
    public double getWinScore() {
        if (shouldEnsureWinHigher) {
            return 10000000;
        } else {
            return approximator.getWinScore();
        }
    }

    @Override
    protected void saveInternalIdentifiers(PrintStream logger) {
        if (shouldPrintout) {
            logger.append("Exceeded basic win score " + numberTimesExceededCount + " times");
        }
        logger.println(approximator.getIdentifier());
    }
    
    @Override
    public void saveParameters(PrintStream logger) {
        logger.append("" + alpha + " " + lambda + " " + gamma + "\n");
        logger.append("Win increased: " + shouldEnsureWinHigher);
        approximator.saveParameters(logger);
    }

    @Override
    public String getIdentifier() {
        return IDENTIFIER;
    }
}
