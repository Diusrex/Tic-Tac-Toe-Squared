package com.diusrex.tictactoe.android.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.diusrex.tictactoe.R;

// Must be attached to an activity that extends the WinDialogListener
public class WinDialogFragment extends DialogFragment {
    static final String PLAYER = "player";

    String player;

    WinDialogActivityListener listener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        listener = (WinDialogActivityListener) activity;
    }

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
        builder = setUpView(builder);

        return builder.create();
    }

    private AlertDialog.Builder setUpView(AlertDialog.Builder builder) {
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();

        View inputInfo = layoutInflater.inflate(R.layout.dialog_win,
                null);

        setUpReturnToGameButton((Button) inputInfo.findViewById(R.id.returnToGameButton));
        setUpRematchButton((Button) inputInfo.findViewById(R.id.rematchButton));
        setUpReturnToMenuButton((Button) inputInfo.findViewById(R.id.returnToMenuButton));

        builder.setView(inputInfo);

        return builder;
    }

    private void setUpReturnToGameButton(Button returnToGameButton) {

        returnToGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.returnToGame();
                dismiss();
            }
        });
    }

    private void setUpRematchButton(Button rematchButton) {

        rematchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.runNewGame();
                dismiss();
            }
        });
    }

    private void setUpReturnToMenuButton(Button returnToMenuButton) {
        returnToMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.returnToMainMenu();
                dismiss();
            }
        });
    }

    private AlertDialog.Builder setUpOutput(AlertDialog.Builder builder) {
        builder.setTitle(R.string.win_dialog_title);

        String message = getString(R.string.congratulate_winner, player);
        builder.setMessage(message);

        return builder;
    }
}
