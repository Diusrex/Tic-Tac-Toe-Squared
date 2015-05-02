package com.diusrex.tictactoe.ai;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.diusrex.tictactoe.data_structures.Move;
import com.diusrex.tictactoe.logic.GridLists;

public class AITournament {
    private static int NUMBER_OF_UNIQUE_AI_ARG_POS = 1;
    private static int NUMBER_OF_THREADS_ARG_POS = 2;
    private static int NUMBER_OF_RESULTS_KEPT_ARG_POS = 3;
    private static int numberOfUniqueAI;
    private static int numberOfThreads;

    private static int numberOfResultsKept;

    private static Thread[] allThreads;

    // Args -> numberAI numberThreads numberKept
    static public void main(String[] args) {
        numberOfUniqueAI = Integer.parseInt(args[NUMBER_OF_UNIQUE_AI_ARG_POS]);
        numberOfThreads = Integer.parseInt(args[NUMBER_OF_THREADS_ARG_POS]);
        numberOfResultsKept = Integer.parseInt(args[NUMBER_OF_RESULTS_KEPT_ARG_POS]);
        
        allThreads = new Thread[numberOfThreads];
        
        List<ScoringValuesTestResults> bestResults = new ArrayList<>();

        setUpStaticObjects();

        long totalStartTime = getCurrentTime();

        // Will run as many times as needed to ensure the final one is full
        for (int i = 0; i < numberOfUniqueAI / numberOfResultsKept; ++i) {
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
        for (int i = 0; i < numberOfResultsKept; ++i) {
            ScoringValuesTestResults result = results.get(i);
            result.reset();
            bestResults.add(result);
        }
    }

    private static void generateAIScorings(List<ScoringValuesTestResults> results) {
        Set<ScoringValues> allValues = new HashSet<ScoringValues>();

        Random random = new Random();

        while (allValues.size() < numberOfUniqueAI) {
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
        int numberPerThread = resultsToRun.size() / numberOfThreads;

        for (int i = 0; i < numberOfThreads; ++i) {
            int start = i * numberPerThread;
            int exclusiveEnd = start + numberPerThread;

            // In the case where not divisible evenly
            if (i == numberOfThreads - 1)
                exclusiveEnd = resultsToRun.size();

            TestRunner runner = new TestRunner(resultsToRun, start, exclusiveEnd);
            allThreads[i] = new Thread(runner);
            allThreads[i].start();
        }

        // Need all threads to be finished first
        for (int i = 0; i < numberOfThreads; ++i) {
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
        Move.init();
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
