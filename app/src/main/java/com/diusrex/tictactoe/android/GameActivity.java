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

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.diusrex.tictactoe.R;
import com.diusrex.tictactoe.android.dialogs.DrawDialogFragment;
import com.diusrex.tictactoe.android.dialogs.GameEndActivityListener;
import com.diusrex.tictactoe.android.dialogs.WinDialogFragment;
import com.diusrex.tictactoe.android.players.AndroidPlayerController;
import com.diusrex.tictactoe.android.players.HumanAndroidPlayer;
import com.diusrex.tictactoe.android.players.MoveListener;
import com.diusrex.tictactoe.data_structures.BoardStatus;
import com.diusrex.tictactoe.data_structures.Move;
import com.diusrex.tictactoe.data_structures.Player;
import com.diusrex.tictactoe.data_structures.SectionPosition;
import com.diusrex.tictactoe.logic.BoardStatusFactory;
import com.diusrex.tictactoe.logic.GeneralTicTacToeLogic;
import com.diusrex.tictactoe.logic.PlayerFactory;

import java.util.Calendar;

public class GameActivity extends Activity implements GameEventHandler, GameEndActivityListener, MoveListener {
    static public final String IS_NEW_GAME = "IsNewGame";
    static public final String SECOND_PLAYER_TYPE = "SecondPlayer";
    static private final String SHOW_GAME_IS_DRAW = "GameIsDraw";
    static public final long COOLDOWN = 250;

    private Button undoButton;

    private BoardStatus board;

    private BoardStateSaverAndLoader saverAndLoader;
    private int currentPlayerIndex;
    private AndroidPlayerController[] players = new AndroidPlayerController[2];

    private GameGraphicsUpdater graphicsUpdater;

    private long previousTime;
    private boolean shownGameIsDraw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        undoButton = (Button) findViewById(R.id.undoButton);

        shownGameIsDraw = savedInstanceState != null && savedInstanceState.getBoolean(SHOW_GAME_IS_DRAW);

        saverAndLoader = new BoardStateSaverAndLoader(this);

        MainGridOwner mainGridOwner = new MainGridOwner(this, this, (MyGridLayout) findViewById(R.id.mainGrid));
        TextView playerInfo = (TextView) findViewById(R.id.player_info);
        graphicsUpdater = new GameGraphicsUpdater(new SelectedSectionOwner(this), mainGridOwner, playerInfo);

        boolean newGame = getIntent().getBooleanExtra(IS_NEW_GAME, true);
        if (newGame) {
            PlayerFactory.WantedPlayer secondPlayer = (PlayerFactory.WantedPlayer) getIntent().getSerializableExtra(SECOND_PLAYER_TYPE);
            saverAndLoader.resetBoardState();

            if (secondPlayer == null) {
                secondPlayer = PlayerFactory.WantedPlayer.Human;
            }
            saverAndLoader.setSecondPlayer(secondPlayer);

            shownGameIsDraw = false;

            // Make sure it does not try this again
            getIntent().putExtra(IS_NEW_GAME, false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        board = saverAndLoader.loadBoard(BoardStatusFactory.createStandardBoard());

        players[0] = new HumanAndroidPlayer(graphicsUpdater.getSelectedSectionOwner(), Player.Player_1);
        players[1] = saverAndLoader.loadSecondPlayer(graphicsUpdater.getSelectedSectionOwner());

        graphicsUpdater.redrawBoard(this, board);

        resumeGame();
    }

    private void resumeGame() {
        SectionPosition selectedSection = saverAndLoader.loadSelectedSection(board);

        if (gameStillRunning()) {
            prepareForNextMove(getCurrentTime(), selectedSection);
        } else {
            disablePerformingMove();
            sectionSelected(selectedSection);
        }

        undoButton.setEnabled(board.ableToUndoLastMove());
    }

    private boolean gameStillRunning() {
        return !winnerExists() && !boardIsFull();
    }

    private boolean boardIsFull() {
        return GeneralTicTacToeLogic.boardIsFull(board);
    }

    private void updateCurrentPlayer() {
        updatePlayerIndex();
        graphicsUpdater.playerChanged(board.getNextPlayer(), getPlayerAsString());
        players[currentPlayerIndex].promptForMove(board, this);
    }

    private void updatePlayerIndex() {
        if (board.getNextPlayer() == Player.Player_1) {
            currentPlayerIndex = 0;
        } else {
            currentPlayerIndex = 1;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        saverAndLoader.saveGameState(board);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(SHOW_GAME_IS_DRAW, shownGameIsDraw);
    }

    @Override
    public void moveChosen(final Move move) {
        final long currentTime = getCurrentTime();
        if (isValidTiming(currentTime) && board.isValidMove(move)) {

            // Ensure that there will not be any problems
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateBoardWithMove(currentTime, move);
                }
            });
        }
    }

    public void updateBoardWithMove(long currentTime, Move move) {
        board.applyMoveIfValid(move);
        graphicsUpdater.redrawBoard(this, board);

        handleWinOrPrepareForNextMove(move.getSection(), currentTime);
        undoButton.setEnabled(board.ableToUndoLastMove());
    }

    private boolean isValidTiming(long currentTime) {
        return currentTime - previousTime > COOLDOWN;
    }

    private void handleWinOrPrepareForNextMove(SectionPosition sectionPosition, long currentTime) {
        if (isADraw() && !shownGameIsDraw)
            showDrawDialog();

        if (winnerExists()) {
            sectionSelected(sectionPosition);
            handleWin();
        } else if (boardIsFull()) {
            sectionSelected(sectionPosition);
            showDrawDialog();
        } else {
            prepareForNextMove(currentTime, board.getSectionToPlayIn());
        }
    }

    private boolean isADraw() {
        return !board.possibleToWin();
    }

    private boolean winnerExists() {
        return board.getWinner() != Player.Unowned;
    }

    private void prepareForNextMove(long currentTime, SectionPosition selectedSection) {
        sectionSelected(selectedSection);

        updateCurrentPlayer();

        graphicsUpdater.sectionToPlayInChanged(board.getSectionToPlayIn());

        previousTime = currentTime;
    }

    private void handleWin() {
        graphicsUpdater.updateWinLine(board);

        String winningPlayer = getPlayerAsString();
        disablePerformingMove();

        showWinDialog(winningPlayer);
    }

    private void disablePerformingMove() {
        // There is no section to play into
        graphicsUpdater.sectionToPlayInChanged(SectionPosition.make(-1, -1));
        graphicsUpdater.noMovesMayBeMade();
    }

    private void showWinDialog(String winningPlayer) {
        DialogFragment fragment = WinDialogFragment.newInstance(winningPlayer);
        fragment.show(getFragmentManager(), "dialog");
    }

    private void showDrawDialog() {
        DialogFragment fragment = DrawDialogFragment.newInstance();
        fragment.show(getFragmentManager(), "dialog");

        shownGameIsDraw = true;
    }

    @Override
    public void sectionSelected(SectionPosition section) {
        saverAndLoader.selectedSectionChanged(section);
        updateGraphicalSelectedSection(section);
    }

    private void updateGraphicalSelectedSection(SectionPosition section) {
        SectionOwner mainSection = new SectionOwner(section, (MyGridLayout) findViewById(R.id.selectedSection), SectionOwner.GRID_LINE_WIDTH_LARGE);
        graphicsUpdater.selectedSectionChanged(this, board, mainSection, section);
    }

    public static long getCurrentTime() {
        Calendar c = Calendar.getInstance();
        return c.getTimeInMillis();
    }

    public void undoMove(View v) {
        if (canUndoLastMove()) {
            players[currentPlayerIndex].undoWasPressed();
            board.undoLastMove();

            updatePlayerIndex();

            if (!players[currentPlayerIndex].mayUndo()) {
                board.undoLastMove();
            }

            graphicsUpdater.redrawBoard(this, board);
            prepareForNextMove(getCurrentTime(), board.getSectionToPlayIn());

            ensureDrawUpdated();
            undoButton.setEnabled(board.ableToUndoLastMove());
        }
    }

    private void ensureDrawUpdated() {
        if (shownGameIsDraw && !isADraw()) {
            shownGameIsDraw = false;
        }
    }

    private String getPlayerAsString() {
        switch (board.getNextPlayer()) {
        case Player_1:
            return getString(R.string.current_player_is_1);

        case Player_2:
            return getString(R.string.current_player_is_2);

        default:
            return getString(R.string.no_player);
        }
    }

    private boolean canUndoLastMove() {
        return board.getAllMoves().size() != 0;
    }

    @Override
    public void returnToMainMenu() {
        // Need to reset these because they are what will be saved
        board = BoardStatusFactory.createStandardBoard();
        saverAndLoader.selectedSectionChanged(board.getSectionToPlayIn());
        finish();
    }

    @Override
    public void returnToGame() {
        // Nothing needs to be done here
    }

    @Override
    public void runNewGame() {
        board = BoardStatusFactory.createStandardBoard();
        graphicsUpdater.redrawBoard(this, board);

        SectionPosition selectedSection = board.getSectionToPlayIn();

        prepareForNextMove(getCurrentTime(), selectedSection);
    }
}
