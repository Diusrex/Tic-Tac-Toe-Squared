package com.diusrex.tictactoe.box_images;

import com.diusrex.tictactoe.R;

public class RegularMove implements BoxImageResourceInfo {

    @Override
    public int getPlayerOneImage() {
        return R.drawable.o_pressed;
    }

    @Override
    public int getPlayerTwoImage() {
        return R.drawable.x_pressed;
    }

    @Override
    public int getUnownedImage() {
        return R.drawable.blank;
    }
}
