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

package com.diusrex.tictactoe.ai;

import com.diusrex.tictactoe.data_structures.Move;
import com.diusrex.tictactoe.data_structures.Player;

import java.util.List;
import java.util.Random;

/*
 *  Currently, am trying to reduce the amount of memory used by this.
 *  Will need to determine the number of nodes that will be created by the end of the game
 *  Handles storing information about the board status
 *  Much of the code is from http://mcts.ai/code/java.html
 *  TODO: Some of this class MUST be synchronized
 */
public class FullMonteCarloPlayerStateNode {
    private Player playerAtNode;
    private Move move;
    private FullMonteCarloPlayerStateNode[] allSubnodes = null;
    private static final double EPSILON = 1e-6;
    private double numVisits, numWins, winPercent;

    FullMonteCarloPlayerStateNode(Player playerAtNode, Move move) {
        this.playerAtNode = playerAtNode;
        this.move = move;
        numVisits = 0;
        numWins = 0;
        winPercent = 0;
    }

    public Move getMove() {
        return move;
    }

    public void expand(List<Move> allMoves) {
        allSubnodes = new FullMonteCarloPlayerStateNode[allMoves.size()];
        for (int i = 0; i < allMoves.size(); i++) {
            Move currentMove = allMoves.get(i);
            allSubnodes[i] = new FullMonteCarloPlayerStateNode(currentMove.getPlayer(), currentMove);
        }
    }

    public boolean isLeaf() {
        return allSubnodes == null;
    }

    public FullMonteCarloPlayerStateNode select(Random random) {
        if (allSubnodes.length == 1) {
            return allSubnodes[0];
        }

        FullMonteCarloPlayerStateNode selected = null;
        double bestValue = Double.MIN_VALUE;

        for (FullMonteCarloPlayerStateNode c : allSubnodes) {
            double uctValue = getNodeScore(random, c);

            if (uctValue > bestValue) {
                selected = c;
                bestValue = uctValue;
            }
        }

        return selected;
    }

    private double getNodeScore(Random random, FullMonteCarloPlayerStateNode node) {
        // TODO: better to keep 'Math.log(numVisits +1)' inside of loop, or outside?
        return node.winPercent + Math.sqrt(Math.log(numVisits + 1) / (node.numVisits + EPSILON)) +
                random.nextDouble() * EPSILON; // Add small random number to break ties
    }

    public Move getBestMove() {
        Move bestMove = null;
        double bestValue = Double.MIN_VALUE;

        for (FullMonteCarloPlayerStateNode c : allSubnodes) {
            double percent = c.winPercent;
            if (percent > bestValue) {
                bestValue = percent;
                bestMove = c.move;
            }
        }

        return bestMove;
    }


    public void updateStats(Player winner) {
        ++numVisits;
        numWins += (winner == playerAtNode) ? 1 : 0;
        winPercent = numWins / numVisits;
    }
}
