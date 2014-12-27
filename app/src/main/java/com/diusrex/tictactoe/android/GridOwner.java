package com.diusrex.tictactoe.android;

import android.app.Activity;

import com.diusrex.tictactoe.box_images.BoxImageResourceInfo;
import com.diusrex.tictactoe.logic.BoardStatus;

public interface GridOwner {
    public void updateWinLine(BoardStatus board);

    public void removeAllViews();

    void addGridItem(Activity activity, BoardStatus board, int x, int y, BoxImageResourceInfo boxImageType);

    void addSingleHorizontalSpace(Activity activity);

    void addSingleVerticalSpace(Activity activity);
}
