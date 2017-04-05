package com.diusrex.tictactoe.ai.scoring_calculations.learning;

import java.io.PrintStream;

import com.diusrex.tictactoe.ai.scoring_calculations.Scorer;
import com.diusrex.tictactoe.ai.scoring_calculations.learning.method.LearningMethod;
import com.diusrex.tictactoe.data_structures.BoardStatus;
import com.diusrex.tictactoe.data_structures.Player;

// This class will use the provided LearningMethod to update the FunctionApproximator at each step.
public class FunctionApproximatorScorer extends Scorer {
    public final static String IDENTIFIER = "FunctionApproximator";
    double previousBoardEstimate;
    double previousGradient[];

    // Is just used to not need to allocate new array every single call
    double ignoredGradient[];

    long numberTimesExceededCount = 0;

    // Allows different feature sets
    FunctionApproximator approximator;

    LearningMethod learningMethod;

    private boolean shouldEnsureWinHigher;
    private boolean shouldPrintout;

    private FunctionApproximatorScorer(Builder builder) {
        approximator = builder.approximator;

        shouldEnsureWinHigher = builder.shouldEnsureWinHigher;
        shouldPrintout = builder.shouldPrintout;

        previousGradient = new double[approximator.numberElements()];
        ignoredGradient = new double[approximator.numberElements()];
    }

    public static class Builder {

        FunctionApproximator approximator;

        LearningMethod learningMethod;

        boolean shouldEnsureWinHigher;

        private boolean shouldPrintout;

        public Builder(FunctionApproximator approximator, LearningMethod learningMethod, boolean shouldEnsureWinHigher) {
            this.approximator = approximator;
            this.learningMethod = learningMethod;
            this.shouldEnsureWinHigher = shouldEnsureWinHigher;
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
        learningMethod.newGame();

        previousBoardEstimate = approximator.getScore(board.getNextPlayer(), board, ignoredGradient);
    }

    // TODO: Make sure this is being called at end of game!
    @Override
    public void learnFromChange(BoardStatus board) {
        // Determine current estimate
        double newStateEstimate = calculateScore(board.getNextPlayer(), board);

        learningMethod.learnFromChange(newStateEstimate, previousBoardEstimate, previousGradient, approximator);

        previousBoardEstimate = approximator.getScore(board.getNextPlayer(), board, previousGradient);
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
        logger.println(learningMethod.getIdentifier());
    }

    @Override
    public void saveParameters(PrintStream logger) {
        learningMethod.saveParameters(logger);
        logger.append("Win increased: " + shouldEnsureWinHigher);
        approximator.saveParameters(logger);
    }

    @Override
    public String getIdentifier() {
        return IDENTIFIER;
    }
}
