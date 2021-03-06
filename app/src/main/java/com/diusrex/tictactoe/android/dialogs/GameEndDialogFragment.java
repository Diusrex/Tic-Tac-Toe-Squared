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

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.diusrex.tictactoe.R;

// Must be attached to an activity that extends the GameEndActivityListener
public abstract class GameEndDialogFragment extends DialogFragment {
    private GameEndActivityListener listener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        listener = (GameEndActivityListener) activity;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        loadArguments(getArguments());
        builder = setUpOutput(builder);
        builder = setUpView(builder);

        return builder.create();
    }

    protected abstract void loadArguments(Bundle arguments);
    protected abstract AlertDialog.Builder setUpOutput(AlertDialog.Builder builder);

    public AlertDialog.Builder setUpView(AlertDialog.Builder builder) {
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();

        View inputInfo = layoutInflater.inflate(R.layout.game_end_dialog,
                null);

        setUpReturnToGameButton((Button) inputInfo.findViewById(R.id.returnToGameButton));
        setUpRematchButton((Button) inputInfo.findViewById(R.id.rematchButton));
        setUpReturnToMenuButton((Button) inputInfo.findViewById(R.id.returnToMenuButton));

        builder.setView(inputInfo);

        return builder;
    }

    private void setUpReturnToGameButton(Button returnToGameButton) {
        returnToGameButton.setText(getStringIdForReturnToGameButton());
        returnToGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.returnToGame();
                dismiss();
            }
        });
    }

    protected abstract int getStringIdForReturnToGameButton();

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


}
