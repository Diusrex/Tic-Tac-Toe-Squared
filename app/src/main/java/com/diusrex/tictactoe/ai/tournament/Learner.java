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

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.diusrex.tictactoe.ai.AIPlayer;
import com.diusrex.tictactoe.ai.ScalingAlphaBetaPlayer;
import com.diusrex.tictactoe.ai.scoring_calculations.Scorer;
import com.diusrex.tictactoe.ai.scoring_calculations.learning.FunctionApproximator;
import com.diusrex.tictactoe.ai.scoring_calculations.learning.FunctionApproximatorScorer;
import com.diusrex.tictactoe.ai.scoring_calculations.learning.StandardSectionAndLineApproximator;
import com.diusrex.tictactoe.ai.scoring_calculations.learning.WeakApproximator;
import com.diusrex.tictactoe.ai.tournament.AIvsAI.GameInfo;
import com.diusrex.tictactoe.ai.tournament.recording.GameTimeRecording;
import com.diusrex.tictactoe.data_structures.Player;
import com.diusrex.tictactoe.logic.GridLists;
import com.diusrex.tictactoe.logic.PlayerFactory;

public class Learner {


    public static void main(String[] args) {
        final AIPlayer other = PlayerFactory.createAIPlayer(PlayerFactory.WantedPlayer.Easy);

        List<Thread> allThreads = new ArrayList<Thread>();
        
        allThreads.add(new Thread(new Runnable() {
            @Override
            public void run() {
                FunctionApproximator approximator = new StandardSectionAndLineApproximator(false);
                Scorer scorer = new FunctionApproximatorScorer.Builder(approximator, false).build();
                ScalingAlphaBetaPlayer player = new ScalingAlphaBetaPlayer(scorer);
                runLearner("lines only basic.txt", player, other, approximator);
            }
        }));
        
        allThreads.add(new Thread(new Runnable() {
            
            @Override
            public void run() {
                FunctionApproximator approximator = new StandardSectionAndLineApproximator(false);
                Scorer scorer = new FunctionApproximatorScorer.Builder(approximator, true).build();
                ScalingAlphaBetaPlayer player = new ScalingAlphaBetaPlayer(scorer);
                runLearner("lines only win increased.txt", player, other, approximator);
            }
        }));
        
        allThreads.add(new Thread(new Runnable() {
            
            @Override
            public void run() {
                FunctionApproximator approximator = new WeakApproximator();
                Scorer scorer = new FunctionApproximatorScorer.Builder(approximator, false).alpha(0.0075).shouldPrintout(true).build();
                ScalingAlphaBetaPlayer player = new ScalingAlphaBetaPlayer(scorer);
                runLearner("Weak minorly Higher alpha.txt", player, other, approximator);
            }
        }));
        
        allThreads.add(new Thread(new Runnable() {
            
            @Override
            public void run() {
                FunctionApproximator approximator = new StandardSectionAndLineApproximator(true);
                Scorer scorer = new FunctionApproximatorScorer.Builder(approximator, true).alpha(0.0005).build();
                ScalingAlphaBetaPlayer player = new ScalingAlphaBetaPlayer(scorer);
                runLearner("lines and sections low alpha.txt", player, other, approximator);
            }
        }));
        
        allThreads.add(new Thread(new Runnable() {
            
            @Override
            public void run() {
                FunctionApproximator approximator = new StandardSectionAndLineApproximator(true);
                Scorer scorer = new FunctionApproximatorScorer.Builder(approximator, true).alpha(0.0001).build();
                ScalingAlphaBetaPlayer player = new ScalingAlphaBetaPlayer(scorer);
                runLearner("lines and sections super low alpha.txt", player, other, approximator);
            }
        }));
        
        GridLists.initialize();
        
        try {
            for (int i = 0; i < allThreads.size(); ++i) {
                System.out.println("Starting " + i);
                allThreads.get(i).start();
            }
            for (int i = 0; i < allThreads.size(); ++i) {
                allThreads.get(i).join();
            }
        } catch (InterruptedException e) {
            // Can't really do anything about this...
            System.out.println("Got interrupted");
        }
    }

    private static void runLearner(String logFileName, ScalingAlphaBetaPlayer player, AIPlayer other, FunctionApproximator approximator) {
        PrintStream logFile = null;
        try {
            logFile = new PrintStream("Log/" + logFileName, "UTF-8");
        } catch (Exception e) {
            System.exit(1);
        }
        
        player.completelySavePlayer(logFile);
        
        int win = 0, tie = 0, loss = 0;
        int winDepth[] = new int[82];
        int drawDepth[] = new int[82];
        Random random = new Random(1);
        
        int numberTimesRecorded = 0;
        double previousParameterWeights[] = new double[approximator.numberElements()];
        int timesAboveZero[] = new int[approximator.numberElements()];
        int timesBelowZero[] = new int[approximator.numberElements()];
        double parameterSum[] = new double[approximator.numberElements()];
        double absoluteChangeSum[] = new double[approximator.numberElements()];
        double squaredChange[] = new double[approximator.numberElements()];
        
        int lastWin = 0, lastTie = 0, lastLoss = 0;
        
        long slowest = 0;
        final int numIterations = 100000;
        for(int i = 0; i < numIterations; ++i) {
            GameTimeRecording timeResults = new GameTimeRecording();
            Player ownPlayer;
            GameInfo info;
            if (random.nextBoolean()) {
                info = AIvsAI.runAndLearnGame(player, other, timeResults);
                ownPlayer = Player.Player_1;
                slowest = Math.max(slowest, timeResults.getPlayerOneTime());
            } else {
                info = AIvsAI.runAndLearnGame(other, player, timeResults);
                ownPlayer = Player.Player_2;
                slowest = Math.max(slowest, timeResults.getPlayerTwoTime());
            }
             
            if (info.getWinner() == Player.Unowned) {
                ++tie;
                drawDepth[info.getDepth()]++;
            } else if (info.getWinner() == ownPlayer) {
                ++win;
                winDepth[info.getDepth()]++;
            } else {
                ++loss;
            }

            //System.out.println(info.getWinner() + " " + timeResults.getPlayerOneTime() + " " + timeResults.getPlayerTwoTime() + ", depth: " + info.getDepth());
            //player.saveState(logFile);
            
            if (i % 100 == 0) {
                //System.out.println("Info: " + win + " " + tie + " " + loss);
                //logFile.println("Info: " + win + " " + tie + " " + loss);
                //player.saveState(logFile);
            }
            
            if (i % 500 == 0 || i + 1 == numIterations) {
                // More often record parameter changes
                ++numberTimesRecorded;
                double newWeights[] = approximator.getParametersCopy();
                for (int element = 0; element < approximator.numberElements(); ++element) {
                    if (newWeights[element] > 0) {
                        ++timesAboveZero[element];
                    } else if (newWeights[element] < 0) {
                        ++timesBelowZero[element];
                    }
                    
                    parameterSum[element] += newWeights[element];
                    
                    double difference = newWeights[element] - previousParameterWeights[element];
                    
                    absoluteChangeSum[element] += Math.abs(difference);
                    squaredChange[element] += difference * difference;
                }
                
                previousParameterWeights = newWeights;
            }
            
            if (i % 1000 == 0) {
                int total = win + tie + loss;
                logFile.println("Games: " + total);
                int currentWin = win - lastWin, currentTie = tie - lastTie, currentLoss = loss - lastLoss;
                int lastTotal = currentWin + currentTie + currentLoss;
                logFile.println("Last games: " + currentWin + " " + currentTie + " " + currentLoss +
                        ". Percents: win " + (100.0 * currentWin / lastTotal) + "% tie " + (100.0 * currentTie / lastTotal) + "%");
                logFile.println("Overall: " + win + " " + tie + " " + loss +
                        ". Percents: win " + (100.0 * win / total) + "% tie " + (100.0 * tie / total) + "%");
                logFile.println();
                //player.saveState(logFile);
                //System.out.println("Completed 500, slowest: " + slowest);
                
                lastWin = win;
                lastTie = tie;
                lastLoss = loss;
            }
            
            if (timeResults.getPlayerOneTime() >= 3000) {
                System.out.println("One was too slow: " + logFileName);
                logFile.println("\nStarted to go too slowly....");
                break;
            }
        }
        logFile.println("Win counts: ");
        for (int d = 30; d <= 81; ++d) {
            logFile.print(winDepth[d] + " ");
        }
        logFile.println("\nDraw counts: ");
        for (int d = 30; d <= 81; ++d) {
            logFile.print(drawDepth[d] + " ");
        }
        
        player.completelySavePlayer(logFile);
        
        // Print out parameter stats
        for (int i = 0; i < approximator.numberElements(); ++i) {
            logFile.println("Param: " + i + ":");
            logFile.println("    Above 0: " + timesAboveZero[i] + " below: " + timesBelowZero[i]);
            logFile.println("    Average: " + parameterSum[i] / numberTimesRecorded);
            logFile.println("    Average abs diff: " + absoluteChangeSum[i] / numberTimesRecorded);
            logFile.println("    MSChange: " + squaredChange[i] / numberTimesRecorded + " RMSChange: " + Math.sqrt(squaredChange[i]) / numberTimesRecorded);
        }
        
        logFile.println("Info: " + win + " " + tie + " " + loss);
    }

}
