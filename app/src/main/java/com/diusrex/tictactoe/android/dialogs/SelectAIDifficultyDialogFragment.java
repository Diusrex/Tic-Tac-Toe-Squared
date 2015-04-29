/*
 * Copyright 2015 Morgan Redshaw
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.diusrex.tictactoe.android.dialogs;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.diusrex.tictactoe.R;
import com.diusrex.tictactoe.logic.PlayerFactory;

public class SelectAIDifficultyDialogFragment extends DialogFragment {
    private SelectAIDifficultyListener listener;

    public static SelectAIDifficultyDialogFragment newInstance() {
        return new SelectAIDifficultyDialogFragment();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        listener = (SelectAIDifficultyListener) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inputInfo = inflater.inflate(R.layout.select_ai_difficulty_dialog,
                null);

        setUpEasyAIButton((Button) inputInfo.findViewById(R.id.easy_ai));
        setUpMediumAIButton((Button) inputInfo.findViewById(R.id.medium_ai));
        setUpHardAIButton((Button) inputInfo.findViewById(R.id.hard_ai));

        getDialog().setTitle(getResources().getString(R.string.choose_ai_difficulty));

        return inputInfo;
    }

    private void setUpEasyAIButton(Button returnToGameButton) {
        returnToGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.difficultySelected(PlayerFactory.WantedPlayer.Easy);
                dismiss();
            }
        });
    }

    private void setUpMediumAIButton(Button rematchButton) {
        rematchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.difficultySelected(PlayerFactory.WantedPlayer.Medium);
                dismiss();
            }
        });
    }

    private void setUpHardAIButton(Button returnToMenuButton) {
        returnToMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.difficultySelected(PlayerFactory.WantedPlayer.Hard);
                dismiss();
            }
        });
    }


}
