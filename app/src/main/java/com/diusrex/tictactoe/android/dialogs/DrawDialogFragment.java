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

