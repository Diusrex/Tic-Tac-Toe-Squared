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

package com.diusrex.tictactoe.data_structures.board_status;

import com.diusrex.tictactoe.data_structures.Player;
import com.diusrex.tictactoe.data_structures.position.BoxPosition;
import com.diusrex.tictactoe.data_structures.position.SectionPosition;
import com.diusrex.tictactoe.logic.GridLists;
import com.diusrex.tictactoe.logic.StandardTicTacToeEngine;

public class BoardStatusFactory {
    private BoardStatusFactory() {
    }

    public static BoardStatus createStandardBoard() {
        return new BoardStatus(new StandardTicTacToeEngine());
    }

    public final static int numberExpectedSubgrids = 9;

    // By giving the grid as {{ 0, 1, 2, 3, 4, 5, 6, 7, 8},
    // { 9, 10, 11, 11, 12, 13, 14, 15, 16},
    // {17, 18, 19, 20, 21, 22, 23, 24, 25},
    // ...(total of 9 rows!)
    // You can specify the players who own each point, when viewing the grid
    // 9 by 9 grid, with each arrays being a single row.
    // The grid does NOT need to be valid, in that one player could have far
    // more moves than the others.
    // Will update section winners + overall grid winner if applicable.
    // Note that there is NO GUARANTEE for the winner of a section if both players could win.
    // If you need a guarantee, and a check to make sure owners are correct, add the argument
    // sectionOwners.
    public static BoardStatus createSpecificStandardBoard(Player nextPlayer, SectionPosition nextSection,
            Player[][] pointOwners) {
        // TODO: Better way to specify
        // TODO: Change to exception raising
        assert (pointOwners.length == 9);
        for (int i = 0; i < pointOwners.length; ++i) {
            assert (pointOwners[i].length == 9);
        }
        BoardStatus board = createStandardBoard();

        for (SectionPosition section : GridLists.getAllStandardSections()) {
            // It shouldn't matter which player gets points first in the
            // section.
            updatePointOwners(board, section, pointOwners, Player.Player_1);
            updatePointOwners(board, section, pointOwners, Player.Player_2);
        }

        board.nextPlayer = nextPlayer;
        board.sectionToPlayIn = nextSection;

        return board;
    }

    // Same comments from createSpecificStandardBoard, but you can specify the
    // player who should win each section.
    // NOTE: THE PLAYER MUST HAVE 3 IN A ROW in that section.
    public static BoardStatus createSpecificStandardBoard(Player nextPlayer, SectionPosition nextSection,
            Player[][] pointOwners, Player[][] sectionOwners) {
        // TODO: Better way to specify
        if (pointOwners.length != 9) {
            throw new IllegalArgumentException("Must give point owners as 9 subarrays (rows).");
        }
        for (int i = 0; i < pointOwners.length; ++i) {
            if (pointOwners[i].length != 9) {
                throw new IllegalArgumentException("Each subarray (row) in pointOwners must have 9 elements");
            }
        }

        BoardStatus board = createStandardBoard();

        for (SectionPosition section : GridLists.getAllStandardSections()) {
            // It shouldn't matter which player gets points first in the section.
            Player wantedOwner = sectionOwners[section.getGridY()][section.getGridX()];
            if (wantedOwner == Player.Player_1 || wantedOwner == Player.Unowned) {
                updatePointOwners(board, section, pointOwners, Player.Player_1);
                updatePointOwners(board, section, pointOwners, Player.Player_2);
            } else {
                updatePointOwners(board, section, pointOwners, Player.Player_2);
                updatePointOwners(board, section, pointOwners, Player.Player_1);
            }
            if (wantedOwner != board.getSectionOwner(section)) {
                throw new IllegalArgumentException("Must be able to set the section to the wanted owner");
            }
        }

        board.nextPlayer = nextPlayer;
        board.sectionToPlayIn = nextSection;

        return board;
    }

    private static void updatePointOwners(BoardStatus board, SectionPosition section, Player[][] pointOwners,
            Player playerToUpdate) {
        // TODO: Better way to specify
        int baseX = section.getGridX() * 3;
        int baseY = section.getGridY() * 3;

        for (BoxPosition box : GridLists.getAllStandardBoxPositions()) {
            int x = baseX + box.getGridX();
            int y = baseY + box.getGridY();

            if (pointOwners[y][x] == playerToUpdate) {
                board.setBoxOwner(section, box, playerToUpdate);
            }
        }
    }
}