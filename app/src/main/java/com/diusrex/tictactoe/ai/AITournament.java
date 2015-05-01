package com.diusrex.tictactoe.ai;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.diusrex.tictactoe.logic.GridLists;

public class AITournament {
    private static final int NUMBER_OF_UNIQUE_AIS = 40;
    private static final int NUMBER_OF_AIS = NUMBER_OF_UNIQUE_AIS;
    private static final int NUMBER_OF_THREADS = 8;

    private static final int NUMBER_OF_RESULTS_KEPT = 8;

    private static Thread[] allThreads = new Thread[NUMBER_OF_THREADS];

    static public void main(String[] args) {
        List<ScoringValuesTestResults> bestResults = new ArrayList<>();

        setUpStaticObjects();

        long totalStartTime = getCurrentTime();

        for (int i = 0; i < 5; ++i) {
            List<ScoringValuesTestResults> results = new ArrayList<>();
            generateAIScorings(results);
            runAllTests(results);

            System.out.println("Completed " + i);
            printOutResult(results, "Results " + i + ".txt");
            addToBestResults(results, bestResults);
        }

        runAllTests(bestResults);
        printOutResult(bestResults, "Best Results.txt");

        System.out.println("Completed after " + (getCurrentTime() - totalStartTime));
    }

    private static void addToBestResults(List<ScoringValuesTestResults> results,
            List<ScoringValuesTestResults> bestResults) {
        for (int i = 0; i < NUMBER_OF_RESULTS_KEPT; ++i) {
            ScoringValuesTestResults result = results.get(i);
            result.reset();
            bestResults.add(result);
        }
    }

    private static void generateAIScorings(List<ScoringValuesTestResults> results) {
        Set<ScoringValues> allValues = new HashSet<ScoringValues>();

        Random random = new Random();

        while (allValues.size() < NUMBER_OF_UNIQUE_AIS) {
            // It makes no sense to give a better score for having one in a row
            // compared to two in a row
            int mainTwoInRowScore = random.nextInt(99) + 1;
            ScoringFunction mainFunction = new ScoringFunction.Builder().setScoreValues(0,
                    random.nextInt(mainTwoInRowScore), mainTwoInRowScore, random.nextInt(100)).build();
            int secondaryTwoInRowScore = random.nextInt(49) + 1;
            ScoringFunction sectionsFunction = new ScoringFunction.Builder().setScoreValues(random.nextInt(20) - 20,
                    random.nextInt(secondaryTwoInRowScore), secondaryTwoInRowScore, random.nextInt(50)).build();
            allValues.add(new ScoringValues(mainFunction, sectionsFunction));
        }

        for (ScoringValues value : allValues) {
            results.add(new ScoringValuesTestResults(value, 4));
        }
    }

    private static void runAllTests(List<ScoringValuesTestResults> resultsToRun) {
        long totalStartTime = getCurrentTime();
        int numberPerThread = resultsToRun.size() / NUMBER_OF_THREADS;

        for (int i = 0; i < NUMBER_OF_THREADS; ++i) {
            int start = i * numberPerThread;
            int exclusiveEnd = start + numberPerThread;

            // In the case where not divisible evenly
            if (i == NUMBER_OF_THREADS - 1)
                exclusiveEnd = resultsToRun.size();

            TestRunner runner = new TestRunner(resultsToRun, start, exclusiveEnd);
            allThreads[i] = new Thread(runner);
            allThreads[i].start();
        }

        // Need all threads to be finished first
        for (int i = 0; i < NUMBER_OF_THREADS; ++i) {
            try {
                allThreads[i].join();
            } catch (InterruptedException e) {
                // Will not happen
            }
        }

        System.out.println("Total time: " + (getCurrentTime() - totalStartTime));
    }

    private static void setUpStaticObjects() {
        GridLists.initialize();
    }

    private static long getCurrentTime() {
        Calendar c = Calendar.getInstance();
        return c.getTimeInMillis();
    }

    private static void printOutResult(List<ScoringValuesTestResults> results, String file) {
        Collections.sort(results);
        Collections.reverse(results);

        PrintStream printStream = System.out;

        try {
            printStream = new PrintStream(file, "UTF-8");
        } catch (Exception e) {
            // Don't really care about the exception, will keep going to not
            // waste the time spent
        }

        for (int i = 0; i < results.size(); ++i) {
            printStream.print("At " + (i + 1) + " ");
            ScoringValuesTestResults result = results.get(i);
            result.printOut(printStream);
            printStream.println("\n");

        }

        if (printStream != System.out) {
            printStream.close();
        }
    }
}
