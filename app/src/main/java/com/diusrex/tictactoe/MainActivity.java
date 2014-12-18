package com.diusrex.tictactoe;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

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

    MyGrid mainGrid;

    MainGridOwner requester;
    SectionOwner mainSection;

    long previousTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            loadBoardStatus(savedInstanceState);
        } else {
            board = new BoardStatus();
        }

        mainGrid = (MyGrid) findViewById(R.id.mainGrid);

        requester = new MainGridOwner(this, this, mainGrid);
    }

    private void loadBoardStatus(Bundle savedInstanceState) {
        String boardState = savedInstanceState.getString(SAVED_BOARD_STATE);
        board = TicTacToeEngine.loadBoardFromString(boardState);
    }

    @Override
    protected void onResume() {
        super.onResume();

        GridOrganizer.populateGrid(this, board, requester);

        currentPlayer = TicTacToeEngine.getNextPlayer(board);

        handleWinOrPrepareForNextMove(new BoxPosition(0, 0), getCurrentTime());
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

            requester.updateBoxValue(board, position);

            handleWinOrPrepareForNextMove(position, currentTime);
        }
    }

    private void handleWinOrPrepareForNextMove(BoxPosition position, long currentTime) {
        if (TicTacToeEngine.getWinner(board) == Player.Unowned) {
            prepareForNextMove(currentTime);
        } else {
            handleWin(position);
        }
    }

    private void prepareForNextMove(long currentTime) {
        currentPlayer = TicTacToeEngine.getNextPlayer(board);

        sectionSelected(board.getSectionToPlayIn());

        updateSectionToPlayIn();

        previousTime = currentTime;
    }

    private void updateSectionToPlayIn() {
        requester.selectionToPlayInChanged(board.getSectionToPlayIn());
    }

    private void handleWin(BoxPosition position) {
        sectionSelected(position.getSectionIn());

        // Don't let any moves be done
        currentPlayer = Player.Unowned;

        // There is no section to play into
        requester.selectionToPlayInChanged(new SectionPosition(-1, -1));

        requester.updateWinLine(board);
    }

    @Override
    public void sectionSelected(SectionPosition section) {
        populateSelectedSection(section);
        requester.selectionSelectedChanged(section);
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

            requester.updateBoxValue(board, lastMove.getPosition());
        }

    }

    private boolean canUndoLastMove() {
        return board.getAllMoves().size() != 0;
    }
}
