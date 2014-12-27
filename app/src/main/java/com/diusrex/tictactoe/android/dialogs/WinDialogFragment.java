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
}
