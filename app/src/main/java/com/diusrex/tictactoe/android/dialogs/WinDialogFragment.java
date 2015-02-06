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
package com.diusrex.tictactoe.android.dialogs;

import android.app.AlertDialog;
import android.os.Bundle;

import com.diusrex.tictactoe.R;

// Must be attached to an activity that extends the GameEndActivityListener
public class WinDialogFragment extends GameEndDialogFragment {
    static final String PLAYER = "player";

    String player;

    public static WinDialogFragment newInstance(String player) {
        WinDialogFragment f = new WinDialogFragment();

        Bundle args = new Bundle();

        args.putString(PLAYER, player);

        f.setArguments(args);

        return f;
    }

    @Override
    protected void loadArguments(Bundle arguments) {
        player = arguments.getString(PLAYER);
    }

    protected AlertDialog.Builder setUpOutput(AlertDialog.Builder builder) {
        builder.setTitle(R.string.win_dialog_title);

        String message = getString(R.string.congratulate_winner, player);
        builder.setMessage(message);

        return builder;
    }

    @Override
    protected int getStringIdForReturnToGameButton() {
        return R.string.back_to_game;
    }
}
