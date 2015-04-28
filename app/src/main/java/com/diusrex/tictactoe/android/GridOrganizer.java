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
package com.diusrex.tictactoe.android;

import android.app.Activity;

import com.diusrex.tictactoe.R;
import com.diusrex.tictactoe.android.box_images.BoxImageResourceInfo;
import com.diusrex.tictactoe.data_structures.BoardStatus;
import com.diusrex.tictactoe.logic.GridConstants;

public class GridOrganizer {
    private static final int SIZE_OF_GRID = GridConstants.NUMBER_OF_SECTIONS_PER_SIDE;
    private static final int LAST_ELEMENT_POS_IN_GRID = SIZE_OF_GRID - 1;

    private GridOrganizer() {
    }

    public static void populateGrid(Activity activity, BoardStatus board, GridOwner gridOwner, BoxImageResourceInfo boxImageType) {
        gridOwner.removeAllViews();
        for (int y = 0; y < SIZE_OF_GRID; ++y) {
            for (int x = 0; x < SIZE_OF_GRID; ++x) {
                gridOwner.addGridItem(activity, board, x, y, boxImageType);

                if (x != LAST_ELEMENT_POS_IN_GRID) {
                    addHorizontalSpace(activity, gridOwner);
                }
            }

            if (y != LAST_ELEMENT_POS_IN_GRID) {
                addVerticalSpace(activity, gridOwner);
            }
        }

        gridOwner.updateWinLine(board);
    }

    private static void addHorizontalSpace(Activity activity, GridOwner gridOwner) {
        gridOwner.addSingleHorizontalSpace(activity);
    }

    private static void addVerticalSpace(Activity activity, GridOwner gridOwner) {
        int boardSize = activity.getResources().getInteger(R.integer.game_board_size);
        for (int i = 0; i < boardSize; ++i) {
            gridOwner.addSingleVerticalSpace(activity);
        }
    }

}
