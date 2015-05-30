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
    private static int numberOfUniqueAI;
    private static int numberOfThreads;

    private static int numberOfResultsKept;

    private static TournamentAIGenerator generator;

    private static Thread[] allThreads;

    static public void main(String[] args) {
        parseArguments(args);

        allThreads = new Thread[numberOfThreads];

        List<BaseScoringValuesTestResults> bestResults = new ArrayList<>();

        setUpStaticObjects();

        long totalStartTime = getCurrentTime();
        if (numberOfResultsKept != 0) {
            // Will run as many times as needed to ensure the final one is full
            for (int i = 0; i < numberOfUniqueAI / numberOfResultsKept; ++i) {
                List<BaseScoringValuesTestResults> results = new ArrayList<>();
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

    private static void parseArguments(String[] args) {
        ArgumentParser parser = ArgumentParsers.newArgumentParser("AITournament").defaultHelp(true)
                .description("Run a tournament of AI");
        parser.addArgument("-n", "--number-AI").dest("number").type(Integer.class).setDefault(0)
                .help("Number of AI to create");
        parser.addArgument("-t", "--threads").type(Integer.class).setDefault(0).help("Specify number of threads");
        parser.addArgument("-k", "--number-AI-kept").dest("kept").type(Integer.class).setDefault(0)
                .help("Will cause a final round to be run, with given number of each earlier run");
        parser.addArgument("-f", "--file").setDefault("").help("File to load AI from");
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
            // Don't really care about the exception, will keep going to not
            // waste the time spent
        }

        for (int i = 0; i < results.size(); ++i) {
            BaseScoringValuesTestResults result = results.get(i);
            result.printOut(printStream);
        }
        printStream.println();
        printTotalAITimes(printStream, results);

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
}
