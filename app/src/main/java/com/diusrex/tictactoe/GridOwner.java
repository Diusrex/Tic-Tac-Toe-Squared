package com.diusrex.tictactoe;

import android.app.Activity;

import com.diusrex.tictactoe.logic.BoardStatus;

public interface GridOwner {
    public void updateWinLine(BoardStatus board);

    public void removeAllViews();

    void addGridItem(Activity activity, BoardStatus board, int x, int y);

    void addSingleHorizontalSpace(Activity activity);

    void addSingleVerticalSpace(Activity activity);
}
