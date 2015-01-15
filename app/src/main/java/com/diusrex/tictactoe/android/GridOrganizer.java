package com.diusrex.tictactoe.android;

import android.app.Activity;

import com.diusrex.tictactoe.R;
import com.diusrex.tictactoe.box_images.BoxImageResourceInfo;
import com.diusrex.tictactoe.logic.BoardStatus;

public class GridOrganizer {
    private static final int SIZE_OF_GRID = BoardStatus.NUMBER_OF_SECTIONS_PER_SIDE;
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
