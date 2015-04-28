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
import android.widget.TextView;

import com.diusrex.tictactoe.R;
import com.diusrex.tictactoe.android.box_images.BoxImageResourceInfo;
import com.diusrex.tictactoe.android.box_images.LargeMove;
import com.diusrex.tictactoe.android.box_images.LargeMoveMostRecent;
import com.diusrex.tictactoe.android.box_images.MostRecentMove;
import com.diusrex.tictactoe.android.box_images.RegularMove;
import com.diusrex.tictactoe.data_structures.BoardStatus;
import com.diusrex.tictactoe.data_structures.Move;
import com.diusrex.tictactoe.data_structures.Player;
import com.diusrex.tictactoe.data_structures.SectionPosition;

public class GameGraphicsUpdater {
    private final MainGridOwner mainGridOwner;
    private final TextView playerInfo;

    private final BoxImageResourceInfo regularBox;
    private final BoxImageResourceInfo mostRecentBox;
    private final BoxImageResourceInfo largeBox;
    private final BoxImageResourceInfo largeBoxMostRecent;

    GameGraphicsUpdater(MainGridOwner mainGridOwner, TextView playerInfo) {
        this.mainGridOwner = mainGridOwner;
        this.playerInfo = playerInfo;
        playerInfo.setCompoundDrawablePadding(20);

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
            mainGridOwner.updateBoxValue(board, mostRecent.getSection(), mostRecent.getBox(), mostRecentBox);
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

        SectionPosition mostRecentMoveSection = mostRecentMove.getSection();
        if (mostRecentMoveSection.equals(section))
            mainSection.updateBoxValue(board, mostRecentMove.getBox(), largeBoxMostRecent);
    }

    public void noMovesMayBeMade() {
        playerInfo.setText(R.string.game_done);
        setPlayerTextImage(android.R.color.transparent);
    }

    public void playerChanged(Player currentPlayer, String playerAsString) {
        playerInfo.setText(playerAsString);

        switch (currentPlayer) {
        case Player_1:
            setPlayerTextImage(regularBox.getPlayerOneImage());
            break;

        case Player_2:
            setPlayerTextImage(regularBox.getPlayerTwoImage());
            break;

        case Unowned:
            setPlayerTextImage(regularBox.getUnownedImage());
            break;
        }
    }

    private void setPlayerTextImage(int imageValue) {
        // Want the image on the right side only
        playerInfo.setCompoundDrawablesWithIntrinsicBounds(0, 0, imageValue, 0);
    }
}
