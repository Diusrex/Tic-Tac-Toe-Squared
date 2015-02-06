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

// Must be attached to an activity that extends the WinDialogListener
public class DrawDialogFragment extends GameEndDialogFragment {
    public static DrawDialogFragment newInstance() {
        return new DrawDialogFragment();
    }

    @Override
    protected void loadArguments(Bundle arguments) {
        // No need to load anything
    }

    @Override
    protected AlertDialog.Builder setUpOutput(AlertDialog.Builder builder) {
        builder.setTitle(R.string.draw_dialog_title);

        String message = getString(R.string.draw_output);
        builder.setMessage(message);

        return builder;
    }

    @Override
    protected int getStringIdForReturnToGameButton() {
        return R.string.resume_game;
    }

}

