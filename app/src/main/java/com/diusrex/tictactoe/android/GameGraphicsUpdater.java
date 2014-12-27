package com.diusrex.tictactoe.android;

import android.app.Activity;

import com.diusrex.tictactoe.box_images.BoxImageResourceInfo;
import com.diusrex.tictactoe.box_images.LargeMove;
import com.diusrex.tictactoe.box_images.LargeMoveMostRecent;
import com.diusrex.tictactoe.box_images.MostRecentMove;
import com.diusrex.tictactoe.box_images.RegularMove;
import com.diusrex.tictactoe.logic.BoardStatus;
import com.diusrex.tictactoe.logic.Move;
import com.diusrex.tictactoe.logic.SectionPosition;

public class GameGraphicsUpdater {
    private final MainGridOwner mainGridOwner;

    private final BoxImageResourceInfo regularBox;
    private final BoxImageResourceInfo mostRecentBox;
    private final BoxImageResourceInfo largeBox;
    private final BoxImageResourceInfo largeBoxMostRecent;

    GameGraphicsUpdater(MainGridOwner mainGridOwner) {
        this.mainGridOwner = mainGridOwner;

        regularBox = new RegularMove();
        mostRecentBox = new MostRecentMove();
        largeBox = new LargeMove();
        largeBoxMostRecent = new LargeMoveMostRecent();
    }

    public void redrawBoard(Activity activity, BoardStatus board) {
        GridOrganizer.populateGrid(activity, board, mainGridOwner, regularBox);
        updateMostRecentBox(board);
    }

    private void updateMostRecentBox(BoardStatus board) {
        Move mostRecent = getMostRecentMoveOrNull(board);

        if (mostRecent != null)
            mainGridOwner.updateBoxValue(board, mostRecent.getPosition(), mostRecentBox);
    }

    private static Move getMostRecentMoveOrNull(BoardStatus board) {
        if (board.getAllMoves().size() == 0)
            return null;

        return board.getAllMoves().peek();
    }

    public void sectionToPlayInChanged(SectionPosition sectionToPlayIn) {
        mainGridOwner.sectionToPlayInChanged(sectionToPlayIn);
    }

    public void updateWinLine(BoardStatus board) {
        mainGridOwner.updateWinLine(board);
    }

    public void selectedSectionChanged(Activity activity, BoardStatus board, SectionOwner mainSection, SectionPosition selectedSection) {
        GridOrganizer.populateGrid(activity, board, mainSection, largeBox);
        updateSelectedSectionMostRecent(board, mainSection, selectedSection);

        mainGridOwner.selectionSelectedChanged(selectedSection);
    }

    private void updateSelectedSectionMostRecent(BoardStatus board, SectionOwner mainSection, SectionPosition section) {
        Move mostRecentMove = getMostRecentMoveOrNull(board);
        if (mostRecentMove == null)
            return;

        SectionPosition mostRecentMoveSection = mostRecentMove.getSectionIn();
        if (mostRecentMoveSection.equals(section))
            mainSection.updateBoxValue(board, mostRecentMove.getPosition(), largeBoxMostRecent);
    }
}
