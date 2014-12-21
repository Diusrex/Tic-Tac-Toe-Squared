package com.diusrex.tictactoe;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;

import com.diusrex.tictactoe.dialogs.WinDialogFragment;
import com.diusrex.tictactoe.logic.BoardStatus;
import com.diusrex.tictactoe.logic.BoxPosition;
import com.diusrex.tictactoe.logic.Move;
import com.diusrex.tictactoe.logic.Player;
import com.diusrex.tictactoe.logic.SectionPosition;
import com.diusrex.tictactoe.logic.TicTacToeEngine;
import com.diusrex.tictactoe.logic.UndoAction;

import java.util.Calendar;

public class MainActivity extends Activity implements GameEventHandler {
    static final String SAVED_BOARD_STATE = "Saved_Board_State";
    static final long COOLDOWN = 250;

    BoardStatus board;
    Player currentPlayer;

    MainGridOwner mainGridOwner;
    SectionOwner mainSection;

    long previousTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        restoreBoard(savedInstanceState);

        MyGrid mainGrid = (MyGrid) findViewById(R.id.mainGrid);

        mainGridOwner = new MainGridOwner(this, this, mainGrid);
    }

    private void restoreBoard(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            loadBoardStatus(savedInstanceState);
        } else {
            board = new BoardStatus();
        }
    }

    private void loadBoardStatus(Bundle savedInstanceState) {
        String boardState = savedInstanceState.getString(SAVED_BOARD_STATE);
        board = TicTacToeEngine.loadBoardFromString(boardState);
    }

    @Override
    protected void onResume() {
        super.onResume();

        GridOrganizer.populateGrid(this, board, mainGridOwner);

        currentPlayer = TicTacToeEngine.getNextPlayer(board);

        if (!winnerExists()) {
            prepareForNextMove(getCurrentTime());
        } else {
            disablePerformingMove();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(SAVED_BOARD_STATE, TicTacToeEngine.getSaveString(board));
    }

    @Override
    public void boxSelected(BoxPosition position) {
        Move move = new Move(position, currentPlayer);

        long currentTime = getCurrentTime();
        if (TicTacToeEngine.isValidMove(board, move) && currentTime - previousTime > COOLDOWN) {
            TicTacToeEngine.applyMoveIfValid(board, move);

            mainGridOwner.updateBoxValue(board, position);

            handleWinOrPrepareForNextMove(position, currentTime);
        }
    }

    private void handleWinOrPrepareForNextMove(BoxPosition position, long currentTime) {
        if (winnerExists()) {
            handleWin(position);
        } else {
            prepareForNextMove(currentTime);
        }
    }

    private boolean winnerExists() {
        return TicTacToeEngine.getWinner(board) != Player.Unowned;
    }

    private void prepareForNextMove(long currentTime) {
        currentPlayer = TicTacToeEngine.getNextPlayer(board);

        sectionSelected(board.getSectionToPlayIn());

        updateSectionToPlayIn();

        previousTime = currentTime;
    }

    private void updateSectionToPlayIn() {
        mainGridOwner.selectionToPlayInChanged(board.getSectionToPlayIn());
    }

    private void handleWin(BoxPosition position) {
        mainGridOwner.updateWinLine(board);

        sectionSelected(position.getSectionIn());

        String winningPlayer = getPlayerAsString();
        disablePerformingMove();

        showWinDialog(winningPlayer);
    }

    private void disablePerformingMove() {
        // Will not allow the player unowned to play
        currentPlayer = Player.Unowned;

        // There is no section to play into
        mainGridOwner.selectionToPlayInChanged(new SectionPosition(-1, -1));
    }

    private void showWinDialog(String winningPlayer) {
        DialogFragment fragment = WinDialogFragment.newInstance(winningPlayer);
        fragment.show(getFragmentManager(), "dialog");
    }

    @Override
    public void sectionSelected(SectionPosition section) {
        populateSelectedSection(section);
        mainGridOwner.selectionSelectedChanged(section);
    }

    private void populateSelectedSection(SectionPosition section) {
        MyGrid selectedSectionGrid = (MyGrid) findViewById(R.id.selectedSection);
        mainSection = new SelectedSectionOwner(section, selectedSectionGrid, this);
        GridOrganizer.populateGrid(this, board, mainSection);
    }

    private long getCurrentTime() {
        Calendar c = Calendar.getInstance();
        return c.getTimeInMillis();
    }

    public void undoMove(View v) {
        if (canUndoLastMove()) {
            Move lastMove = board.getAllMoves().peek();

            UndoAction.undoLastMove(board);
            prepareForNextMove(getCurrentTime());

            mainGridOwner.updateBoxValue(board, lastMove.getPosition());
            mainGridOwner.updateWinLine(board);
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
}
