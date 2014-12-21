package com.diusrex.tictactoe.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.diusrex.tictactoe.R;

/**
 * Created by Diusrex on 2014-12-17.
 */
public class WinDialogFragment extends DialogFragment {
    static final String PLAYER = "player";

    String player;

    public static WinDialogFragment newInstance(String player) {
        WinDialogFragment f = new WinDialogFragment();

        Bundle args = new Bundle();

        args.putString(PLAYER, player);

        f.setArguments(args);

        return f;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        player = getArguments().getString(PLAYER);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder = setUpOutput(builder);
        builder = setUpButtons(builder);

        return builder.create();
    }

    private AlertDialog.Builder setUpOutput(AlertDialog.Builder builder) {
        builder.setTitle(R.string.win_dialog_title);

        String message = getString(R.string.congratulate_winner, player);
        builder.setMessage(message);

        return builder;
    }

    AlertDialog.Builder setUpButtons(AlertDialog.Builder builder) {
        builder.setNegativeButton(getString(R.string.back_to_menu), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.setNeutralButton(R.string.return_to_game, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.setPositiveButton(getString(R.string.rematch), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        return builder;
    }
}
