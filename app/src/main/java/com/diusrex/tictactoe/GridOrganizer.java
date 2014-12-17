package com.diusrex.tictactoe;

import android.app.Activity;

import com.diusrex.tictactoe.logic.BoardStatus;

public class GridOrganizer {
    private GridOrganizer() {
    }

    public static void populateGrid(Activity activity, BoardStatus board, GridOwner gridOwner) {
        gridOwner.removeAllViews();
        for (int y = 0; y < BoardStatus.NUMBER_OF_SECTIONS_PER_SIDE; ++y) {
            for (int x = 0; x < BoardStatus.NUMBER_OF_SECTIONS_PER_SIDE; ++x) {
                gridOwner.addGridItem(activity, board, x, y);

                if (x != 2) {
                    addHorizontalSpace(activity, gridOwner);
                }
            }

            if (y != 2) {
                addVerticalSpace(activity, gridOwner);
            }
        }

        gridOwner.updateWinLine(board);
    }

    private static void addHorizontalSpace(Activity activity, GridOwner gridOwner) {
        gridOwner.addSingleHorizontalSpace(activity);
    }

    private static void addVerticalSpace(Activity activity, GridOwner gridOwner) {
        // This should be changed to not be hardcoded in
        for (int i = 0; i < 5; ++i) {
            gridOwner.addSingleVerticalSpace(activity);
        }
    }

}
