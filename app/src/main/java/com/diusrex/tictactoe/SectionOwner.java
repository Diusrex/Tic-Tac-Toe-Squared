package com.diusrex.tictactoe;

import android.app.Activity;
import android.graphics.Color;
import android.widget.ImageView;
import android.widget.Space;

import com.diusrex.tictactoe.box_images.BoxImageResourceInfo;
import com.diusrex.tictactoe.logic.BoardStatus;
import com.diusrex.tictactoe.logic.BoxPosition;
import com.diusrex.tictactoe.logic.Line;
import com.diusrex.tictactoe.logic.Player;
import com.diusrex.tictactoe.logic.SectionPosition;

public class SectionOwner implements GridOwner {
    static private final int GRID_LINE_WIDTH = 15;
    static private final int GRID_LINE_COLOR = Color.GREEN;
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

    protected void setUpGridLines(MyGrid grid) {
        grid.setLineWidth(GRID_LINE_WIDTH);
        grid.setLineColor(GRID_LINE_COLOR);
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
