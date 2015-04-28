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
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Space;

import com.diusrex.tictactoe.R;
import com.diusrex.tictactoe.android.box_images.BoxImageResourceInfo;
import com.diusrex.tictactoe.data_structures.BoardStatus;
import com.diusrex.tictactoe.data_structures.BoxPosition;
import com.diusrex.tictactoe.data_structures.Line;
import com.diusrex.tictactoe.data_structures.Player;
import com.diusrex.tictactoe.data_structures.SectionPosition;
import com.diusrex.tictactoe.logic.GridConstants;

public class MainGridOwner implements GridOwner {
    static private final int GRID_LINE_WIDTH = 50;
    private static final int SIZE_OF_SPACE = 30;

    private final GameEventHandler handler;
    private final MyGridLayout mainGrid;

    private final SectionOverlayHandler overlayHandler;
    private final SectionOwner[][] sections;

    MainGridOwner(Activity activity, GameEventHandler handler, MyGridLayout mainGrid) {
        this.handler = handler;
        this.mainGrid = mainGrid;
        setUpMainGridLines();

        sections = new SectionOwner[GridConstants.NUMBER_OF_SECTIONS_PER_SIDE][GridConstants.NUMBER_OF_SECTIONS_PER_SIDE];
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
        setUpSelectingSection(frame, pos);

        MyGridLayout currentGrid = (MyGridLayout) frame.findViewById(R.id.mainGrid);
        sections[x][y] = new SectionOwner(pos, currentGrid);

        mainGrid.addView(frame);

        GridOrganizer.populateGrid(activity, board, sections[x][y], boxImageType);
    }

    private void setUpSelectingSection(FrameLayout upSelectingGrid, SectionPosition pos) {
        upSelectingGrid.setTag(pos);
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

    public void updateBoxValue(BoardStatus board, SectionPosition section, BoxPosition position, BoxImageResourceInfo boxImageType) {
        getSectionOwner(section).updateBoxValue(board, position, boxImageType);
    }

    private SectionOwner getSectionOwner(SectionPosition section) {
        return sections[section.getGridX()][section.getGridY()];
    }

    @Override
    public void updateWinLine(BoardStatus board) {
        if (board.getWinner() != Player.Unowned) {
            Line line = board.getWinLine();
            overlayHandler.setWinLine(mainGrid, line);
        } else {
            overlayHandler.removeWinLine(mainGrid);
        }
    }
}
