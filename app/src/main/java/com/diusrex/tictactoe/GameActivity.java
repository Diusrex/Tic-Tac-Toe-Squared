package com.diusrex.tictactoe;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;

import com.diusrex.tictactoe.box_images.BoxImageResourceInfo;
import com.diusrex.tictactoe.box_images.LargeMove;
import com.diusrex.tictactoe.box_images.LargeMoveMostRecent;
import com.diusrex.tictactoe.box_images.MostRecentMove;
import com.diusrex.tictactoe.box_images.RegularMove;
import com.diusrex.tictactoe.dialogs.WinDialogFragment;
import com.diusrex.tictactoe.logic.BoardStatus;
import com.diusrex.tictactoe.logic.BoxPosition;
import com.diusrex.tictactoe.logic.Move;
import com.diusrex.tictactoe.logic.Player;
import com.diusrex.tictactoe.logic.SectionPosition;
import com.diusrex.tictactoe.logic.TicTacToeEngine;
import com.diusrex.tictactoe.logic.UndoAction;

import java.util.Calendar;

public class GameActivity extends Activity implements GameEventHandler {
    static public final String IS_NEW_GAME = "IsNewGame";
    static private final long COOLDOWN = 250;

    BoardStatus board;
    Player currentPlayer;

    BoardStateSaverAndLoader saverAndLoader;

    SectionPosition currentSelectedSection;

    MainGridOwner mainGridOwner;
    SectionOwner mainSection;

    BoxImageResourceInfo regularBox;
    BoxImageResourceInfo mostRecentBox;
    BoxImageResourceInfo largeBox;
    BoxImageResourceInfo largeBoxMostRecent;

    long previousTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        saverAndLoader = new BoardStateSaverAndLoader(this);

        regularBox = new RegularMove();
        mostRecentBox = new MostRecentMove();
        largeBox = new LargeMove();
        largeBoxMostRecent = new LargeMoveMostRecent();

        MyGrid mainGrid = (MyGrid) findViewById(R.id.mainGrid);

        mainGridOwner = new MainGridOwner(this, this, mainGrid);

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

        redrawBoard();

        SectionPosition selectedSection = saverAndLoader.loadSelectedSection(board);

        if (!winnerExists()) {
            prepareForNextMove(getCurrentTime(), selectedSection);
        } else {
            disablePerformingMove();
            sectionSelected(selectedSection);
        }
    }

    private void updateCurrentPlayer() {
        currentPlayer = TicTacToeEngine.getNextPlayer(board);
    }

    private void redrawBoard() {
        GridOrganizer.populateGrid(this, board, mainGridOwner, regularBox);
        updateMostRecentBox();
    }

    @Override
    protected void onPause() {
        super.onPause();

        saverAndLoader.saveGameState(board, currentSelectedSection);
    }

    @Override
    public void boxSelected(BoxPosition position) {
        Move move = new Move(position, currentPlayer);

        long currentTime = getCurrentTime();
        if (TicTacToeEngine.isValidMove(board, move) && currentTime - previousTime > COOLDOWN) {
            changeMostRecentMoveToRegular();

            TicTacToeEngine.applyMoveIfValid(board, move);

            mainGridOwner.updateBoxValue(board, position, mostRecentBox);

            handleWinOrPrepareForNextMove(position, currentTime);
        }
    }

    private void changeMostRecentMoveToRegular() {
        Move mostRecent = TicTacToeEngine.getMostRecentMoveOrNull(board);

        if (mostRecent != null) {
            mainGridOwner.updateBoxValue(board, mostRecent.getPosition(), regularBox);
        }
    }

    private void handleWinOrPrepareForNextMove(BoxPosition position, long currentTime) {
        if (winnerExists()) {
            handleWin(position);
        } else {
            prepareForNextMove(currentTime, board.getSectionToPlayIn());
        }
    }

    private boolean winnerExists() {
        return TicTacToeEngine.getWinner(board) != Player.Unowned;
    }

    private void prepareForNextMove(long currentTime, SectionPosition selectedSection) {
        updateCurrentPlayer();

        sectionSelected(selectedSection);

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
        mainGridOwner.selectionToPlayInChanged(SectionPosition.make(-1, -1));
    }

    private void showWinDialog(String winningPlayer) {
        DialogFragment fragment = WinDialogFragment.newInstance(winningPlayer);
        fragment.show(getFragmentManager(), "dialog");
    }

    @Override
    public void sectionSelected(SectionPosition section) {
        currentSelectedSection = section;
        populateSelectedSection(section);
        mainGridOwner.selectionSelectedChanged(section);
    }

    private void populateSelectedSection(SectionPosition section) {
        MyGrid selectedSectionGrid = (MyGrid) findViewById(R.id.selectedSection);
        mainSection = new SelectedSectionOwner(section, selectedSectionGrid, this);
        GridOrganizer.populateGrid(this, board, mainSection, largeBox);
        updateSelectedSectionMostRecent(section);
    }

    private void updateSelectedSectionMostRecent(SectionPosition section) {
        Move mostRecentMove = TicTacToeEngine.getMostRecentMoveOrNull(board);
        if (mostRecentMove == null)
            return;

        SectionPosition mostRecentMoveSection = mostRecentMove.getSectionIn();
        if (mostRecentMoveSection.equals(section)) {
            mainSection.updateBoxValue(board, mostRecentMove.getPosition(), largeBoxMostRecent);
        }
    }

    private long getCurrentTime() {
        Calendar c = Calendar.getInstance();
        return c.getTimeInMillis();
    }

    public void undoMove(View v) {
        if (canUndoLastMove()) {
            Move lastMove = board.getAllMoves().peek();

            UndoAction.undoLastMove(board);
            prepareForNextMove(getCurrentTime(), board.getSectionToPlayIn());

            mainGridOwner.updateBoxValue(board, lastMove.getPosition(), regularBox);
            mainGridOwner.updateWinLine(board);

            updateMostRecentBox();
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

    private void updateMostRecentBox() {
        Move mostRecent = TicTacToeEngine.getMostRecentMoveOrNull(board);

        if (mostRecent != null) {
            mainGridOwner.updateBoxValue(board, mostRecent.getPosition(), mostRecentBox);
        }
    }
}
