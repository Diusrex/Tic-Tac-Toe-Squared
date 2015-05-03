package com.diusrex.tictactoe.ai.tournament;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import com.diusrex.tictactoe.data_structures.Move;
import com.diusrex.tictactoe.logic.GridLists;

public class AITournament {
    private static int NUMBER_OF_UNIQUE_AI_ARG_POS = 0;
    private static int NUMBER_OF_THREADS_ARG_POS = 1;
    private static int NUMBER_OF_RESULTS_KEPT_ARG_POS = 2;
    private static int FILE_TO_LOAD_FROM_ARG_POS = 3;
    private static int numberOfUniqueAI;
    private static int numberOfThreads;

    private static int numberOfResultsKept;
    
    private static Scanner fileScanner;

    private static Thread[] allThreads;

    // Args -> numberAI numberThreads numberKept
    static public void main(String[] args) {
        for (String arg : args) {
            System.out.println(arg);
        }
        numberOfUniqueAI = Integer.parseInt(args[NUMBER_OF_UNIQUE_AI_ARG_POS]);
        numberOfThreads = Integer.parseInt(args[NUMBER_OF_THREADS_ARG_POS]);
        numberOfResultsKept = Integer.parseInt(args[NUMBER_OF_RESULTS_KEPT_ARG_POS]);
        
        if (args.length > FILE_TO_LOAD_FROM_ARG_POS) {
            try {
                fileScanner = new Scanner(new File(args[FILE_TO_LOAD_FROM_ARG_POS]));
            } catch (FileNotFoundException e) {
                System.out.println("Loading file scanner failed. Will just randomly generate instead");
            }
        }
        
        allThreads = new Thread[numberOfThreads];
        
        List<ScoringValuesTestResults> bestResults = new ArrayList<>();

        setUpStaticObjects();

        long totalStartTime = getCurrentTime();
        if (numberOfResultsKept < numberOfUniqueAI) {
            // Will run as many times as needed to ensure the final one is full
            for (int i = 0; i < numberOfUniqueAI / numberOfResultsKept; ++i) {
                List<ScoringValuesTestResults> results = new ArrayList<>();
                generateAIScorings(results);
                runAllTests(results);
    
                System.out.println("Completed " + i);
                printOutResult(results, "Results " + i + ".txt");
                addToBestResults(results, bestResults);
            }
        } else {
            generateAIScorings(bestResults);
        }
        
        System.out.println("Running best results");

        runAllTests(bestResults);
        printOutResult(bestResults, "Best Results.txt");

        System.out.println("Completed after " + (getCurrentTime() - totalStartTime));
    }

    private static void generateAIScorings(List<ScoringValuesTestResults> results) {
        if (fileScanner == null) {
            System.out.println("Randomly generating");
            RandomScoringsGenerator.generateAIScorings(results, numberOfUniqueAI);
        } else {
            System.out.println("Loading from file");
            ScoringsFromFile.loadAIScorings(fileScanner, results, numberOfUniqueAI);
        }
        
    }

    private static void addToBestResults(List<ScoringValuesTestResults> results,
            List<ScoringValuesTestResults> bestResults) {
        for (int i = 0; i < numberOfResultsKept; ++i) {
            ScoringValuesTestResults result = results.get(i);
            result.reset();
            bestResults.add(result);
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

    public static long getCurrentTime() {
        Calendar c = Calendar.getInstance();
        return c.getTimeInMillis();
    }

    public static void printOutResult(List<ScoringValuesTestResults> results, String file) {
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
