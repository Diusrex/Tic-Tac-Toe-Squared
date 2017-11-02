/*
 * Copyright 2015 Morgan Redshaw
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.diusrex.tictactoe.ai.tournament;

import com.diusrex.tictactoe.ai.ScalingAlphaBetaPlayer;
import com.diusrex.tictactoe.ai.ScalingMiniMaxPlayer;
import com.diusrex.tictactoe.ai.UnScalingAlphaBetaPlayer;
import com.diusrex.tictactoe.ai.UnScalingMiniMaxPlayer;
import com.diusrex.tictactoe.ai.tournament.test_results.BaseScoringValuesTestResults;
import com.diusrex.tictactoe.data_structures.Move;
import com.diusrex.tictactoe.logic.GridLists;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.*;

public class AITournament {
    public static final int MAX_NUM_MOVES = 81;
    private static int numberOfUniqueAI;
    private static int numberOfThreads;

    private static int numberOfResultsKept;

    private static boolean isTesting;

    private static String resultsFile;
    private static String verboseFile;

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

        List<BaseScoringValuesTestResults> bestResults = new ArrayList<BaseScoringValuesTestResults>();

        setUpStaticObjects();

        long totalStartTime = getCurrentTime();
        if (numberOfResultsKept != 0) {
            // Will run as many times as needed to ensure the final one is full
            for (int i = 0; i < numberOfUniqueAI / numberOfResultsKept; ++i) {
                List<BaseScoringValuesTestResults> results = new ArrayList<BaseScoringValuesTestResults>();
                generateAIScorings(results);

                long currentRunStart = getCurrentTime();

                if (!isTesting)
                    runAllTests(results);

                logFile.println("Completed run " + i + " after " + (getCurrentTime() - currentRunStart));

                System.out.println("Completed " + i);
                printOutResult(results, "Results_" + i + ".txt", null);
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

        printOutResult(bestResults, resultsFile, verboseFile);

        logFile.println("Completed all after " + (getCurrentTime() - totalStartTime));
    }

    private static void parseArguments(String[] args) {
        ArgumentParser parser = ArgumentParsers.newArgumentParser("AITournament").defaultHelp(true)
                .description("Run a tournament of AI");
        parser.addArgument("-n", "--number-AI").dest("number").type(Integer.class).setDefault(0)
                .help("Number of AI to create");
        parser.addArgument("-t", "--threads").type(Integer.class).setDefault(1).help("Specify number of threads");
        parser.addArgument("-k", "--number-AI-kept").dest("kept").type(Integer.class).setDefault(0)
                .help("Will cause a final round to be run, with given number of each earlier run");
        parser.addArgument("-f", "--file").help("File to load AI from");
        parser.addArgument("--test").type(Boolean.class).setDefault(false).action(Arguments.storeTrue())
                .help("See what will be created without running anything");
        parser.addArgument("AITypes")
                .nargs("+")
                .choices(UnScalingMiniMaxPlayer.IDENTIFIER, ScalingMiniMaxPlayer.IDENTIFIER,
                        UnScalingAlphaBetaPlayer.IDENTIFIER, ScalingAlphaBetaPlayer.IDENTIFIER).help("AITypes to use");
        parser.addArgument("-o", "--output_file").type(String.class).setDefault("Best_Results.txt")
                .help("File to write parameters of bots, in order of strength");
        parser.addArgument("-v", "--verbose").type(String.class).required(false)
                .help("File to save additional stats about the bots, optional");

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

        resultsFile = ns.getString("output_file");


        List<String> AITypes = ns.getList("AITypes");
        String fileName = ns.getString("file");
        if (fileName != null) {
            try {
                Scanner scanner = new Scanner(new File(fileName));
                generator = new GenerateAIFromFile(AITypes, scanner);
            } catch (FileNotFoundException e) {
                System.out.println(e);
                System.exit(1);
            }
        } else {
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

    // If the user doesn't want verbose output, have verboseFile be null
    public static void printOutResult(List<BaseScoringValuesTestResults> results, String resultsFile, String verboseFile) {
        Collections.sort(results);
        Collections.reverse(results);

        PrintStream resultsStream = System.out;
        PrintStream verboseStream = null;

        try {
            resultsStream = new PrintStream(resultsFile, "UTF-8");
        } catch (Exception e) {
            // Will keep going to not waste the time spent
        }

        for (int i = 0; i < results.size(); ++i) {
            BaseScoringValuesTestResults result = results.get(i);
            result.printOutCompact(resultsStream);
        }

        if (resultsStream != System.out) {
            resultsStream.close();
        }

        if (verboseFile != null) {
            try {
                verboseStream = new PrintStream(verboseFile, "UTF-8");
            } catch (Exception e) {
                // Will keep going to not waste the time spent
            }

            for (int i = 0; i < results.size(); ++i) {
                verboseStream.print("At " + (i + 1) + ": ");
                BaseScoringValuesTestResults result = results.get(i);
                result.printOutVerbose(verboseStream);

                verboseStream.println();
            }

            resultsStream.println();
            StandardPrintouts.printTotalAITimes(resultsStream, results);
            StandardPrintouts.printAIDepthInformation(resultsStream, results);
        }
    }

}
