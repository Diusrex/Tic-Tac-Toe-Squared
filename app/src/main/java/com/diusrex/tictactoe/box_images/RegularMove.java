package com.diusrex.tictactoe.box_images;

import com.diusrex.tictactoe.R;

public class RegularMove implements BoxImageResourceInfo {

    @Override
    public int getPlayerOneImage() {
        return R.drawable.game_o;
    }

    @Override
    public int getPlayerTwoImage() {
        return R.drawable.game_x;
    }

    @Override
    public int getUnownedImage() {
        return R.drawable.game_blank;
    }
}
