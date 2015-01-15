package com.diusrex.tictactoe.box_images;

import com.diusrex.tictactoe.R;

public class LargeMoveMostRecent implements BoxImageResourceInfo {
    @Override
    public int getPlayerOneImage() {
        return R.drawable.game_large_o_recent;
    }

    @Override
    public int getPlayerTwoImage() {
        return R.drawable.game_large_x_recent;
    }

    @Override
    public int getUnownedImage() {
        return R.drawable.game_large_blank;
    }
}
