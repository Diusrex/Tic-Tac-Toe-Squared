package com.diusrex.tictactoe.box_images;

import com.diusrex.tictactoe.R;

public class LargeMove implements BoxImageResourceInfo {
    @Override
    public int getPlayerOneImage() {
        return R.drawable.o_pressed_large;
    }

    @Override
    public int getPlayerTwoImage() {
        return R.drawable.x_pressed_large;
    }

    @Override
    public int getUnownedImage() {
        return R.drawable.blank_large;
    }
}
