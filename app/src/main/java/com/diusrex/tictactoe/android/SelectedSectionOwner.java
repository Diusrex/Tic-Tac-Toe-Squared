package com.diusrex.tictactoe.android;

import android.view.View;
import android.widget.ImageView;

import com.diusrex.tictactoe.logic.BoxPosition;
import com.diusrex.tictactoe.logic.SectionPosition;

public class SelectedSectionOwner extends SectionOwner {
    static private final int GRID_LINE_WIDTH = 30;
    private final GameEventHandler handler;


    SelectedSectionOwner(SectionPosition sectionPosition, MyGrid grid, GameEventHandler handler) {
        super(sectionPosition, grid);
        this.handler = handler;
    }

    @Override
    protected int getLineWidth() {
        return GRID_LINE_WIDTH;
    }

    @Override
    protected void setUpSpecialInformation(ImageView imageView, BoxPosition posInBoard) {
        imageView.setTag(posInBoard);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.boxSelected((BoxPosition) v.getTag());
            }
        });
    }
}
