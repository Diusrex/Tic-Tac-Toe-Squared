package com.diusrex.tictactoe.ai.tournament;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

import com.diusrex.tictactoe.ai.ScalingAlphaBetaPlayer;
import com.diusrex.tictactoe.ai.ScalingMiniMaxPlayer;
import com.diusrex.tictactoe.ai.UnScalingAlphaBetaPlayer;
import com.diusrex.tictactoe.ai.UnScalingMiniMaxPlayer;
import com.diusrex.tictactoe.data_structures.Move;
import com.diusrex.tictactoe.logic.GridLists;

public class AITournament {
    public static final int MAX_NUM_MOVES = 81;
    private static int numberOfUniqueAI;
    private static int numberOfThreads;

    private static int numberOfResultsKept;

    private static boolean isVerbose;
    private static boolean isTesting;

    private static TournamentAIGenerator generator;

    private static Thread[] allThreads;

    private static PrintStream logFile;

    static public void main(String[] args) {
        parseArguments(args);

        try {
            logFile = new PrintStream("log.txt", "UTF-8");
        } catch (Exception e) {
            System.exit(1);
        }

        allThreads = new Thread[numberOfThreads];

        List<BaseScoringValuesTestResults> bestResults = new ArrayList<>();

        setUpStaticObjects();

        long totalStartTime = getCurrentTime();
        if (numberOfResultsKept != 0) {
            // Will run as many times as needed to ensure the final one is full
            for (int i = 0; i < numberOfUniqueAI / numberOfResultsKept; ++i) {
                List<BaseScoringValuesTestResults> results = new ArrayList<>();
                generateAIScorings(results);

                long currentRunStart = getCurrentTime();

                if (!isTesting)
                    runAllTests(results);

                logFile.println("Completed run " + i + " after " + (getCurrentTime() - currentRunStart));

                System.out.println("Completed " + i);
                printOutResult(results, "Results " + i + ".txt");
                addToBestResults(results, bestResults);
            }
        } else {
            generateAIScorings(bestResults);
        }

        System.out.println("Running best results");

        long finalRunStart = getCurrentTime();

        if (!isTesting) {
            runAllTests(bestResults);
        }

        logFile.println("Completed all runs " + (getCurrentTime() - finalRunStart));

        printOutResult(bestResults, "Best Results.txt");

        logFile.println("Completed all aftessr " + (getCurrentTime() - totalStartTime));
    }

    private static void parseArguments(String[] args) {
        ArgumentParser parser = ArgumentParsers.newArgumentParser("AITournament").defaultHelp(true)
                .description("Run a tournament of AI");
        parser.addArgument("-n", "--number-AI").dest("number").type(Integer.class).setDefault(0)
                .help("Number of AI to create");
        parser.addArgument("-t", "--threads").type(Integer.class).setDefault(0).help("Specify number of threads");
        parser.addArgument("-k", "--number-AI-kept").dest("kept").type(Integer.class).setDefault(0)
                .help("Will cause a final round to be run, with given number of each earlier run");
        parser.addArgument("-f", "--file").setDefault("").help("File to load AI from");
        parser.addArgument("-v", "--verbose").type(Boolean.class).setDefault(false).action(Arguments.storeTrue())
                .help("Print out additional information on how the AI's did");
        parser.addArgument("--test").type(Boolean.class).setDefault(false).action(Arguments.storeTrue())
                .help("See what will be created without running anything");
        parser.addArgument("AITypes")
                .nargs("+")
                .choices(UnScalingMiniMaxPlayer.IDENTIFIER, ScalingMiniMaxPlayer.IDENTIFIER,
                        UnScalingAlphaBetaPlayer.IDENTIFIER, ScalingAlphaBetaPlayer.IDENTIFIER).help("AITypes to use");

        Namespace ns = null;
        try {
            ns = parser.parseArgs(args);
        } catch (ArgumentParserException e) {
            parser.handleError(e);
            System.exit(1);
        }

        numberOfUniqueAI = ns.getInt("number");
        numberOfThreads = ns.getInt("threads");
        numberOfResultsKept = ns.getInt("kept");

        isTesting = ns.getBoolean("test");
        isVerbose = ns.getBoolean("verbose");

        List<String> AITypes = ns.getList("AITypes");
        String fileName = ns.getString("file");

        try {
            Scanner scanner = new Scanner(new File(fileName));
            generator = new GenerateAIFromFile(AITypes, scanner);
        } catch (FileNotFoundException e) {
            generator = new GenerateAIRandomly(AITypes);
        }
    }

    private static void generateAIScorings(List<BaseScoringValuesTestResults> results) {
        generator.generateAIScorings(results, numberOfUniqueAI);
    }

    private static void addToBestResults(List<BaseScoringValuesTestResults> results,
            List<BaseScoringValuesTestResults> bestResults) {
        for (int i = 0; i < numberOfResultsKept; ++i) {
            BaseScoringValuesTestResults result = results.get(i);
            result.reset();
            bestResults.add(result);
        }
    }

    private static void runAllTests(List<BaseScoringValuesTestResults> resultsToRun) {
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

    public static void printOutResult(List<BaseScoringValuesTestResults> results, String file) {
        Collections.sort(results);
        Collections.reverse(results);

        PrintStream printStream = System.out;

        try {
            printStream = new PrintStream(file, "UTF-8");
        } catch (Exception e) {
            // Will keep going to not waste the time spent
        }

        for (int i = 0; i < results.size(); ++i) {
            if (isVerbose) {
                printStream.print("At " + (i + 1) + ": ");
            }
            BaseScoringValuesTestResults result = results.get(i);
            result.printOut(printStream, isVerbose);
            
            printStream.println();
        }

        printStream.println();
        printTotalAITimes(printStream, results);
        printAIDepthInformation(printStream, results);

        if (printStream != System.out) {
            printStream.close();
        }
    }

    private static void printTotalAITimes(PrintStream printStream, List<BaseScoringValuesTestResults> results) {
        Map<String, List<Long>> timesMap = new HashMap<>();
        for (BaseScoringValuesTestResults result : results) {
            String aiIdentifier = result.getPlayer().getIdentifier();

            if (!timesMap.containsKey(aiIdentifier)) {
                timesMap.put(aiIdentifier, new ArrayList<Long>());
            }

            List<Long> allTimes = result.getAllTimes();
            for (Long time : allTimes) {
                timesMap.get(aiIdentifier).add(time);
            }
        }

        for (Map.Entry<String, List<Long>> entry : timesMap.entrySet()) {
            double average = TimeInfo.getAverageTime(entry.getValue());
            double stdDev = TimeInfo.getTimeStdDev(entry.getValue());
            printStream.println(entry.getKey() + ": " + average + ", std-dev " + stdDev);
        }
    }

    private static void printAIDepthInformation(PrintStream printStream, List<BaseScoringValuesTestResults> results) {
        int[] totalWinDepths = new int[MAX_NUM_MOVES + 1];
        int[] totalWinAsFirstDepths = new int[MAX_NUM_MOVES + 1];
        int[] totalWinAsSecondDepths = new int[MAX_NUM_MOVES + 1];

        Map<String, int[]> winDepthMap = new HashMap<>();
        Map<String, int[]> lossDepthMap = new HashMap<>();
        Map<String, int[]> winAsFirstMap = new HashMap<>();
        Map<String, int[]> winAsSecondMap = new HashMap<>();

        int lowestWin = MAX_NUM_MOVES + 1, highestWin = 0;

        for (BaseScoringValuesTestResults result : results) {
            String aiIdentifier = result.getPlayer().getIdentifier();

            if (!winDepthMap.containsKey(aiIdentifier)) {
                winDepthMap.put(aiIdentifier, new int[MAX_NUM_MOVES + 1]);
                lossDepthMap.put(aiIdentifier, new int[MAX_NUM_MOVES + 1]);
                winAsFirstMap.put(aiIdentifier, new int[MAX_NUM_MOVES + 1]);
                winAsSecondMap.put(aiIdentifier, new int[MAX_NUM_MOVES + 1]);
            }

            lowestWin = Math.min(lowestWin, findMin(result.getWinDepths()));
            highestWin = Math.max(highestWin, findMax(result.getWinDepths()));

            mergeInto(result.getWinDepths(), totalWinDepths);
            mergeInto(result.getWinAsFirstDepths(), totalWinAsFirstDepths);
            mergeInto(result.getWinAsSecondDepths(), totalWinAsSecondDepths);

            mergeInto(result.getWinDepths(), winDepthMap.get(aiIdentifier));
            mergeInto(result.getLossDepths(), lossDepthMap.get(aiIdentifier));
            mergeInto(result.getWinAsFirstDepths(), winAsFirstMap.get(aiIdentifier));
            mergeInto(result.getWinAsSecondDepths(), winAsSecondMap.get(aiIdentifier));
        }

        printStream.println("\nEarliest win: " + lowestWin + "\nLatest win: " + highestWin);

        printStream.println("\nWinDepth:");
        printOutDepthInformation(printStream, totalWinDepths, lowestWin, highestWin);
        iterateAndPrint(printStream, winDepthMap, lowestWin, highestWin);

        printStream.println("\nLossDepth:");
        iterateAndPrint(printStream, lossDepthMap, lowestWin, highestWin);

        printStream.println("\nFirstWin:");
        printOutDepthInformation(printStream, totalWinAsFirstDepths, lowestWin, highestWin);
        iterateAndPrint(printStream, winAsFirstMap, lowestWin, highestWin);

        printStream.println("\nSecondWin:");
        printOutDepthInformation(printStream, totalWinAsSecondDepths, lowestWin, highestWin);
        iterateAndPrint(printStream, winAsSecondMap, lowestWin, highestWin);
    }

    private static int findMax(int[] winDepths) {
        int i;

        for (i = MAX_NUM_MOVES; i >= 0 && winDepths[i] == 0; --i)
            ;

        return i;
    }

    private static int findMin(int[] winDepths) {
        int i;

        for (i = 0; i <= MAX_NUM_MOVES && winDepths[i] == 0; ++i)
            ;

        return i;
    }

    private static void iterateAndPrint(PrintStream printStream, Map<String, int[]> map, int lowestWin, int highestWin) {
        for (Map.Entry<String, int[]> entry : map.entrySet()) {
            printStream.print(entry.getKey() + ":\n");
            int[] depths = entry.getValue();

            printOutDepthInformation(printStream, depths, lowestWin, highestWin);
        }
    }

    private static void printOutDepthInformation(PrintStream printStream, int[] depths, int lowestWin, int highestWin) {
        for (int i = 0; i <= MAX_NUM_MOVES; ++i) {
            printStream.print(depths[i] + " ");
        }
        printStream.println();
    }

    private static void mergeInto(int[] from, int[] into) {
        for (int i = 0; i <= MAX_NUM_MOVES; ++i) {
            into[i] += from[i];
        }
    }
}
