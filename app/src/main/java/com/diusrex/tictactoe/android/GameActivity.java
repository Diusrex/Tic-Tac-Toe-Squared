package com.diusrex.tictactoe.android;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.diusrex.tictactoe.R;
import com.diusrex.tictactoe.android.dialogs.DrawDialogFragment;
import com.diusrex.tictactoe.android.dialogs.GameEndActivityListener;
import com.diusrex.tictactoe.android.dialogs.WinDialogFragment;
import com.diusrex.tictactoe.logic.BoardStatus;
import com.diusrex.tictactoe.logic.BoxPosition;
import com.diusrex.tictactoe.logic.Move;
import com.diusrex.tictactoe.logic.Player;
import com.diusrex.tictactoe.logic.PossibleToWinChecker;
import com.diusrex.tictactoe.logic.SectionPosition;
import com.diusrex.tictactoe.logic.TicTacToeEngine;
import com.diusrex.tictactoe.logic.UndoAction;

import java.util.Calendar;

public class GameActivity extends Activity implements GameEventHandler, GameEndActivityListener {
    static public final String IS_NEW_GAME = "IsNewGame";
    static private final String SHOW_GAME_IS_DRAW = "GameIsDraw";
    static private final long COOLDOWN = 250;

    private BoardStatus board;
    private Player currentPlayer;

    private BoardStateSaverAndLoader saverAndLoader;

    private GameGraphicsUpdater graphicsUpdater;

    private long previousTime;
    private boolean shownGameIsDraw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        if (savedInstanceState != null) {
            shownGameIsDraw = savedInstanceState.getBoolean(SHOW_GAME_IS_DRAW);
        } else {
            shownGameIsDraw = false;
        }

        saverAndLoader = new BoardStateSaverAndLoader(this);

        MainGridOwner mainGridOwner = new MainGridOwner(this, this, (MyGrid) findViewById(R.id.mainGrid));
        TextView playerInfo = (TextView) findViewById(R.id.player_info);
        graphicsUpdater = new GameGraphicsUpdater(mainGridOwner, playerInfo);

        boolean newGame = getIntent().getBooleanExtra(IS_NEW_GAME, true);
        if (newGame) {
            saverAndLoader.resetBoardState();
            shownGameIsDraw = false;

            // Make sure it does not try this again
            getIntent().putExtra(IS_NEW_GAME, false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        board = saverAndLoader.loadBoard();
        updateCurrentPlayer();

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
    }

    private boolean gameStillRunning() {
        return !winnerExists() && !boardIsFull();
    }

    private boolean boardIsFull() {
        return TicTacToeEngine.boardIsFull(board);
    }

    private void updateCurrentPlayer() {
        currentPlayer = TicTacToeEngine.getNextPlayer(board);
        graphicsUpdater.playerChanged(currentPlayer, getPlayerAsString());
    }

    @Override
    protected void onPause() {
        super.onPause();

        saverAndLoader.saveGameState(board);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(SHOW_GAME_IS_DRAW, shownGameIsDraw);
    }

    @Override
    public void boxSelected(BoxPosition position) {
        Move move = new Move(position, currentPlayer);

        long currentTime = getCurrentTime();
        if (TicTacToeEngine.isValidMove(board, move) && currentTime - previousTime > COOLDOWN) {
            TicTacToeEngine.applyMoveIfValid(board, move);
            graphicsUpdater.redrawBoard(this, board);

            handleWinOrPrepareForNextMove(position, currentTime);
        }
    }

    private void handleWinOrPrepareForNextMove(BoxPosition position, long currentTime) {
        if (isADraw() && !shownGameIsDraw)
            showDrawDialog();

        if (winnerExists()) {
            handleWin(position);
        } else if (boardIsFull()){
            sectionSelected(position.getSectionIn());
            showDrawDialog();
        } else {
            prepareForNextMove(currentTime, board.getSectionToPlayIn());
        }
    }

    private boolean isADraw() {
        return !PossibleToWinChecker.isStillPossibleToWin(board);
    }

    private boolean winnerExists() {
        return TicTacToeEngine.getWinner(board) != Player.Unowned;
    }

    private void prepareForNextMove(long currentTime, SectionPosition selectedSection) {
        updateCurrentPlayer();

        sectionSelected(selectedSection);

        graphicsUpdater.sectionToPlayInChanged(board.getSectionToPlayIn());

        previousTime = currentTime;
    }

    private void handleWin(BoxPosition position) {
        graphicsUpdater.updateWinLine(board);

        sectionSelected(position.getSectionIn());

        String winningPlayer = getPlayerAsString();
        disablePerformingMove();

        showWinDialog(winningPlayer);
    }

    private void disablePerformingMove() {
        // Will not allow the player unowned to play
        currentPlayer = Player.Unowned;

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
        SectionOwner mainSection = new SelectedSectionOwner(section, (MyGrid) findViewById(R.id.selectedSection), this);
        graphicsUpdater.selectedSectionChanged(this, board, mainSection, section);
    }

    private long getCurrentTime() {
        Calendar c = Calendar.getInstance();
        return c.getTimeInMillis();
    }

    public void undoMove(View v) {
        if (canUndoLastMove()) {
            UndoAction.undoLastMove(board);

            graphicsUpdater.redrawBoard(this, board);
            prepareForNextMove(getCurrentTime(), board.getSectionToPlayIn());

            ensureDrawUpdated();
        }
    }

    private void ensureDrawUpdated() {
        if (shownGameIsDraw && !isADraw()) {
            shownGameIsDraw = false;
        }
    }

    private String getPlayerAsString() {
        switch (currentPlayer) {
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
        board = new BoardStatus();
        saverAndLoader.selectedSectionChanged(board.getSectionToPlayIn());
        finish();
    }

    @Override
    public void returnToGame() {
        // Nothing needs to be done here
    }

    @Override
    public void runNewGame() {
        board = new BoardStatus();
        graphicsUpdater.redrawBoard(this, board);

        SectionPosition selectedSection = board.getSectionToPlayIn();

        prepareForNextMove(getCurrentTime(), selectedSection);
    }
}
