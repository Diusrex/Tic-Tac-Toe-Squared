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

import com.diusrex.tictactoe.logic.BoardStatus;
import com.diusrex.tictactoe.logic.BoxPosition;
import com.diusrex.tictactoe.logic.Player;

import java.util.Scanner;

public class HumanPlayer implements PlayerController {
    Player whoIs;
    Scanner scanner;

    HumanPlayer(Player whoIs, Scanner scanner) {
        this.whoIs = whoIs;
        this.scanner = scanner;
    }

    @Override
    public BoxPosition getPositionToPlay(BoardStatus board) {
        printOutInformation(board);

        System.out.print("What x position do you want to play in? ");
        int x = scanner.nextInt();

        System.out.print("What y position do you want to play in? ");
        int y = scanner.nextInt();

        return BoxPosition.make(x, y);
    }

    private void printOutInformation(BoardStatus board) {
        System.out.print("The board is:\n");

        for (int x = 0; x < BoardStatus.NUMBER_OF_BOXES_PER_SIDE; ++x) {
            if (x % 3 == 0)
                System.out.print("  ");
            System.out.print(" " + x);
        }
        System.out.print("\n");
        
        for (int y = 0; y < BoardStatus.NUMBER_OF_BOXES_PER_SIDE; ++y) {
            if (y % 3 == 0)
                printHorizontalLine();
            
            System.out.print(y);
            for (int x = 0; x < BoardStatus.NUMBER_OF_BOXES_PER_SIDE; ++x) {
                if (x % 3 == 0)
                    System.out.print("| ");
                System.out.print(board.getBoxOwner(x, y) + " ");
            }
            System.out.println("|");
        }
        printHorizontalLine();
        
        BoxPosition validSpot = board.getSectionToPlayIn().getTopLeftPosition();
        System.out.println("Valid x range: (" + validSpot.getX() + ", " + (validSpot.getX() + 2) + ")");
        System.out.println("Valid y range: (" + validSpot.getY() + ", " + (validSpot.getY() + 2) + ")");
    }

    private void printHorizontalLine() {
        System.out.print(" ");
        for (int x = 0; x < BoardStatus.NUMBER_OF_BOXES_PER_SIDE; ++x) {
            if (x % 3 == 0)
                System.out.print("+-");
            if (x + 1 != BoardStatus.NUMBER_OF_BOXES_PER_SIDE)
                System.out.print("--");
            else
                System.out.print("--+");
        }
        System.out.println("");
    }

    @Override
    public Player getWhichPlayer() {
        return whoIs;
    }

    @Override
    public void alertInvalidMove() {
        System.out.println("Unfortunately, that move was invalid");
    }

}
