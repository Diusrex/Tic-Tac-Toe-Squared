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
import android.widget.ImageView;
import android.widget.Space;

import com.diusrex.tictactoe.R;
import com.diusrex.tictactoe.box_images.BoxImageResourceInfo;
import com.diusrex.tictactoe.data_structures.BoardStatus;
import com.diusrex.tictactoe.data_structures.BoxPosition;
import com.diusrex.tictactoe.data_structures.Line;
import com.diusrex.tictactoe.data_structures.Player;
import com.diusrex.tictactoe.data_structures.Position;
import com.diusrex.tictactoe.data_structures.SectionPosition;
import com.diusrex.tictactoe.logic.GridConstants;

public class SectionOwner implements GridOwner {
    static private final int GRID_LINE_WIDTH = 15;
    static private final int SIZE_OF_SPACE = 10;

    private final SectionPosition sectionPosition;
    private final MyGridLayout gridLayout;

    private final ImageView allBoxes[][];

    SectionOwner(SectionPosition sectionPosition, MyGridLayout gridLayout) {
        this.sectionPosition = sectionPosition;
        this.gridLayout = gridLayout;
        this.allBoxes = new ImageView[GridConstants.SIZE_OF_SECTION][GridConstants.SIZE_OF_SECTION];

        setUpGridLines(this.gridLayout);
    }

    private void setUpGridLines(MyGridLayout grid) {
        grid.setLineWidth(getLineWidth());
        grid.setLineColor(grid.getResources().getColor(R.color.section_win_line));
    }

    protected int getLineWidth() {
        return GRID_LINE_WIDTH;
    }

    @Override
    public void removeAllViews() {
        gridLayout.removeAllViews();
    }

    @Override
    public void addGridItem(Activity activity, BoardStatus board, int x, int y, BoxImageResourceInfo boxImageType) {
        BoxPosition pos = BoxPosition.make(x, y);
        allBoxes[pos.getGridX()][pos.getGridY()] = generateBox(board.getBoxOwner(sectionPosition, pos), activity, boxImageType);

        setUpSpecialInformation(allBoxes[x][y], pos);

        gridLayout.addView(allBoxes[x][y]);
    }

    protected void setUpSpecialInformation(ImageView imageView, BoxPosition posInBoard) {
    }

    protected ImageView generateBox(Player owner, Activity activity, BoxImageResourceInfo boxImageType) {
        ImageView image = new ImageView(activity);

        setBoxImage(owner, image, boxImageType);

        return image;
    }

    @Override
    public void addSingleHorizontalSpace(Activity activity) {
        Space space = new Space(activity);
        space.setMinimumWidth(SIZE_OF_SPACE);
        gridLayout.addView(space);
    }

    @Override
    public void addSingleVerticalSpace(Activity activity) {
        Space space = new Space(activity);
        space.setMinimumHeight(SIZE_OF_SPACE);
        gridLayout.addView(space);
    }

    @Override
    public void updateWinLine(BoardStatus board) {
        Line winLine = board.getSectionWinLine(sectionPosition);
        if (shouldDrawWinLine(winLine)) {
            drawWinLine(winLine);
        } else if (shouldRemoveWinLineFromGrid(winLine)) {
            gridLayout.removeLine();
        }
    }

    private boolean shouldDrawWinLine(Line winLine) {
        return winLine != null;
    }

    private boolean shouldRemoveWinLineFromGrid(Line winLine) {
        return winLine == null && gridLayout.hasLine();
    }

    private void drawWinLine(Line winLine) {
        gridLayout.setLine(getBox(winLine.getStart()), getBox(winLine.getEnd()));
    }

    public void updateBoxValue(BoardStatus board, BoxPosition positionInBoard, BoxImageResourceInfo boxImageType) {
        setBoxImage(board.getBoxOwner(sectionPosition, positionInBoard), getBox(positionInBoard), boxImageType);

        updateWinLine(board);
    }

    protected void setBoxImage(Player owner, ImageView image, BoxImageResourceInfo boxImageType) {
        switch (owner) {
        case Player_1:
            image.setImageResource(boxImageType.getPlayerOneImage());
            break;

        case Player_2:
            image.setImageResource(boxImageType.getPlayerTwoImage());
            break;

        case Unowned:
            image.setImageResource(boxImageType.getUnownedImage());
            break;
        }
    }

    private ImageView getBox(Position pos) {
        return allBoxes[pos.getGridX()][pos.getGridY()];
    }

}
