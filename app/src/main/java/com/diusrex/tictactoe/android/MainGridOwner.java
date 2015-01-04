package com.diusrex.tictactoe.android;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Space;

import com.diusrex.tictactoe.R;
import com.diusrex.tictactoe.box_images.BoxImageResourceInfo;
import com.diusrex.tictactoe.logic.BoardStatus;
import com.diusrex.tictactoe.logic.BoxPosition;
import com.diusrex.tictactoe.logic.GridChecker;
import com.diusrex.tictactoe.logic.Line;
import com.diusrex.tictactoe.logic.Player;
import com.diusrex.tictactoe.logic.SectionPosition;
import com.diusrex.tictactoe.logic.TicTacToeEngine;

public class MainGridOwner implements GridOwner {
    static private final int GRID_LINE_WIDTH = 50;
    private static final int SIZE_OF_SPACE = 30;

    private final GameEventHandler handler;
    private final MyGrid mainGrid;

    private final SectionOverlayHandler overlayHandler;
    private final SectionOwner[][] sections;

    MainGridOwner(Activity activity, GameEventHandler handler, MyGrid mainGrid) {
        this.handler = handler;
        this.mainGrid = mainGrid;
        setUpMainGridLines();

        sections = new SectionOwner[BoardStatus.NUMBER_OF_SECTIONS_PER_SIDE][BoardStatus.NUMBER_OF_SECTIONS_PER_SIDE];
        overlayHandler = new SectionOverlayHandler(activity);
    }

    private void setUpMainGridLines() {
        mainGrid.setLineWidth(GRID_LINE_WIDTH);
        mainGrid.setLineColor(mainGrid.getResources().getColor(R.color.game_win_line));
    }

    @Override
    public void removeAllViews() {
        mainGrid.removeAllViews();
    }

    @Override
    public void addGridItem(Activity activity, BoardStatus board, int x, int y, BoxImageResourceInfo boxImageType) {
        SectionPosition pos = SectionPosition.make(x, y);

        FrameLayout frame = (FrameLayout) activity.getLayoutInflater().inflate(R.layout.section_layout, null);
        overlayHandler.setSectionFrame(frame, x, y);
        setUpSelectingSection(frame, x, y);

        MyGrid currentGrid = (MyGrid) frame.findViewById(R.id.mainGrid);
        sections[x][y] = new SectionOwner(pos, currentGrid);

        mainGrid.addView(frame);

        GridOrganizer.populateGrid(activity, board, sections[x][y], boxImageType);
    }

    private void setUpSelectingSection(FrameLayout upSelectingGrid, int x, int y) {
        upSelectingGrid.setTag(SectionPosition.make(x, y));
        upSelectingGrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.sectionSelected((SectionPosition) v.getTag());
            }
        });
    }

    @Override
    public void addSingleHorizontalSpace(Activity activity) {
        Space space = new Space(activity);
        space.setMinimumWidth(SIZE_OF_SPACE);
        mainGrid.addView(space);
    }

    @Override
    public void addSingleVerticalSpace(Activity activity) {
        Space space = new Space(activity);
        space.setMinimumHeight(SIZE_OF_SPACE);
        mainGrid.addView(space);
    }

    public void sectionToPlayInChanged(SectionPosition sectionToPlayIn) {
        overlayHandler.sectionToPlayInChanged(sectionToPlayIn);
    }

    public void selectionSelectedChanged(SectionPosition section) {
        overlayHandler.selectionSelectedChanged(section);
    }

    public void updateBoxValue(BoardStatus board, BoxPosition position, BoxImageResourceInfo boxImageType) {
        SectionPosition sectionToChange = position.getSectionIn();
        sections[sectionToChange.getX()][sectionToChange.getY()].updateBoxValue(board, position, boxImageType);
    }

    @Override
    public void updateWinLine(BoardStatus board) {
        if (TicTacToeEngine.getWinner(board) != Player.Unowned) {
            Line line = GridChecker.searchForLineOrGetNull(board.getOwnerGrid());
            overlayHandler.setWinLine(mainGrid, line);
        } else {
            overlayHandler.removeWinLine(mainGrid);
        }
    }
}
