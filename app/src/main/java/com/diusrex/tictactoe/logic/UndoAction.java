/*
 * Copyright 2015 Morgan Redshaw
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 **/
package com.diusrex.tictactoe.logic;

import java.util.Stack;

import com.diusrex.tictactoe.data_structures.BoardStatus;
import com.diusrex.tictactoe.data_structures.Move;
import com.diusrex.tictactoe.data_structures.Player;
import com.diusrex.tictactoe.data_structures.SectionGrid;
import com.diusrex.tictactoe.data_structures.SectionPosition;


public class UndoAction {
    public static boolean moveLostOwnership(SectionGrid undoneMovesSection, Move topMove) {
        // In this case, it is impossible
        if (topMove.getPlayer() != undoneMovesSection.getGridOwner())
            return false;

        Player completedWinner = GridChecker.searchForOwner(undoneMovesSection);

        // If there is no found match, then the grid was lost
        return completedWinner == Player.Unowned;
    }

    public static SectionPosition getSectionToPlayIn(Stack<Move> allMoves, Move undoneTopMove) {
        // If > 0, can get where to play from previous move
        if (allMoves.size() > 0) {
            Move previousMove = allMoves.peek();

            return TicTacToeEngine.getSectionToPlayInNext(previousMove);
        } else {
            return undoneTopMove.getSection();
        }
    }
}
