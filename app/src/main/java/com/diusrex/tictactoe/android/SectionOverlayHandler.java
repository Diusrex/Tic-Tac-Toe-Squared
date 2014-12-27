package com.diusrex.tictactoe.android;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.widget.FrameLayout;

import com.diusrex.tictactoe.R;
import com.diusrex.tictactoe.logic.BoardStatus;
import com.diusrex.tictactoe.logic.BoxPosition;
import com.diusrex.tictactoe.logic.Line;
import com.diusrex.tictactoe.logic.SectionPosition;

public class SectionOverlayHandler {
    private final FrameLayout[][] frames;

    private final Drawable currentlySelectedAndToPlayIn;
    private final Drawable currentToPlayIn;
    private final Drawable currentlySelected;
    private final Drawable regularForeground;

    public SectionOverlayHandler(Activity activity) {
        currentlySelectedAndToPlayIn = activity.getResources().getDrawable(R.color.selected_and_play_in);
        currentToPlayIn = activity.getResources().getDrawable(R.color.play_in);
        currentlySelected = activity.getResources().getDrawable(R.color.selected);
        regularForeground = activity.getResources().getDrawable(android.R.color.transparent);

        frames = new FrameLayout[BoardStatus.NUMBER_OF_SECTIONS_PER_SIDE][BoardStatus.NUMBER_OF_SECTIONS_PER_SIDE];
    }

    public void setSectionFrame(FrameLayout frame, int x, int y) {
        frame.setForeground(regularForeground);
        frames[x][y] = frame;
    }

    // This will not crash if given an invalid section
    public void sectionToPlayInChanged(SectionPosition sectionToPlayIn) {
        int addedToX = sectionToPlayIn.getX();
        int addedToY = sectionToPlayIn.getY();

        for (int y = 0; y < BoardStatus.NUMBER_OF_SECTIONS_PER_SIDE; ++y) {
            for (int x = 0; x < BoardStatus.NUMBER_OF_SECTIONS_PER_SIDE; ++x) {
                if (x == addedToX && y == addedToY)
                    addPlayInToForeground(frames[x][y]);
                else
                    removePlayInFromForeground(frames[x][y]);
            }
        }
    }

    // This will not crash if given an invalid section
    public void selectionSelectedChanged(SectionPosition section) {
        int addedToX = section.getX();
        int addedToY = section.getY();

        for (int y = 0; y < BoardStatus.NUMBER_OF_SECTIONS_PER_SIDE; ++y) {
            for (int x = 0; x < BoardStatus.NUMBER_OF_SECTIONS_PER_SIDE; ++x) {
                if (x == addedToX && y == addedToY)
                    addSelectedToForeground(frames[x][y]);
                else
                    removeSelectedFromForeground(frames[x][y]);
            }
        }
    }

    private void removePlayInFromForeground(FrameLayout frameLayout) {
        if (frameLayout.getForeground() == currentlySelectedAndToPlayIn)
            frameLayout.setForeground(currentlySelected);
        else if (frameLayout.getForeground() == currentToPlayIn)
            frameLayout.setForeground(regularForeground);
    }

    private void addPlayInToForeground(FrameLayout frameLayout) {
        if (frameLayout.getForeground() == currentlySelected)
            frameLayout.setForeground(currentlySelectedAndToPlayIn);
        else if (frameLayout.getForeground() == regularForeground)
            frameLayout.setForeground(currentToPlayIn);
    }

    private void removeSelectedFromForeground(FrameLayout frameLayout) {
        if (frameLayout.getForeground() == currentlySelectedAndToPlayIn)
            frameLayout.setForeground(currentToPlayIn);
        else if (frameLayout.getForeground() == currentlySelected)
            frameLayout.setForeground(regularForeground);
    }

    private void addSelectedToForeground(FrameLayout frameLayout) {
        if (frameLayout.getForeground() == currentToPlayIn)
            frameLayout.setForeground(currentlySelectedAndToPlayIn);
        else if (frameLayout.getForeground() == regularForeground)
            frameLayout.setForeground(currentlySelected);
    }

    public void setWinLine(MyGrid grid, Line line) {
        grid.setLine(getFrame(line.getStart()), getFrame(line.getEnd()));
    }

    private FrameLayout getFrame(BoxPosition pos) {
        return frames[pos.getX()][pos.getY()];
    }

    public void removeWinLine(MyGrid grid) {
        if (grid.hasLine()) {
            grid.removeLine();
        }
    }
}
