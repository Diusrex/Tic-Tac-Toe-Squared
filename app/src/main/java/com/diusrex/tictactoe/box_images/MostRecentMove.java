package com.diusrex.tictactoe.box_images;

import com.diusrex.tictactoe.R;

public class MostRecentMove implements BoxImageResourceInfo {

    @Override
    public int getPlayerOneImage() {
        return R.drawable.o_pressed_most_recent;
    }

    @Override
    public int getPlayerTwoImage() {
        return R.drawable.x_pressed_most_recent;
    }

    @Override
    public int getUnownedImage() {
        return R.drawable.blank;
    }
}
