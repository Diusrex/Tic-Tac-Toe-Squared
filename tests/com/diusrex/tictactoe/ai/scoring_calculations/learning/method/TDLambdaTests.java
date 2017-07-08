package com.diusrex.tictactoe.ai.scoring_calculations.learning.method;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.PrintStream;

import org.junit.Before;
import org.junit.Test;
import org.mockito.AdditionalMatchers;
import org.mockito.Mock;

import com.diusrex.tictactoe.ai.scoring_calculations.learning.FunctionApproximator;
import com.diusrex.tictactoe.data_structures.BoardStatus;
import com.diusrex.tictactoe.data_structures.Player;

// This test is meant to both ensure the calculations for TDLambda are correct
// and give an idea of how the values will change over time.
public class TDLambdaTests {

    int numElements = 5;
    TestingFunctionApproximator approximator;
    
    @Before
    public void setup() {
        approximator = new TestingFunctionApproximator(numElements);
    }
    
    @Test
    public void ensureValidUpdateIncreaseStates() {
        double alpha = 0.005;
        double gamma = 0.95;
        double lambda = 0.005;
        
        
        TDLambda trace = new TDLambda.Builder(numElements).alpha(alpha)
                .gamma(gamma).lambda(lambda).build();
        
        trace.newGame();
        

        double[] features = new double[numElements];
        for (int i = 0; i < numElements; ++i) {
            features[i] = i;
        }

        // Estimate before any learning
        assertEquals(0.0,
                approximator.getScore(null, null, features), 1e-7);
        
        // delta = gamma * (nextEstimate - previousEstimate)
        double nextEstimate = 1.0;
        double previousEstimate = 0.0;
        
        trace.learnFromChange(nextEstimate, previousEstimate, features, approximator);
        
        // Expect 5 elements were updated correctly
        // Trace was 0
        verifyApproximatorUpdatedOnceForIndex(0, 0.0);
        verifyApproximatorUpdatedOnceForIndex(1, 4.75e-3); // alpha * delta * 1.0 (trace)
        verifyApproximatorUpdatedOnceForIndex(2, 9.5e-3); // alpha * delta * 2.0 (trace)
        verifyApproximatorUpdatedOnceForIndex(3, 14.25e-3); // alpha * delta * 3.0 (trace)
        verifyApproximatorUpdatedOnceForIndex(4, 19.0e-3); // alpha * delta * 4.0 (trace)
        
        // Ensure traces was properly updated
        assertEquals(0.0, trace.trace[0], 1e-7);
        assertEquals(1.0, trace.trace[1], 1e-7);
        assertEquals(2.0, trace.trace[2], 1e-7);
        assertEquals(3.0, trace.trace[3], 1e-7);
        assertEquals(4.0, trace.trace[4], 1e-7);
        
        reset(approximator.internalApproximator);
        
        // Estimate after change:
        assertEquals(4.75e-3 + 2 * 9.5e-3 + 3 * 14.25e-3 + 4 * 19.0e-3, // 0.1425
                approximator.getScore(null, null, features), 1e-7);
        
        // Re-run the trace, should be updated differently
        // Note that, in reality, the previousEstiamte would have also increased.
        trace.learnFromChange(nextEstimate, previousEstimate, features, approximator);
        
        // Expect 5 elements were updated correctly
        verifyApproximatorUpdatedOnceForIndex(0, 0);
        
        // First part is from the current missed estimate
        // Second part is from the trace of past calls
        // features[i] * alpha * delta  + features[i] * gamma * lambda * alpha * delta
        verifyApproximatorUpdatedOnceForIndex(1,
                4.75e-3 + 2.256e-5); 
        verifyApproximatorUpdatedOnceForIndex(2,
                9.5e-3 + 4.5125e-5);
        verifyApproximatorUpdatedOnceForIndex(3,
                14.25e-3 + 6.7688e-5);
        verifyApproximatorUpdatedOnceForIndex(4,
                19.0e-3 + 9.025e-5);
    

        // Adjustment was faster than normal to make calculations easier to estimate
        assertEquals(4.75e-3 + 4.75e-3 + 2.256e-5 +
                2 * (9.5e-3 + 9.5e-3 + 4.5125e-5) +
                3 * (14.25e-3 + 14.25e-3 + 6.7688e-5) +
                4 * (19.0e-3 + 19.0e-3 + 9.025e-5), // 0.2857
                approximator.getScore(null, null, features), 1e-7);
    }

    @Test
    public void ensureValidUpdateUnchangedThenIncreased() {
        double alpha = 0.005;
        double gamma = 0.95;
        double lambda = 0.005;
        
        
        TDLambda trace = new TDLambda.Builder(numElements).alpha(alpha)
                .gamma(gamma).lambda(lambda).build();
        
        trace.newGame();
        

        double[] previousFeatures = new double[numElements];
        for (int i = 0; i < numElements; ++i) {
            previousFeatures[i] = i;
        }
        
        // delta = 0.0
        double nextEstimate = 0.0;
        double previousEstimate = 0.0;
        
        trace.learnFromChange(nextEstimate, previousEstimate, previousFeatures, approximator);

        
        // Nothing should have been changed in approximator, since the delta was 0.0
        verifyApproximatorUpdatedOnceForIndex(0, 0);
        verifyApproximatorUpdatedOnceForIndex(1, 0.0); // alpha * delta * 1.0 (trace)
        verifyApproximatorUpdatedOnceForIndex(2, 0.0); // alpha * delta * 2.0 (trace)
        verifyApproximatorUpdatedOnceForIndex(3, 0.0); // alpha * delta * 3.0 (trace)
        verifyApproximatorUpdatedOnceForIndex(4, 0.0); // alpha * delta * 4.0 (trace)
        
        // But the traces should have been updated anyway - represent the history that we have seen
        assertEquals(0.0, trace.trace[0], 1e-7);
        assertEquals(1.0, trace.trace[1], 1e-7);
        assertEquals(2.0, trace.trace[2], 1e-7);
        assertEquals(3.0, trace.trace[3], 1e-7);
        assertEquals(4.0, trace.trace[4], 1e-7);

        
        reset(approximator.internalApproximator);

        // delta = gamma * (nextEstimate - previousEstimate)
        nextEstimate = 1.0;
        
        
        // Re-run the trace, should be updated differently
        trace.learnFromChange(nextEstimate, previousEstimate, previousFeatures, approximator);
        
        // Expect 5 elements were updated correctly
        // Trace was 0
        // First part is from the current missed estimate
        // Second part is from the trace of past calls
        // features[i] * alpha * delta  + features[i] * gamma * lambda * alpha * delta
        verifyApproximatorUpdatedOnceForIndex(1,
                4.75e-3 + 2.256e-5); 
        verifyApproximatorUpdatedOnceForIndex(2,
                9.5e-3 + 4.5125e-5);
        verifyApproximatorUpdatedOnceForIndex(3,
                14.25e-3 + 6.7688e-5);
        verifyApproximatorUpdatedOnceForIndex(4,
                19.0e-3 + 9.025e-5);
        
        // Ensure traces was properly updated
        assertEquals(0.0, trace.trace[0], 1e-7);
        // First part is from the current call, second is from the first call in this function
        assertEquals(1.0 + 1.0 * gamma * lambda, trace.trace[1], 1e-7);
        assertEquals(2.0 + 2.0 * gamma * lambda, trace.trace[2], 1e-7);
        assertEquals(3.0 + 3.0 * gamma * lambda, trace.trace[3], 1e-7);
        assertEquals(4.0 + 4.0 * gamma * lambda, trace.trace[4], 1e-7);
    }

    private void verifyApproximatorUpdatedOnceForIndex(int index, double change) {
        verify(approximator.internalApproximator, times(1)).update(
                eq(index), AdditionalMatchers.eq(change, 1e-7));
    }
    
    // Everything starts off with 0.0, so estimate will always be 0
    // Will also pass all calls to internalApproximator
    static class TestingFunctionApproximator implements FunctionApproximator {
        double weights[];
        
        @Mock FunctionApproximator internalApproximator;
        
        TestingFunctionApproximator(int numElements) {
            weights = new double[numElements];
            internalApproximator = mock(FunctionApproximator.class);
        }

        // WARNING: Assumes the features is correct size + elements have been populated
        @Override
        public double getScore(Player positivePlayer, BoardStatus board, double[] features) {
            internalApproximator.getScore(positivePlayer, board, features);
            
            double sum = 0.0;
            for (int i = 0; i < features.length; ++i) {
                sum += weights[i] * features[i];
            }
            return sum;
        }

        @Override
        public void update(int element, double change) {
            weights[element] += change;

            internalApproximator.update(element, change);
        }

        @Override
        public int numberElements() {
            return weights.length;
        }

        @Override
        public double getWinScore() {
            return 0;
        }

        @Override
        public void saveParameters(PrintStream logger) {
            throw new UnsupportedOperationException("This should not be called");
        }

        @Override
        public String getIdentifier() {
            throw new UnsupportedOperationException("This should not be called");
        }

        @Override
        public double[] getParametersCopy() {
            throw new UnsupportedOperationException("This should not be called");
        }
        
    }
}
