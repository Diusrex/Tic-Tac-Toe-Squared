package com.diusrex.tictactoe.android;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;

import com.diusrex.tictactoe.R;
import com.diusrex.tictactoe.android.dialogs.DrawDialogFragment;
import com.diusrex.tictactoe.android.dialogs.GameEndActivityListener;
import com.diusrex.tictactoe.android.dialogs.WinDialogFragment;
import com.diusrex.tictactoe.logic.BoardStatus;
import com.diusrex.tictactoe.logic.BoxPosition;
import com.diusrex.tictactoe.logic.Move;
import com.diusrex.tictactoe.logic.Player;
import com.diusrex.tictactoe.logic.SectionPosition;
import com.diusrex.tictactoe.logic.TicTacToeEngine;
import com.diusrex.tictactoe.logic.UndoAction;

import java.util.Calendar;

public class GameActivity extends Activity implements GameEventHandler, GameEndActivityListener {
    static public final String IS_NEW_GAME = "IsNewGame";
    static private final long COOLDOWN = 250;

    BoardStatus board;
    Player currentPlayer;

    BoardStateSaverAndLoader saverAndLoader;

    GameGraphicsUpdater graphicsUpdater;

    long previousTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        saverAndLoader = new BoardStateSaverAndLoader(this);

        MainGridOwner mainGridOwner = new MainGridOwner(this, this, (MyGrid) findViewById(R.id.mainGrid));
        graphicsUpdater = new GameGraphicsUpdater(mainGridOwner);

        boolean newGame = getIntent().getBooleanExtra(IS_NEW_GAME, true);
        if (newGame) {
            saverAndLoader.resetBoardState();

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

        SectionPosition selectedSection = saverAndLoader.loadSelectedSection(board);

        // ToDo: Refactor this if else into a function
        if (gameStillRunning()) {
            prepareForNextMove(getCurrentTime(), selectedSection);
        } else {
            // Make it so no player can make a move
            disablePerformingMove();
            sectionSelected(selectedSection);
        }
    }

    private boolean gameStillRunning() {
        return !winnerExists() && !isADraw();
    }

    private void updateCurrentPlayer() {
        currentPlayer = TicTacToeEngine.getNextPlayer(board);
    }

    @Override
    protected void onPause() {
        super.onPause();

        saverAndLoader.saveGameState(board);
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
        if (winnerExists()) {
            handleWin(position);
        } else if (isADraw()) {
            handleDraw(position);
        } else {
            prepareForNextMove(currentTime, board.getSectionToPlayIn());
        }
    }

    private boolean isADraw() {
        return TicTacToeEngine.boardIsFull(board);
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

    private void handleDraw(BoxPosition position) {
        sectionSelected(position.getSectionIn());
        disablePerformingMove();

        showDrawDialog();
    }

    private void disablePerformingMove() {
        // Will not allow the player unowned to play
        currentPlayer = Player.Unowned;

        // There is no section to play into
        graphicsUpdater.sectionToPlayInChanged(SectionPosition.make(-1, -1));
    }

    private void showWinDialog(String winningPlayer) {
        DialogFragment fragment = WinDialogFragment.newInstance(winningPlayer);
        fragment.show(getFragmentManager(), "dialog");
    }

    private void showDrawDialog() {
        DialogFragment fragment = DrawDialogFragment.newInstance();
        fragment.show(getFragmentManager(), "dialog");
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
        }
    }

    private String getPlayerAsString() {
        switch (currentPlayer) {
        case Player_1:
            return getString(R.string.player_1);

        case Player_2:
            return getString(R.string.player_2);

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
