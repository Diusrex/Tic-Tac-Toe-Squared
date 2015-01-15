package com.diusrex.tictactoe.box_images;

import com.diusrex.tictactoe.R;

public class LargeMove implements BoxImageResourceInfo {
    @Override
    public int getPlayerOneImage() {
        return R.drawable.game_large_o;
    }

    @Override
    public int getPlayerTwoImage() {
        return R.drawable.game_large_x;
    }

    @Override
    public int getUnownedImage() {
        return R.drawable.game_large_blank;
    }
}
