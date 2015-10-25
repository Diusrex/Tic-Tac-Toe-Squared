package com.diusrex.tictactoe.ai.tournament;

import com.diusrex.tictactoe.ai.tournament.test_results.BaseScoringValuesTestResults;
import com.diusrex.tictactoe.ai.tournament.test_results.PlayerTimeResults;
import com.diusrex.tictactoe.logic.TicTacToeEngine;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StandardPrintouts {

    public static void printAIDepthInformation(PrintStream printStream, List<BaseScoringValuesTestResults> results) {
        int[] totalWinDepths = new int[TicTacToeEngine.MAX_NUM_MOVES + 1];
        int[] totalWinAsFirstDepths = new int[TicTacToeEngine.MAX_NUM_MOVES + 1];
        int[] totalWinAsSecondDepths = new int[TicTacToeEngine.MAX_NUM_MOVES + 1];

        Map<String, int[]> winDepthMap = new HashMap<String, int[]>();
        Map<String, int[]> lossDepthMap = new HashMap<String, int[]>();
        Map<String, int[]> winAsFirstMap = new HashMap<String, int[]>();
        Map<String, int[]> winAsSecondMap = new HashMap<String, int[]>();

        int lowestWin = TicTacToeEngine.MAX_NUM_MOVES + 1, highestWin = 0;

        for (BaseScoringValuesTestResults result : results) {
            String aiIdentifier = result.getPlayer().getIdentifier();

            if (!winDepthMap.containsKey(aiIdentifier)) {
                winDepthMap.put(aiIdentifier, new int[TicTacToeEngine.MAX_NUM_MOVES + 1]);
                lossDepthMap.put(aiIdentifier, new int[TicTacToeEngine.MAX_NUM_MOVES + 1]);
                winAsFirstMap.put(aiIdentifier, new int[TicTacToeEngine.MAX_NUM_MOVES + 1]);
                winAsSecondMap.put(aiIdentifier, new int[TicTacToeEngine.MAX_NUM_MOVES + 1]);
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

    public static void printTotalAITimes(PrintStream printStream, List<BaseScoringValuesTestResults> results) {
        Map<String, List<Long>> timesMap = new HashMap<String, List<Long>>();
        for (BaseScoringValuesTestResults result : results) {
            String aiIdentifier = result.getPlayer().getIdentifier();

            if (!timesMap.containsKey(aiIdentifier)) {
                timesMap.put(aiIdentifier, new ArrayList<Long>());
            }

            List<Long> allTimes = result.getTimeResults();
            for (Long time : allTimes) {
                timesMap.get(aiIdentifier).add(time);
            }
        }

        for (Map.Entry<String, List<Long>> entry : timesMap.entrySet()) {
            double average = PlayerTimeResults.getAverageTime(entry.getValue());
            double stdDev = PlayerTimeResults.getTimeStdDev(entry.getValue());
            printStream.println(entry.getKey() + ": " + average + ", std-dev " + stdDev);
        }
    }

    private static int findMax(int[] winDepths) {
        int i = TicTacToeEngine.MAX_NUM_MOVES;
        while (i >= 0 && winDepths[i] == 0) {
            --i;
        }

        return i;
    }

    private static int findMin(int[] winDepths) {
        int i = 0;
        while (i <= TicTacToeEngine.MAX_NUM_MOVES && winDepths[i] == 0) {
            ++i;
        }

        return i;
    }

    private static void mergeInto(int[] from, int[] into) {
        for (int i = 0; i <= TicTacToeEngine.MAX_NUM_MOVES; ++i) {
            into[i] += from[i];
        }
    }

    private static void iterateAndPrint(PrintStream printStream, Map<String, int[]> map, int lowestWin, int highestWin) {
        for (Map.Entry<String, int[]> entry : map.entrySet()) {
            printStream.print(entry.getKey() + ":\n");
            int[] depths = entry.getValue();

            printOutDepthInformation(printStream, depths, lowestWin, highestWin);
        }
    }

    private static void printOutDepthInformation(PrintStream printStream, int[] depths, int lowestWin, int highestWin) {
        for (int i = 0; i <= TicTacToeEngine.MAX_NUM_MOVES; ++i) {
            printStream.print(depths[i] + " ");
        }
        printStream.println();
    }
}
