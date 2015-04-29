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
package com.diusrex.tictactoe.textbased;

import java.util.Scanner;

import com.diusrex.tictactoe.data_structures.BoardStatus;
import com.diusrex.tictactoe.data_structures.BoxPosition;
import com.diusrex.tictactoe.data_structures.Move;
import com.diusrex.tictactoe.data_structures.SectionPosition;
import com.diusrex.tictactoe.logic.GeneralTicTacToeLogic;
import com.diusrex.tictactoe.logic.GridConstants;

public class HumanPlayer implements PlayerController {
    Scanner scanner;

    HumanPlayer(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public Move getPositionToPlay(BoardStatus board) {
        printOutBoard(board);
        
        System.out.println("If you want to cancel your input, use -1 for any integer value.\n\n");

        if (canPlayInAnySection(board)) {
            return getMoveInAnySection(board);
        } else {
            return getMoveInRequiredSection(board);
        }
    }

    private Move getMoveInRequiredSection(BoardStatus board) {
        SectionPosition requiredSection = board.getSectionToPlayIn();

        BoxPosition pos = null;

        while (pos == null) {
            System.out.println("You must play in the section at (" + requiredSection.getGridX() + ", "
                    + requiredSection.getGridY() + ")");

            pos = getWantedBoxPosition();
        }

        return new Move(requiredSection, pos, board.getNextPlayer());
    }

    private Move getMoveInAnySection(BoardStatus board) {
        SectionPosition section = null;
        BoxPosition pos = null;

        while (section == null || pos == null) {
            section = getWantedSection();
            pos = getWantedBoxPosition();
        }

        return new Move(section, pos, board.getNextPlayer());
    }

    private SectionPosition getWantedSection() {
        System.out.println("You can play in any section in the range [0, 2]");
        System.out.println("What section do you want to play in?");

        System.out.print("X: ");
        int sectionX = scanner.nextInt();
        System.out.print("Y: ");
        int sectionY = scanner.nextInt();
        
        return SectionPosition.make(sectionX, sectionY);
    }

    private BoxPosition getWantedBoxPosition() {
        System.out.println("The box you play in must be in the range [0, 2]");
        System.out.print("X: ");
        int x = scanner.nextInt();
        System.out.print("Y: ");
        int y = scanner.nextInt();

        return BoxPosition.make(x, y);
    }

    private boolean canPlayInAnySection(BoardStatus board) {
        return GeneralTicTacToeLogic.sectionIsFull(board, board.getSectionToPlayIn());
    }

    private void printOutBoard(BoardStatus board) {
        System.out.print("The board is:\n");
        
        for (int x = 0; x < GridConstants.NUMBER_OF_BOXES_PER_SIDE; ++x) {
            if (x % 3 == 0)
                System.out.print("  ");
            System.out.print(" " + x);
        }
        System.out.println("");

        for (int y = 0; y < GridConstants.NUMBER_OF_BOXES_PER_SIDE; ++y) {
            if (y % 3 == 0)
                printHorizontalLine();

            System.out.print(y);
            
            for (int x = 0; x < GridConstants.NUMBER_OF_BOXES_PER_SIDE; ++x) {
                if (x % 3 == 0)
                    System.out.print("| ");
                
                SectionPosition section = SectionPosition.make(x / GridConstants.SIZE_OF_SECTION, y / GridConstants.SIZE_OF_SECTION);
                BoxPosition pos = BoxPosition.make(x % GridConstants.SIZE_OF_SECTION, y % GridConstants.SIZE_OF_SECTION);
                
                System.out.print(board.getBoxOwner(section, pos) + " ");
            }
            
            System.out.println("|");
        }
        printHorizontalLine();
    }

    private void printHorizontalLine() {
        System.out.print(" ");
        for (int x = 0; x < GridConstants.NUMBER_OF_BOXES_PER_SIDE; ++x) {
            if (x % 3 == 0)
                System.out.print("+-");
            if (x + 1 != GridConstants.NUMBER_OF_BOXES_PER_SIDE)
                System.out.print("--");
            else
                System.out.print("--+");
        }
        System.out.println("");
    }

    @Override
    public void alertInvalidMove() {
        System.out.println("Unfortunately, that move was invalid");
    }

}
