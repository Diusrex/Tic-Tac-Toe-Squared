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
package com.diusrex.tictactoe.android;

import android.content.Context;
import android.content.SharedPreferences;

import com.diusrex.tictactoe.android.players.AndroidAIPlayer;
import com.diusrex.tictactoe.android.players.AndroidPlayerController;
import com.diusrex.tictactoe.android.players.HumanAndroidPlayer;
import com.diusrex.tictactoe.data_structures.BoardStatus;
import com.diusrex.tictactoe.data_structures.Player;
import com.diusrex.tictactoe.data_structures.SectionPosition;
import com.diusrex.tictactoe.logic.BoardStatusFactory;
import com.diusrex.tictactoe.logic.PlayerFactory;
import com.diusrex.tictactoe.logic.StringSaver;

public class BoardStateSaverAndLoader {
    static private final String SAVED_BOARD_PREFERENCE_FILE = "PreferenceFile";
    static private final String SAVED_BOARD_STATE = "SavedBoardState";
    static private final String SAVED_SELECTED_SECTION = "SavedSelectedSection";
    static private final String SAVED_SECOND_PLAYER = "SavedSecondPlayer";

    SharedPreferences prefs;

    BoardStateSaverAndLoader(Context context) {
        prefs = context.getSharedPreferences(SAVED_BOARD_PREFERENCE_FILE, 0);
    }

    public boolean saveGameExists() {
        BoardStatus savedBoard = loadBoard(BoardStatusFactory.createStandardBoard());

        return savedBoard.getAllMoves().size() > 0;
    }

    public void resetBoardState() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(SAVED_BOARD_STATE);
        editor.remove(SAVED_SELECTED_SECTION);
        editor.apply();
    }

    public void saveGameState(BoardStatus board) {
        String saveGameString = StringSaver.getSaveString(board);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SAVED_BOARD_STATE, saveGameString);
        editor.apply();
    }

    public BoardStatus loadBoard(BoardStatus board) {
        String boardString = prefs.getString(SAVED_BOARD_STATE, "");
        return StringSaver.loadBoardFromString(board, boardString);
    }

    public SectionPosition loadSelectedSection(BoardStatus board) {
        SectionPosition defaultSection = board.getSectionToPlayIn();
        String defaultSectionString = defaultSection.toString();

        // This defaultSectionString would only be used if there is no previously saved section
        String selectedSection = prefs.getString(SAVED_SELECTED_SECTION, defaultSectionString);
        return SectionPosition.fromString(selectedSection);
    }

    public void selectedSectionChanged(SectionPosition selectedSection) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SAVED_SELECTED_SECTION, selectedSection.toString());
        editor.apply();
    }

    public void setSecondPlayer(PlayerFactory.WantedPlayer secondPlayer) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SAVED_SECOND_PLAYER, secondPlayer.toString());
        editor.apply();
    }

    public AndroidPlayerController loadSecondPlayer(SelectedSectionOwner selectedSectionOwner) {
        String secondPlayerStr = prefs.getString(SAVED_SECOND_PLAYER, PlayerFactory.WantedPlayer.Human.toString());
        PlayerFactory.WantedPlayer player = PlayerFactory.WantedPlayer.valueOf(secondPlayerStr);
        if (player == PlayerFactory.WantedPlayer.Human)
            return new HumanAndroidPlayer(selectedSectionOwner, Player.Player_2);

        return new AndroidAIPlayer(PlayerFactory.createAIPlayer(player));
    }


}
