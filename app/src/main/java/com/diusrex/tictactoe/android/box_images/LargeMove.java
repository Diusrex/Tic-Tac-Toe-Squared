/*
 * Copyright 2015 Morgan Redshaw
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 **/
package com.diusrex.tictactoe.android.box_images;

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
