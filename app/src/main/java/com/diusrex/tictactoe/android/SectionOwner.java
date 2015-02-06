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
import com.diusrex.tictactoe.logic.BoardStatus;
import com.diusrex.tictactoe.logic.BoxPosition;
import com.diusrex.tictactoe.logic.Line;
import com.diusrex.tictactoe.logic.Player;
import com.diusrex.tictactoe.logic.SectionPosition;

public class SectionOwner implements GridOwner {
    static private final int GRID_LINE_WIDTH = 15;
    static private final int SIZE_OF_SPACE = 10;

    private final SectionPosition sectionPosition;
    private final BoxPosition boxOffset;
    private final MyGrid grid;

    private final ImageView allBoxes[][];

    SectionOwner(SectionPosition sectionPosition, MyGrid grid) {
        this.sectionPosition = sectionPosition;
        this.boxOffset = sectionPosition.getTopLeftPosition();
        this.grid = grid;
        this.allBoxes = new ImageView[BoardStatus.SIZE_OF_SECTION][BoardStatus.SIZE_OF_SECTION];

        setUpGridLines(this.grid);
    }

    private void setUpGridLines(MyGrid grid) {
        grid.setLineWidth(getLineWidth());
        grid.setLineColor(grid.getResources().getColor(R.color.section_win_line));
    }

    protected int getLineWidth() {
        return GRID_LINE_WIDTH;
    }

    @Override
    public void removeAllViews() {
        grid.removeAllViews();
    }

    @Override
    public void addGridItem(Activity activity, BoardStatus board, int x, int y, BoxImageResourceInfo boxImageType) {
        BoxPosition posInBoard = boxOffset.increaseBy(x, y);
        allBoxes[x][y] = generateBox(board.getBoxOwner(posInBoard), activity, boxImageType);

        setUpSpecialInformation(allBoxes[x][y], posInBoard);

        grid.addView(allBoxes[x][y]);
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
        grid.addView(space);
    }

    @Override
    public void addSingleVerticalSpace(Activity activity) {
        Space space = new Space(activity);
        space.setMinimumHeight(SIZE_OF_SPACE);
        grid.addView(space);
    }

    @Override
    public void updateWinLine(BoardStatus board) {
        Line winLine = board.getLine(sectionPosition);
        if (shouldDrawWinLine(winLine)) {
            drawWinLine(winLine);
        } else if (shouldRemoveWinLineFromGrid(winLine)) {
            grid.removeLine();
        }
    }

    private boolean shouldDrawWinLine(Line winLine) {
        return winLine != null;
    }

    private boolean shouldRemoveWinLineFromGrid(Line winLine) {
        return winLine == null && grid.hasLine();
    }

    private void drawWinLine(Line winLine) {
        BoxPosition startPos = convertPositionToSectionRelative(winLine.getStart());
        BoxPosition endPos = convertPositionToSectionRelative(winLine.getEnd());
        grid.setLine(getBox(startPos), getBox(endPos));
    }

    public void updateBoxValue(BoardStatus board, BoxPosition positionInBoard, BoxImageResourceInfo boxImageType) {
        BoxPosition positionInOwnBoxes = convertPositionToSectionRelative(positionInBoard);

        setBoxImage(board.getBoxOwner(positionInBoard), getBox(positionInOwnBoxes), boxImageType);

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

    private BoxPosition convertPositionToSectionRelative(BoxPosition pos) {
        return pos.decreaseBy(boxOffset);
    }

    private ImageView getBox(BoxPosition pos) {
        return allBoxes[pos.getX()][pos.getY()];
    }

}
