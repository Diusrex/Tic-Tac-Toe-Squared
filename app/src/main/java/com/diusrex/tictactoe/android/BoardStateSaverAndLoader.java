package com.diusrex.tictactoe.android;

import android.content.Context;
import android.content.SharedPreferences;

import com.diusrex.tictactoe.logic.BoardStatus;
import com.diusrex.tictactoe.logic.SectionPosition;
import com.diusrex.tictactoe.logic.TicTacToeEngine;

public class BoardStateSaverAndLoader {
    static private final String SAVED_BOARD_PREFERENCE_FILE = "PreferenceFile";
    static private final String SAVED_BOARD_STATE = "SavedBoardState";
    static private final String SAVED_SELECTED_SECTION = "SavedSelectedSection";

    SharedPreferences prefs;

    BoardStateSaverAndLoader(Context context) {
        prefs = context.getSharedPreferences(SAVED_BOARD_PREFERENCE_FILE, 0);
    }

    public boolean saveGameExists() {
        BoardStatus savedBoard = loadBoard();

        return !savedBoard.equals(new BoardStatus());
    }

    public void resetBoardState() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(SAVED_BOARD_STATE);
        editor.remove(SAVED_SELECTED_SECTION);
        editor.apply();
    }

    public BoardStatus loadBoard() {
        String boardString = prefs.getString(SAVED_BOARD_STATE, "");
        return TicTacToeEngine.loadBoardFromString(boardString);
    }

    public SectionPosition loadSelectedSection(BoardStatus board) {
        SectionPosition defaultSection = board.getSectionToPlayIn();
        String defaultSectionString = TicTacToeEngine.sectionPositionToString(defaultSection);

        // This defaultSectionString would only be used if there is no previously saved section
        String selectedSection = prefs.getString(SAVED_SELECTED_SECTION, defaultSectionString);
        return TicTacToeEngine.stringToSectionPosition(selectedSection);
    }

    public void selectedSectionChanged(SectionPosition selectedSection) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SAVED_SELECTED_SECTION, TicTacToeEngine.sectionPositionToString(selectedSection));
        editor.apply();
    }

    public void saveGameState(BoardStatus board) {
        String saveGameString = TicTacToeEngine.getSaveString(board);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SAVED_BOARD_STATE, saveGameString);
        editor.apply();
    }
}
