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

import java.io.PrintStream;

import com.diusrex.tictactoe.data_structures.BoardStatus;
import com.diusrex.tictactoe.data_structures.Move;
import com.diusrex.tictactoe.logic.GeneralTicTacToeLogic;
import com.diusrex.tictactoe.logic.StringSaver;

public abstract class AIPlayer {
    public Move getPositionToPlay(BoardStatus board) {
        String copyString = StringSaver.getSaveString(board);

        BoardStatus newBoard = new BoardStatus(board);
        return choosePosition(StringSaver.loadBoardFromString(newBoard, copyString));
    }

    protected boolean canPlayInAnySection(BoardStatus board) {
        return GeneralTicTacToeLogic.sectionIsFull(board, board.getSectionToPlayIn());
    }

    protected abstract Move choosePosition(BoardStatus board);

    public abstract String getIdentifier();
    
    // Should be learning from this, if it will do any learning
    public abstract void learnFromChange(BoardStatus board);
    
    // Should be used to reset any learning it was doing
    public abstract void newGame(BoardStatus board);
    
    // This should allow the bot type to be completely recreated, both the types inside it
    // and the weights used.
    public final void completelySavePlayer(PrintStream printStream) {
        savePlayerSpecification(printStream);
        saveParameters(printStream);
    }
    
    // This should allow the bot type to be completely recreated, albiet without
    // any weights
    public final void savePlayerSpecification(PrintStream printStream) {
        printStream.println(getIdentifier());
        saveInternalPlayerSpecification(printStream);
    }

    // This should allow the bot type to be completely recreated, other than the necessary weights.
    // Should NOT save the weights.
    protected abstract void saveInternalPlayerSpecification(PrintStream printStream);

    // This should allow the bot to be completely recreated given that we already knew
    // what type of bot it was - just need to know the numbers
    public abstract void saveParameters(PrintStream printStream);
}
