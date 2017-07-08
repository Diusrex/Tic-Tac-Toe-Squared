package com.diusrex.tictactoe.ai.scoring_calculations.learning.method;

import java.io.PrintStream;

import com.diusrex.tictactoe.ai.scoring_calculations.learning.FunctionApproximator;

// Backward View of TD(lambda)
// General formula will be:
//   delta = gamma*currentEstimate - previousEstimate
//   trace = gamma * lambda * previousTrace + features of previousEstimate
//   weights += alpha * delta * trace
// Will be learning for what we estimated for previousEstimate.
public class TDLambda implements LearningMethod {
    public static final String IDENTIFIER = "EligibilityTrace";
    final double trace[];
    final int numberElements;

    // Formula using these variables is in learnFromChange
    final double alpha;
    // Can likely have gamma be quite large
    // Since is fine for the reward to be in the future
    final double gamma;
    final double lambda;
    
    // Replacing will do max(old * gamma * lambda, newFeatures)
    public static enum TraceVersion { ACCUMULATING, REPLACING };
    TraceVersion traceVersion;
    
    public static class Builder {
        final int numberElements;
        
        double alpha = 0.005;
        double gamma = 0.005;
        double lambda = 0.005;
        
        TraceVersion traceVersion = TraceVersion.ACCUMULATING;
        
        public Builder(int numberElements) {
            this.numberElements = numberElements;
        }
        public TDLambda build() {
            return new TDLambda(this);
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
        public Builder traceVersion(TraceVersion traceVersion) {
            this.traceVersion = traceVersion;
            return this;
        }
    }
    
    private TDLambda(Builder builder) {
        trace = new double[builder.numberElements];
        this.numberElements = builder.numberElements;
        
        alpha = builder.alpha;
        gamma = builder.gamma;
        lambda = builder.lambda;
        
        traceVersion = builder.traceVersion;
    }

    @Override
    public void newGame() {
        for (int i = 0; i < numberElements; ++i) {
            trace[i] = 0;
        }
    }

    // When debugging, would be useful to print out the trace before + after each step
    @Override
    public void learnFromChange(double newStateEstimate, double previousBoardEstimate,
            double[] previousFeatures, FunctionApproximator approximator) {
        double delta = gamma * newStateEstimate - previousBoardEstimate;
        for (int i = 0; i < numberElements; ++i) {
            
            switch (traceVersion) {
            case ACCUMULATING:
                trace[i] = gamma * lambda * trace[i] + previousFeatures[i];
                
                break;
            case REPLACING:
                if (previousFeatures[i] == 0) {
                    trace[i] = gamma * lambda * trace[i];
                } else {
                    trace[i] = previousFeatures[i];
                }
                
                break;
            }
            
            approximator.update(i, alpha * delta * trace[i]);
        }
    }

    @Override
    public String getIdentifier() {
        return IDENTIFIER;
    }

    @Override
    public void saveParameters(PrintStream logger) {
        logger.println("" + alpha + " " + gamma + " " + lambda);
    }
}
